// Name : MIN SOE HTUT 
// Student ID : 1631938

import java.net. InetAddress;
import java.net.UnknownHostException;

public class reverse{
    public static void main(String[] args){

        //Check the input and print out error if has no input 
        if (args.length == 0){
            System.err.println("Usage: reverse <ip1> <ip2> ... <ipN>");
            return;
        }

        //Check the input of the IP address
        for (String ip : args){
            InetAddress ia;
            try {
                // get the Address object for the IP address
                ia = InetAddress.getByName(ip);
                // If the IP address has error print an error message
            } catch (UnknownHostException e){
                System.err.println(ip + " : unknown host");
                return; 
            }

            // get the host name from the IP address
            String hostName = ia.getHostName();

            // Check if the host name is the same as the IP address
            if (hostName.compareTo(ip) == 0){
                System.out.println(ip + " : no name");
            } else {
                // Print the IP address and host name
                System.out.println(ip + " : " + hostName);
            }
        }
    }
}

