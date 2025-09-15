// MIN SOE HTUT 
// 16311938

// Java provides SSLSocket and SSLServerSocket classes, which are roughly 
// equivalent to Socket and ServerSocket:
//       SSLServerSocket listens on a port for incoming connections, like ServerSocket
//       SSLSocket connects to an SSLServerSocket, like Socket, and represents an individual 
//       connection accepted from an SSLServerSocket.
// To create a SSLSocket or SSLServerSocket, we must use "factories"

// Socket factories are a convenient way to set TLS parameters that will 
// apply to Sockets created from the factory, e.g:
//       Which TLS versions to support
//       Which Ciphers and Hashes to use
//       Which Keys to use and which Certificates to trust
// As you might guess by the names
//       SSLServerSocketFactory creates SSLServerSocket objects
//       SSLSocketFactory creates SSLSocket objects

// Java uses KeyStore objects to store Keys and Certificates
// A KeyStore object is used when encrypting and authenticating
// The files that contain Keys and Certificates are password protected

// THE CODE BELOW IS INCOMPLETE AND HAS PROBLEMS
// FOR EXAMPLE, IT IS MISSING THE NECESSARY EXCEPTION HANDLING

import java.io.*;
import java.security.KeyStore;
import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.security.NoSuchAlgorithmException;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.UnrecoverableKeyException;
import java.security.KeyManagementException;

public class MyTLSFileServer {
    // Method to set up the SSL context and create an SSLServerSocketFactory
   private static ServerSocketFactory getSSF() throws Exception
   {
      // Get 
      //    an SSL Context that speaks some version of TLS, 
      //    a KeyManager that can hold certs in X.509 format,  
      //    and a JavaKeyStore (JKS) instance   
      SSLContext ctx = SSLContext.getInstance("TLS");
      KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
      KeyStore ks = KeyStore.getInstance("JKS");

      // Store the passphrase to unlock the JKS file.   
      // Securely get passphrase
      Console console = System.console();
      char[] passphrase = console.readPassword("Enter passphrase for server.jks: ");

      // Load the keystore file. The passphrase is   
      // an optional parameter to allow for integrity   
      // checking of the keystore. Could be null   
      ks.load(new FileInputStream("server.jks"), passphrase);

      // Init the KeyManagerFactory with a source   
      // of key material. The passphrase is necessary   
      // to unlock the private key contained.   
      kmf.init(ks, passphrase);

      // initialise the SSL context with the keys.   
      ctx.init(kmf.getKeyManagers(), null, null);

      // Get the factory we will use to create   
      // our SSLServerSocket   
      SSLServerSocketFactory ssf = ctx.getServerSocketFactory();
      return ssf;
   }

   public static void main(String args[]) 
   { 
      try{
      // use the getSSF method to get a  SSLServerSocketFactory and 
      // create our  SSLServerSocket, bound to specified port  
      SSLServerSocket ss =  (SSLServerSocket) getSSF().createServerSocket(50202);

      // Enable specific TLS protocols (TLSv1.2 and TLSv1.3)
      String EnabledProtocols[] = {"TLSv1.2", "TLSv1.3"}; 
      ss.setEnabledProtocols(EnabledProtocols); 

      System.out.println("TLS Server listening on port : 50202...");
      
      // Continuously accept client connections in a loop
      while (true) {
        // Accept an incoming client connection
         SSLSocket socket = (SSLSocket) ss.accept();
         System.out.println("Client connected");

         // Receive the requested file name from the client
         BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         String requestedFile = reader.readLine();  // Get requested file name
         File file = new File(requestedFile);

         // Check if the file exists and is not a directory
         if (!file.exists() || file.isDirectory()) {
             System.out.println("File not found or invalid: " + requestedFile);
             socket.close();
             continue;
         }

         // Send the requested file to the client
         try (FileInputStream fileIn = new FileInputStream(file);
              OutputStream out = socket.getOutputStream()) {

             byte[] buffer = new byte[4096]; // Buffer for reading file data
             int bytesRead;

             // Read the file and send the contents to the client
             while ((bytesRead = fileIn.read(buffer)) != -1) {
                 out.write(buffer, 0, bytesRead);  // Send the file 
             }
             System.out.println("File sent: " + requestedFile);
         }
         
         socket.close();  // Close connection
     }
 } catch (Exception e) {
     e.printStackTrace();
 }
}
}


