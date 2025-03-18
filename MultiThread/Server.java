import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Server {
    private static final int THREAD_POOL_SIZE = 10; // Adjust based on expected load

    public Consumer<Socket> getConsumer() {
        return (clientSocket) -> {
            try (PrintWriter toSocket = new PrintWriter(clientSocket.getOutputStream(), true)) {
                toSocket.println("Hello from server " + clientSocket.getInetAddress());
            } catch (IOException ex) {
                System.err.println("Error handling client request: " + ex.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    System.err.println("Error closing client socket: " + ex.getMessage());
                }
            }
        };
    }

    public static void main(String[] args) {
        int port = 8010;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        Server server = new Server();
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setSoTimeout(90000);
            System.out.println("Server is listening on port " + port);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket.getInetAddress());


                    threadPool.submit(() -> server.getConsumer().accept(clientSocket));
                } catch (SocketTimeoutException ex) {
                    System.out.println("Server accept() timed out. Continuing...");
                } catch (IOException ex) {
                    System.err.println("Error accepting client connection: " + ex.getMessage());
                }
            }
        } catch (IOException ex) {
            System.err.println("Could not start server on port " + port + ": " + ex.getMessage());
        } finally {
            threadPool.shutdown();
        }
    }
}