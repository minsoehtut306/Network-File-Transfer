# TFTP File Transfer (Java)

This project provides a simple implementation of the Trivial File Transfer Protocol (**TFTP**) using Java sockets over UDP.

---

## 📂 Contents
- `TftpServer.java` – Server that listens for read requests (RRQ) and spawns worker threads to transfer files.  
- `TftpClient.java` – Client that requests a file from the server and saves it locally.  
- Any test files you want to make available for download should be placed in the same directory as the server.

---

## 🛠️ Language & Tools
- Java (JDK 8+ recommended)  
- UDP sockets (`DatagramSocket`, `DatagramPacket`)

---

## 🚀 How to Run

1. **Compile both files:**
   ```bash
   javac TftpServer.java TftpClient.java
   ```

2. **Start the server:**
   ```bash
   java TftpServer
   ```
   The server prints the port it is listening on.

3. **Run the client:**
   ```bash
   java TftpClient
   ```
   - Enter the port number printed by the server.  
   - Enter the filename you want to download (must exist in the server’s working directory).

4. **Result:**
   - The client saves the file locally as `tftp_<filename>`.  
   - Logs show block transfers, ACKs, and errors if any.

---

## 📌 Notes
- Transfer mode is **octet** (binary).  
- Supports retransmissions if ACKs are lost.  
- Sends error packets when files are missing or requests are invalid.  
