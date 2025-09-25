# Java TLS File Transfer

A secure file transfer system implemented in **Java** using TLS sockets.  
The server and client communicate with encryption and authentication via certificates.

---

## ⚙️ Components
- **MyTLSFileServer.java** – TLS-enabled server  
- **MyTLSFileClient.java** – TLS-enabled client  
- **server.jks / server-cert.pem** – server keystore and certificate  
- **ca-cert.jks / ca-cert.pem** – certificate authority files  
- **ca-private.pem / server.csr** – CA and server key material  

---

## 🚀 How to Run

1. **Compile:**
   ```bash
   javac MyTLSFileServer.java MyTLSFileClient.java
   ```

2. **Start the server:**
   ```bash
   java MyTLSFileServer <port> server.jks <keystore-password>
   ```

3. **Trust setup for the client:**
   Import `ca-cert.pem` into a truststore:
   ```bash
   keytool -import -trustcacerts -keystore clientTruststore.jks            -alias myCA -file ca-cert.pem
   ```

4. **Run the client:**
   ```bash
   java -Djavax.net.ssl.trustStore=clientTruststore.jks         -Djavax.net.ssl.trustStorePassword=<truststore-password>         MyTLSFileClient localhost <port> <filename>
   ```

---

## 🛠️ Technologies Used
- Java (TLS sockets, SSLContext)  
- Java KeyStore (JKS) and X.509 certificates
