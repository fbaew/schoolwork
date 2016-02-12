/**
 * MovieExplore, A Project for SENG301
 * Copyright 2010, Gregg and Kyle
 *
 * Refer to LICENSE for legalese.
 */


public class MovieExplore {

	public static void main(String[] raw_args) {

		ArgParser args = new ArgParser(raw_args);

		if (args.option().equals("stats"))
			Stats.main();
		else if (args.option().equals("top"))
			Top.main(args);
		else if (args.option().equals("user"))
			UserInfo.main(args);
		else if (args.option().equals("similar-users"))
			SimilarUsers.main(args);
		else if (args.option().equals("movie-recommendations"))
			Recommendations.main(args);
		else
			Util.exit(1, "Command not recognized.");

		System.exit(0);
	}
}
