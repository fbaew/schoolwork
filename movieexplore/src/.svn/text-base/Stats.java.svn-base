/**
 * MovieExplore, A Project for SENG301
 * Copyright 2010, Gregg and Kyle
 *
 * Refer to LICENSE for legalese.
 */

import java.util.Formatter;


public class Stats {

	/**
	 * Stats display all sorts of cool information.
	 */
	public static void main () {

		DataCenter dc = DataCenter.get_instance();
		Formatter formatter = new Formatter(System.out);

		formatter.format("%1$-13s%2$6d\n", "users:", dc.filter_users_by(null).size());
		formatter.format("%1$-13s%2$6d\n", "  males:", dc.filter_users_by(new GenderFilter('M')).size());
		formatter.format("%1$-13s%2$6d\n", "  females:", dc.filter_users_by(new GenderFilter('F')).size());

		formatter.format("%1$-13s%2$6d\n", "movies:", dc.filter_movies_by(null).size());
		for (String genre: dc.genres())
			formatter.format("%1$-13s%2$6d\n", "  "+genre, dc.filter_movies_by(new GenreFilter(genre)).size());

		formatter.format("%1$-13s%2$6d\n", "ratings:", dc.filter_ratings_by(null).size());
		for (int i=1; i<=5; i++)
			formatter.format("%1$-13s%2$6d\n", "  "+(i), dc.filter_ratings_by(new ValueRatingFilter(i)).size());
	}
}

