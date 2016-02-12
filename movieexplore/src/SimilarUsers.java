/**
 * MovieExplore, A Project for SENG301
 * Copyright 2010, Gregg and Kyle
 *
 * Refer to LICENSE for legalese.
 */

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

import java.util.Collections;
import java.util.Comparator;

import java.util.Formatter;


public class SimilarUsers {

	/**
	 * Just a small display function. All of the heavy lifting for this feature is
	 * in find_similar_users().
	 */
	public static void main (ArgParser args) {

		DataCenter dc = DataCenter.get_instance();
		Formatter formatter = new Formatter(System.out);

		formatter.format("ID  Age Gen Occupation Zip\n");
		formatter.format("----------------------------\n");

		// Run a loop over 5 of the most similar users and display info about them.
		for (Integer similar_user: find_similar_users(args.user_id())) {
			User user = dc.filter_users_by(new UserFilter(similar_user)).get(0);
			formatter.format("%1$-3d %2$2d %3$2s   %4$-10s %5$s\n", 
			user.id(), user.age(), user.gender(), user.occupation(), user.zip());
		}
	}

	/**
	 * This function will find the 5 most similar users in the data center to the
	 * user id given. It does this by checking movies that the given user has rated 
	 * against other users ratings in the data center. If the given user has closely 
	 * rated many of the same movies as some other user in the data center, then
	 * a strong correlation between them is recorded.
	 * @param uid The user you want to find similar users to.
	 * @return    A list of the 5 most similar users.
	 */
	public static List<Integer> find_similar_users (int uid) {
 
		DataCenter dc = DataCenter.get_instance();

		// Hashmap representing a user id -to- how similar are my movie tastes to
		// this users movie tastes map.
		Map<Integer, Float> movie_taste_relatedness = new HashMap<Integer, Float>();

		for (Rating outer_rating: dc.filter_ratings_by(new UserRatingFilter(uid))) {
			AndFilter<Rating> rating_filters = new AndFilter<Rating>();
			rating_filters.add(new MovieRatingFilter(outer_rating.movie_id()));
			rating_filters.add(new NegatedUserRatingFilter(uid));
			for (Rating inner_rating: dc.filter_ratings_by(rating_filters)) {
				if (movie_taste_relatedness.containsKey(inner_rating.user())==false)
						movie_taste_relatedness.put(inner_rating.user(), new Float(0));
				Float score = movie_taste_relatedness.get(inner_rating.user());
				score += (float) Math.exp(-Math.abs(inner_rating.rating()-outer_rating.rating()));
				movie_taste_relatedness.put(inner_rating.user(), score);
			}
		}

		// Make a list of map entries so that it can be sorted.
		List<Map.Entry<Integer, Float>> temp = 
	  new ArrayList<Map.Entry<Integer, Float>>(movie_taste_relatedness.entrySet());
		Collections.sort(temp, new CompareUsers());
		if (temp.size() > 5)
			temp = temp.subList(0, 5);

		// Build the return list of similar users.
		List<Integer> ret = new ArrayList<Integer>();
		for (Map.Entry<Integer, Float> entry: temp)
			ret.add(entry.getKey());
		return ret;

	}
}

class CompareUsers implements Comparator<Map.Entry<Integer, Float>> {

	/**
	 * Comparator used to sort key-value pairs from a map. Simply just sorts by
	 * the values in the map.
	 */
	public int compare(Map.Entry<Integer, Float> left, Map.Entry<Integer, Float> right) {
		float difference = left.getValue() - right.getValue();
		if (difference != 0)
			return (difference<0) ? 1 : -1;
		return left.getKey().compareTo(right.getKey());
	}
}

