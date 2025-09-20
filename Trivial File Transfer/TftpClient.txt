import java.net.*;
import java.io.*;
import java.util.Scanner;

public class TftpClient {
    private static final byte RRQ = 1;
    private static final byte DATA = 3;
    private static final byte ACK = 4;
    private static final byte ERROR = 5;

    public static void main(String[] args) {
        // Create a scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Ask for the server port (IP is hardcoded as localhost)
        System.out.print("Enter the server port number: ");
        int port = Integer.parseInt(scanner.nextLine());

        // Ask for the filename on the server
        System.out.print("Enter the filename you want to download: ");
        String filename = scanner.nextLine();

        // Set the local filename to "tftp_<filename>"
        String localFilename = "tftp_" + filename;

        // Begin the file transfer process
        try {
            InetAddress serverAddress = InetAddress.getByName("127.0.0.1"); // Fixed IP as localhost
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(5000);  // Set a timeout of 5 seconds for receiving packets

            // Send RRQ (Read Request) to the server
            byte[] rrqPacket = createRRQ(filename);
            DatagramPacket sendPacket = new DatagramPacket(rrqPacket, rrqPacket.length, serverAddress, port);
            socket.send(sendPacket);
            System.out.println("[CLIENT] Sent RRQ for file: " + filename);

            // Initialize variables for receiving data
            byte[] buffer = new byte[516]; // 512 bytes of data + 4 bytes of TFTP header
            boolean lastPacket = false;
            int expectedBlock = 1;
            FileOutputStream fos = null; // Delay creating the file until valid data is received

            while (!lastPacket) {
                // Receive data packet
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(receivePacket);
                } catch (SocketTimeoutException e) {
                    System.out.println("[CLIENT] Timeout: No response from the server.");
                    socket.close();
                    return;
                }

                // Parse the received packet
                int opcode = buffer[1]; // Opcode is in the second byte
                int blockNumber = ((buffer[2] & 0xFF) << 8) | (buffer[3] & 0xFF); // Block number

                if (opcode == DATA && blockNumber == expectedBlock) {
                    // Delay file creation until we receive valid data
                    if (fos == null) {
                        fos = new FileOutputStream(localFilename); // Create the file once we receive the first data packet
                    }

                    int dataSize = receivePacket.getLength() - 4;  // Calculate the actual data size

                    // Write the received data to the local file
                    fos.write(buffer, 4, dataSize);
                    System.out.println("[CLIENT] Received block #" + blockNumber + " (" + dataSize + " bytes)");

                    // Send ACK for the received block
                    sendAck(blockNumber, serverAddress, receivePacket.getPort(), socket);
                    System.out.println("[CLIENT] Sent ACK for block #" + blockNumber);

                    // If the packet size is less than 512, it's the last block
                    if (dataSize < 512) {
                        System.out.println("[CLIENT] Last block received with " + dataSize + " bytes.");
                        lastPacket = true;
                    }

                    expectedBlock++;
                } else if (opcode == ERROR) {  // If an error packet is received
                    System.out.println("[CLIENT] Error received from server: " + new String(buffer, 4, receivePacket.getLength() - 4));
                    socket.close();
                    return;  // Exit without creating a file
                } else if (blockNumber < expectedBlock) {  // If a duplicate block is received (retransmission)
                    System.out.println("[CLIENT] Duplicate block #" + blockNumber + " received, sending ACK.");
                    sendAck(blockNumber, serverAddress, receivePacket.getPort(), socket);
                } else {
                    System.out.println("[CLIENT] Received unexpected block number.");
                }
            }

            // Close the file and socket after the transfer is complete
            if (fos != null) {
                fos.close();
            }
            socket.close();

            // Check if the file is empty after downloading
            if (fos != null) {  // Only check the file if it was created
                File downloadedFile = new File(localFilename);
                if (downloadedFile.length() == 0) {
                    System.out.println("[CLIENT] File transfer complete, but the file is empty!");
                } else {
                    System.out.println("[CLIENT] File transfer complete. Saved as: " + localFilename);
                }
            }
        } catch (Exception e) {
            System.out.println("[CLIENT] There was an error: " + e.getMessage());
        }
    }

    // Helper method to create an RRQ (Read Request) packet
    private static byte[] createRRQ(String filename) {
        ByteArrayOutputStream rrq = new ByteArrayOutputStream();
        rrq.write(0);
        rrq.write(RRQ); // Opcode 1 for RRQ
        try {
            rrq.write(filename.getBytes());
            rrq.write(0); // Null-terminated filename
            rrq.write("octet".getBytes()); // Mode: octet (binary)
            rrq.write(0); // Null-terminated mode
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rrq.toByteArray();
    }

    // Helper method to send an ACK packet
    private static void sendAck(int blockNumber, InetAddress address, int port, DatagramSocket socket) throws IOException {
        byte[] ack = new byte[4];
        ack[0] = 0;
        ack[1] = ACK; // Opcode for ACK
        ack[2] = (byte) (blockNumber >> 8);
        ack[3] = (byte) (blockNumber & 0xFF);

        DatagramPacket ackPacket = new DatagramPacket(ack, ack.length, address, port); // Use the same port from which the data packet was received
        socket.send(ackPacket);
    }
}
