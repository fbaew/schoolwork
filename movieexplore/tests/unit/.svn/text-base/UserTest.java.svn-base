import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;


public class UserTest {

	private User user;

	@Before
	public void user_setup() {
		user = new User(new String[] {"2", "24", "F", "Engineer", "90210"});
	}

	@Test
	public void testAge() {
		assertTrue(user.age()==24);
		assertFalse(user.age()==23);
	}

	@Test
	public void testId() {
		assertTrue(user.id()==2);
	}

	@Test
	public void testGender() {
		assertTrue(user.gender()=='F');
	}

	@Test
	public void testOccupation() {
		assertTrue(user.occupation().equals("Engineer"));
	}

	@Test
	public void testZip() {
		assertTrue(user.zip().equals("90210"));
	}

}
