import java.io.*;
import java.net.*;
import java.util.*;
import java.math.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.spec.*;
import javax.crypto.*;

/**
 * Thread to deal with clients who connect to Server.  Put what you want the
 * thread to do in it's run() method.
 * @author Karel P. Bergmann
 */

public class ServerThread extends Thread
{
	private Socket sock;  //The socket it communicates with the client on.
	private Server parent;  //Reference to Server object for message passing.
	private int idnum;  //The client's id number.
	private String publicKeyString = "";
	private int state = 0;
	private BigInteger p = null;
	private BigInteger g = null;
	private BigInteger gx = null;
	private BigInteger y = BigInteger.ZERO;
	private BigInteger bigKey = null;
	private String gxString = "";
	private String pString = "";
	private String gString = "";
	private SecretKeySpec key = null; //this is our session key.
	private SecretKeySpec mackey = null;
    private Cipher cipher = null;
    private Mac mac = null;
    private boolean allGood = true;
	
    private int filesize = 0;
    private String filename = "";
	private int position = 0;
	private byte[] filedata = null;
	/**
	 * Constructor, does the usual stuff.
	 * @param s Communication Socket.
	 * @param p Reference to parent thread.
	 * @param id ID Number.
	 */
	public ServerThread (Socket s, Server p, int id)
	{
		parent = p;
		sock = s;
		idnum = id;
	}
	
	/**
	 * Getter for id number.
	 * @return ID Number
	 */
	public int getID ()
	{
		return idnum;
	}
	
	/**
	 * Getter for the socket, this way the parent thread can
	 * access the socket and close it, causing the thread to
	 * stop blocking on IO operations and see that the server's
	 * shutdown flag is true and terminate.
	 * @return The Socket.
	 */
	public Socket getSocket ()
	{
		return sock;
	}
	
	/**
	 * This is what the thread does as it executes.  Listens on the socket
	 * for incoming data and then echos it to the screen.  A client can also
	 * ask to be disconnected with "exit" or to shutdown the server with "die".
	 */
	public void run ()
	{
	    SecureRandom random = null;
		BufferedReader in = null;
		String incoming = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader (new InputStreamReader (sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream());


//          Generate an RSA keypair
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            random = SecureRandom.getInstance("SHA1PRNG");
            keyGen.initialize(1024, random); //use something other than F4?
            KeyPair keys = keyGen.generateKeyPair();

            System.out.println(keys.getPublic().getFormat());
            for (String s : splitString(keys.getPublic().getEncoded())) {
                out.println("PUBKEY:" + s);
                out.checkError();
            }

            out.println("ENDKEY");
            out.checkError();
		}
		catch (UnknownHostException e) {
			System.out.println ("Unknown host error.");
			return;
		}
		catch (IOException e) {
			System.out.println ("Could not establish communication.");
			return;
		}
		catch (NoSuchAlgorithmException e) {
		    System.out.println("No such algo...");
		    return;
		}
		
		/* Try to read from the socket */
		try {
			incoming = in.readLine ();
		}
		catch (IOException e) {
			if (parent.getFlag())
			{
				System.out.println ("shutting down.");
				return;
			}
			return;
		}
		
		/* See if we've recieved something */
		while (incoming != null)
		{
			/* If the client has sent "exit", instruct the server to
			 * remove this thread from the vector of active connections.
			 * Then close the socket and exit.
			 */
			 
			 if (incoming.split(":")[0].compareTo("PUBKEY") == 0) {
			     publicKeyString += incoming.split(":")[1];
			 }
			 
			 else if (incoming.compareTo("ENDKEY") == 0) {
                state = 1; //set state to reflect that we have the key

                //construct a Key object from that key.
                PublicKey remotePubKey = getKeyFromString(publicKeyString);
			 }
			 
			 else if (incoming.split(":")[0].compareTo("P") == 0) {
                 pString += incoming.split(":")[1];
 			 }
             else if (incoming.split(":")[0].compareTo("G") == 0) {
                 gString += incoming.split(":")[1];
 			 }
 			 
 			 else if (incoming.split(":")[0].compareTo("GX") == 0) {
 			     gxString += incoming.split(":")[1];
 			 }
 			 
 			 else if (incoming.split(":")[0].compareTo("ENDP") == 0) {
 			     p = new BigInteger(pString);
 			 }
 			 
 			 else if (incoming.split(":")[0].compareTo("ENDG") == 0) {
 			     g = new BigInteger(gString);
 			 }
 			 
             else if (incoming.split(":")[0].compareTo("ENDGX") == 0) {
                 gx = new BigInteger(gxString);
                 //once we've received GX, compute and transmit GY
                 //let x be a random int 1 < x < p-2

                 while (y.compareTo(p.subtract(new BigInteger("2"))) == 1 || y.compareTo(BigInteger.ZERO) == 0) {
                     y = new BigInteger(p.bitLength(), random);
                 }


                 System.out.println("computing gy...");
                 BigInteger gy =  g.modPow(y,p);
                 System.out.println("Computed gy...");
                 bigKey = gx.modPow(y,p);
                 System.out.println("Big Key:");
                 System.out.println(bigKey);
                 for (String s : splitString(gy.toString())) {
                     out.println("GY:" + s);
                     out.checkError();
                 }
                 out.println("ENDGY");
                 out.checkError();
                 key = new SecretKeySpec(bigKey.toString().getBytes(),100,16,"AES");

                 
                 
                 try {
                     byte[] iv = new byte[16];

                     for (int i = 0; i<16; i++) {
                         iv[i] = bigKey.toString().getBytes()[i+116];//use another part of the random bigkey we agreed on as an iv
                     }

                     AlgorithmParameterSpec cipherParams = new IvParameterSpec(iv);
                     cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                     cipher.init(Cipher.DECRYPT_MODE, key,cipherParams);

                     mackey = new SecretKeySpec(bigKey.toString().getBytes(),100,16,"HmacSHA1");
                     mac = Mac.getInstance("HmacSHA1");


                     mac.init(mackey);


                 } catch (NoSuchAlgorithmException e) {}
                 catch (Exception e) { System.out.println("something went wrong with the cipher/mac creation/init");}
                 
                 
             }
			 
			else if (incoming.compareTo("exit") == 0)
			{
				parent.kill (this);
				try {
					in.close ();
					sock.close ();
				}
				catch (IOException e)
				{/*who cares*/}
				return;
			}
			
			/* If the client has sent "die", instruct the server to
			 * signal all threads to shutdown, then exit.
			 */
			else if (incoming.compareTo("die") == 0)
			{
				parent.killall ();
				return;
			}	
			
			
			
			else { //message is encrypted...
                incoming = decryptNetworkMessage(incoming, cipher,mac);


                if (incoming.split(":")[0].compareTo("FILENAME") == 0) {
                    filename = incoming.split(":")[1];
                }
                
                else if (incoming.split(":")[0].compareTo("FILESIZE") == 0) {
                    filesize = Integer.parseInt(incoming.split(":")[1]);
		            filedata = new byte[filesize];
                }


			    else if (incoming.split(":")[0].compareTo("DATA") == 0) {
			        
			        for (byte b : toByteArray(incoming.split(":")[1])) {
			            filedata[position] = b;
			            position++;
			        }
                }
                
                else if (incoming.split(":")[0].compareTo("ENDDATA") == 0) {
                    if (allGood) {
//                        encryptNetworkMessage("ALLGOOD",cipher, out, mac);
                        FileOutputStream outfile = null;
                            try {
                                try { 
                                    outfile = new FileOutputStream("uploads/" + filename);
                                    System.out.println("Writing data...");
                                    for (byte b : filedata) {
                                        outfile.write(b);
                                    }
                                } finally {
                                    if (out != null) {
                                        outfile.close();
                                        System.out.println ("Wrote " + filename + " to uploads/");
                                    }
                                }
                            } catch (Exception e) { System.out.println(e); }
                    }
                    else {
                        encryptNetworkMessage("NOGOOD",cipher, out, mac);
                    }
                        
                }
                
                
                
                
                incoming = "(Decrypted) " + incoming;
			}
			
			
			
			
			/* Otherwise, just echo what was recieved. */
			System.out.println ("Client " + idnum + ": " + incoming);
			
			/* Try to get the next line.  If an IOException occurs it is
			 * probably because another client told the server to shutdown,
			 * the server has closed this thread's socket and is signalling
			 * for the thread to shutdown using the shutdown flag.
			 */
			try {
				incoming = in.readLine ();
			}
			catch (IOException e) {
				if (parent.getFlag())
				{
					System.out.println ("shutting down.");
					return;
				}
				else
				{
					System.out.println ("IO Error.");
					return;
				}
			}
		}
	}
	
