import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PurchaseTest {

	@Test
	public void NoMemberShipValuesShouldChange() {
		User testUser = new User(5, "Bob", "Charles", 100000, 0, 
				false, "bobc", "password");
		Car testCar = new Sedan(1, "Model X", false, "White", 2, "Hybrid",
				true, "F1290FJ", 20000, 1, true);
		testUser.purchase(testCar);
		
		assertEquals(21250, testUser.getTotal(testCar));
		assertEquals(78750, testUser.getBalance());
		assertEquals(0, testCar.getAvailable());
		assertTrue(testUser.tickets[0] == null);


	}
	
	@Test
	public void MemberShipValuesShouldChange() {
		User testUser = new User(5, "Bob", "Charles", 100000, 0, 
				true, "bobc", "password");
		Car testCar = new Sedan(1, "Model X", false, "White", 2, "Hybrid",
				true, "F1290FJ", 20000, 1, true);
		testUser.purchase(testCar);
		
		assertEquals(19125, testUser.getTotal(testCar));
		assertEquals(80875, testUser.getBalance());
	}
}
