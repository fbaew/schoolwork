/**
 * MovieExplore, A Project for SENG301
 * Copyright 2010, Gregg and Kyle
 *
 * Refer to LICENSE for legalese.
 */

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.Runtime;


public class TestHelper {

	/**
	 * Helper function for each test. Spawns a new MovieExplore process and
	 * captures the standard output, putting it into a buffer. Then check the 
	 * buffer one line at a time against known good output. 
	 * @param command         The command that you want tested.
	 * @param expected_output The output that this command should output. 
	 * @param return_value    The return value expected.
	 */
	public static void run_test(String command, String[] expected_output, int return_value) {
		try {
			// Note that to get the output stream of Process p you do a 
			// p.getInputStream(), which is counter-intuitive and confusing.
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));

			int i=0;
			String read_line;
			while((read_line = b.readLine()) != null) {

				// This shouldn't be that big of deal (However, I assume that it is).
				boolean result = expected_output[i].trim().equals(read_line.trim());
				// Make debugging tests WAY easier, show what line failed.
				if (result==false) {
					System.out.println("\n****");
				 	System.out.println("Difference @ Line #"+(i+1)+" detected!!");
					System.out.println("Expecting:        "+expected_output[i]+"<");
					System.out.println("Read from stdout: "+read_line+"<");
					System.out.println("Command:          "+command);
					System.out.println("****\n");
				}
				assertTrue(result);
				i++;

			}
			assertTrue(i==expected_output.length);
			assertTrue(return_value == p.waitFor());
		// .waitFor() requires this to be caught.
		} catch (InterruptedException n) {
			fail();
		// .exec() and .readLine() both require this to be caught.
		} catch (IOException i) {
			System.out.println("Executing or read line threw IOException.");
		}
	}

	public static String[] make_output(String specific_error_message, int nothing) {
		String[] out = new String[2+Util.usage.length];
		out[0] = "";
		out[1] = specific_error_message;
		for (int i=0; i<Util.usage.length; i++)
			out[i+2] = Util.usage[i];
		return out;
	}
}
