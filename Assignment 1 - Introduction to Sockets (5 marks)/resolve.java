// Name : MIN SOE HTUT 
// Student ID : 1631938

import java.net. InetAddress;
import java.net.UnknownHostException;

public class resolve{
    public static void main(String[] args){
    
        //Check the input and print out error if has no input 
        if (args.length == 0){
            System.err.println("Usage: resolve <name1> <name2> ... <nameN>");
            return;
        }
        
        //Check the input of the host name 
        for (String hostname : args){
            try{
                //If the host name is correct get the IP daaress 
                InetAddress ia = InetAddress.getByName(hostname);
                String ip = ia.getHostAddress();
                System.out.println(hostname + " : " + ip);
                //If the host name is not correct 
            }catch (UnknownHostException e){
                    System.err.println(hostname + ": unknown host");
                    return;
            }

        }

    }
    
}
