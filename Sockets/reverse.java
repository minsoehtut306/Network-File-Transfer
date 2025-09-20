import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * A simple utility that resolves IP addresses to their hostnames.
 */
public class reverse {
    public static void main(String[] args) {

        // Require at least one IP address as an argument
        if (args.length == 0) {
            System.err.println("Usage: java reverse <ip1> <ip2> ... <ipN>");
            return;
        }

        // Attempt to resolve each IP address
        for (String ip : args) {
            try {
                InetAddress ia = InetAddress.getByName(ip);
                String hostName = ia.getHostName();

                // If no hostname is found, indicate this
                if (hostName.equals(ip)) {
                    System.out.println(ip + " : no name");
                } else {
                    System.out.println(ip + " : " + hostName);
                }
            } catch (UnknownHostException e) {
                System.err.println(ip + " : unknown host");
            }
        }
    }
}
