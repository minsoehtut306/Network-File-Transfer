// Name : MIN SOE HTUT 
// Student ID : 1631938

import java.net.*;
import java.io.*;

public class SimpleClient {
    public static void main(String[] args) {
        // Check if the number of arguments is exactly 2 (hostname and port)
        if (args.length != 2) {
            System.err.println("Usage: SimpleClient <hostname> <port>");
            return;
        }

        // Get the hostname and port number from the user
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            // Create a socket to connect to the server using the hostname and port
            Socket socket = new Socket(hostname, port);

            // Create input and output streams for the socket
	    PrintWriter write = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Read and print the messages from the server
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Close the connection
            socket.close();
        } catch (UnknownHostException e) {
            // Print error where the hostname is unknown
            System.err.println("Unknown host: " + hostname);
        } catch (IOException e) {
            // Print error I/O exceptions during communication
            System.err.println("IOException: " + e.getMessage());
        }
    }
}

