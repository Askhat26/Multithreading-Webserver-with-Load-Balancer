import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Server {

    public void run() {
        int port = 8010;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setSoTimeout(20000);
            System.out.println("Server is listening on port: " + port);

            while (true) {
                try {
                    Socket acceptedConnection = serverSocket.accept();
                    System.out.println("Connected to " + acceptedConnection.getRemoteSocketAddress());

                    try (
                            PrintWriter toClient = new PrintWriter(acceptedConnection.getOutputStream(), true);
                            BufferedReader fromClient = new BufferedReader(new InputStreamReader(acceptedConnection.getInputStream()))
                    ) {
                        toClient.println("Hello  from the server");
                    }
                    acceptedConnection.close();
                } catch (IOException e) {
                    System.out.println("Error handling client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not start the server: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
