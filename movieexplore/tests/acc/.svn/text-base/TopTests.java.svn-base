/**
 * MovieExplore, A Project for SENG301
 * Copyright 2010, Gregg and Kyle
 *
 * Refer to LICENSE for legalese.
 */

import org.junit.Test;


public class TopTests {

	@Test
	public void single_equals () {
    TestHelper.run_test("java -cp build MovieExplore data/large top =", 
		                    TestHelper.make_output("Single equals sign found.", 1),
												1);
	}

	@Test
	public void manufactured_data() {
		String[] correct_output = { 
			"Pop  Title",
			"-------------------",
			"92   Best Movie Ever (1995)",
			"89   Almost the Best (1995)",
			"88   Another Good Movie (1995)",
			"88   One Good Movie (1995)",
			"67   Worst of the best (1995)"
		};
 		TestHelper.run_test("java -cp build MovieExplore data/basic top", 
		                    correct_output,
												0);
 	}

  @Test
	public void original_data() {
		String[] correct_output = { 
			"Pop  Title",
			"-------------------",
			"84   Star Wars (1977)",
			"83   Schindler's List (1993)",
			"83   Shawshank Redemption, The (1994)",
			"82   Casablanca (1942)",
			"82   Godfather, The (1972)"
		};
		TestHelper.run_test("java -cp build MovieExplore data/large top", 
		                    correct_output,
												0);
	}
	
	@Test
 	public void top_in_genre() {
  	// Make sure that filtering by genre returns only results in the specified 
		// genre. (Top list would normally be mostly documentaries)
		String[] correct_output = { 
	    "Pop  Title",
  	  "-------------------",
    	"100   Another Thrilling Thriller 5",
	    "100   Thrilling Thriller 2",
  	  "100   Thrilling Thriller 3",
    	"100   Thrilling Thriller 4",
	    "100   Titillating Thrilling Thriller 1", 
		};
 		TestHelper.run_test("java -cp build MovieExplore data/genrefilter top --genre=Thriller", 
		                    correct_output,
												0);
 	}

  @Test
 	public void results_less_than_limit() {
	  // Make sure that when there are < the limit movies to display, they get 
		// displayed followed by a short message saying what happened.  There are only 
		// 2 comedy movies, so this should happen when we filter for comedy.
		String[] correct_output = {
			"Pop  Title",
			"-------------------",
			"100   Funny Educational Documentary 1",
			"100   Titillating Thrilling Thriller 1",
			"There are no more movies to display. Perhaps try a more general query."
		};
		TestHelper.run_test("java -cp build MovieExplore data/genrefilter top --genre=Comedy", 
		                    correct_output,
												0);
 	}

  @Test
  public void no_results() {
  	// Similar to above, make sure a message (and nothing else) is outputted 
		// when there are no movies to show after filtering.
    TestHelper.run_test("java -cp build MovieExplore data/genrefilter top --genre=Horror", 
		                    TestHelper.make_output("No movies were found which matched the criteria specified. Perhaps try a more general query.", 1),
												1);
  }

  @Test
  public void numeric_genre() {
  	// Make sure that filtering by providing a genre number works just the same as filtering by genre name.
		String[] correct_output = {
			"Pop  Title",
			"-------------------",
			"100   Educational Documentary 2",
			"100   Educational Documentary 3",
 			"100   Educational Documentary 4",
			"100   Educational Documentary 5",
			"100   Funny Educational Documentary 1"
		};
    TestHelper.run_test("java -cp build MovieExplore data/genrefilter top --genre=7", 
		                    correct_output,
												0);
  }
	
	@Test
	public void top_documentary_all_data() {
	  	// This is really more of a regression test.
		String[] correct_output = { 
			"Pop  Title",
			"-------------------",
			"79   Hoop Dreams (1994)",
			"75   When We Were Kings (1996)",
			"74   Celluloid Closet, The (1995)",
			"73   Crumb (1994)",
			"71   Paradise Lost: The Child Murders at Robin Hood Hills (1996)"
		};
		TestHelper.run_test("java -cp build MovieExplore data/large top --genre=Documentary", 
		                    correct_output,
												0);
	}
	
  @Test
	public void genre_invalid_argument() {
		TestHelper.run_test("java -cp build MovieExplore data/large top --genre=asdf", 
		                    TestHelper.make_output("Tried to parse an invalid integer.", 1),
												1);
	}

