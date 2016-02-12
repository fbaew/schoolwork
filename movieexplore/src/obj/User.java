/**
 * MovieExplore, A Project for SENG301
 * Copyright 2010, Gregg and Kyle
 *
 * Refer to LICENSE for legalese.
 */


public class User {

  private int id;
	private int age;

	private char gender;

	private String occupation;
	private String zip;

	/**
	 * User constructor. Internal object fields get populated by the passed in
	 * string array.
	 * @param parsed_fields The raw string array from the u.user file.
	 */
	User(String[] parsed_fields) {
		this.id = Util.parse_int(parsed_fields[0]);
		this.age = Util.parse_int(parsed_fields[1]);
		this.gender = parsed_fields[2].charAt(0);
		this.occupation = parsed_fields[3];
		this.zip = parsed_fields[4];
	}
  
	/**
	 * @return The age of the user.
	 */
  public int age() {
    return age;
  }

	/**
	 * @return The user id.
	 */
  public int id() {
		return id;
	}

	/**
	 * @return The gender of the user.
	 */
  public char gender() {
    return gender;
  }
  
	/**
	 * @return The occupation of the user.
	 */
  public String occupation() {
    return occupation;
  }
  
	/**
	 * @return The zip code of the user.
	 */
  public String zip() {
    return zip;
  }
}
