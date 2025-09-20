import java.net.*;
import java.io.*;

/**
 * A simple TCP client that connects to a server,
 * receives messages, and prints them to the console.
 */
public class SimpleClient {
    public static void main(String[] args) {
        // Require exactly 2 arguments: hostname and port
        if (args.length != 2) {
            System.err.println("Usage: java SimpleClient <hostname> <port>");
            return;
        }

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            // Establish a connection to the server
            Socket socket = new Socket(hostname, port);

            // Set up I/O streams for communication
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            // Read and print all messages sent by the server
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Close the socket when finished
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + hostname);
        } catch (IOException e) {
            System.err.println("I/O error during communication: " + e.getMessage());
        }
    }
}
