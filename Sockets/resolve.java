import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * A simple utility that resolves hostnames to their IP addresses.
 */
public class resolve {
    public static void main(String[] args) {

        // Require at least one hostname as an argument
        if (args.length == 0) {
            System.err.println("Usage: java resolve <name1> <name2> ... <nameN>");
            return;
        }

        // Attempt to resolve each hostname provided
        for (String hostname : args) {
            try {
                InetAddress ia = InetAddress.getByName(hostname);
                String ip = ia.getHostAddress();
                System.out.println(hostname + " : " + ip);
            } catch (UnknownHostException e) {
                System.err.println(hostname + " : unknown host");
            }
        }
    }
}
