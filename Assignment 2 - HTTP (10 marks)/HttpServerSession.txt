// MIN SOE HTUT 
// 16311938

import java.net.*;
import java.io.*;

class HttpServerSession extends Thread {
    private Socket clientSocket;

    // Step 3: Constructor to initialize the session with a client socket
    public HttpServerSession(Socket socket) {
        this.clientSocket = socket;
    }

    // Step 3: The run method is where the session's work is done
    public void run() {
        try {
            System.out.println("Processing connection from " + clientSocket.getInetAddress());

            //BufferedReader to read the request from the client
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //BufferedOutputStream to send the response to the client
            BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());

            //Create an instance of HttpServerRequest to parse the incoming request
            HttpServerRequest request = new HttpServerRequest();

            String inputLine;
            while (true) {
                //Read each line from the client input stream
                inputLine = in.readLine();
                if (inputLine == null || inputLine.isEmpty()) {
                    break; //Break if the line is empty (end of headers)
                }
                //Process the line with HttpServerRequest
                request.process(inputLine);
                System.out.println("Request: " + inputLine);
            }

            System.out.println("Finished reading the request.");

            String requestedFile = request.getFile();
            String host = request.getHost();

            //Extract only the hostname, ignoring the port number
            if (host != null && host.contains(":")) {
                host = host.split(":")[0];
            }

            System.out.println("Requested file: " + requestedFile);
            System.out.println("Host: " + host);

            if (requestedFile != null && !requestedFile.isEmpty()) {
                //Handle virtual hosting by constructing the file path based on the host
                String basePath = host != null ? host : "localhost";
                File file = new File(basePath, requestedFile);

                System.out.println("Looking for file at: " + file.getAbsolutePath());

                //Check if the requested file exists and is not a directory
                if (file.exists() && !file.isDirectory()) {
                    System.out.println("File found, preparing to send...");
                    sendFile(out, file); // Step 3: Send the file to the client
                } else {
                    System.out.println("File not found: " + file.getAbsolutePath());
                    sendResponse(out, "HTTP/1.1 404 Not Found", "404 File Not Found");
                }
            } else {
                System.out.println("Bad request: no file specified.");
                sendResponse(out, "HTTP/1.1 400 Bad Request", "400 Bad Request");
            }

            clientSocket.close(); // Step 3: Close the connection after sending the response
        } catch (IOException e) {
            System.out.println("Session error: " + e.getMessage());
        }
    }

    // Step 4: Method to send a file as a response
    private void sendFile(BufferedOutputStream out, File file) throws IOException {
        System.out.println("Sending file: " + file.getName());
        out.write("HTTP/1.1 200 OK\r\n".getBytes());
        out.write(("Content-Type: " + getContentType(file) + "\r\n").getBytes());
        out.write(("Content-Length: " + file.length() + "\r\n").getBytes());
        out.write(("\r\n").getBytes());  // Step 4: End of headers

        // Read the file and send its contents to the client
        FileInputStream fileIn = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileIn.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            out.flush(); // Step 4: Ensure the data is sent immediately
            // Step 9:
            /*
            try {
                //A delay of 1000 milliseconds
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Sleep interrupted: " + e.getMessage());
            }
            */
        }

        fileIn.close();
    }

    // Step 5: Method to determine the MIME type based on the file extension
    private String getContentType(File file) {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
            return "text/html";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript";
        } else {
            return "application/octet-stream";  //Default binary file type
        }
    }

    // Step 6: Method to send a simple text response with a status line and body
    private void sendResponse(BufferedOutputStream out, String statusLine, String body) throws IOException {
        System.out.println("Sending response: " + statusLine);
        out.write((statusLine + "\r\n").getBytes());
        out.write(("Content-Type: text/plain\r\n").getBytes());
        out.write(("Content-Length: " + body.length() + "\r\n").getBytes());
        out.write(("\r\n").getBytes());  //End of headers

        //Send the body of the response
        out.write((body + "\r\n").getBytes());
        out.flush();
    }
}


