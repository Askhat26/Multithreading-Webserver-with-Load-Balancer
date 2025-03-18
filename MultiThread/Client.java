import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String loadBalancerHost = "localhost";
        int loadBalancerPort = 8080;

        // Allow host and port to be passed as command-line arguments
        if (args.length >= 2) {
            loadBalancerHost = args[0];
            loadBalancerPort = Integer.parseInt(args[1]);
        }

        try (Socket socket = new Socket(loadBalancerHost, loadBalancerPort);
             BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println(" Connected to Load Balancer at " + loadBalancerHost + ":" + loadBalancerPort);

            // Read input from the user and send it to the server
            System.out.print("Enter a message to send to the server (or 'exit' to quit): ");
            String userMessage;
            while ((userMessage = userInput.readLine()) != null) {
                if ("exit".equalsIgnoreCase(userMessage)) {
                    break; // Exit the loop if the user types 'exit'
                }

                // Send the message to the server
                toServer.println(userMessage);
                System.out.println(" Sent message: " + userMessage);

                // Read the server's response
                String response = fromServer.readLine();
                if (response != null) {
                    System.out.println(" Response from Server: " + response);
                } else {
                    System.out.println(" Server closed the connection.");
                    break;
                }

                System.out.print("Enter another message (or 'exit' to quit): ");
            }

        } catch (UnknownHostException e) {
            System.err.println(" Error: Unknown host " + loadBalancerHost);
        } catch (IOException e) {
            System.err.println(" Error connecting to Load Balancer: " + e.getMessage());
        } finally {
            System.out.println(" Client disconnected.");
        }
    }
}