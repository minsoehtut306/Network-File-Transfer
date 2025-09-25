# Simple Java HTTP Server

A lightweight HTTP server implemented in **Java**.  
It accepts client connections, parses requests, and serves static files.

---

## ⚙️ Features
- Handles multiple clients with threads  
- Supports GET requests  
- Basic MIME type handling  
- Virtual hosting (`./sites/<host>/`)  
- Returns standard HTTP status codes  

---

## 🚀 How to Run

1. Compile the sources:
   ```bash
   javac HttpServer.java HttpServerSession.java HttpServerRequest.java
   ```

2. Start the server (default port **50505**):
   ```bash
   java HttpServer
   ```

3. Place site files under `./sites/localhost/` (e.g., `index.html`).  
4. Open in browser: `http://localhost:50505/`

---

## 🛠️ Technologies Used
- Java (core networking APIs: ServerSocket, Socket)
