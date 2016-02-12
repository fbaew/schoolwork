import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;


public class MovieTest {

	private Movie movie;

	@Before
	public void MovieTest_setup() {
		movie = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0"});
		movie.add_user(10);
		movie.add_user(11);
		movie.add_rating(new Rating(new String[] {"10", "3", "5", ""}, "12345"));
		movie.add_rating(new Rating(new String[] {"11", "3", "5", ""}, "12347"));
		movie.compute_bayesian(2, 5);
	}

	@Test
	public void test_is_genre() {
		assertTrue(movie.is_genre(1));
		assertTrue(movie.is_genre(12));
	}

	@Test
	public void test_title() {
		assertTrue(movie.title().equals("Some movie (1999)"));
	}

	@Test
	public void test_id() {
		assertTrue(movie.id()==3);
	}

	@Test
	public void test_year() {
		assertTrue(movie.year().equals("1999"));
	}

	@Test
	public void test_user_rated_this_movie() {
		assertTrue(movie.user_rated_this_movie(10));
		assertTrue(movie.user_rated_this_movie(11));
	}

	@Test
	public void test_add_rating() {
		assertTrue(movie.ratings_size()==2);
	}

	@Test
	public void test_avg_rating() {
		assertTrue(movie.avg_rating()==5);
	}

	@Test
	public void test_bayesian_avg() {
		assertTrue(movie.bayesian_avg()==100);
	}

	@Test
	public void testHistogram() {
		assertTrue(movie.histogram(false).equals(""));
		assertTrue(movie.histogram(true).equals("******************************"));
	}
}
