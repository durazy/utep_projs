
public class Ticket {
	
	/** The ID of the car at the time of purchase */
	private int id;
	
	/** The type of car (Sedan, SUV, Hatchback, Pickup) */
	private String carType;
	
	/** The name of the car */
	private String carModel;
	
	/** The cost of the car */
	private double price;
	
	/** The username of the buyer */
	private String user;
	
	//constructor
	
	public Ticket (int id, String car, String model, double price, String user) {
		this.id = id;
		carType = car;
		carModel = model;
		this.price = price;
		this.user = user;
	}
	
	//methods
	
	/**
	 * Gets the ID of the car at the time of purchase
	 * @return the ID of the car
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Returns the original type of the car bought
	 * @return the type of the car paid for on the ticket
	 */
	public String getCarType() {
		return carType;
	}
	
	/**
	 * Returns the original price of the car bought
	 * @return the price of the car paid for on the ticket
	 */
	public double getPrice() {
		return price;
	}
	
	/**
	 * Returns the original user of the car bought
	 * @return the username that paid for this ticket
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * Returns the String version of the Ticket class
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Car: ");
		result.append(carType);
		result.append(" Model: ");
		result.append(carModel);
		result.append(" Price: ");
		result.append(price);
		return result.toString();
	}
}
