import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestDataEntry {
	
	private static HashMap<String, String> dataBus;
	
	
	@BeforeAll
	static void setup() {
		dataBus = new HashMap<String, String>();
		dataBus.put("ID", "1");
		dataBus.put("First Name", "Paul");
		dataBus.put("Last Name", "Paulie");
		dataBus.put("Username", "paul101");
		dataBus.put("Password", "notpaul101");
		dataBus.put("MinerCars Membership", "True");
		dataBus.put("Cars Purchased", "0");
		dataBus.put("Money Available", "900000");
	}
	
	
	@Test
	void InvalidUserIDTest() {
		dataBus.put("ID", "a");
		assertThrows(InvalidFieldException.class, () -> {
			RunShop.createUser(dataBus);
			});
		dataBus.put("ID", "1");
	}
	
	@Test
	void InvalidUserPurchasedTest() {
		dataBus.put("Cars Purchased", "a");
		assertThrows(InvalidFieldException.class, () -> {
			RunShop.createUser(dataBus);
			});
		dataBus.put("Cars Purchased", "0");
	}

}