  @Test
	public void partial_year_no_cutoff() {
		// Make sure that when we apply a --year filter and there are <limit> movies,
		// we see all <limit> movies and no warning message.
		String[] correct_output = {
			"Pop  Title",
			"-------------------",
			"100   First movie of the 80s",
			"100   Last Movie of the 90s",
			"100   Last movie of the 80s",
			"100   Second Movie of the 90s",
			"100   The First Movie of the 90s"


		};
		TestHelper.run_test("java -cp build MovieExplore data/yearfilter top --year=19", 
		                    correct_output,
												0);
	}
	
	@Test
	public void partial_year_not_enough_results() {
		String[] correct_output = {
			"Pop  Title",
			"-------------------",
			"100   Last Movie of the 90s",
			"100   Second Movie of the 90s",
			"100   The First Movie of the 90s",
			"There are no more movies to display. Perhaps try a more general query."
		};
		TestHelper.run_test("java -cp build MovieExplore data/yearfilter top --year=199", 
		                    correct_output,
												0);
	}
	
	@Test
	public void complete_year_not_enough_results() {
		String[] correct_output = {
			"Pop  Title",
			"-------------------",
			"100   Last movie of the 80s",
			"There are no more movies to display. Perhaps try a more general query."
		};
		TestHelper.run_test("java -cp build MovieExplore data/yearfilter top --year=1988", 
		                    correct_output,
												0);
	}
	
	@Test
	public void no_results_1() {
		TestHelper.run_test("java -cp build MovieExplore data/yearfilter top --year=2000", 
		                    TestHelper.make_output("No movies were found which matched the criteria specified. Perhaps try a more general query.", 1),
												1);
	}
  
	@Test
	public void more_than_one_year() {
	  TestHelper.run_test("java -cp build MovieExplore data/yearfilter top --year=2000 --year=2000", 
												TestHelper.make_output("Only one --year switch is allowed.", 1),
												1);
	}

	@Test
	public void year_is_not_an_integer() {
	  TestHelper.run_test("java -cp build MovieExplore data/yearfilter top --year=asdf", 
		                    TestHelper.make_output("Tried to parse an invalid integer.", 1),
												1);
	}

	@Test
	public void top_in_specific_zip() {
		// Given a specific zip code, make sure we output only movies which rank 
		// within that zip.
  	String[] correct_output = { 
      "Pop  Title",
      "-------------------",
      "38   1934: The HEIGHT of Science (1940)",
      "38   301: A SENG Odyssey (2010)",
      "38   A New Pope (1960)",
      "38   Gregg and Kyle Strike Back (2002)",
      "38   Gregg and Kyle's Excellent Adventure (2010)"
  	};
		TestHelper.run_test("java -cp build MovieExplore data/zipfilter top --zip=90210", 
		                    correct_output,
												0);
	}

	@Test
	public void no_users_from_zip() {
		// Given a specific zip code, see that the case where there are no ratings 
		// from that zip code.
		TestHelper.run_test("java -cp build MovieExplore data/zipfilter top --zip=11111", 
		                    TestHelper.make_output("No movies were found which matched the criteria specified. Perhaps try a more general query.", 1),
												1);
	}

	@Test
	public void less_than_limit_found() {
		// Given a specific zip code, make sure that if less than the limit 
		// (default 5) movies have ratings from that zip code, make sure we 
		// display the right movie(s) and then a friendly message letting the user
		// know "that's it, that's all"
		String[] correct_output = {
			"Pop  Title",
			"-------------------",
			"82   A Pretty Okay Movie (1984)",
			"There are no more movies to display. Perhaps try a more general query."
		};
		TestHelper.run_test("java -cp build MovieExplore data/zipfilter top --zip=90277", 
		                    correct_output, 
												0);
	}

	@Test
	public void ambiguous_zip_provided() {
		// Given the starting portion of a zip, make sure we output only movies 
		// which rank within that zip prefix.
		String[] correct_output = { 
			"Pop  Title",
			"-------------------",
			"65   A Pretty Okay Movie (1984)",
			"41   1934: The HEIGHT of Science (1940)",
			"41   301: A SENG Odyssey (2010)",
			"41   A New Pope (1960)",
			"41   Gregg and Kyle Strike Back (2002)"
		};
		TestHelper.run_test("java -cp build MovieExplore data/zipfilter top --zip=902", 
		                    correct_output,
												0);
	}

