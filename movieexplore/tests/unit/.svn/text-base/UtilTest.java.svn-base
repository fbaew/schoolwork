import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;
import java.util.ArrayList;


public class UtilTest {


/* exit() is not tested because its "correctness" is easy to see from the code */

//	@Test
	public void testCompute_bayesian() {
		fail("Not yet implemented");
	}

	@Test
	public void testParse_int() {
		assertTrue(Util.parse_int("4321") == 4321);
		/* If we give an invalid integer... it just calls System.exit() so we
		   can't test it, it seems. */
	}

	@Test //Make sure trimming whitespace from the trailing end of a string works...
	public void testTrim_output() {
		assertTrue(Util.trim_output("  Test         ").equals("  Test"));
	}

	@Test //Bayesian Comparator... Should compare titles when there's a tie
	public void testBayesianComp_tiebreak() {
		Movie movie1 = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0"});
		movie1.add_user(10);
		movie1.add_user(11);
		movie1.add_rating(new Rating(new String[] {"10", "3", "1", ""}, "12345"));
		movie1.add_rating(new Rating(new String[] {"11", "3", "1", ""}, "12347"));
		
		Movie movie2 = new Movie(new String[] {"4", "Awesome movie (1999)", "01-Jan-1999", "", "", 
			"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0"});
		movie2.add_user(10);
		movie2.add_user(11);
		movie2.add_rating(new Rating(new String[] {"10", "4", "5", ""}, "12345"));
		movie2.add_rating(new Rating(new String[] {"11", "4", "5", ""}, "12347"));
		
		BayesianComparator bc = new BayesianComparator();
		assertTrue(bc.compare(movie1,movie2) > 0);
		
	}

	@Test //Bayesian Comparator... should give us >0 when Movie 2 scores higher than movie 1
	public void testBayesianComp() {
		Movie movie1 = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0"});
		movie1.add_user(10);
		movie1.add_user(11);
		movie1.add_rating(new Rating(new String[] {"10", "3", "1", ""}, "12345"));
		movie1.add_rating(new Rating(new String[] {"11", "3", "1", ""}, "12347"));
		
		Movie movie2 = new Movie(new String[] {"4", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0"});
		movie2.add_user(10);
		movie2.add_user(11);
		movie2.add_rating(new Rating(new String[] {"10", "4", "5", ""}, "12345"));
		movie2.add_rating(new Rating(new String[] {"11", "4", "5", ""}, "12347"));
		
		List<Movie> movies = new ArrayList<Movie>();
		movies.add(movie1);
		movies.add(movie2);
		
		Util.compute_bayesian(movies);
		
		
		BayesianComparator bc = new BayesianComparator();
		assertTrue(bc.compare(movie1,movie2) > 0);
		
	}

}
