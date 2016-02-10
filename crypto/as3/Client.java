import java.io.*;
import java.net.*;
import java.math.*;
import java.util.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.spec.*;
import javax.crypto.*;
/**
 * Client program.  Connects to the server and sends text accross.
 * @author Karel P. Bergmann
 */

public class Client 
{
    
    private int state; //state of the system...
    private int fakestate = 0;
	private Socket sock;  //Socket to communicate with.
	private BigInteger x = BigInteger.ZERO;
	private BigInteger p = null;
    private BigInteger g = null;
    private String gyString = "";
    private BigInteger bigKey = null;
    private BigInteger gy = null;
    private static boolean debugging = false;
    private int sessionKey = 0;
	private SecretKeySpec key = null;
	private SecretKeySpec mackey = null;
	private static boolean allGood = true;
	
	byte[] filedata = null;
	private static String filename = "";
	/**
	 * Main method, starts the client.
	 * @param args args[0] needs to be a hostname, args[1] a port number.
	 */
	public static void main (String [] args)
	{
		if (args.length != 2 && args.length != 3)
		{
			System.out.println ("Usage: java Client hostname port#");
			System.out.println ("hostname is a string identifying your server");
			System.out.println ("port is a positive integer identifying the port to connect to the server");
			return;
		}
		
		if (args.length == 3) {
		    if(args[0].compareTo("debug") == 0) { debugging = true; }
    		try {
    			Client c = new Client (args[1], Integer.parseInt(args[2]));
    		}
    		catch (NumberFormatException e) {
    			System.out.println ("Usage: java Client hostname port#");
    			System.out.println ("Second argument was not a port number");
    			return;
    		}
		}
		
		if (args.length == 2) {
		    try {
    			Client c = new Client (args[0], Integer.parseInt(args[1]));
    		}

    		catch (NumberFormatException e) {
    			System.out.println ("Usage: java Client hostname port#");
    			System.out.println ("Second argument was not a port number");
    			return;
    		}
		}

	}
	
