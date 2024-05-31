import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestLoading {
	
	
	@BeforeAll
	static void setup() {
		RunShop.loadCars();
		RunShop.loadUsers();
	}
	
	@Test
	void TestCarLoading() {
		Car testCar = RunShop.cars[4];
		assertEquals(200, RunShop.cars.length);
		assertEquals(5, testCar.getID());
		assertEquals("Toyota Camry", testCar.getModel());
		assertEquals(null, RunShop.cars[101]);
	}
	
	@Test
	void TestUserLoading() {
		User testUser = RunShop.users[4];
		assertEquals(72, RunShop.users.length);
		assertEquals(5, testUser.getUserID());
		assertEquals("captainamerica", testUser.getUser());
		assertEquals(null, RunShop.users[37]);
	}
	
	@Test
	void TestCarIDRetrival () {
		assertEquals(-1, RunShop.getCarIndex(101));
		assertEquals(99, RunShop.getCarIndex(100));
		assertEquals(0, RunShop.getCarIndex(1));
	}
}
