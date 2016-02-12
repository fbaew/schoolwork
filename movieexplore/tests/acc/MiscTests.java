/**
 * MovieExplore, A Project for SENG301
 * Copyright 2010, Gregg and Kyle
 *
 * Refer to LICENSE for legalese.
 *
 * Tests to add this revision:
 
 Unrecognized Switch
 Malformed Switch
 random-cased input (genre=DOCumeNTarY)
 
 */

import org.junit.Test;


public class MiscTests {

	@Test
	public void too_little_args() {
		TestHelper.run_test("java -cp build MovieExplore data/large", 
		                    TestHelper.make_output("At least 2 parameters are required.", 1),
												1);
	}

 	@Test
 	public void data_directory_exists() {
 		TestHelper.run_test("java -cp build MovieExplore this_data_directory_surely_does_not_exist stats", 
		                    TestHelper.make_output("First argument not a valid directory.", 2),
												2);
 	}
 	
	@Test
	public void data_all_files_present() {
		TestHelper.run_test("java -cp build MovieExplore data/missingfile stats", 
		                    TestHelper.make_output("Data files not found in specified directory.", 2),
												2);
	}

	@Test
	public void invalid_command() {
		TestHelper.run_test("java -cp build MovieExplore data/large surely_this_command_does_not_exist", 
		                    TestHelper.make_output("Command not recognized.", 1),
												1);
	}
	
	@Test
	public void invalid_switch() {
		TestHelper.run_test("java -cp build MovieExplore data/large top some_invalid_switch_input", 
		                    TestHelper.make_output("Unknown switch.", 1),
												1);
	}

	@Test
	public void too_many_equals_signs() {
		TestHelper.run_test("java -cp build MovieExplore data/large top --asdf=asdf=asdf", 
		                    TestHelper.make_output("Switch with more than one equals sign found.", 1),
												1);
	}

	@Test
	public void bogus_switch() {
		TestHelper.run_test("java -cp build MovieExplore data/large top --bogus_switch=True",
		                    TestHelper.make_output("Unknown switch.", 1),
												1);
	}

} 
