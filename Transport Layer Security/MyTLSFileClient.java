// MIN SOE HTUT 
// 16311938

// The client is usually much more straight forward
// Defaults will load Java’s set of Trusted Certificates
// Java will validate there is a path to a trusted CA
// By default, Java will NOT do hostname validation,
// but the more secure thing to do is to check!

// THE CODE BELOW IS INCOMPLETE AND HAS PROBLEMS
// FOR EXAMPLE, IT IS MISSING THE NECESSARY EXCEPTION HANDLING

import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.security.cert.X509Certificate;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;


public class MyTLSFileClient {
  public static void main(String args[])
  {
    // Validate the number of arguments (hostname, port, and file to retrieve)
    if (args.length != 3) {
      System.err.println("Usage: java MyTLSFileClient <hostname> <port> <file>");
      return;
    }
    
    // Get the server hostname, port, and the requested file from command-line arguments
    String host = args[0];  
    int port = Integer.parseInt(args[1]);  
    String file = args[2];  

    try {

      // Step 2: Create an SSLSocket to connect to the server
      SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
      SSLSocket socket = (SSLSocket)factory.createSocket(host, port);

      // set HTTPS-style checking of HostName _before_ 
      // the handshake
      //SSLParameters params = new SSLParameters();
      //params.setEndpointIdentificationAlgorithm("HTTPS");
      //socket.setSSLParameters(params);

      //socket.startHandshake(); // explicitly starting the TLS handshake
      //System.out.println("TLS handshake completed");

      // at this point, can use getInputStream and 
      // getOutputStream methods as you would in a regular Socket

      // get the X509Certificate for this session
      //SSLSession session = socket.getSession();
      //X509Certificate cert = (X509Certificate) session.getPeerCertificates()[0];

      // extract the CommonName, and then compare
      //String commonName = getCommonName(cert);
      //System.out.println("Connected to server with Common Name: " + commonName);

      // Send the file name to the server
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      writer.write(file + "\n");  // Send file name
      writer.flush();

      // Receive the file and save it locally
      try (InputStream in = socket.getInputStream();
      FileOutputStream fileOut = new FileOutputStream("_" + file)) {

      byte[] buffer = new byte[4096];// Buffer for reading 
      int bytesRead;
      // Read the file data from the server and write it to the local file
      while ((bytesRead = in.read(buffer)) != -1) {
          fileOut.write(buffer, 0, bytesRead);  // Write data to the local file
      }
      System.out.println("File received and saved as: _" + file);// Confirm that the file is saved
      }
      socket.close(); // Close the connection 
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String getCommonName(X509Certificate cert) throws Exception
  {
    String name = cert.getSubjectX500Principal().getName();
    LdapName ln = new LdapName(name);
    String cn = null;
    
    // Rdn: Relative Distinguished Name
    for(Rdn rdn : ln.getRdns()) 
      if("CN".equalsIgnoreCase(rdn.getType()))
        cn = rdn.getValue().toString();
    return cn;
  }
}
