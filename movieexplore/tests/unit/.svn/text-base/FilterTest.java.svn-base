import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;


/* DummyFilter()   -  No test for this, a quick look at the code shows that
   										it always returns true.*/


public class FilterTest {

	@Before
	public void FilterSetup() {
		
	}

	@Test //Make sure that a movie with the given genre passes, and a movie without doesn't.
	public void testGenreFilter() {
		Filter<Movie> flt = new GenreFilter(1);
		Movie should_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0"});
		Movie should_not_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1988", "", "", 
			"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
		assertTrue(flt.is_match(should_match));
		assertTrue(!flt.is_match(should_not_match));
	}

	@Test //Make sure that a movie with the given year passes, and a movie without doesn't.
				//Here we are testing for a partial year.
	public void testYearFilter_Partial() { 
		Filter<Movie> flt = new YearFilter("199");
		Movie should_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0"});
		Movie should_not_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1988", "", "", 
			"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
		assertTrue(flt.is_match(should_match));
		assertTrue(!flt.is_match(should_not_match));
	}

	@Test //Make sure that a movie with the given year passes, and a movie without doesn't.
				//Here we are testing for a full year.
	public void testYearFilter_Full() { 
		Filter<Movie> flt = new YearFilter("1988");
		Movie should_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1988", "", "", 
			"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0"});
		Movie should_not_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
		assertTrue(flt.is_match(should_match));
		assertTrue(!flt.is_match(should_not_match));
	}

	@Test //Make sure that a movie with the given id passes, and a movie without doesn't.
	public void testMovieFilter() { 
		Filter<Movie> flt = new MovieFilter(3);
		Movie should_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1988", "", "", 
			"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0"});
		Movie should_not_match = new Movie(new String[] {"2", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
		assertTrue(flt.is_match(should_match));
		assertTrue(!flt.is_match(should_not_match));
	}


	@Test //Ensure that a User with the given gender passes, and a user without doesn't.
	public void testGenderFilter() {
		Filter<User> flt = new GenderFilter('M');
		User should_match = new User(new String[] {"3","21","M","fireman","12345"});
		User should_not_match = new User(new String[] {"4","22","F","astronaut","12345"});
		assertTrue(flt.is_match(should_match));
		assertTrue(!flt.is_match(should_not_match));
		
	}

	@Test //Ensure that a User with the given gender passes, and a user without doesn't.
	public void testUserFilter() {
		Filter<User> flt = new UserFilter(3);
		User should_match = new User(new String[] {"3","21","M","fireman","12345"});
		User should_not_match = new User(new String[] {"4","22","F","astronaut","12345"});
		assertTrue(flt.is_match(should_match));
		assertTrue(!flt.is_match(should_not_match));
		
	}

	@Test //Ensure that a rating with the given zip passes and one without doesn't.
		//Note that we're testing a partial zip.
	public void testZipFilter_Partial() {
		Filter<Rating> flt = new ZipFilter("123");
		Rating should_match = new Rating(new String[] {"3","3","5"}, "12345");
		Rating should_not_match = new Rating(new String[] {"3","3","5"}, "54321");
		assertTrue(flt.is_match(should_match));
		assertFalse(flt.is_match(should_not_match));		
	}


	@Test //Ensure that a rating with the given zip passes and one without doesn't.
		//Note that we're testing a full zip.
	public void testZipFilter_Full() {
		Filter<Rating> flt = new ZipFilter("12345");
		Rating should_match = new Rating(new String[] {"3","3","5"}, "12345");
		Rating should_not_match = new Rating(new String[] {"3","3","5"}, "54321");
		assertTrue(flt.is_match(should_match));
		assertFalse(flt.is_match(should_not_match));		
	}


	@Test //Ensure that a rating with the given score/value passes, and one without doesn't.
	public void testValueRatingFilter() {
		Filter<Rating> flt = new ValueRatingFilter(3);
		Rating should_match = new Rating(new String[] {"3","3","3"}, "12345");
		Rating should_not_match = new Rating(new String[] {"3","3","5"}, "54321");
		assertTrue(flt.is_match(should_match));
		assertFalse(flt.is_match(should_not_match));		
	}

	@Test //Ensure that a rating with the given movieID passes, and one without doesn't.
	public void testMovieRatingFilter() {
		Filter<Rating> flt = new MovieRatingFilter(100);
		Rating should_match = new Rating(new String[] {"3","100","3"}, "12345");
		Rating should_not_match = new Rating(new String[] {"3","4","5"}, "54321");
		assertTrue(flt.is_match(should_match));
		assertFalse(flt.is_match(should_not_match));		
	}

	@Test //Ensure that a rating belonging to a given user passes, and one that doesn't doesn't
	public void testUserRatingFilter() {
		Filter<Rating> flt = new UserRatingFilter(9);
		Rating should_match = new Rating(new String[] {"9","100","3"}, "12345");
		Rating should_not_match = new Rating(new String[] {"10","4","5"}, "54321");
		assertTrue(flt.is_match(should_match));
		assertFalse(flt.is_match(should_not_match));		
	}

	@Test //Ensure that a rating not belonging to a given user passes, and one that does doesn't
	public void testNegatedUserRatingFilter() {
		Filter<Rating> flt = new NegatedUserRatingFilter(10);
		Rating should_match = new Rating(new String[] {"9","100","3"}, "12345");
		Rating should_not_match = new Rating(new String[] {"10","4","5"}, "54321");
		assertTrue(flt.is_match(should_match));
		assertFalse(flt.is_match(should_not_match));		
	}

	@Test //Make sure that a movie with the given id passes, and a movie without doesn't.
	public void testUserMovieFilter() { 
		Filter<Movie> flt = new UserMovieFilter(3);
		Movie should_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1988", "", "", 
			"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0"});
		Movie should_not_match = new Movie(new String[] {"2", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
		should_match.add_user(3);
		assertTrue(flt.is_match(should_match));
		assertTrue(!flt.is_match(should_not_match));
	}

	@Test //Make sure that a movie with the given id fails, and a movie without doesn't.
	public void testNegatedUserMovieFilter() { 
		Filter<Movie> flt = new NegatedUserMovieFilter(3);
		Movie should_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1988", "", "", 
			"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0"});
		Movie should_not_match = new Movie(new String[] {"2", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
		should_not_match.add_user(3);
		assertTrue(flt.is_match(should_match));
		assertTrue(!flt.is_match(should_not_match));
	}


	@Test //Make sure that a movie with the given year+genre passes, and a movie without one or the other or both doesn't.
	public void testAndFilter() { 
		Filter<Movie> flt1 = new YearFilter("199");
		Filter<Movie> flt2 = new GenreFilter(1);
		AndFilter<Movie> flt = new AndFilter<Movie>();
		flt.add(flt1);
		flt.add(flt2);
		
		Movie should_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0"});
		Movie should_not_match1 = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1988", "", "", 
			"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
		Movie should_not_match2 = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
		Movie should_not_match3 = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1988", "", "", 
			"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
		assertTrue(flt.is_match(should_match));
		assertTrue(!flt.is_match(should_not_match1));
		assertTrue(!flt.is_match(should_not_match2));
		assertTrue(!flt.is_match(should_not_match3));
	}



	@Test //Make sure that a movie with the given year passes, and a movie without doesn't.
				//Here we are testing for a partial year.
	public void testOrFilter() { 
		Filter<Movie> flt1 = new YearFilter("199");
		Filter<Movie> flt2 = new GenreFilter(1);
		OrFilter<Movie> flt = new OrFilter<Movie>();
		flt.add(flt1);
		flt.add(flt2);
		
		Movie should_match1 = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0"});
		Movie should_not_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1988", "", "", 
			"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
		Movie should_match2 = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
		Movie should_match3 = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1988", "", "", 
			"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
		assertTrue(flt.is_match(should_match1));
		assertTrue(flt.is_match(should_match2));
		assertTrue(flt.is_match(should_match3));
		assertTrue(!flt.is_match(should_not_match));
	}
}

