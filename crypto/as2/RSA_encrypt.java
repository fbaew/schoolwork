import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.security.*;
import java.math.*;

public class RSA_encrypt {
    private BigInteger n = null;
    private BigInteger p = null;
    private BigInteger q = null;
    private BigInteger d = null;
    private BigInteger e = null;
    private BigInteger phi = null;
    private static final int myBlockSize = 64;
    private ArrayList<Byte> filelist = new ArrayList<Byte>();
    public void initializeKeys() {
        /*
         * Generates n, e, and d for RSA encryption and decryption. Rules:
         * n:
         * 
         * e:
         * 
         * d:
         * 
         */
         
         //Generate n
         //To do this we must generate distinct primes p and q which are 512 bits and
         //|p-q| > 2^80
         BigInteger tolerableProximity = new BigInteger("2");
         try {
             SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

             tolerableProximity = tolerableProximity.pow(80);
             p = BigInteger.probablePrime(512, random);
             q = p.add(tolerableProximity); //q starts out way too close to p, and possibly not prime.

             System.out.println("Generating p and q...");
             while ((p.subtract(q)).abs().compareTo(tolerableProximity) <= 0) {
                 //we need to make sure p and q are at least 2^80 apart.
                 q = BigInteger.probablePrime(512,random);
             }

             n = p.multiply(q); //let n = pq
             phi = (((p.multiply(q)).subtract(q)).subtract(p)).add(BigInteger.ONE); //(p-1)(q-1) pq-q-p+1





             while (e == null || gcd(e,phi).compareTo(BigInteger.ONE) != 0 
                              || e.compareTo(phi) >= 0 
                              || e.compareTo(BigInteger.ONE) <= 0) {

                 e = new BigInteger(32, random); //could replace 32 by phi.bitLength() but this gives us huge values for e.
                                                //32 bits should be fine.
             }

             d = dValue(e,phi)[0];
             if (d.compareTo(BigInteger.ZERO) < 0)
                d = phi.add(d); //if d is negative, add it to phi.
            // System.out.println("p = " + p);
            // System.out.println("q = " + q);
            System.out.println("n = " + n);
            // System.out.println("phi = " + phi);
            // System.out.println("e = " + e);
            // System.out.println("d = " + d);

        } catch (NoSuchAlgorithmException e) {}
    }
    
    public void initializeKeysToAssignment() {
        /*
         * This is just a testing method to let me compare my results with those in the
         * written portion of the assignment (question 4).
         */
        p = new BigInteger("11");
        q = new BigInteger("7");
        d = new BigInteger("11");
        e = new BigInteger("11");
        n = new BigInteger("77");
    }
    
    private BigInteger[] dValue(BigInteger e, BigInteger phi) {
        BigInteger[] result = new BigInteger[2];
        //we want to solve ed is congruent to 1 mod phi
        if (e.mod(phi).equals(BigInteger.ZERO)) {
            result[0] = BigInteger.ZERO;
            result[1] = BigInteger.ONE;
            return result;
        }

        else {
            result = dValue(phi, e.mod(phi));
            BigInteger[] newresult = new BigInteger[2];
            newresult[0] = result[1];
            newresult[1] = result[0].subtract(result[1].multiply(e.divide(phi)));
            return newresult;
        }

    }
    

    
    private BigInteger gcd(BigInteger a, BigInteger b) {
            while (b.compareTo(BigInteger.ZERO) != 0) {
                BigInteger t = b.add(BigInteger.ZERO);
                b = a.mod(b);
                a = t.add(BigInteger.ZERO);
            }
            return a;
    }
    
    private BigInteger generateRandomHugeNumber(SecureRandom random) {
            byte randomBytes[] = new byte[64];
            random.nextBytes(randomBytes);
            BigInteger result = new BigInteger(randomBytes);
            result = result.abs();
            return result;
    }
    
