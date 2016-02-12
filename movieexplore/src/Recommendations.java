/**
 * MovieExplore, A Project for SENG301
 * Copyright 2010, Gregg and Kyle
 *
 * Refer to LICENSE for legalese.
 */

import java.util.List;
import java.util.Collections;


public class Recommendations {

	/**
	 * This function computes the most highly recommended movies for a given user.
	 * It uses the 5 most similar users and finds any movies that they have seen
	 * and the given user has not. It then uses a bayesian average on those movies
	 * to determine the most popular ones.
	 * @param
	 */
	public static void main (ArgParser args) {

		DataCenter dc = DataCenter.get_instance();

		OrFilter<Rating> rating_flt = new OrFilter<Rating>();
		for (Integer user: SimilarUsers.find_similar_users(args.user_id()))
			rating_flt.add(new UserRatingFilter(user));

		AndFilter<Movie> movie_flt = new AndFilter<Movie>();
		movie_flt.add(new NegatedUserMovieFilter(args.user_id()));
		movie_flt.add(args.movie_filter());

		List<Movie> movies_seen_by_similar_users = dc.filter_by(movie_flt, rating_flt);

		// Compute the bayesian and sort.
		Util.compute_bayesian(movies_seen_by_similar_users);
		Collections.sort(movies_seen_by_similar_users, new BayesianComparator());

		// Only splice the list if it has more than 5 elements.
		if (movies_seen_by_similar_users.size() > 5)
			movies_seen_by_similar_users = movies_seen_by_similar_users.subList(0, 5);
		// Loop through the movies and display titles.
		else if (movies_seen_by_similar_users.size() == 0)
			System.out.println("There were no suitable movie recommendations found."+
			" Perhaps try a different user or a less restrictive search.");
		for (Movie movie: movies_seen_by_similar_users)
			System.out.println(movie.title());
		if (movies_seen_by_similar_users.size() > 0 && movies_seen_by_similar_users.size() < 5)
			System.out.println("There are no more suitable movie recommendations to display.");

	}
}

