import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

class HttpServerSession extends Thread {
    private Socket clientSocket;

    // Initialize the session with a client socket
    public HttpServerSession(Socket socket) {
        this.clientSocket = socket;
    }

    // Each session runs in its own thread to handle a client request
    public void run() {
        try {
            System.out.println("Processing connection from " + clientSocket.getInetAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());

            HttpServerRequest request = new HttpServerRequest();
            String line;
            while (true) {
                line = in.readLine();
                if (line == null || line.isEmpty()) break;
                request.process(line);
                System.out.println("Request: " + line);
            }
            System.out.println("Finished reading the request.");

            // Reject unsupported HTTP methods (only GET is allowed)
            try {
                String method = request.getMethod();
                if (method != null && !"GET".equals(method)) {
                    sendResponse(out, "HTTP/1.1 405 Method Not Allowed", "Method Not Allowed");
                    clientSocket.close();
                    return;
                }
            } catch (Throwable ignore) {
                // If request.getMethod() is not implemented, just continue
            }

            String requestedFile = request.getFile();
            String host = request.getHost();

            if (host != null && host.contains(":")) {
                host = host.split(":", 2)[0];
            }
            if (host == null || host.isEmpty()) {
                host = "localhost";
            }

            if (requestedFile == null || requestedFile.isEmpty()) {
                System.out.println("Bad request: no file specified.");
                sendResponse(out, "HTTP/1.1 400 Bad Request", "400 Bad Request");
                clientSocket.close();
                return;
            }

            // Clean up the request path: remove query string and URL-decode
            String cleanPath = requestedFile.split("\\?", 2)[0];
            cleanPath = java.net.URLDecoder.decode(cleanPath, StandardCharsets.UTF_8);

            // Use a site root folder for virtual hosting (./sites/<host>/)
            File siteRoot = new File("sites", host);
            File target = new File(siteRoot, cleanPath);

            // If the request ends with "/", look for index.html
            if (cleanPath.endsWith("/")) {
                target = new File(target, "index.html");
            }

            // Prevent directory traversal by verifying canonical paths
            File canonicalRoot = siteRoot.getCanonicalFile();
            File canonicalTarget = target.getCanonicalFile();
            System.out.println("Looking for file at: " + canonicalTarget.getAbsolutePath());

            if (!canonicalTarget.getPath().startsWith(canonicalRoot.getPath())) {
                System.out.println("Forbidden path traversal attempt blocked.");
                sendResponse(out, "HTTP/1.1 403 Forbidden", "Forbidden");
                clientSocket.close();
                return;
            }

            // If directory is requested, try index.html
            if (canonicalTarget.isDirectory()) {
                canonicalTarget = new File(canonicalTarget, "index.html").getCanonicalFile();
            }

            // Serve the file if found, otherwise return 404
            if (canonicalTarget.exists() && canonicalTarget.isFile()) {
                System.out.println("File found, preparing to send...");
                sendFile(out, canonicalTarget);
            } else {
                System.out.println("File not found: " + canonicalTarget.getAbsolutePath());
                sendResponse(out, "HTTP/1.1 404 Not Found", "404 File Not Found");
            }

            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Session error: " + e.getMessage());
        }
    }

    // Send a file as an HTTP response with appropriate headers
    private void sendFile(BufferedOutputStream out, File file) throws IOException {
        System.out.println("Sending file: " + file.getName());
        out.write("HTTP/1.1 200 OK\r\n".getBytes());
        out.write(("Content-Type: " + getContentType(file) + "\r\n").getBytes());
        out.write(("Content-Length: " + file.length() + "\r\n").getBytes());
        out.write(("Connection: close\r\n").getBytes());
        out.write(("\r\n").getBytes());  // End of headers

        try (FileInputStream fileIn = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        out.flush();
    }

    // Determine the MIME type based on file extension
    private String getContentType(File file) {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) return "text/html";
        if (fileName.endsWith(".jpg")  || fileName.endsWith(".jpeg")) return "image/jpeg";
        if (fileName.endsWith(".png")) return "image/png";
        if (fileName.endsWith(".gif")) return "image/gif";
        if (fileName.endsWith(".css")) return "text/css";
        if (fileName.endsWith(".js"))  return "application/javascript";
        return "application/octet-stream";  // Default binary file type
    }

    // Send a simple text response with headers and a message body
    private void sendResponse(BufferedOutputStream out, String statusLine, String body) throws IOException {
        System.out.println("Sending response: " + statusLine);
        byte[] payload = (body + "\r\n").getBytes(StandardCharsets.UTF_8);
        out.write((statusLine + "\r\n").getBytes());
        out.write(("Content-Type: text/plain; charset=utf-8\r\n").getBytes());
        out.write(("Content-Length: " + payload.length + "\r\n").getBytes());
        out.write(("Connection: close\r\n").getBytes());
        out.write(("\r\n").getBytes());
        out.write(payload);
        out.flush();
    }
}