	/**
	 * Constructor, in this case does everything.
	 * @param ipaddress The hostname to connect to.
	 * @param port The port to connect to.
	 */
	public Client (String ipaddress, int port)
	{
		/* Allows us to get input from the keyboard. */
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String userinput;
		PrintWriter out;
    	FileInputStream in_file = null;
    	BufferedReader in = null;
    	String incoming = null;
    	String serverPubKeyString = "";
    	PublicKey remotePubKey = null;

        /* We open a file for reading and load it into a byte array */

        System.out.print("Please enter the path to the file you wish to upload: ");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             String name = null;
             try {
               filename = br.readLine();
             } catch (IOException e) {
               System.out.println("You broke it.");
               System.exit(1);
             }

        try{
        	//open files
            in_file = new FileInputStream(filename);
        	//read file into a byte array
            filedata = new byte[in_file.available()];
        	int read_bytes = in_file.read(filedata);
        }
        catch(Exception e){
        	System.out.println(e);
        }
        finally{
        	if (in_file != null){
        		try { in_file.close(); }
        		catch (Exception e) {}
        	}
        }
        

		
		/* Try to connect to the specified host on the specified port. */
		try {
			sock = new Socket (InetAddress.getByName(ipaddress), port);
		}
		catch (UnknownHostException e) {
			System.out.println ("Usage: java Client hostname port#");
			System.out.println ("First argument is not a valid hostname");
			return;
		}
		catch (IOException e) {
			System.out.println ("Could not connect to " + ipaddress + ".");
			return;
		}
		
		/* Status info */
		System.out.println ("Connected to " + sock.getInetAddress().getHostAddress() + " on port " + port);
		
		try {
			out = new PrintWriter(sock.getOutputStream());
			in = new BufferedReader (new InputStreamReader (sock.getInputStream()));
		}
		catch (IOException e) {
			System.out.println ("Could not create output stream.");
			return;
		}
		
		/* Wait for the user to type stuff. */
		try {

//            gotta come up with our public/private keypair, and send the public key to the server.

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
//            random.setSeed(System.currentTimeMillis());

            
            
            keyGen.initialize(1024, random); //use something other than F4?
            KeyPair keys = keyGen.generateKeyPair();

            for (String s : splitString(keys.getPublic().getEncoded())) {
                out.println("PUBKEY:" + s);
                out.checkError();
                debugOut("CLIENT>> PUBKEY:" + s);
            }
            
            out.println("ENDKEY");
            out.checkError();
            debugOut("CLIENT>> " + "ENDKEY");

            //okay so here we have to implement STS protocol to mutually authenticate and establish a session key.
            //Client can be A, server will be B
            //Step 1/3: Send the server g^x where x is a random number.

            while (fakestate < 1) { //busy wait for a public key from the server
                try {
        			incoming = in.readLine ();
        		} catch (Exception e) {}
        		if (incoming != null) {
        		    

        		    if (incoming.length() >= 7 && incoming.substring(0,7).compareTo("PUBKEY:") == 0) {
                        serverPubKeyString += incoming.split(":")[1];
    		        }
//                    if (incoming.split(":")[0])


        		    else if (incoming.compareTo("ENDKEY") == 0) {
                        remotePubKey = getKeyFromString(serverPubKeyString);
        		        fakestate++;
        		    }
        		    
        		    
                    
        		    
        		    debugOut("SERVER>> " + incoming);
        		    incoming = null;
        		}
            }
            

            //now it's time to do the key agreement:

            DHParams params = new DHParams();
            g = params.specs().getG();
            p = params.specs().getP();


            for (String s : splitString(p.toString())) {
                out.println("P:" + s);
                out.checkError();
                debugOut("CLIENT>>      P:" + s);
            }
            out.println("ENDP");
            out.checkError();
            debugOut("CLIENT>> ENDP");
            
            
            for (String s : splitString(g.toString())) {
                out.println("G:" + s);
                out.checkError();
                debugOut("CLIENT>>      G:" + s);
            }
            out.println("ENDG");
            out.checkError();
            debugOut("CLIENT>> ENDG");

            //let x be a random int 1 < x < p-2
            while (x.compareTo(p.subtract(new BigInteger("2"))) == 1 || x.compareTo(BigInteger.ZERO) == 0) {
                x = new BigInteger(p.bitLength(), random);
            }
            
            BigInteger gx =  g.modPow(x,p);
            
            for (String s : splitString(gx.toString())) {
                out.println("GX:" + s);
                out.checkError();
                debugOut("CLIENT>>     GX:" + s);
            }
            out.println("ENDGX");
            out.checkError();
            debugOut("CLIENT>> ENDGX");
            

            while (fakestate < 2) { //busy wait for a GY from the server
                try {
        			incoming = in.readLine ();
        		} catch (Exception e) {}
        		if (incoming != null) {


        		    if (incoming.split(":")[0].compareTo("GY") == 0) {
                        gyString += incoming.split(":")[1];
    		        }
//                    if (incoming.split(":")[0])


        		    else if (incoming.compareTo("ENDGY") == 0) {
                        gy = new BigInteger(gyString);
                        fakestate++;
        		    }

        		    debugOut("SERVER>> " + incoming);
        		    incoming = null;
        		}
            }

            

            bigKey = gy.modPow(x,p);

            key = new SecretKeySpec(bigKey.toString().getBytes(),100,16,"AES");
            mackey = new SecretKeySpec(bigKey.toString().getBytes(),100,16,"HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(mackey);


            byte[] iv = new byte[16];

            for (int i = 0; i<16; i++) {
                iv[i] = bigKey.toString().getBytes()[i+116]; //use another part of the random bigkey we agreed on as an iv
            }

            AlgorithmParameterSpec cipherParams = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key,cipherParams);

            sendFileInfo(out,filedata,cipher,mac);


            sendFile(out, filedata, cipher, mac); //send the file.
            System.out.println("Finished uploading file.");
            // while (fakestate < 3) {
            //     try {
            //         incoming = in.readLine();
            //     } catch (Exception e) {}
            //     
            //     if (incoming != null) {
            //             incoming = decryptNetworkMessage(incoming,cipher,mac);
            // 
            // 
            //             if (incoming.split(":")[0].compareTo("ALLGOOD") == 0) {
            //                 fakestate++;
            //             }
            //             
            //             incoming = "(Decrypted) " + incoming;   
            //     }
            // }



		} catch (Exception e) {
			System.out.println (e);
			return;
		}		
	}
		
	private void sendFile(PrintWriter out, byte[] filedata, Cipher cipher, Mac mac) {
        for (String s : splitString(filedata)) {
            encryptNetworkMessage("DATA:" + s, cipher,out,mac);
        }
        encryptNetworkMessage("ENDDATA",cipher,out,mac);
	}
	
