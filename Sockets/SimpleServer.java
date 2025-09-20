import java.io.*;
import java.net.*;

/**
 * A simple TCP server that accepts client connections,
 * greets them by hostname, and returns their IP address.
 */
public class SimpleServer {
    public static void main(String[] args) {
        try {
            // Bind the server to an available port
            ServerSocket serverSocket = new ServerSocket(0);

            // Display the port number so clients know where to connect
            int port = serverSocket.getLocalPort();
            System.out.println("Server is listening on port " + port);

            // Keep the server running indefinitely
            while (true) {
                // Accept an incoming client connection
                Socket clientSocket = serverSocket.accept();

                // Retrieve the client's IP and hostname
                InetAddress clientAddress = clientSocket.getInetAddress();
                String clientIp = clientAddress.getHostAddress();
                String clientName = clientAddress.getHostName();

                // Prepare greeting messages
                String greeting = "Hello, " + clientName + ".";
                String ipMessage = "Your IP address is " + clientIp;

                // Set up I/O streams for communication
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream())
                );

                // Send messages to the client
                writer.println(greeting);
                writer.println(ipMessage);

                // Close the connection
                clientSocket.close();
            }
        } catch (IOException e) {
            // Handle I/O errors gracefully
            System.err.println("Server encountered an error: " + e.getMessage());
        }
    }
}
