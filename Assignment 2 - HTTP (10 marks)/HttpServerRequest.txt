// MIN SOE HTUT 
// 16311938

//Step 2
class HttpServerRequest {
    private String file = null;  
    private String host = null;  
    private boolean done = false;
    private int line = 0; 

    //Accessor methods to get the file and host after parsing
    public boolean isDone() { return done; }
    public String getFile() { return file; }
    public String getHost() { return host; }

    //Method to process each line of the HTTP request
    public void process(String in) {
        //Increment the line number
        line++;

        // Process the first line (the GET request line)
        if (line == 1) {
            String[] parts = in.split(" ");
            if (parts.length == 3 && parts[0].equals("GET")) {
                //Extract the requested file, removing the leading slash
                file = parts[1].substring(1);
                //If the requested file ends with a "/", serve the index.html
                if (file.endsWith("/")) {
                    file += "index.html";
                }
            } else {
                //Malformed request or unsupported method
                done = true;
            }
        } else if (in.startsWith("Host: ")) {
            //Extract the Host header
            host = in.substring(6).trim();
        }

        // End of headers (indicated by an empty line)
        if (in.isEmpty()) {
            done = true;
            // Debug statement to confirm when done is set to true
            System.out.println("Request parsing done.");
        }
    }
}
