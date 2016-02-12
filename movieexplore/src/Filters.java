/**
 * MovieExplore, A Project for SENG301
 * Copyright 2010, Gregg and Kyle
 *
 * Refer to LICENSE for legalese.
 */

import java.util.List;
import java.util.LinkedList;


/**
 * All filters must implement this method.
 * Returns true if the object matches the filter, false if it does not.
 */
interface Filter<T> {
	public boolean is_match(T object);
}

// Composite Filters

/**
 * An `and` filter meaning all of the filters contained in this object are
 * compared to each other using logical `and`.
 */
class AndFilter<T> implements Filter<T> {
	
	private List<Filter<T>> filters = new LinkedList<Filter<T>>();

	/**
	 * Mandatory implementation of is_match(T)
	 * @param object A movie or rating object that is being matched against the 
	 * filter.
	 * @return True if all the filters match an object.
	 */
	public boolean is_match(T object) {
		if (filters.size()==0)
			return true;
		for (Filter<T> filter : filters)
			// The first time an and filter fails the check fails as a whole.
			if (filter.is_match(object)==false)
				return false;
		return true;
	}

	public void add(Filter<T> new_filter) {
		filters.add(new_filter);
	}
}

/**
 * A composite `or` filter meaning all the filters in this composite are compared
 * to each other using logical `or`.
 */
class OrFilter<T> implements Filter<T> {

	private List<Filter<T>> filters = new LinkedList<Filter<T>>();

	/**
	 * Mandatory implementation of is_match(T)
	 * @param object A movie or rating object that is being matched against the 
	 * filter.
	 * @return True if at least one filter matches an object.
	 */
	public boolean is_match(T object) {
		if (filters.size()==0)
			return true;
		for (Filter<T> filter : filters)
			// The first time the or filter succeeds the check succeeds as a whole.
			if (filter.is_match(object))
				return true;
		return false;
	}

	public void add(Filter<T> new_filter) {
		filters.add(new_filter);
	}
}

// Special, always match filter

class DummyFilter<T> implements Filter<T> {
	public boolean is_match(T object) {
		return true;
	}
}

// Rating filters

class UserRatingFilter implements Filter<Rating> {
	private int uid;

	UserRatingFilter(int id) {
		this.uid = id;
	}
	public boolean is_match(Rating rating) {
		return (rating.user()==uid);
	}
}

class NegatedUserRatingFilter implements Filter<Rating> {
	private int uid;

	NegatedUserRatingFilter(int id) {
		this.uid = id;
	}
	public boolean is_match(Rating rating) {
		return !(rating.user()==uid);
	}
}

class ZipFilter implements Filter<Rating> {
	private String zip;

	ZipFilter(String zip) {
		this.zip = zip;
	}
	public boolean is_match(Rating rating) {
		return rating.zip().startsWith(zip);
	}
}

class ValueRatingFilter implements Filter<Rating> {
	private int target_rating;

	ValueRatingFilter(int target_rating) {
		this.target_rating = target_rating;
	}
	public boolean is_match(Rating remote_rating) {
		return (target_rating == remote_rating.rating());
	}
}

class MovieRatingFilter implements Filter<Rating> {
	private int movie_id;

	MovieRatingFilter (int movie_id) {
		this.movie_id = movie_id;
	}
	public boolean is_match(Rating rating) {
		return (rating.movie_id()==movie_id);
	}
}

// Movie filters // // //

class MovieFilter implements Filter<Movie> {
	private int movie_id;

	MovieFilter (int movie_id) {
		this.movie_id = movie_id;
	}
	public boolean is_match(Movie movie) {
		return (movie.id()==movie_id);
	}
}

class UserMovieFilter implements Filter<Movie> {
	private int uid;

	UserMovieFilter(int uid) {
		this.uid = uid;
	}
	public boolean is_match(Movie movie) {
		return movie.user_rated_this_movie(uid);
	}
}

class NegatedUserMovieFilter implements Filter<Movie> {
	private int uid;

	NegatedUserMovieFilter(int uid) {
		this.uid = uid;
	}
	public boolean is_match(Movie movie) {
		return !(movie.user_rated_this_movie(uid));
	}
}

class YearFilter implements Filter<Movie> {
	private String year = "";

	YearFilter(String year) {
		this.year = year;
	}
	public boolean is_match(Movie movie) {
		return movie.year().startsWith(year);
	}
}

class GenreFilter implements Filter<Movie> {
	private int genre;

	GenreFilter(int genre) {
		this.genre = genre;
	}
	GenreFilter(String genre) {
		this.genre = DataCenter.get_instance().genres().indexOf(genre);
	}
	public boolean is_match(Movie movie) {
		return movie.is_genre(genre);
	}
}

// User Filters // // //

class UserFilter implements Filter<User> {
	private int uid;

	UserFilter(int uid) {
		this.uid = uid;
	}
	public boolean is_match(User user) {
		return this.uid == user.id();
	}
}

class GenderFilter implements Filter<User> {
	private char sex;

	GenderFilter(char sex) {
		this.sex = sex;
	}
	public boolean is_match(User user) {
		return this.sex==user.gender();
	}
}
