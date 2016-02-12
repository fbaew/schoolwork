/**
 * MovieExplore, A Project for SENG301
 * Copyright 2010, Gregg and Kyle
 *
 * Refer to LICENSE for legalese.
 */


import org.junit.Test;

public class RecommendationsTests {

	@Test
	public void movie_recs_large_set() {
		String[] correct_output = {
			"Boogie Nights (1997)",
			"Titanic (1997)",
			"One Flew Over the Cuckoo's Nest (1975)",
			"Schindler's List (1993)",
			"Face/Off (1997)",
	};
	TestHelper.run_test("java -cp build MovieExplore data/large movie-recommendations 1",
	                    correct_output,
											0);
	}

	@Test
	public void movie_recs_large_set_2() {
		String[] correct_output = {
			"Usual Suspects, The (1995)",
			"Lone Star (1996)",
			"Amadeus (1984)",
			"Princess Bride, The (1987)",
			"Schindler's List (1993)",
	};
	TestHelper.run_test("java -cp build MovieExplore data/large movie-recommendations 2",
	                    correct_output,
											0);
	}

	@Test
	public void movie_recs_large_set_3() {
		String[] correct_output = {
			"Annie Hall (1977)",
			"Godfather: Part II, The (1974)",
			"Big Night (1996)",
			"Godfather, The (1972)",
			"Leaving Las Vegas (1995)",
	};
	TestHelper.run_test("java -cp build MovieExplore data/large movie-recommendations 3",
	                    correct_output,
											0);
	}

	@Test
	public void movie_recs_large_set_4() {
		String[] correct_output = {
			"Good Will Hunting (1997)",
			"L.A. Confidential (1997)",
			"Titanic (1997)",
			"Braveheart (1995)",
			"Die Hard (1988)",
	};
	TestHelper.run_test("java -cp build MovieExplore data/large movie-recommendations 4",
	                    correct_output,
											0);
	}                

	@Test
	public void movie_recs_large_set_5() {
		String[] correct_output = {
			"Dr. Strangelove or: How I Learned to Stop Worrying and Love the Bomb (1963)",
			"Godfather, The (1972)",
			"Pulp Fiction (1994)",
			"Terminator 2: Judgment Day (1991)",
			"Chinatown (1974)",
	};
	TestHelper.run_test("java -cp build MovieExplore data/large movie-recommendations 5",
	                    correct_output,
											0);
	}              

	@Test
	public void movie_recs_large_set_6() {
		String[] correct_output = {
			"Rear Window (1954)",
			"Singin' in the Rain (1952)",
			"In the Company of Men (1997)",
			"Chinatown (1974)",
			"High Noon (1952)",
	};
	TestHelper.run_test("java -cp build MovieExplore data/large movie-recommendations 6",
	                    correct_output,
											0);
	}                                                                           

	@Test
	public void get_movie_recommendations() {
		String[] correct_output = {
			"Lethal Weapon 1 (1987)",
			"Lethal Weapon 2 (1989)",
			"Lethal Weapon 3 (1992)",
			"Lethal Weapon 4 (1998)",
			"The Naked Gun (1988)"
		};
		TestHelper.run_test("java -cp build MovieExplore data/recommendations movie-recommendations 1",
		                    correct_output,
												0);
	}
	
	@Test
	public void get_filtered_movie_recommendations() {
		String[] correct_output = {
			"Lethal Weapon 4 (1998)",
			"There are no more suitable movie recommendations to display."
		};
		TestHelper.run_test("java -cp build MovieExplore data/recommendations movie-recommendations 1 --genre=War --year=199",
												correct_output,
												0);
	}
	
} 

