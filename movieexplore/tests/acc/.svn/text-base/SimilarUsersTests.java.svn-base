/**
 *
 * MovieExplore, A Project for SENG301
 * Copyright 2010, Gregg and Kyle
 *
 * Refer to LICENSE for legalese.
 *
 */

import org.junit.Test;

public class SimilarUsersTests {

	@Test
	public void find_similar_users() {
		String[] correct_output = {
			"ID  Age Gen Occupation Zip",
			"----------------------------",
			"2   23  M   administrator 13453",
			"3   18  F   retailer   43332",
			"4   33  F   doctor     90210",
			"5   48  F   educator   11111",
			"6   33  F   artist     19234"

		};
		TestHelper.run_test("java -cp build MovieExplore data/simusers similar-users 1",
		                    correct_output,
												0);
	}

	@Test
	public void find_similar_users_large() {
		String[] correct_output = {
			"ID  Age Gen Occupation Zip",
			"----------------------------",
			"276 21  M   student    95064",
			"303 19  M   student    14853",
			"429 27  M   student    29205",
			"92  32  M   entertainment 80525",
			"435 24  M   engineer   60007",
		};
		TestHelper.run_test("java -cp build MovieExplore data/large similar-users 1",
		                    correct_output,
												0);
	}

} 

