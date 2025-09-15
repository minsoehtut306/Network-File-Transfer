// Name : MIN SOE HTUT 
// Student ID : 1631938

import java.io.*;
import java.net.*;

public class SimpleServer {
    public static void main(String[] args) {
        try {
            // Create a ServerSocket object that link to an available port
            ServerSocket serverSocket = new ServerSocket(0);
            
            // Get and print the port number the server is listening on
            int port = serverSocket.getLocalPort();
            System.out.println("Listening on port " + port);

            while (true) {
                // Accept new connections from clients
                Socket clientSocket = serverSocket.accept();

                // Get the IP address of the connected client
                InetAddress clientAddress = clientSocket.getInetAddress();
                String clientIp = clientAddress.getHostAddress();
                String clientName = clientAddress.getHostName();

                // Prepare greeting messages for the client
                String greeting = "Hello, " + clientName + ".";
                String ipMessage = "Your IP address is " + clientIp;

                // Create input and output streams for the socket
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader read = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // Send the greeting messages to the client
                writer.println(greeting);
                writer.println(ipMessage);

                // Close the connection with the client
                clientSocket.close();
            }
        } catch (IOException e) {
            // Print error I/O exceptions
            System.err.println("Exception: " + e.getMessage());
        }
    }
}

