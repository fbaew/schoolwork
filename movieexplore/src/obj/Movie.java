/**
 * MovieExplore, A Project for SENG301
 * Copyright 2010, Gregg and Kyle
 *
 * Refer to LICENSE for legalese.
 */

import java.lang.Math;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Movie {

	private List<Integer> genres = new ArrayList<Integer>();
	private List<Rating> ratings = new ArrayList<Rating>();
	private List<Integer> users_who_rated_this_movie = new LinkedList<Integer>();

	private int total_rating = 0;
	private float avg_rating = 0;
	private float bayesian_average = 0;

	private int id;

	private String title;
	private String year;

	/**
	 * Movie constructor. Initializes Lists, zeroes rating statistics, and sets the
	 * appropriate movie information fields from the data file.
	 * @param parsed_fields The string array parsed direcly from the data file.
	 */
	public Movie (String[] parsed_fields) {

		// Set all information that is coming from the data file.
		this.id = Util.parse_int(parsed_fields[0]);
		this.title = parsed_fields[1];
		assert parsed_fields[2].split("-").length==2;
		this.year = parsed_fields[2].split("-")[2];
		// parsed_fields[3] is empty.
		// parsed_fields[4] is the url.
		for (int i = 5; i <= 23; i++)
			if (parsed_fields[i].equals("1"))
				genres.add(i-5);
	}

	// One liners

	/**
	 * @return A boolean value that is true when the movie is in fact apart of the
	 * specified genre.
	 */
	public boolean is_genre(int genre) {
		return genres.contains(genre);
	}

	/**
	 * @return The average movie rating, out of 5 (not the same as bayesian average).
	 */
	public float avg_rating () {
		return this.avg_rating;
	}

	/**
	 * @return The title of the movie as a string.
	 */
	public String title() {
		return title;
	}
	
	/**
	 * @return The movie ID as an integer.
	 */
	public int id() {
		return id;
	}
	
	/**
	 * @return The year of the movie as a string.
	 */
	public String year() {
		return year;
	}

	// Bayesian methods

	/**
	 * This function computes the bayesian average for the movie. In order for this
	 * to work properly 2 constants need to be calculated on the set of movies this
	 * movie is apart of. Once that is done then the bayesian can be calculated 
	 * accurately.
	 * @param avg_ratings_size The average number of ratings per movie in the set.
	 * @param avg_rating         The overall average rating of movies in the set.
	 */
	public void compute_bayesian(float avg_ratings_size, float avg_rating) {
		float numerator = ((avg_ratings_size*avg_rating) + (this.ratings.size()*this.avg_rating));
		float denominator = (avg_ratings_size+this.ratings.size());
		this.bayesian_average =  numerator / denominator;
	}

	/**
	 * Return the bayesian average in percent.
	 * @return Bayesian average percentage.
	 */
	public float bayesian_avg() {
		return 100 * this.bayesian_average / (float) 5.0;
	}

	// Misc
	
	/**
	 * This function uses the formula given by Dr Sillito to compute an asterisk
	 * histogram for each movie.
	 * @return A string of asterisks proportional in size to the movie rating.
	 */
	public String histogram(boolean enabled) {
		if (enabled) {
			int stars = (int) (30 * Math.log(bayesian_avg()) / Math.log(100.0));
			String out = "";
			for (int i = 0; i < stars; i++)
				out += "*";
			return out;	
		} else 
			return "";
	}

	// Ratings

	public int ratings_size() {
		return ratings.size();
	}

	/**
	 * Function used to add a rating to this movie.
	 * @param rating The rating object to add.
	 */
	public void add_rating(Rating rating) {

		// Add the rating to the list.
		this.ratings.add(rating);
		// Keep track of a `total rating` which is just a summation of every rating
		// this movie has. 
		this.total_rating += rating.rating();
		// Re compute this movies average rating.
		this.avg_rating = (float) this.total_rating / (float) this.ratings.size();
	}

	/**
	 * This method checks whether or not a given user id has rated this movie.
	 * @param uid The user you want to check against the people who actually rated
	 * this movie.
	 * @return True if the user has in fact rated this movie, False if not.
	 */
	public boolean user_rated_this_movie (int uid) {
		return users_who_rated_this_movie.contains(uid);
	}

	public void add_user(int uid) {
		users_who_rated_this_movie.add(uid);
	}

}

