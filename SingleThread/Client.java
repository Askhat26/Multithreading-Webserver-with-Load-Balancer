import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public void run() throws UnknownHostException, IOException {
        int port = 8090;
        InetAddress address = InetAddress.getByName("localhost");
        Socket socket = new Socket(address, port);


        PrintWriter toClient = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader fromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));


        toClient.println("Hello from socket " + socket.getLocalSocketAddress());


        String line = fromSocket.readLine();
        System.out.println("Server says: " + line);


        toClient.close();
        fromSocket.close();
        socket.close();
    }

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
