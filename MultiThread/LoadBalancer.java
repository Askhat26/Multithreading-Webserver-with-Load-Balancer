import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class LoadBalancer {
    // List of available backend servers
    private static final List<String> SERVERS = Arrays.asList("localhost:8010", "localhost:8011", "localhost:8012");
    private static final AtomicInteger currentIndex = new AtomicInteger(0);
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10); // Thread pool for handling requests

    public static void main(String[] args) {
        try (ServerSocket loadBalancerSocket = new ServerSocket(8080)) {
            System.out.println(" Load Balancer started on port 8080...");

            while (true) {
                Socket clientSocket = loadBalancerSocket.accept();
                String selectedServer = getNextServer();
                System.out.println(" Forwarding request to " + selectedServer);

                // Handle request in a separate thread from the pool
                threadPool.submit(() -> forwardRequest(clientSocket, selectedServer));
            }
        } catch (IOException e) {
            System.err.println(" Load Balancer Error: " + e.getMessage());
        } finally {
            threadPool.shutdown(); // Shutdown the thread pool when done
        }
    }

    private static String getNextServer() {
        int index = currentIndex.getAndIncrement() % SERVERS.size();
        return SERVERS.get(index); // Round-robin selection
    }

    private static void forwardRequest(Socket clientSocket, String server) {
        String[] parts = server.split(":");
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);

        try (
                Socket serverSocket = new Socket(host, port);
                BufferedReader fromServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
                PrintWriter toServer = new PrintWriter(serverSocket.getOutputStream(), true);
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            // Forward client's message to the selected backend server
            String clientMessage;
            while ((clientMessage = fromClient.readLine()) != null) {
                toServer.println(clientMessage);
                System.out.println(" Forwarded message: " + clientMessage);
            }

            // Forward response from the backend server to the client
            String serverResponse;
            while ((serverResponse = fromServer.readLine()) != null) {
                toClient.println(serverResponse);
                System.out.println(" Response from " + server + ": " + serverResponse);
            }

        } catch (IOException e) {
            System.err.println(" Error forwarding request to " + server + ": " + e.getMessage());
        } finally {
            try {
                clientSocket.close(); // Ensure the client socket is closed
            } catch (IOException e) {
                System.err.println(" Error closing client socket: " + e.getMessage());
            }
        }
    }
}