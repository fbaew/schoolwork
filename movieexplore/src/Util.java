/**
 * MovieExplore, A Project for SENG301
 * Copyright 2010, Gregg and Kyle
 *
 * Refer to LICENSE for legalese.
 */

import java.util.List;
import java.util.Comparator;
import java.util.Collection;


public class Util {

	/**
	 * This function is called whenever the program needs to exit for any non normal
	 * reason. It displays a customized error message and then the program usage,
	 * and exits on a specified error code.
	 */
	public static void exit(int error_code, String message) {
		System.out.println();
		System.out.println(message);
		for (int i=0; i<usage.length; i++)
			System.out.println(usage[i]);
		System.exit(error_code);
	}

	/**
	 * The program usage.
	 */
	public static String[] usage = {
		"",
		"Usage: MovieExplore <data> <command> ",
		"",
		"  Where <data> is the path to the 6 data files, and",
		"        <command> is one of:",
		"           stats",
		"           top",
		"           top --genre=",
		"           top --year=",
		"           top --zip=",
		"           top --year= --genre=",
		"           top --genre= --limit=",
		"           top --*= --histogram",
		"           user <userid>",
		"           similar-users <userid>",
		"           movie-recommendations <userid>",
		""
	};

	/**
	 * Helper function that computes 2 bayesian constants from a given movie set and
	 * uses those values to assign bayesian averages to each individual movie. This
	 * has to be done in 2 steps, and cannot be reduced to one. The first constant
	 * is the average number of ratings per movie, while the second is the overall
	 * average rating of the entire set.
	 */
	public static void compute_bayesian (List<Movie> movies) {

		// These 2 constants first need to be found first.
		int total_ratings = 0;
		int cumulative_rating = 0;
		// Loop through the given movies, appending as we go.
		for (Movie movie: movies) {
			total_ratings += movie.ratings_size();
			cumulative_rating += movie.avg_rating();
		}

		// Normalize constants.
		float avg_ratings_size = (float) total_ratings / (float) movies.size();
		float avg_rating = (float) cumulative_rating / (float) movies.size();

		// Now apply constants to each movie individually.
		for (Movie movie: movies)
			movie.compute_bayesian(avg_ratings_size, avg_rating);

		/*
		System.out.println("\nBayesian statistics: ");
		System.out.println("\t"+total_ratings+" ratings for "+movies.size()+" movies.");
		System.out.println("\t"+avg_ratings_size+" ratings per movie (average).");
		System.out.println("\t"+avg_rating+" average rating.\n");
		*/
	}

	public static int parse_int(String int_to_parse) {
		try {
			return Integer.parseInt(int_to_parse);
		} catch (NumberFormatException n) {
			exit(1, "Tried to parse an invalid integer.");
			return 0;
		}
	}
	
	public static String trim_output(String input) {
		if (input.endsWith(" "))
			return trim_output(input.substring(0,input.length()-1));
		else
			return input;
	}
}

class BayesianComparator implements Comparator<Movie> {

	/**
	 * This comparator is used to rank each movie based on their bayesian average.
	 */
	public int compare(Movie v, Movie w) {

		if (v.bayesian_avg() == w.bayesian_avg())
			return v.title().compareTo(w.title());
		else if (v.bayesian_avg() < w.bayesian_avg())
			return 1;
		return -1;
	}
}

