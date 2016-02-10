import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.LinkedList;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecureFile {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        if (args.length != 2) {
            System.out.println("Usage: java SecureFile <filename> <key seed>");
            System.exit(-1);
        }
        FileInputStream in = null;
        FileOutputStream out = null;
        LinkedList<Byte> filelist = new LinkedList<Byte>();
        MessageDigest digestor = MessageDigest.getInstance("SHA-256");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(args[1].getBytes()); //seed our prng with the user input
        byte[] iv ={0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
                    0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};

        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(128, random);
        SecretKey key = keygen.generateKey(); //get a key...


        try {
            in = new FileInputStream(args[0]);
            out = new FileOutputStream(args[0] + ".enc");
            int c;

            while ((c = in.read()) != -1) { //load the file into our linked
                //                            list of bytes...
                filelist.add(new Byte((byte) c));
            }
            
            Byte[] file = filelist.toArray(new Byte[filelist.size()]);
            //make that linked list an array because we have to iterate
            //over it a bit.
            byte[] filebytes = new byte[filelist.size()];

            for (int i = 0; i < filelist.size(); i++) {
                filebytes[i] = file[i].byteValue();
            }

            digestor.update(filebytes);
            byte[] hash = digestor.digest();

            byte[] output = new byte[filelist.size() + hash.length];

            for (int i = 0; i < filelist.size(); i++) {
                output[i] = file[i].byteValue();
            }            
            for (int i = 0; i < hash.length; i++) {
                output[i + filelist.size()] = hash[i];
            }
            byte[] encryptedoutput = null;
            try {
                AlgorithmParameterSpec params = new IvParameterSpec(iv);
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, key, params);
                encryptedoutput = cipher.doFinal(output);
            } catch (NoSuchPaddingException e) {
            } catch (InvalidKeyException e) {
           } catch (IllegalBlockSizeException e) {
           } catch (BadPaddingException e) {
           } catch (java.security.InvalidAlgorithmParameterException e) {
           }
   


            for (int i = 0; i<encryptedoutput.length; i++) {
                out.write(encryptedoutput[i]);
            }


        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
        System.out.println("Finished encrypting " + args[0] + ". Encrypted file saved to " + args[0] + ".enc");
    }
}