
public abstract class Car implements AsCSV {
	
	/** The ID of the car relative to car_data.csv */
	private int id;
	
	/** The name of the car's model */
	private String model;
	
	/** Whether or not the car is used (New if false) */
	private boolean isUsed;
	
	/** The color of the car */
	private String color;
	
	/** The amount of seats the car holds */
	private int capacity;
	
	/** The mileage of the car (in miles obviously) */
	private int mileage;
	
	/** The type of fuel the car uses */
	private String fuelType;
	
	/** Whether or not the car is Automatic (Manual if false) */
	private boolean isAuto;
	
	/** The VIN of the car */
	private String vin;
	
	/** The price of the car */
	private double price;
	
	/** The number of this type of car that are available */
	private int available;
	
	/** The presence of a turbo feature in the car */
	private boolean hasTurbo;
	
	
	// constructors
	
	public Car(int id, String model, boolean used, String color, int capacity, String fuel,
			boolean auto, String vin, double price, int available, boolean turbo) {
		this.id = id;
		this.model = model;
		isUsed = used;
		this.color = color;
		this.capacity = capacity;
		//this.mileage = miles;
		fuelType = fuel;
		isAuto = auto;
		this.vin = vin;
		this.price = price;
		this.available = available;
		hasTurbo = turbo;
	}
	
	
	
	//getters and setters
	
	/**
	 * @return the car's id in the file
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Sets the car's id to the given id
	 * @param id The model's id
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	
	/**
	 * @return the name of the car's model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets the car's model to model
	 * @param model The model's name
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return whether or not the car is a used car
	 */
	public boolean getUsed() {
		return isUsed;
	}

	/**
	 * Sets the car's used status to the given value
	 * @param isUsed Whether the car's used or not
	 */
	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	/**
	 * @return the color of the car
	 */
	public String getColor() {
		return color;
	}
	
	/**
	 * Sets the car's color to the given value
	 * @param color The color to use
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return how many people the car can hold
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Sets the car's capacity to the given capacity
	 * @param capacity The capacity to use
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * @return the car's mileage
	 */
	public int getMileage() {
		return mileage;
	}

	/**
	 * Sets the car's mileage to the given mileage
	 * @param mileage The car's mileage to use
	 */
	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	/**
	 * @return the car's fuel type
	 */
	public String getFuel() {
		return fuelType;
	}

	/**
	 * Sets the car's fuel type to the given type
	 * @param fuelType The given fuel type
	 */
	public void setFuel(String fuelType) {
		this.fuelType = fuelType;
	}

	/**
	 * If the car is not automatic, it's manual
	 * @return whether or not the car is Automatic 
	 */
	public boolean getAuto() {
		return isAuto;
	}

	/**
	 * Sets the car's Automatic status to the given value
	 * If the car is not Automatic, it's Manual
	 * @param isAuto Is true if the car is automatic
	 */
	public void setAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}

	/**
	 * @return the car's VIN
	 */
	public String getVin() {
		return vin;
	}

	/**
	 * Sets the car's VIN to the given vin
	 * @param vin The car's vin
	 */
	public void setVin(String vin) {
		this.vin = vin;
	}

	/**
	 * @return the car's price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Sets the car's price to the given price
	 * @param price Base price of the car
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return how many of the car are available
	 */
	public int getAvailable() {
		return available;
	}

	/**
	 * Sets the car's number of available to given available
	 * @param available How many car's are available
	 */
	public void setAvailable(int available) {
		this.available = available;
	}
	
	/**
	 * @return whether or not the car has a turbo feature
	 */
	public boolean getTurbo() {
		return hasTurbo;
	}

	/**
	 * Sets the car's hasTurbo to the given value
	 * @param turbo Whether or not the car has turbo
	 */
	public void setTurbo(boolean turbo) {
		this.hasTurbo = turbo;
	}
	
	//methods
	/**
	 * @return Whether or not the current car has any available to buy
	 */
	public boolean hasInventory() {
		return available > 0;
	}
	
	/**
	 * Reduces the number of available cars by 1
	 */
	public void removeCar() {
		available--;
	}
	
	/**
	 * Increases the number of available cars by 1
	 */
	public void addCar() {
		available++;
	}
	
	/**
	 * Returns the string version of Car without including too much detail
	 */
	public String toString() {
		return this.getClass().getName() +  " Model: " + model + " Color: " + color + " hasTurbo: " + hasTurbo + " Fuel Type: " 
	+ fuelType + " Used: " + isUsed + " Price: " +  String.format("%.2f", price);
	}
	
	/**
	 * Returns the csv string of Car
	 */
	public String toCSV() {
		StringBuilder builder = new StringBuilder();
		builder.append(id);
		builder.append(",");
		builder.append(this.getClass().getName());
		builder.append(",");
		builder.append(model);
		builder.append(",");
		builder.append(isUsed ? "Used" : "New");
		builder.append(",");
		builder.append(color);
		builder.append(",");
		builder.append(capacity);
		builder.append(",");
		builder.append(fuelType);
		builder.append(",");
		builder.append(isAuto ? "Automatic" : "Manual");
		builder.append(",");
		builder.append(vin);
		builder.append(",");
		builder.append( String.format("%.2f", price) );
		builder.append(",");
		builder.append(available);
		builder.append(",");
		builder.append(hasTurbo ? "Yes" : "No");
		return builder.toString();
	}
}
