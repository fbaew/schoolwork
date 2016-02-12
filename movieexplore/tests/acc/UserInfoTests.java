/**
 * MovieExplore, A Project for SENG301
 * Copyright 2010, Gregg and Kyle
 *
 * Refer to LICENSE for legalese.
 */

import org.junit.Test;


public class UserInfoTests {

	@Test
	public void no_movies_rated() {
		String[] correct_output = {
			"User id:    8", 
			"Age:        11",
			"Gender:     M",
			"Occupation: student",
			"Zip:        12345",
			"Ratings:    ",
			"  Total:    0",
		};
		TestHelper.run_test("java -cp build MovieExplore data/basic user 8", 
		                    correct_output,
												0);
	}

	@Test
	public void some_movies_rated_no_rounding() {
		String[] correct_output = {
			"User id:    6", 
			"Age:        33",
			"Gender:     F",
			"Occupation: artist",
			"Zip:        19234",
			"Ratings:    ",
			"  Total:    1",
			"  Average:  5.00",
			"Top:        5 - Best Movie Ever (1995)",
			"Bottom:     5 - Best Movie Ever (1995)",
		};
		TestHelper.run_test("java -cp build MovieExplore data/basic user 6", 
		                    correct_output,
												0);

	}

	@Test
	public void some_movies_rated_do_rounding() {
		String[] correct_output = {
			"User id:    243",
			"Age:        33",
			"Gender:     M",
			"Occupation: educator",
			"Zip:        60201",
			"Ratings:    ",
			"  Total:    81",
			"  Average:  3.64",
			"Top:        5 - Breaking the Waves (1996)",
			"Bottom:     1 - Up Close and Personal (1996)",			
		};
		TestHelper.run_test("java -cp build MovieExplore data/large user 243", 
		                    correct_output,
												0);
	}
	
//	@Test
	public void no_such_user() {
		String[] correct_output = {
			"",
			"No such user, sorry",
			""
		};
		TestHelper.run_test("java -cp build MovieExplore data/basic user 243", 
		                    correct_output,
												0);
	}

} 

