import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;


public class ArgParserTest {

	@Before
	public void arg_parser_setup () {
	}

	@Test
	public void testArgParser() {
		ArgParser args = new ArgParser(new String[] {"data/large", "top" });
		assertTrue(args.option()=="top");
	}

	@Test
	public void testArgParser_2() {
		ArgParser args = new ArgParser(new String[] {"data/large", "top", "--histogram"});
		assertTrue(args.histogram()==true);
	}

	@Test
	public void testArgParser_5() {
		ArgParser args = new ArgParser(new String[] {"data/large", "top"});
		assertTrue(args.limit()==5);
	}

	@Test
	public void testArgParser_6() {
		ArgParser args = new ArgParser(new String[] {"data/large", "top", "--limit=99"});
		assertTrue(args.limit()==99);
	}

	@Test
	public void testArgParser_3() {
		ArgParser args = new ArgParser(new String[] {"data/large", "top", "--genre=12"});
		Movie should_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0"});
		Movie should_not_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
		assertTrue(args.movie_filter().is_match(should_match));
		assertTrue(args.movie_filter().is_match(should_not_match)==false);
	}

	@Test
	public void testArgParser_4() {
		ArgParser args = new ArgParser(new String[] {"data/large", "top", "--year=1999"});
		Movie should_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0"});
		Movie should_not_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1988", "", "", 
			"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
		assertTrue(args.movie_filter().is_match(should_match));
		assertTrue(args.movie_filter().is_match(should_not_match)==false);
	}

	@Test
	public void testArgParser_7() {
		ArgParser args = new ArgParser(new String[] {"data/large", "top", "--zip=123"});
		Rating should_match = new Rating(new String[] {"10", "10", "5"}, "12344");
		Rating should_not_match = new Rating(new String[] {"10", "10", "5"}, "12244");
		assertTrue(args.rating_filter().is_match(should_match));
		assertTrue(args.rating_filter().is_match(should_not_match)==false);
	}

	@Test
	public void testArgParser_8() {
		ArgParser args = new ArgParser(new String[] {"data/large", "similar-users", "199"});
		assertTrue(args.user_id()==199);
	}

	@Test
	public void testMovie_filter() {
		ArgParser args = new ArgParser(new String[] {"data/large", "top", "--year=1999", "--genre=action"});
		Movie should_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0"});
		Movie should_not_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
		assertTrue(args.movie_filter().is_match(should_match));
		assertTrue(args.movie_filter().is_match(should_not_match)==false);
	}

	@Test
	public void testMovie_filter_1() {
		ArgParser args = new ArgParser(new String[] {"data/large", "top", "--year=1999", "--genre=action"});
		Movie should_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0"});
		Movie should_not_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1988", "", "", 
			"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
		assertTrue(args.movie_filter().is_match(should_match));
		assertTrue(args.movie_filter().is_match(should_not_match)==false);
	}

	@Test
	public void double_filter() {
		ArgParser args = new ArgParser(new String[] {"data/large", "top", "--year=1999", "--genre=action", "--zip=312"});
		Movie m_should_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1999", "", "", 
			"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0"});
		Movie m_should_not_match = new Movie(new String[] {"3", "Some movie (1999)", "01-Jan-1988", "", "", 
			"0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
		Rating r_should_match = new Rating(new String[] {"10", "3", "5"}, "31234");
		Rating r_should_not_match = new Rating(new String[] {"11", "3", "1"}, "32145");
		assertTrue(args.movie_filter().is_match(m_should_match));
		assertTrue(args.movie_filter().is_match(m_should_not_match)==false);
		assertTrue(args.rating_filter().is_match(r_should_match));
		assertTrue(args.rating_filter().is_match(r_should_not_match)==false);
	}
}