	@Test
	public void more_than_one_zip() {
		TestHelper.run_test("java -cp build MovieExplore data/zipfilter top --zip=902 --zip=123", 
		                    TestHelper.make_output("Only one --zip switch is allowed.", 1),
												1);
	}

	@Test
	public void zip_is_not_an_integer() {
		TestHelper.run_test("java -cp build MovieExplore data/zipfilter top --zip=asdf", 
		                    TestHelper.make_output("Tried to parse an invalid integer.", 1),
												1);
	}

	@Test
	public void one () {
  	String[] correct_output = { 
      "Pop  Title",
      "-------------------",
			"69   Men in Black (1997)",
			"52   Money Talks (1997)",
			"50   Money Talks (1997)",
			"49   Best Men (1997)",
			"47   Beverly Hills Ninja (1997)",
  	};
		TestHelper.run_test("java -cp build MovieExplore data/large top --genre=Comedy --year=1997 --genre=Action", 
		                    correct_output,
												0);
	}

	@Test
	public void two () {
  	String[] correct_output = { 
      "Pop  Title",
      "-------------------",
			"75   Full Monty, The (1997)",                      
			"73   As Good As It Gets (1997)",
			"71   Men in Black (1997)",
			"71   Kolya (1996)",
			"68   Grosse Pointe Blank (1997)",
  	};
		TestHelper.run_test("java -cp build MovieExplore data/large top --genre=Comedy --year=1997", 
		                    correct_output,
												0);
	}

	@Test
	public void three () {
  	String[] correct_output = { 
      "Pop  Title",
      "-------------------",
			"82   Star Wars (1977)",
			"79   Godfather, The (1972)",
			"78   Raiders of the Lost Ark (1981)",
			"77   Titanic (1997)",
			"77   Empire Strikes Back, The (1980)",
			"76   Princess Bride, The (1987)",
			"75   Return of the Jedi (1983)",
			"75   Braveheart (1995)",
			"74   Fugitive, The (1993)",
			"73   Alien (1979)"
  	};
		TestHelper.run_test("java -cp build MovieExplore data/large top --genre=Action --limit=10", 
		                    correct_output,
												0);
	}

	@Test
	public void four () {
		String[] correct_output = { 
			"Pop  Title",
			"-------------------",
			"74   Psycho (1960)",
			"72   Alien (1979)",
			"71   Young Frankenstein (1974)",
			"69   Jaws (1975)",
			"67   Birds, The (1963)",
  	};
		TestHelper.run_test("java -cp build MovieExplore data/large top --genre=Horror --zip=1", 
		                    correct_output,
												0);
	}

	@Test
	public void five () {
		String[] correct_output = { 
			"Pop  Title",
			"-------------------",
			"74   Psycho (1960)",
			"71   Jaws (1975)",
			"71   Young Frankenstein (1974)",
			"70   Alien (1979)",
			"70   Birds, The (1963)",
  	};
		TestHelper.run_test("java -cp build MovieExplore data/large top --genre=Horror --zip=2", 
		                    correct_output,
												0);
	}

	@Test
	public void six () {
		String[] correct_output = { 
			"Pop  Title",
			"-------------------",
			"80   Alien (1979)",
			"77   Shining, The (1980)",
			"75   Psycho (1960)",
			"73   Evil Dead II (1987)",
			"72   Young Frankenstein (1974)"
  	};
		TestHelper.run_test("java -cp build MovieExplore data/large top --genre=Horror --zip=3", 
		                    correct_output,
												0);
	}

	@Test
	public void low_limit() {
		//No need to have a test like high_limit() because top with no limit specified (like in Feature_2.java)
		//uses the exact same code to output exactly 5 lines (the assumed default). The following test illustrates
		//that the limit parameter is passed along to the "top" function. Having something testing --limit=7 or
		//the like would be redundant, since many of our other tests effectively test --limit=5. 
		String[] correct_output = { 
			"Pop  Title",
			"-------------------",
			"84   Star Wars (1977)",
			"83   Schindler's List (1993)"
		};
    TestHelper.run_test("java -cp build MovieExplore data/large top --limit=2",
		                    correct_output,
												0);
	}

