import java.util.HashMap;
import java.util.Scanner;

public class Admin {

	/** The tickets from all transactions in the current run of the system */
	public Ticket[] allTransactions = new Ticket[500];

	//constructors

	public Admin () {

	}

	//methods

	/**
	 * Find the first empty slot for tickets and insert the given car ticket
	 * @param t The ticket to add
	 */
	public void addTicket(Ticket t) {
		for (int i = 0; i < allTransactions.length; i++) {
			if (allTransactions[i] == null) {
				allTransactions[i] = t;
				break;
			}
		}
	}
	
	/**
	 * Get a new car based on the fields entered by an admin
	 * Admin must enter valid values for each field otherwise
	 * an exception is thrown.
	 * @param input How input is read from admin
	 * @return A new car object
	 * @throws Exception in case invalid fields are entered during creation
	 */
	public Car getNewCar(Scanner input) throws InvalidFieldException {
		HashMap<String, String> carData = new HashMap<String, String>();

		//get inputs
		System.out.println("\nEnter new car info");
		System.out.print("ID: ");
		carData.put("ID", input.nextLine());
		System.out.print("Car type: ");
		carData.put("Car Type", input.nextLine());
		System.out.print("Car model: ");
		carData.put("Model", input.nextLine());
		System.out.print("Condition: ");
		carData.put("Condition", input.nextLine());
		System.out.print("Color: ");
		carData.put("Color", input.nextLine());
		System.out.print("Capacity: ");
		carData.put("Capacity", input.nextLine());
		System.out.print("Fuel type: ");
		carData.put("Fuel Type", input.nextLine());
		System.out.print("Transmission: ");
		carData.put("Transmission", input.nextLine());
		System.out.print("VIN: ");
		carData.put("VIN", input.nextLine());
		System.out.print("Price: ");
		carData.put("Price", input.nextLine());
		System.out.print("Cars Available: ");
		carData.put("Cars Available", input.nextLine());
		System.out.print("Does it have Turbo? (Yes/No): ");
		carData.put("hasTurbo", input.nextLine());
		
		try {
			Car newCar = new CarFactory().getCar(carData);
			if (newCar != null) {
				return newCar;
			}
			throw new InvalidFieldException();
		} catch (NumberFormatException e) {
			throw new InvalidFieldException();
		}
	}


	/**
	 * Allows the admin to view the revenue for a particular type of car or
	 * a particular ID. The passed choice determines which revenue type
	 * the admin will view.
	 * @param choice The choice of either revenue by ID or by car type
	 */
	public void viewRevenue(String choice) {
		if (choice.equals("exit")) {
			return;
		}

		double total = 0;
		System.out.print("Revenue for ");
		if(isNumeric(choice)) { //get revenue by id-
			int selection = Integer.parseInt(choice);
			System.out.print("ID #" + choice);
			for (int i = 0; i < allTransactions.length; i++) {
				Ticket current = allTransactions[i];
				if (current == null) {
					break;
				}
				if (current.getID() == selection) {
					total += current.getPrice();
				}
			}
		} else { // get revenue of the car type
			System.out.print(choice);
			for (int i = 0; i < allTransactions.length; i++) {
				Ticket current = allTransactions[i];
				if (current == null) {
					break;
				}
				if (current.getCarType().toLowerCase().equals(choice)) {
					total += current.getPrice();
				}	
			}
		}
		System.out.print(": $");
		System.out.println(String.format("%.2f", total));
		if (total == 0) {
			System.out.println("There have been no sales yet for the selected vehicle\n");
		}
	}

	/**
	 * Gets the type of revenue that the admin is requesting
	 * @param input 
	 * @return A string for the type of revenue to be checked
	 */
	public String getRevenueChoice(Scanner input, int totalCars) {

		String choice = "";
		boolean valid = false;
		while (!valid) {
			System.out.println("\nHow would you like to view revenue?");
			System.out.print("Enter a car type or an id of a car for revenue by id: ");
			choice = input.nextLine().toLowerCase(); 

			if (choice.equals("sedan") | choice.equals("suv")| choice.equals("hatchback") 
					| choice.equals("pickup") | choice.equals("exit")) {
				return choice;
			}
			
			if (isNumeric(choice)) {
				int value = Integer.parseInt(choice); {
					if (value < totalCars & value > 0) {
						valid = true;
					} else {
						System.out.println("ID was not found in the system.");
					}
				}
			}
		}
		return choice;

	}


	/**
	 * Prompt Admin for the fields required to create a User account
	 * @param input How input is read from admin
	 * @return A HashMap with all of the User's information
	 */
	public HashMap<String, String> newUserData(Scanner input) throws InvalidFieldException {
		HashMap<String, String> data = new HashMap<String, String>();

		//get inputs 

		System.out.println("\nEnter new user info");
		System.out.print("ID: ");
		data.put("ID", input.nextLine());
		System.out.print("First Name: ");
		data.put("First Name", input.nextLine());
		System.out.print("Last Name: ");
		data.put("Last Name", input.nextLine());
		System.out.print("User funds: ");
		data.put("Money Available", input.nextLine());
		data.put("Cars Purchased", "0");
		System.out.print("Do they have a membership? (TRUE/FALSE): ");
		data.put("MinerCars Membership", input.nextLine());
		System.out.print("Username: ");
		data.put("Username", input.nextLine());
		System.out.print("Password: ");
		data.put("Password", input.nextLine());


		return data;
	}

	/**
	 * Checks if the given string is a number
	 * @param str String to check
	 * @return true if the given string is a number
	 */
	public boolean isNumeric(String s) { 
		try {  
			Double.parseDouble(s);  
			return true;
		} catch(NumberFormatException e){  
			return false;  
		}  
	}
	
	/**
	 * Refunds the ticket from the given user and given car ID
	 * Moves all the remaining tickets to the left so that
	 * there is not a null between the valid tickets.
	 * @param user User to refund the ticket from
	 * @param id ID of the car bought
	 */
	public void refundTicket(String user, int id) {
		for (int i = 0; i < allTransactions.length; i++) {
			Ticket current = allTransactions[i];
			if (current == null) {
				break;
			} else if (current.getUser().equals(user) & current.getID() == id) {
				for (int j = i; j < allTransactions.length - 1; j++) {
					if (allTransactions[j] != null) {
						allTransactions[j] = allTransactions[j + 1];
					} else {
						break;
					}
				}
				break;
			}
		}
	}

}
