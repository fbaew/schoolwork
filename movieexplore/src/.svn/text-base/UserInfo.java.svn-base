/**
 * MovieExplore, A Project for SENG301
 * Copyright 2010, Gregg and Kyle
 *
 * Refer to LICENSE for legalese.
 */

import java.util.Collections;
import java.util.Formatter;
import java.util.List;

public class UserInfo {

	/**
	 * This function takes a user id and computes and displays some information
	 * about the user. 
	 * @param
	 */
	public static void main (ArgParser args) {

		DataCenter dc = DataCenter.get_instance();
		Formatter out = new Formatter(System.out);

		User user = dc.filter_users_by(new UserFilter(args.user_id())).get(0);

		out.format("%1$-12s%2$d\n", "User id:", user.id());
		out.format("%1$-12s%2$d\n", "Age:", user.age());
		out.format("%1$-12s%2$c\n", "Gender:", user.gender());
		out.format("%1$-12s%2$s\n", "Occupation:", user.occupation());
		out.format("%1$-12s%2$s\n", "Zip:", user.zip());

		List<Rating> ratings = dc.filter_ratings_by(new UserRatingFilter(args.user_id()));
		out.format("Ratings:\n");
		out.format("%1$-12s%2$d\n", "  Total:", ratings.size());

		if (ratings.size() > 0) {
			int ratings_sum = 0;
			for (Rating rating: ratings)
				ratings_sum += rating.rating();
			out.format("%1$-12s%2$1.2f\n", "  Average:", (float)ratings_sum/(float)ratings.size());

			Collections.sort(ratings, new RatingComparator());
			Rating top_rating = ratings.get(0);
			Rating bot_rating = ratings.get(ratings.size()-1);
			int top_id = top_rating.movie_id();
			String top_movie_title = dc.filter_movies_by(new MovieFilter(top_id)).get(top_id).title();
			int bot_id = bot_rating.movie_id();
			String bot_movie_title = dc.filter_movies_by(new MovieFilter(bot_id)).get(bot_id).title();
			out.format("%1$-12s%2$s\n", "Top:"   , top_rating.rating()+" - "+top_movie_title);
			out.format("%1$-12s%2$s\n", "Bottom:", bot_rating.rating()+" - "+bot_movie_title);
		}
	}
}

