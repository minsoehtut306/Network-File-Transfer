import java.net.*;
import java.io.*;

class HttpServer {

    public static void main(String args[]) {
        System.out.println("Web Server starting...");

        // Default port
        int port = 50505;

        // Allow override via command-line argument
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number. Using default: " + port);
            }
        }

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Listening on " + serverSocket.getLocalSocketAddress());

            while (true) {
                // Accept a new client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection accepted from " + clientSocket.getInetAddress());

                // Handle request in a separate thread
                HttpServerSession session = new HttpServerSession(clientSocket);
                session.start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }

        System.out.println("Server shutting down.");
    }
}
