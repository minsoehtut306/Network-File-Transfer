# Simple Java Networking Tools

A collection of small networking programs written in **Java**, demonstrating basic socket programming and hostname/IP resolution.

---

## Components

- **SimpleServer.java**  
  A TCP server that listens on an available port, accepts client connections, and greets them with their hostname and IP address.

- **SimpleClient.java**  
  A TCP client that connects to the server and prints the messages sent by it.

- **resolve.java**  
  Resolves hostnames to their corresponding IP addresses.

- **reverse.java**  
  Resolves IP addresses back to their hostnames.

---

## How to Run

1. **Compile all programs:**
   ```bash
   javac SimpleServer.java SimpleClient.java resolve.java reverse.java
   ```

2. **Run the server:**
   ```bash
   java SimpleServer
   ```
   The server will print the port number it is listening on.

3. **Run the client:**
   ```bash
   java SimpleClient <hostname> <port>
   ```
   Replace `<hostname>` and `<port>` with the server’s host and port.

4. **Run the resolver (hostname → IP):**
   ```bash
   java resolve <name1> <name2> ... <nameN>
   ```

5. **Run the reverse resolver (IP → hostname):**
   ```bash
   java reverse <ip1> <ip2> ... <ipN>
   ```

---

## Technologies Used
- Java (core networking APIs: ServerSocket, Socket, InetAddress)