	@Test
	public void more_than_one_limit() {
		TestHelper.run_test("java -cp build MovieExplore data/large top --limit=2 --limit=4", 
		                    TestHelper.make_output("Only one --limit switch is allowed.", 1),
												1);
	}

	@Test
	public void limit_not_an_integer() {
		TestHelper.run_test("java -cp build MovieExplore data/large top --limit=asdf", 
		                    TestHelper.make_output("Tried to parse an invalid integer.", 1),
												1);
  }

	// Feature 8

	@Test
	public void histogram_one () {
  	String[] correct_output = { 
      "Pop  Title",
      "-------------------",
			"69   Men in Black (1997)                       ***************************",
			"52   Money Talks (1997)                        *************************",
			"50   Money Talks (1997)                        *************************",
			"49   Best Men (1997)                           *************************",
			"47   Beverly Hills Ninja (1997)                *************************"
			
  	};
		TestHelper.run_test("java -cp build MovieExplore data/large top --genre=Comedy --year=1997 --genre=Action --histogram", 
		                    correct_output,
												0);
	}

	@Test
	public void histogram_two () {
  	String[] correct_output = { 
      "Pop  Title",
      "-------------------",
			"75   Full Monty, The (1997)                    ****************************",
			"73   As Good As It Gets (1997)                 ***************************",
			"71   Men in Black (1997)                       ***************************",
			"71   Kolya (1996)                              ***************************",
			"68   Grosse Pointe Blank (1997)                ***************************"
  	};
		TestHelper.run_test("java -cp build MovieExplore data/large top --genre=Comedy --year=1997 --histogram", 
		                    correct_output,
												0);
	}

	@Test
	public void histogram_three () {
  	String[] correct_output = { 
      "Pop  Title",
      "-------------------",
			"82   Star Wars (1977)                          ****************************",
			"79   Godfather, The (1972)                     ****************************",
			"78   Raiders of the Lost Ark (1981)            ****************************",
			"77   Titanic (1997)                            ****************************",
			"77   Empire Strikes Back, The (1980)           ****************************",
			"76   Princess Bride, The (1987)                ****************************",
			"75   Return of the Jedi (1983)                 ****************************",
			"75   Braveheart (1995)                         ****************************",
			"74   Fugitive, The (1993)                      ****************************",
			"73   Alien (1979)                              ***************************"
  	};
		TestHelper.run_test("java -cp build MovieExplore data/large top --genre=Action --limit=10 --histogram", 
		                    correct_output,
												0);
	}

	@Test
	public void histogram_four () {
		String[] correct_output = { 
			"Pop  Title",
			"-------------------",
			"83   Star Wars (1977)                          ****************************",
			"80   Raiders of the Lost Ark (1981)            ****************************",
			"79   Empire Strikes Back, The (1980)           ****************************",
			"78   Return of the Jedi (1983)                 ****************************",
			"78   Godfather, The (1972)                     ****************************",
  	};
		TestHelper.run_test("java -cp build MovieExplore data/large top --genre=Action --zip=5 --histogram", 
		                    correct_output,
												0);
	}

	@Test
	public void histogram_five () {
		String[] correct_output = { 
			"Pop  Title",
			"-------------------",
			"78   Raiders of the Lost Ark (1981)            ****************************",
			"76   Empire Strikes Back, The (1980)           ****************************",
			"75   Princess Bride, The (1987)                ****************************",
			"74   Aliens (1986)                             ****************************",
			"73   Terminator, The (1984)                    ***************************",
  	};
		TestHelper.run_test("java -cp build/ MovieExplore data/large/ top --genre=action --zip=6 --histogram --year=198", 
		                    correct_output,
												0);
	}

	@Test
	public void histogram_six () {
		String[] correct_output = { 
			"Pop  Title",
			"-------------------",
			"88   Air Force One (1997)                      *****************************",
			"88   Apollo 13 (1995)                          *****************************",
			"88   Con Air (1997)                            *****************************",
			"88   Fled (1996)                               *****************************",
			"88   Indiana Jones and the Last Crusade (1989)  *****************************",
  	};
		TestHelper.run_test("java -cp build MovieExplore data/large top --genre=action --zip=67 --histogram", 
		                    correct_output,
												0);
	}

}