	private void sendFileInfo(PrintWriter out, byte[] filedata, Cipher cipher, Mac mac) {
	    encryptNetworkMessage("FILESIZE:" + filedata.length,cipher,out,mac);
	    encryptNetworkMessage("FILENAME:" + filename.split("/")[filename.split("/").length-1],cipher,out,mac);
	}
	
	private String encryptNetworkMessage(String message, Cipher cipher, PrintWriter out, Mac mac) {
        try {
            byte[] msgBytes = message.getBytes();
	        byte[] encryptedMsg = cipher.doFinal(msgBytes);
            byte[] msgMAC = mac.doFinal(encryptedMsg);
            String msg = toHex(encryptedMsg) + ":" + toHex(msgMAC);
            out.println(msg);
            out.checkError();
            debugOut("CLIENT(Encrypted)>>  : " + message);
            return msg;
        }
        catch (Exception e) {System.out.println("Encryption error...");}
        return "";
	}
	
	
	private String decryptNetworkMessage(String message, Cipher cipher, Mac mac) {
        try {
            String msg = message.split(":")[0];
            String msgMac = message.split(":")[1];
            String compMac = "";
            String result = "";
            byte[] decryptedMsg = cipher.doFinal(toByteArray(msg));

            
            
            compMac = toHex(mac.doFinal(toByteArray(msg)));
            if (compMac.compareTo(msgMac) != 0) {
                System.out.println("MESSAGE FAILED INTEGRITY CHECK!");
                allGood = false;
            }
            result = new String(decryptedMsg);
            
            
            return result;


        }
        catch (Exception e) {System.out.println("Encryption error...");}
        return "";
	}
	private ArrayList<String> splitString(byte[] data) {
	    ArrayList<String> result = new ArrayList<String>();
	    String nextString = "";
        boolean allDone = false;
        int resultsize = 0;

	    for (int i=0; i<data.length; i++) {
	        allDone = false;
	        nextString += binToHex(data[i]);
	        if (nextString.length() == 32) {
	            result.add(new String(nextString));
	            resultsize += nextString.length();
	            nextString = "";
	            allDone = true;
	        }
	    }
	    if (!allDone) {
	        result.add(new String(nextString));
            resultsize += nextString.length();
	    }
	    return result;
	}
	
	private ArrayList<String> splitString(String data) {
	    ArrayList<String> result = new ArrayList<String>();
	    String nextString = "";
        boolean allDone = false;
        int resultsize = 0;

	    for (int i=0; i<data.length(); i++) {
	        allDone = false;
	        nextString += data.charAt(i);
	        if (nextString.length() == 32) {
	            result.add(new String(nextString));
	            resultsize += nextString.length();
	            nextString = "";
	            allDone = true;
	        }
	    }
	    if (!allDone) {
	        result.add(new String(nextString));
            resultsize += nextString.length();
	    }
	    return result;
	}
	
	private static String binToHex(byte data) {
	    String[] chars = {  "0", "1", "2", "3",
	                        "4", "5", "6", "7",
	                        "8", "9", "A", "B",
	                        "C", "D", "E", "F"  };

	    return (chars[(data & 0xf0) >> 4]) + (chars[data & 0x0f]);
	}
	private static String toHex(byte[] data) {
        String result = "";
	    for (byte b : data) {
    	    result += binToHex(b);
	    }
        return result;
	}	
	
	private PublicKey getKeyFromString(String keystring) {
        PublicKey clientKey = null;
        X509EncodedKeySpec remotePubKey = new X509EncodedKeySpec(toByteArray(keystring));
        try {
            KeyFactory keyMaker = KeyFactory.getInstance("RSA"); 
            try { 
            clientKey = keyMaker.generatePublic(remotePubKey);
            }
            catch (InvalidKeySpecException e) { }
        }
        catch (NoSuchAlgorithmException e) {  }
        if (clientKey == null) {System.out.println("Something fishy is afoot!");}
        return clientKey;
	}
	private static void debugOut(String message) {
	    if (debugging) {
	        System.out.println(message);
	    }
	}
	private byte[] toByteArray(String input) {
        byte[] result = new byte[input.length()/2];
	    for (int i=0; i<input.length(); i+=2) {
	        result[i/2] = (byte)(Integer.parseInt(input.substring(i,i+2),16));
	    }
	    return result;
	}
}
