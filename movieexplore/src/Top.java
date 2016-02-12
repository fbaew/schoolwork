/**
 * MovieExplore, A Project for SENG301
 * Copyright 2010, Gregg and Kyle
 *
 * Refer to LICENSE for legalese.
 */

import java.util.Collections;
import java.util.Formatter;
import java.util.List;


public class Top {

	/**
	 * Calculates the top movies given different filters. 
	 * @param
	 */
	public static void main(ArgParser args) {

		DataCenter dc = DataCenter.get_instance();
		Formatter formatter = new Formatter(System.out);

		// Get a movie list filtered by the argsitches specified on the command line.
		List<Movie> movies = dc.filter_by(args.movie_filter(), args.rating_filter());

		if (movies.size() == 0) 
			Util.exit(1, "No movies were found which matched the criteria specified."+
			             " Perhaps try a more general query.");

		// Compute the bayesian average for all movies in the list, then sort.
		Util.compute_bayesian(movies);
    Collections.sort(movies, new BayesianComparator());

		formatter.format("Pop  Title\n");
		formatter.format("-------------------\n");

		// Find minimum of movies.size() or args.limit()
		int limit = movies.size() < args.limit() ? movies.size() : args.limit();
		for (Movie movie: movies.subList(0, limit))
			formatter.format("%1$2.0f   %2$-40s  %3$2s\n", 
					movie.bayesian_avg(), movie.title(), movie.histogram(args.histogram()));

		if (movies.size() < args.limit())
      System.out.println("There are no more movies to display. Perhaps try a more general query.");
	}
}

