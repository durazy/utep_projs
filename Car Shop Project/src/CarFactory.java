import java.util.HashMap;

public class CarFactory {

	/**
	 * Factory method to build a car based on the type of car given
	 * Uses the data to construct the Car
	 * @param dataBus The HasMap containing all the car's data to create it
	 * @return A subclass of Car based on the given type
	 */
	public Car getCar(HashMap<String, String> dataBus) throws NumberFormatException {

		//parse values
		int id = Integer.parseInt(dataBus.get("ID"));
		String carType = dataBus.get("Car Type");
		String model = dataBus.get("Model");
		boolean used = dataBus.get("Condition").equals("New") ? false : true;
		String color = dataBus.get("Color");
		int capacity = Integer.parseInt(dataBus.get("Capacity"));
		String fuel = dataBus.get("Fuel Type");
		boolean auto = dataBus.get("Transmission").equals("Automatic") ? true : false;
		String vin = dataBus.get("VIN");
		double price = Double.parseDouble(dataBus.get("Price"));
		int available = Integer.parseInt(dataBus.get("Cars Available"));
		boolean turbo = dataBus.get("hasTurbo").equals("Yes") ? true : false;
		
		String carTypeNoCase = carType.toLowerCase();
		if (carTypeNoCase.equals("sedan")) {
			return new Sedan(id, model, used, color, capacity, fuel, auto, vin, price, available, turbo);
		} else if (carTypeNoCase.equals("suv")) {
			return new SUV(id, model, used, color, capacity, fuel, auto, vin, price, available, turbo);
		} else if (carTypeNoCase.equals("pickup")) {
			return new Pickup(id, model, used, color, capacity, fuel, auto, vin, price, available, turbo);
		} else if (carTypeNoCase.equals("hatchback")) {
			return new Hatchback(id, model, used, color, capacity, fuel, auto, vin, price, available, turbo);
		}
		
		return null;
	}
	
}
