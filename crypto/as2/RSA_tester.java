/******************************************************************************
File: 	    RSA_tester.java
Purpose:        Java driver to test Problem 7 of CPSC 418 Assignment 2, for correctness.
Created:	    January 26, 2008
Revised:        
Author:         Heather Crawford
Modified:       Renate Scheidler, October 18, 2010 (just the "Purpose" line above)

Input (command line arguments): 
  1)Plaintext file name- source of plaintext to be encrypted.
  2)Ciphertext file name- destination file for encrypted plaintext.
  3)Decrypted cipher text file name- destination file for decrypted ciphertext.

Output:         if encryption/decryption passed or failed,
                and the differences between the two files (written to 
                DIFF_FILE). 

Description:	This program does the following:
                  Create RSA_encrypt object, call intializeKeys(),
                  call encrypt_file( char *infile, char *outfile),
                  call decrypt_file( char *infile, char *outfile),
                  and calculate the diff on plaintext and decrypted ciphertext
                  files respectively by comparing the two files using the UNIX diff command.

Requires:       java.io.*, 

Compilation:    javac RSA_tester.java

Execution: java plaintextFileName cipherTextFileName decryptedPlaintextFileName

Notes:
Please note that the code for the file comparison code came from http://www.devdaily.com/java/edu/pj/pj010016/pj010016.shtml.

******************************************************************************/

import java.io.*;

public class RSA_tester {
	public static void main(String[] args) throws IOException {
		final String DIFF_FILE = "diff.txt";
		final int MAX_COMMAND_LENGTH = 500;

		BufferedReader plainStream = null;
		BufferedWriter cipherStream = null;
		String testchar = null;

		//check number of args
		if (args.length != 3)
		{
			System.out.println("\nBad command, the correct formation is:");
			System.out.println("java RSA_tester <plainTextFileName> <cipherTextFileName> <decryptedTextFilename>");
			System.out.println("Please Try Again.\n");
		}
		else
		{
			//encryption and decryption test
			//create an RSA object, initialize the keys, encrypt the plaintext,
			//decrypt the ciphertext, and then compare the plaintext and decrypted ciphertext
			RSA_encrypt RSAEncryptObject = new RSA_encrypt();
			RSAEncryptObject.initializeKeys();
			RSAEncryptObject.encrypt_file(args[0], args[1]);
			RSAEncryptObject.decrypt_file(args[1], args[2]);

			//Compare the plaintext and decrypted ciphertext files via filesAreIdentical function,
			//write the differences to stdout, then say whether the decryption passed or failed.
			if (filesAreIdentical(args[0], args[2])){
				System.out.println("\nTherefore, the decryption of the encrypted plaintext is correct.\n");
			}
			else{
				System.out.println("\nTherefore, encryption of the encrypted plaintext failed.\n");
			}
		}
	}

	public static boolean filesAreIdentical(String plain, String decrypt) throws IOException
   	{
		boolean ret = true;
		String s = null;
		//run the diff command at the UNIX command line
		Process p = Runtime.getRuntime().exec("diff " + plain + " " + decrypt);
		
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

		//write out the results to stdout
		//System.out.println("Here is the standard output of the diff command:");
          while ((s = stdInput.readLine()) != null) {
			ret = false;
               //System.out.println(s);
          }
		return ret;
    	}

}