    private ArrayList<byte[]> split(byte[] wholeFile, int blockSize) {
        /*
         * Takes in an array of bytes representing a file, and splits it into smaller
         * arrays that are small enough to encrypt.
         */

//        int blockSize;
        ArrayList<byte[]> result = new ArrayList<byte[]>();
        int wholeBlocks = (int)Math.floor(wholeFile.length/blockSize);

        for (int i = 0; i<wholeBlocks; i++) {
            byte[] block = new byte[blockSize];
            for (int j = 0; j < blockSize; j++) {
                block[j] = wholeFile[i*blockSize + j];
            }
            result.add(block);
        }

        byte[] block = new byte[blockSize];        
        int j=0;
        for (int i = 0; i<(wholeFile.length - (wholeBlocks*blockSize)); i++) {
            block[i] = wholeFile[i+wholeBlocks*blockSize];
            j = i;
        }
        for (int i=j; i<blockSize; i++) {
            block[i] = 0x00;
        }


        result.add(block);
        //         
        // for (int i=0; i<result.size(); i++) {
        //     for (int k = 0; k<blockSize; k++) {
        //         System.out.print((char) result.get(i)[k]);
        //     }
        //     System.out.println("---------->");
        // }
        // 
        return result;
    }
    
    public void encrypt_file(String infile, String outfile) {
        /*
         * encrypts the contents of infile and writes the encryption to outfile. The
         * parameters represent filenames.
         */
         
         FileInputStream in = null;
         FileOutputStream out = null;
         try {
             in = new FileInputStream(infile);
             out = new FileOutputStream(outfile);
             int b;
             while ((b = in.read()) != -1) {
                 filelist.add(new Byte((byte) b));
             }
             
             
             Byte[] file = filelist.toArray(new Byte[filelist.size()]);
             //make that linked list an array because we have to iterate
             //over it a bit.
             byte[] filebytes = new byte[filelist.size()];

             for (int i = 0; i < filelist.size(); i++) {
                 filebytes[i] = file[i].byteValue();
             }
             
             ArrayList<byte[]> messageBlocks = split(filebytes,myBlockSize);     //split filebytes into blocks that are small enough to be m

             int blockno = 0;
             for (byte[] block : messageBlocks) {
                 BigInteger m = new BigInteger(block); //create m from the fileblocks

                 BigInteger c = m.modPow(e,n); //encrypt m -> c
                 out.write(c.toByteArray());
                 System.out.println("Wrote block " + blockno + " size " + c.toByteArray().length + " encrypted from block of size " + block.length);
                 blockno++;
             }
             
         } catch (IOException e) {
             System.out.println("Bad filename, probably.");
         }
         finally {
             try {
                 if (in != null) {
                     in.close();
                 }
                 if (out != null) {
                     out.close();
                 }
             } catch (IOException e) {
                  System.out.println("Bad filename, probably.");
              }
         }
         
    }
    
    public void decrypt_file(String infile, String outfile) {
        /*
         * decrypts the contents of infile and writes the decryption to outfile. The
         * parameters represent filenames.
         */
         
         FileInputStream in = null;
         FileOutputStream out = null;
         try {
             in = new FileInputStream(infile);
             out = new FileOutputStream(outfile);
             int b;
             filelist.clear();
             while ((b = in.read()) != -1) {
                 filelist.add(new Byte((byte) b));
             }
             
             
             Byte[] file = filelist.toArray(new Byte[filelist.size()]);
             //make that linked list an array because we have to iterate
             //over it a bit.
             byte[] filebytes = new byte[filelist.size()];

             for (int i = 0; i < filelist.size(); i++) {
                 filebytes[i] = file[i].byteValue();
             }
             
             ArrayList<byte[]> messageBlocks = split(filebytes,myBlockSize*2);

             for (int i=0; i < messageBlocks.size()-1; i++) {
                 BigInteger c = new BigInteger(messageBlocks.get(i));
                 System.out.println("Read block" + i + ", size " + messageBlocks.get(i).length);
                 BigInteger m = c.modPow(d,n); //decrypt the ciphertext
                 out.write(m.toByteArray());
             }
             BigInteger c = new BigInteger(messageBlocks.get(messageBlocks.size()-1));
             BigInteger m = c.modPow(d,n);
             out.write(m.shiftRight(m.getLowestSetBit()).toByteArray());
             
             // byte[] output = m.toByteArray();
             // for (int i=0; i<output.length; i++) {
             //     out.write(output[i]);
             // }
             
         } catch (IOException e) {
             System.out.println("Bad filename, probably.");
         }
         finally {
             try {
                 if (in != null) {
                     in.close();
                 }
                 if (out != null) {
                     out.close();
                 }
             } catch (IOException e) {
                  System.out.println("Bad filename, probably.");
              }
         }
    }
    
}