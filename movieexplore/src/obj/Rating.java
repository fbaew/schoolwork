/**
 * MovieExplore, A Project for SENG301
 * Copyright 2010, Gregg and Kyle
 *
 * Refer to LICENSE for legalese.
 */

import java.util.Comparator;


public class Rating {

	private int rating;
	private int user;
	private int movie_id;

	
private String zip;
	
	/**
	 * Rating contructor. Takes the raw parsed fields and a zip code as arguments
	 * and populates the internal fields with this information.
	 * @param parsed_fields The string array that comes directly from a row in the 
	 * data file.
	 * @param zip The zip code this rating came from. Not available in the raw row
	 * data this comes from a cross lookup of the user id.
	 */
	Rating(String[] parsed_fields, String zip) {

		this.user = Util.parse_int(parsed_fields[0]);
		this.movie_id = Util.parse_int(parsed_fields[1]);
		this.rating = Util.parse_int(parsed_fields[2]);
		// parsed_fields[3] is the timestamp.
		this.zip = zip;
	}

	/**
	 * @return An integer value of this ratings rating.
	 */
	public int rating() {
		return rating;
	}
	
	/**
	 * @return An integer value of this ratings 
user id.
	 */
	public int user() {
		return user;
	}

	/**
	 * @return An integer value of this ratings movie id.
	 */
	public int movie_id() {
		return movie_id;
	}

	/**
	 * @return A string of this ratings zip code.
	 */
	public String zip() {
		return zip;
	}
}


class RatingComparator implements Comparator<Rating> {

	public int compare(Rating rating1, Rating rating2) {
			if (rating1.rating() > rating2.rating())
				return -1;
			else if (rating1.rating() < rating2.rating())
				return 1;
			else
				return 0;
	}	
}