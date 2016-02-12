import static org.junit.Assert.*;
import org.junit.Before;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import org.junit.Test;


public class DataCenterTest {
	DataCenter dc = DataCenter.get_instance();

	@Before
	public void setUp() {
			dc.set_path("data/basic/");
	}

	// @Test
	// public void testGet_instance() {
	// 	fail("Not yet implemented");
	// }

	// @Test
	// public void testSet_path() {
	// 	
	// }

////////////////////////3 Tests for filter_users_by()

	@Test //does the filter we pass actually get applied?
	public void testFilter_users_by() {
		for (User user : dc.filter_users_by(new GenderFilter('M')))
			assertTrue(user.gender() == 'M');
	}

	@Test //do we just get an empty set when we filter by two mutually
				//exclusive things?
	public void testFilter_users_by_Empty_Result() {
		AndFilter<User> filt = new AndFilter<User>();
		filt.add(new GenderFilter('M'));
		filt.add(new GenderFilter('F'));
		assertTrue(dc.filter_users_by(filt).size() == 0);
	}
	
	@Test //If we pass a null filter, is the result the same as the
				//unfiltered data set?
	public void testFilter_users_by_Null_Filter() {
		for (int i = 0; i < 8; i++)
			assertTrue(dc.filter_users_by(null).get(i).id() == i+1);
	}
	
	/////////////////3 tests for filter_by()
	@Test //Given 2 valid filters, does it work properly?
	public void testFilter_by() {
		Filter<Rating> ratingFilter = new UserRatingFilter(1);
		Filter<Movie> movieFilter = new GenreFilter(1);
		List<Integer> control = Arrays.asList(new Integer[]{2,4});
		for (Movie m : dc.filter_by(movieFilter, ratingFilter))
			assertTrue(control.contains(m.id()));
		assertTrue(control.size() == dc.filter_by(movieFilter, ratingFilter).size());
		
	}

	@Test //given null filters, we should get the whole data set back.
	public void testFilter_by_Null_Filter() {
//		List<Integer> control = new ArrayList<Integer>();
		List<Integer> control = Arrays.asList(new Integer[]{1,2,3,4,5,6});

		for (Movie m : dc.filter_by(null, null))
			assertTrue(control.contains(m.id()));
		assertTrue(control.size() == dc.filter_by(null, null).size());	
	}
	
	@Test //given an overly restrictive set of filters, we should get no data back.
	public void testFilter_by_Empty_Results() {
		AndFilter<Rating> ratingFilter = new AndFilter<Rating>();
		//Note that ratingFilter returns false unconditionally.
		ratingFilter.add(new UserRatingFilter(1));
		ratingFilter.add(new NegatedUserRatingFilter(1));
		assertTrue(dc.filter_by(null, ratingFilter).size() == 0);
	}
	
	//////////////Some Tests For filter_ratings_by()
	
	@Test //See that a rating belonging to the specified user passes, and one that does not does not
	public void testFilter_ratings_by() {
		Filter<Rating> ratingFilter = new UserRatingFilter(1);
		for (Rating r : dc.filter_ratings_by(ratingFilter))
			assertTrue(r.user() == 1);
		assertTrue(dc.filter_ratings_by(ratingFilter).size() == 6 );
	}
	
	@Test //Ensure that the system doesn't freak out when we filter out -everything-
	public void testFilter_ratings_by_Empty_Result() {
		AndFilter<Rating> falseFilter = new AndFilter<Rating>();
		//Note that ratingFilter returns false unconditionally.
		falseFilter.add(new UserRatingFilter(1));
		falseFilter.add(new NegatedUserRatingFilter(1));
		assertTrue(dc.filter_ratings_by(falseFilter).size() == 0 );
	}
	
@Test
	public void testFilter_ratings_by_Null_Filter() {
		List<Integer> control = Arrays.asList(new Integer[]{1,2,3,1,2,3,1,2,3,4,1,
																												2,1,2,3,4,5,6,7,1});
		for (int i = 0; i <= 19; i++) {
			assertTrue(dc.filter_ratings_by(null).get(i).user() == control.get(i));
		}
	}
	
	////////////////Some tests for filter_movies_by()

	
	@Test //make sure filtering by some filter works as expected.
	public void testFilter_movies_by() {
		List<Integer> control = Arrays.asList(new Integer[]{2,4});
		for (Map.Entry<Integer, Movie> entry: dc.filter_movies_by(new GenreFilter(1)).entrySet())
			assertTrue(control.contains(entry.getKey()));
		assertTrue(dc.filter_movies_by(new GenreFilter(1)).entrySet().size() ==
							 control.size());
	}
	
	@Test //make sure filtering by a null filt works.
	public void testFilter_movies_by_Null_Filter() {
		List<Integer> control = Arrays.asList(new Integer[]{1,2,3,4,5,6});
		for (Map.Entry<Integer, Movie> entry: dc.filter_movies_by(null).entrySet())
			assertTrue(control.contains(entry.getKey()));
		assertTrue(dc.filter_movies_by(null).entrySet().size() ==
							 control.size());
	}
	
	@Test //Filter which doesn't allow any movies to pass...
	public void testFilter_movies_by_Empty_Results() {
		AndFilter<Movie> movieFilter = new AndFilter<Movie>();
		for (int i = 0; i<19; i++)
			movieFilter.add(new GenreFilter(i));
		
		
		assertTrue(dc.filter_movies_by(movieFilter).entrySet().size() == 0);
	}
	
	
	//Really just one case to consider for genres...

	@Test
	public void testGenres() {
		
		List<String> control = Arrays.asList(new String[] {
			"unknown",
			"Action",
			"Adventure",
			"Animation",
			"Children's",
			"Comedy",
			"Crime",
			"Documentary",
			"Drama",
			"Fantasy",
			"Film-Noir",
			"Horror",
			"Musical",
			"Mystery",
			"Romance",
			"Sci-Fi",
			"Thriller",
			"War",
			"Western"});
		
		for (String g : dc.genres())
			assertTrue(control.contains(g));
		assertTrue(control.size() == dc.genres().size());
	}

}
