/**
 * MovieExplore, A Project for SENG301
 * Copyright 2010, Gregg and Kyle
 *
 * Refer to LICENSE for legalese.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

import java.util.Iterator;


public class DataCenter {

	private Map<Integer, Movie> movies = null;
	private Map<Integer, User> users = null;
	private List<Rating> ratings = null;
	private List<String> genres = null;
	private String path;

	/**
	 * Private constructor prevents instantiation from other classes.
	 */
	private DataCenter() {
	}
 
	/**
	 * DataCenterHolder is loaded on the first execution of DataCenter.get_instance() 
	 * or the first access to DataCenterHolder.INSTANCE, not before
	 */
	private static class DataCenterHolder { 
		private static final DataCenter INSTANCE = new DataCenter();
	}
 
	/**
	 * Used to get the static instance of the data center.
	 */
	public static DataCenter get_instance() {
		return DataCenterHolder.INSTANCE;
	}

	/**
	 * Because the constructor is never used this is needed to set where the data
	 * center should look for its files.
	 * @param p String path to the data files.
	 */
	public void set_path(String p) {
		// In case we are re-using this Data Center to open data from a different
		// directory, clear out the old lists and maps.
		movies = null;
		users = null;
		ratings = null;
		genres = null;
		// Check if the directory actually exists.
		if (!new File(p).isDirectory())
			Util.exit(2, "First argument not a valid directory.");
		// Check if all _required_ files are found. Note u.info and u.occupation are
		// not required for full functionality.
		for (String file_name: new String[] {"u.item", "u.data", "u.genre", "u.item"})
			if (!new File(p+File.separator+file_name).isFile())
				Util.exit(2, "Data files not found in specified directory.");
		// All files found, set the path.
		this.path = p;
	}

	// Filter methods

	/**
	 * This function takes both a movie and rating filter and will do 3 actions.
	 * It will first screen the movies data using the given movie filter. Next, 
	 * it will screen the ratings data using the given ratings filter. Then, it
	 * will append all of a movies ratings directly to the movie object. And last,
	 * it will delete any movies that have 0 ratings appended to them.
	 * @param movie_flt A filter that will be used on the movie data.
	 * @param rating_flt A filter that will be used on the ratings data. 
	 * @return A List of movie objects with ratings appended to the movies.
	 */
	public List<Movie> filter_by(Filter<Movie> movie_flt, Filter<Rating> rating_flt) {
		// Keep movies in dictionary for log(n) lookup speed.
		Map<Integer, Movie> movie_dict = filter_movies_by(movie_flt);
		// If null is passed to either filters, then don't filter anything.
		if (movie_flt == null)
			movie_flt = new DummyFilter<Movie>();
		if (rating_flt == null)
			rating_flt = new DummyFilter<Rating>();
		// Loop through all of the filtered ratings and attach them to their
		// respective movies.
		for (Rating rating: filter_ratings_by(rating_flt))
			if (movie_dict.containsKey(rating.movie_id()))
				movie_dict.get(rating.movie_id()).add_rating(rating);

		// Take all of the values from the dictionary and put them in a linked list.
		// Iterate through the list, removing any movies that have 0 ratings. 
		List<Movie> ret = new LinkedList<Movie>(movie_dict.values());
		Iterator<Movie> ret_iter = ret.iterator();
		while(ret_iter.hasNext()) 
			if (ret_iter.next().ratings_size()==0)
				ret_iter.remove();

		return ret;
	}

	/** 
	 * This method takes any user filter and will return matching users.
	 * @param user_filter
	 * @return A List of Users.
	 */
	public List<User> filter_users_by(Filter<User> user_filter) {
		load_users();

		// If the filter passed is null, then match everything.
		if (user_filter == null)
			user_filter = new DummyFilter<User>();
		// Start with empty linked list.
		List<User> ret = new LinkedList<User>();
		// Loop through all users from data and apply filter, add anything that matches.
		for (Map.Entry<Integer, User> entry: users.entrySet())
			if (user_filter.is_match(entry.getValue()))
				ret.add(entry.getValue());
		return ret;
	}

	/**
	 * This method takes any rating filters and returns a list of any that match.
	 * @param rating_flt
	 * @return A List of Ratings.
	 */
	public List<Rating> filter_ratings_by(Filter<Rating> rating_flt) {
		load_ratings();
		// If null is passed in, match every rating.
		if (rating_flt == null)
			rating_flt = new DummyFilter<Rating>();
		// Start with an empty ratings list.
		List<Rating> ret = new LinkedList<Rating>();
		// Loop through all ratings from data and add any that match the filter.
		for (Rating rating: ratings)
			if (rating_flt.is_match(rating))
				ret.add(rating);
		return ret;
	}
	
	/**
	 * This method takes any movie filter and returns a hashmap of <Integer, Movies>.
	 * @param movie_flt
	 * @return A HashMap of <Integer, Movies>.
	 */
	public Map<Integer, Movie> filter_movies_by(Filter<Movie> movie_flt) {
		load_movies();
		// If null is passed in, match every movie.
		if (movie_flt == null)
			movie_flt = new DummyFilter<Movie>();
		// Start with an empty HashMap.
		Map<Integer, Movie> ret = new HashMap<Integer, Movie>();
		// Loop through all movies, adding the ones that match. 
		for (Map.Entry<Integer, Movie> entry: movies.entrySet())
			if (movie_flt.is_match(entry.getValue()))
				ret.put(entry.getKey(), entry.getValue());
		return ret;
	}
	
	// Misc methods // // //

	/**
	 * Load the genres list and return everything it contains.
	 * @return A list of strings each representing a genre.
	 */
	public List<String> genres() {
		load_genres();
		return genres;
	}
	
	// Private methods // // //

	/**
	 * Private method to load all of the genres from the u.genre data file.
	 * These genres are used to map between integer values of genres and the 
	 * actual name of the genre. 
	 */
	private void load_genres() {
		// Only proceed if genres has NOT been initialized yet.
		if (genres==null) {
			genres = new ArrayList<String>();
			// u.genre has 2 fields delimited by |. 
			// Expected form is `name|id` .
			for (String[] parsed_genre: parse("u.genre", "\\|", 2))
				genres.add(parsed_genre[0]);
		}
	}

	/**
	 * Private method to load all of the movies from the u.item file. This file
	 * contains all of the movie meta data such as title, year of release, url, 
	 * and genre information. 
	 */
	private void load_movies() {
		// Only proceed if movies has NOT been initialized yet.
		if (movies==null) {
			load_ratings();
			movies = new HashMap<Integer, Movie>();
			// u.item has 24 fields and is delimited by |.
			// Expected form is 
			// id|title|release_date||url|19 genre fields each delimited by |.
			for (String[] parsed_movie: parse("u.item", "\\|", 24))
				movies.put(Util.parse_int(parsed_movie[0]), new Movie(parsed_movie));
			for (Rating rating: ratings)
				movies.get(rating.movie_id()).add_user(rating.user());
		}
	}

	/**
	 * Private method to load all of the ratings from the u.data file. This file
	 * contains all of the rating data such as movie id, rating, a timestamp, and
	 * the user id that submitted the rating.
	 */
	private void load_ratings() {
		// Only proceed if the ratings have not yet been initialized.
		if (ratings==null) {
			// The users are needed to load the ratings, so load them now.
			load_users();
			ratings = new ArrayList<Rating>();
			// u.data has 4 fields delimited by \t.
			// Expected form is user id\tmovie id\trating\ttimestamp.
			for (String[] parsed_rating: parse("u.data", "\\t", 4)) {
				// The users need to be loaded because the zip code needs to be known for
				// each rating at rating object creation time.
				String zip = users.get(Util.parse_int(parsed_rating[0])).zip();
				ratings.add(new Rating(parsed_rating, zip));
			}
		}
	}

	/**
	 * Private method to load all of the users from the u.user file. This file
	 * contains all of the user meta data such as age, user id, occupation, and
	 * zip code.
	 */
	private void load_users() {
		// Only proceed if users has NOT been initialized.
		if (users==null) {
			users = new HashMap<Integer, User>();
			// u.user has 5 fields delimited by |.
			// Expected form is user id|age|sex|occupation|zip code.
			for (String[] parsed_user: parse("u.user", "\\|", 5))
				users.put(Util.parse_int(parsed_user[0]), new User(parsed_user));
		}
	}

	/**
	 * Private method used by the load functions to get a list of string arrays,
	 * each string array representing a row in the raw data file.
	 * @param file_name The file name to be parsed.
	 * @param delimiter The delimiter in the file name to be parsed.
	 * @param fields_exp The number of fields expected after splitting at the delimiter.
	 * @return A list of string arrays.
	 */
	private List<String[]> parse(String file_name, String delimiter, int fields_exp) {
		List<String[]> list = new ArrayList<String[]>();
		try {
			// Read some buffered input from the input file name.
			BufferedReader in = new BufferedReader(new FileReader(path+File.separator+file_name));
			// temp will hold each full unsplit line.
			String temp;
			while ((temp = in.readLine()) != null) {
				// Split the raw line around the given delimiter.
				String[] array = temp.split(delimiter).clone();
				// Check if the number of fields expected matches the string array length.
				if (array.length == fields_exp)
					list.add(array);
			}
			in.close();
		} catch (IOException e) {
			// File could not be read or something, exit on error code 2.
			Util.exit(2, "An IOException was thrown while reading the file "+file_name+".");
		}
		return list;

	}
}


