import java.net.*;
import java.io.*;
import java.util.*;

class TftpServerWorker extends Thread {
    private DatagramPacket req;
    private static final byte RRQ = 1;
    private static final byte DATA = 3;
    private static final byte ACK = 4;
    private static final byte ERROR = 5;

    //This method is send the requested file
    private void sendFile(String filename) 
    {
        try {
            File file = new File(filename); //Open the request file 

            // If the file doesn't exist or isn't readable, send an error packet
            if (!file.exists() || !file.canRead()) {
                System.out.println("[SERVER] File not found: " + filename);
                sendError(1, "File not found");
                return;
            }
            FileInputStream fis = new FileInputStream(file); // Open the file for reading
            byte[] buffer = new byte[512];
            int blockNumber = 1;
            int bytesRead;
            boolean transferComplete = false;
            DatagramSocket socket = new DatagramSocket();
            InetAddress clientAddress = req.getAddress();
            int clientPort = req.getPort();
            //While the transfer is not complete 
            while (!transferComplete) {
                bytesRead = fis.read(buffer);
                // if the end of file is reached,end the loop
                if (bytesRead == -1) {
                    System.out.println("[SERVER] End of file reached, transfer complete.");
                    break;
                }
                boolean ackReceived = false;
                while (!ackReceived) {
                    // Send the current data block to the client
                    sendDataBlock(buffer, bytesRead, blockNumber, clientAddress, clientPort, socket);
                    // Wait for the client to send an ACK for the block
                    ackReceived = waitForAck(blockNumber, clientAddress, clientPort, socket);
                    // If no ACK is received, resend the block
                    if (!ackReceived) {
                        System.out.println("[SERVER] Timeout or no ACK received, resending block #" + blockNumber);
                    }
                }
                // Check if the current block is less than 512 bytes it is the last block
                if (bytesRead < 512) {
                    System.out.println("[SERVER] Last block sent with " + bytesRead + " bytes.");
                    transferComplete = true;
                }
                blockNumber++;
            }
            fis.close();// Close the file input stream
            socket.close();// Close the socket
            System.out.println("[SERVER] File transfer complete for file: " + filename);
        } catch (IOException e) {
            System.err.println("[SERVER] Error sending file: " + e.getMessage());
        }
    }

    // The run method is executed when the worker thread starts
    public void run() 
    {
        try {
            // Print information about the received request, including the client's IP address and port
            System.out.println("[SERVER] New request received from: " + req.getAddress() + ":" + req.getPort());
            // Extract the data from the request packet RRQ
            byte[] data = req.getData();
            int len = req.getLength();
            System.out.println("[SERVER] Request data length: " + len);
            // Extract the opcode from the second byte of the request packet
            int opcode = data[1];
            // If it's a Read Request (RRQ), process it
            if (opcode == RRQ) {
                // Extract filename from RRQ
                String filename = extractFilename(data, len);
                // If a valid filename was extracted, proceed to send the file
                if (filename != null) {
                    System.out.println("[SERVER] Received RRQ for file: " + filename);
                    // Send the file
                    sendFile(filename);
                } else {
                    // If the filename is invalid, send an error packet
                    System.out.println("[SERVER] Invalid filename in RRQ.");
                    sendError(4, "Invalid filename.");
                }
            } else {
                // If the request type is unsupported, send an error packet
                System.out.println("[SERVER] Unsupported request type: " + opcode);
                sendError(4, "Unsupported request.");
            }
        } catch (Exception e) {
            System.err.println("[SERVER] Error in TftpServerWorker: " + e.getMessage());
        }
    }

    // Helper method to extract the filename from the RRQ packet
    private String extractFilename(byte[] data, int len) 
    {
        StringBuilder filename = new StringBuilder();
        int index = 2; // Filename starts after the first two bytes (opcode)
        // Loop through the packet to extract the filename, stopping at the null byte
        while (index < len && data[index] != 0) {
            filename.append((char) data[index]);
            index++;
        }
        // If no null-terminator was found, the filename is invalid
        if (index == len) {
            System.out.println("[SERVER] Reached end of packet without null terminator, invalid RRQ.");
            return null; // Invalid if we don't get a null-terminated string
        }
        System.out.println("[SERVER] Extracted filename: " + filename.toString());
        return filename.toString();
    }

