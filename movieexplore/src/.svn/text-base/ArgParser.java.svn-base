/**
 * MovieExplore, A Project for SENG301
 * Copyright 2010, Gregg and Kyle
 *
 * Refer to LICENSE for legalese.
 */

import java.util.List;
import java.util.ArrayList;


public class ArgParser {

	private List<Integer> genres = new ArrayList<Integer>();
	private int limit = -1;
	private int user_id = 0;
	private String year = null;
	private String zip = null;
	private boolean histogram = false;
	private String option = null;

	public ArgParser(String[] args) {

		if (args.length < 2)
			Util.exit(1, "At least 2 parameters are required.");
			// Util.exit(1, "At least 2 parameters are required for all options.");

		// Tell the data center where to expect the data files.
		DataCenter.get_instance().set_path(args[0]);

		// Should maybe check here if args[1] is in 'user', 'top', 'similar-users', ...
		this.option = args[1];

		if (args.length < 3)
			if (option.equals("user") || option.equals("similar-users") || option.equals("movie-recommendations"))
				Util.exit(1, "At least 3 parameters are required for user, similar-users, and movie-recommendations");

		// Start at argument 3
    for (int i=2; i<args.length; i++) {

    	String[] split_arg = args[i].split("=");

    	// More than one `=` sign is not allowed.
    	if (split_arg.length > 2)
    		Util.exit(1, "Switch with more than one equals sign found.");
			// Single equals sign not allowed.
			else if (split_arg.length == 0)
				Util.exit(1, "Single equals sign found.");

			// Check for valid switches one at a time.
			if (split_arg[0].equals("--histogram"))
				histogram = true;
			else if (split_arg[0].equals("--limit"))
				parse_limit(split_arg);
			else if (split_arg[0].equals("--year"))
				parse_year(split_arg);
			else if (split_arg[0].equals("--zip"))
				parse_zip(split_arg);
			else if (split_arg[0].equals("--genre"))
				parse_genres(split_arg);
			else {
				try {
					try_to_parse_user_id(split_arg);
				} catch (NumberFormatException n) {
 					Util.exit(1, "Unknown switch.");
				}
			}
		}
	}

	private void try_to_parse_user_id(String[] arg) throws NumberFormatException {
		int tmp = Integer.parseInt(arg[0]);
		if (user_id_valid(tmp))
			user_id = tmp;
		else
			// Invalid user id found
			user_id = -1;
	}

	public int user_id() {
		if (user_id > 0)
			return user_id;
		else if (user_id == 0)
			Util.exit(1, "User ID needed for this option but not found on command line.");
		else if (user_id == -1)
			Util.exit(1, "User ID does not exist.");
		return 1;
	}
  
	/**
	 * @return An integer that is the amount of resuls the user wants displayed.
	 */
	public int limit() {
		if (limit==-1)
			return 5;
		return limit;
	}

	/**
	 * @return A boolean value whether or not a histogram should be displayed.
	 */
	public boolean histogram() {
		return histogram;
	}

	public String option() {
		return option;
	}

	// Filter Functions

	public AndFilter<Movie> movie_filter() {
		AndFilter<Movie> filter = new AndFilter<Movie>();
		for (Integer i : genres)
			filter.add(new GenreFilter(i));
		if (year != null)
			filter.add(new YearFilter(year));
		return filter;
		
	}
	
	public AndFilter<Rating> rating_filter() {
		AndFilter<Rating> filter = new AndFilter<Rating>();
		if (zip != null)
			filter.add(new ZipFilter(zip));
		return filter;
	}

	// Parse Methods

	/**
	 * Private method that checks the valid switches for --limit. If it is found, 
	 * set the internal limit variable accordingly.
	 */
	private void parse_limit(String[] sw) {

		String rhs = rhs_OK(sw);
		// Try to convert the right hand side to an integer.
		if (limit==-1) {
			limit = Util.parse_int(rhs);
			if (limit <= 0)
				Util.exit(1,"Invalid limit entered.");
		}
		else 
			Util.exit(1, "Only one --limit switch is allowed."); 
	}

	/** 
	 * Private method that checks the valid switches for --year. 
	 */
  private void parse_year(String[] sw) {

		String rhs = rhs_OK(sw);
		// Only allow one --year switch. Do this because if 2 --year switches
		// are allowed then are they treated as `and` or `or`? I don't even care
		// just don't allow this to happen.
		if (year!=null)
			Util.exit(1, "Only one --year switch is allowed.");
		// Check if the right had side of the --year switch is actually an integer.
		Util.parse_int(rhs);
		// Year is a String because we use .startWith() method to easily check, 
		// for example, if a movie from 1997 should be included when the user
		// enters 199 (via .startsWith("199")). There is no integer equivalent. 
		year = rhs;
	}
  
	/**
	 * Private method that checks the valid switches for --zip.
	 */
  private void parse_zip(String[] sw) {

		if (option.equals("movie-recommendations"))
			Util.exit(1, "--zip not allowed when movie-explorer is the option.");
		String rhs = rhs_OK(sw);
		// Only allow 1 --zip switch otherwise who knows whether they are `or` or
		// `and`. Just don't allow this to take the easy route out.
		if (zip!=null)
			Util.exit(1, "Only one --zip switch is allowed.");
		// Check if the right had side of the --zip switch is actually an integer.
		Util.parse_int(rhs);
		// Leave zip as a string for the same reasons as year, above. 
		zip = rhs;
	}

	/**
	 * Private method that checks the valid switches for --genre. 
	 */
	private void parse_genres(String[] sw) {

		String rhs = rhs_OK(sw);
		// This is OK to do every time becuase dc.genres() caches the results from
		// the genres file and does not re read it every time dc.genres() is called.
		DataCenter dc = DataCenter.get_instance();
		List<String> valid_genres = dc.genres();

		// Capitalize the right hand side of --genre.
		rhs = capitalize(rhs);
		// Check if the right hand side of --genre is a valid genre.
		if (valid_genres.contains(rhs))
			// If it is, add it to the list of genres to check.
			genres.add(valid_genres.indexOf(rhs));
		else {
			// Something failed, try and see if a valid genre index number is given.
			int genre_index = Util.parse_int(rhs);
			// Check the range.
			if (check_for_valid_range(genre_index, 0, valid_genres.size())==false)
				Util.exit(1, "Genre index out of bounds.");
			genres.add(genre_index);
		}
	}

	private boolean check_for_valid_range (int int_to_check, int min, int max) {
		if ((int_to_check >= min) && (int_to_check <= max))
			return true;
		return false;
	}

	private boolean user_id_valid(int uid) {
		return DataCenter.get_instance().filter_users_by(new UserFilter(uid)).size()>0;
	}

	private static String capitalize(String s) {
		if (s.length() == 0) return s;
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

	private String rhs_OK (String[] sw) {
		if (sw.length != 2)
			Util.exit(1, "Malformed right hand side.");
		return sw[1];
	}

}