	private static void test(byte[] data) {
        System.out.println("The Key we made is:");
	    for (byte b : data) {
    	    System.out.print(binToHex(b));
	    }
	    System.out.println();
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
	
	private String encryptNetworkMessage(String message, Cipher cipher, PrintWriter out, Mac mac) {
        try {
            byte[] msgBytes = message.getBytes();

	        byte[] encryptedMsg = cipher.doFinal(msgBytes);
            byte[] msgMAC = mac.doFinal(encryptedMsg);	        
            String msg = toHex(encryptedMsg) + ":" + toHex(msgMAC);
            out.println(msg);
            out.checkError();
//            debugOut("CLIENT(Encrypted)>>  : " + message);
            return msg;
        }
        catch (Exception e) {
            System.out.println("Encryption error...");
            System.out.println("Problem: " + message);
            System.out.println(e);}
        return "";
	}
	
	private PublicKey getKeyFromString(String keystring) {
        PublicKey clientKey = null;
        X509EncodedKeySpec remotePubKeySpec = new X509EncodedKeySpec(toByteArray(keystring));
        try {
            KeyFactory keyMaker = KeyFactory.getInstance("RSA"); 
            try { 
            clientKey = keyMaker.generatePublic(remotePubKeySpec);
            }
            catch (InvalidKeySpecException e) { System.out.println("Invalid keyspec..."); }
        }
        catch (NoSuchAlgorithmException e) {  }
        if (clientKey == null) {System.out.println("Something fishy is afoot!");}
        return clientKey;
	}
	
	private byte[] toByteArray(String input) {
        byte[] result = new byte[input.length()/2];
	    for (int i=0; i<input.length(); i+=2) {
	        result[i/2] = (byte)(Integer.parseInt(input.substring(i,i+2),16));
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

	private static String toHex(byte[] data) {
        String result = "";
	    for (byte b : data) {
    	    result += binToHex(b);
	    }
        return result;
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
}
