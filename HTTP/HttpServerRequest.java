class HttpServerRequest {
    private String file = null;  
    private String host = null;  
    private boolean done = false;
    private int line = 0; 

    // Accessors
    public boolean isDone() { return done; }
    public String getFile() { return file; }
    public String getHost() { return host; }

    /**
     * Process a single line of the HTTP request.
     * The first line is expected to be the request line (e.g., "GET /index.html HTTP/1.1").
     * Subsequent lines may include headers such as "Host:".
     */
    public void process(String in) {
        line++;

        // Handle the request line
        if (line == 1) {
            String[] parts = in.split(" ");
            if (parts.length == 3 && parts[0].equals("GET")) {
                // Remove leading slash from the path
                file = parts[1].substring(1);

                // If path ends with "/", serve index.html
                if (file.endsWith("/")) {
                    file += "index.html";
                }
            } else {
                // Unsupported method or malformed request
                done = true;
            }
        } 
        // Handle the Host header
        else if (in.toLowerCase().startsWith("host:")) {
            host = in.substring(5).trim();
        }

        // Empty line marks the end of headers
        if (in.isEmpty()) {
            done = true;
            System.out.println("Request parsing complete.");
        }
    }
}
