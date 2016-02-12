/**
 * MovieExplore, A Project for SENG301
 * Copyright 2010, Gregg and Kyle
 *
 * Refer to LICENSE for legalese.
 */

import org.junit.Test;


public class StatsTests {

	@Test
	public void original_data() {
		String[] correct_output = {
			"users:          943", 
			"  males:        670",
			"  females:      273",
			"movies:        1682",
			"  unknown         2",
			"  Action        251",
			"  Adventure     135",
			"  Animation      42",
			"  Children's    122",
			"  Comedy        505",
			"  Crime         109",
			"  Documentary    50",
			"  Drama         725",
			"  Fantasy        22",
			"  Film-Noir      24",
			"  Horror         92",
			"  Musical        56",
			"  Mystery        61",
			"  Romance       247",
			"  Sci-Fi        101",
			"  Thriller      251",
			"  War            71",
			"  Western        27",
			"ratings:     100000",
			"  1            6110",
			"  2           11370",
			"  3           27145",
			"  4           34174",
			"  5           21201",
		};
		TestHelper.run_test("java -cp build MovieExplore data/large stats", 
		                    correct_output,
												0);
	}

	@Test
	public void manufactured_data() {
		String[] correct_output = {
			"users:            8", 
			"  males:          3",
			"  females:        5",
			"movies:           6",
			"  unknown         0",
			"  Action          2",
			"  Adventure       1",
			"  Animation       1",
			"  Children's      1",
			"  Comedy          2",
			"  Crime           1",
			"  Documentary     0",
			"  Drama           3",
			"  Fantasy         0",
			"  Film-Noir       0",
			"  Horror          0",
			"  Musical         0",
			"  Mystery         0",
			"  Romance         0",
			"  Sci-Fi          0",
			"  Thriller        3",
			"  War             0",
			"  Western         0",
			"ratings:         20",
			"  1               1",
			"  2               1",
			"  3               1",
			"  4               0",
			"  5              17",
		};
		TestHelper.run_test("java -cp build MovieExplore data/basic stats", 
		                    correct_output,
												0);
	}

} 

