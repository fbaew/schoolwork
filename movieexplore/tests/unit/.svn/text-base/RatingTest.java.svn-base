import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;


public class RatingTest {

	private Rating rating;

	@Before
	public void rating_setup() {
		rating = new Rating(new String[] {"10", "199", "4", ""}, "12345");
	}

	@Test
	public void test_rating() {
		assertTrue(rating.rating()==4);
	}

	@Test
	public void test_user() {
		assertTrue(rating.user()==10);
	}

	@Test
	public void test_movie_id() {
		assertTrue(rating.movie_id()==199);
	}

	@Test
	public void test_zip() {
		assertTrue(rating.zip().equals("12345"));
	}

	@Test
	public void test_rating_comparator() {
		Rating higher_rating = new Rating(new String[] {"11", "199", "5", ""}, "22345");
		Rating lower_rating = new Rating(new String[] {"12", "199", "3", ""}, "22344");
		RatingComparator cmp = new RatingComparator();
		assertTrue(cmp.compare(rating, higher_rating) > 0);
		assertTrue(cmp.compare(rating, lower_rating) < 0);
	}

}