    // Helper method to send a block of data to the client
    private void sendDataBlock(byte[] buffer, int bytesRead, int blockNumber, InetAddress clientAddress, int clientPort, DatagramSocket socket) throws IOException 
    {
         // Create a packet to hold the TFTP data block (4-byte header + file data)
        byte[] dataPacket = new byte[4 + bytesRead];
        dataPacket[0] = 0; // TFTP header (first byte)
        dataPacket[1] = DATA; // Opcode for data packet (3)
        dataPacket[2] = (byte) (blockNumber >> 8); // Block number high byte
        dataPacket[3] = (byte) (blockNumber & 0xFF); // Block number low byte
        // Copy the file data into the packet
        System.arraycopy(buffer, 0, dataPacket, 4, bytesRead);
        // Create a DatagramPacket to send the data to the client
        DatagramPacket sendPacket = new DatagramPacket(dataPacket, dataPacket.length, clientAddress, clientPort);
        System.out.println("[SERVER] Sending block #" + blockNumber + " (" + bytesRead + " bytes)");
        // Send the data packet
        socket.send(sendPacket);
    }

    // Helper method to wait for the ACK packet
    private boolean waitForAck(int blockNumber, InetAddress clientAddress, int clientPort, DatagramSocket socket) 
    {
        try {
            byte[] ackBuffer = new byte[4]; // ACK packet is 4 bytes
            DatagramPacket ackPacket = new DatagramPacket(ackBuffer, ackBuffer.length);
            socket.setSoTimeout(5000); // Set a timeout of 5 seconds
            socket.receive(ackPacket);
            // Parse the ACK packet
            int opcode = ackBuffer[1]; // Opcode is in the second byte
            int ackBlockNumber = ((ackBuffer[2] & 0xFF) << 8) | (ackBuffer[3] & 0xFF);
            // If the ACK is for the current block, return true
            if (opcode == ACK && ackBlockNumber == blockNumber) {
                System.out.println("[SERVER] Received ACK for block #" + blockNumber);
                return true; // Correct ACK received
            } else {
                // If the ACK is invalid, print an error message
                System.out.println("[SERVER] Received invalid ACK or incorrect block number.");
            }
        } catch (SocketTimeoutException e) {
            // If a timeout occurs, print an error message
            System.out.println("[SERVER] Timeout waiting for ACK for block #" + blockNumber);
        } catch (IOException e) {
            System.err.println("[SERVER] Error receiving ACK: " + e.getMessage());
        }
        return false; // Return false if no valid ACK was received
    }

    // Helper method to send an error packet
    private void sendError(int errorCode, String errorMsg) 
    {
        try {
            byte[] errMsgBytes = errorMsg.getBytes();// Convert the error message to bytes
            byte[] errorPacket = new byte[4 + errMsgBytes.length + 1];
            errorPacket[0] = 0;
            errorPacket[1] = ERROR;// Opcode for error packet (5)
            errorPacket[2] = 0;
            errorPacket[3] = (byte) errorCode;
            // Copy the error message into the error packet
            System.arraycopy(errMsgBytes, 0, errorPacket, 4, errMsgBytes.length);
            errorPacket[errorPacket.length - 1] = 0; // Null-terminate error message
            DatagramPacket packet = new DatagramPacket(errorPacket, errorPacket.length, req.getAddress(), req.getPort());
            DatagramSocket socket = new DatagramSocket();
            System.out.println("[SERVER] Sending error packet: " + errorMsg);
            socket.send(packet);
        } catch (IOException e) {
            System.err.println("[SERVER] Error sending error packet: " + e.getMessage());
        }
    }
    public TftpServerWorker(DatagramPacket req) {
        this.req = req;
    }
}

// The main TftpServer class that listens for incoming packets and spawns worker threads
class TftpServer {
    public void start_server() {
        try {
            // Create a DatagramSocket to listen for incoming packets
            DatagramSocket ds = new DatagramSocket();
            System.out.println("TftpServer on port " + ds.getLocalPort());
            // Infinite loop to keep the server running and listening for requests
            for(;;) {
                // Create a buffer to hold the incoming data packet
                byte[] buf = new byte[1472]; // Standard UDP packet size
                DatagramPacket p = new DatagramPacket(buf, 1472);
                // Block and wait for an incoming packet (this is where the server listens)
                ds.receive(p);
                // Spawn a new TftpServerWorker thread to handle the request
                TftpServerWorker worker = new TftpServerWorker(p);
                worker.start();
            }
        } catch (Exception e) {
            // Print any exceptions that occur
            System.err.println("Exception: " + e);
        }
    }
    
    public static void main(String args[]) {
        TftpServer d = new TftpServer();
        d.start_server();
    }
}
