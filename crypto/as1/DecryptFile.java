import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.LinkedList;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DecryptFile {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        if (args.length != 2) {
            System.out.println("Usage: java DecryptFile <filename> <key seed>");
            System.exit(-1);
        }
        FileInputStream in = null;
        FileOutputStream out = null;
        LinkedList<Byte> filelist = new LinkedList<Byte>();
        MessageDigest digestor = MessageDigest.getInstance("SHA-256");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(args[1].getBytes()); //seed our prng with the user input
        byte[] iv ={0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
                    0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00}; //dummy iv
                    
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(128, random);
        SecretKey key = keygen.generateKey(); //get a key...

                
        try {
            in = new FileInputStream(args[0]);
            out = new FileOutputStream(args[0].substring(0,args[0].length()-4));
            int c;

            while ((c = in.read()) != -1) { //load the file into our linked
                //                            list of bytes...
                filelist.add(new Byte((byte) c));
            }
            
            byte[] encryptedfilebytes = new byte[filelist.size()];
            for (int i = 0; i<filelist.size(); i++) {
                encryptedfilebytes[i] = filelist.get(i).byteValue();
            }


//decrypt file...
            byte[] decryptedfile;
            decryptedfile = null;
            try {

                AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
                decryptedfile = cipher.doFinal(encryptedfilebytes);

            } catch (NoSuchPaddingException e) {
            } catch (InvalidKeyException e) {
            } catch (IllegalBlockSizeException e) {
                System.out.println("The specified file is not the right size. This probably means someone has tampered with it. Exiting.");
                System.exit(-1);
            } catch (BadPaddingException e) {
                System.out.println("You probably specified the wrong password for this file.");
                System.exit(-1);
            } catch (InvalidAlgorithmParameterException e) {
            }

//            LinkedList<Byte> message = new LinkedList<Byte>();
//            LinkedList<Byte> givenhash = new LinkedList<Byte>();



            byte[] filebytes = new byte[decryptedfile.length-32];
            byte[] givenhash = new byte[32];
            
            for (int i=0; i < decryptedfile.length-32; i++) {
                filebytes[i] = decryptedfile[i];
//                message.add(decryptedfile[i]);
            }
            for (int i = decryptedfile.length - 32; i<decryptedfile.length; i++) {
                givenhash[i-(decryptedfile.length-32)] = decryptedfile[i];
//                givenhash.add(decryptedfile[i]);
            }
            
            
            // Byte[] file = message.toArray(new Byte[message.length]);
            // //make that linked list an array because we have to iterate
            // //over it a bit.
            // byte[] filebytes = new byte[message.size()];
            // 
            // for (int i = 0; i < message.size(); i++) {
            //     filebytes[i] = file[i].byteValue();
            // }

            digestor.update(filebytes);
            byte[] computedhash = digestor.digest();


            for (int i = 0; i < filebytes.length; i++) {
                out.write(filebytes[i]);
            }
            
            
            System.out.println("Computing file digest, and comparing to digest provided.");
            for (int i = 0; i < computedhash.length; i++) { //compare the given hash to
                //                  the one we just computed.
                if (computedhash[i] == givenhash[i]) {
                    System.out.print(".");
                }
                else {
                    System.out.println("Uh oh! The computed digest of that file does not match the one found in the file. Someone's been tampering!");
                    System.exit(-1);
                }
            }
            System.out.println("\nMessage digests match. Message integrity has been preserved!");
            if (givenhash.length != computedhash.length) {
                System.out.println("Given and computed hash lengths are different.");
                System.exit(-1);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
        System.out.println("Decrypted file " + args[0] + ". Decrypted file saved to " + args[0].substring(0,args[0].length()-4));
    }
}

