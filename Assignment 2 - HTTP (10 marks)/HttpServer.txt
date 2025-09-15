// MIN SOE HTUT 
// 16311938

import java.net.*;
import java.io.*;

class HttpServer {
    
    // Step 1
    public static void main(String args[]) {
        //Print a message when server is starting
        System.out.println("Web Server starting");

        // Choose a port number
        int port = 50505;

        try {
            // Create a ServerSocket 
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Listening on port: " + port);

            while (true) {
                //Accept a connection from a client
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection accepted from " + clientSocket.getInetAddress());

                //Create a new HttpServerSession to handle the connection in a new thread
                HttpServerSession session = new HttpServerSession(clientSocket);
                session.start(); // Start the thread to handle the connection
            }

        } catch (IOException e) {
            // Handle any IOExceptions
            System.out.println("Server error: " + e.getMessage());
        }
    }
}

