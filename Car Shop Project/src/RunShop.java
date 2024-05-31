import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.HashMap;

public class RunShop {
	
	/** file to retrieve and store user data */
	final static String USER_DATA = "src/user_data.csv";
	
	/** file to retrieve and store car data */
	final static String CAR_DATA = "src/car_data.csv";
	
	/** Stores the cars and their info */
	public static Car[] cars;
	
	/** The total number of car types in the system **/
	public static int totalCars;
	
	/** Stores the users and their info */
	public static User[] users;
	
	/** The total number of users in the system **/
	public static int totalUsers;
	
	/** The administrator for the system */
	public static Admin admin = new Admin();
	
	/** The current iteration's logs (Max is 999)*/
	public static Log[] logs;
	
	
	public static void main(String[] args) {
		//CarFactory ner = new CarFactory();
		Scanner input = new Scanner(System.in);
		logs = new Log[999];
		loadCars();
		loadUsers();
		runMenu(input);
		exit();
	}
	
	//methods to run
	
	
	/**
	 * Prompt the client for a username and password
	 * attempt to login
	 * @param input How input is read from user
	 * @return The user data of the logged user
	 */
	public static User login(Scanner input) {
		boolean exit = false;
		while (!exit) {
			System.out.println();
			System.out.println("Login");
			System.out.println("Enter a '1' to return to the main menu");
			System.out.print("Or press anything else to continue: " );
			String choice = input.nextLine();
			if (choice.equals("1")) {
				exit = true;
			} else {
				System.out.print("Username: ");
				String user = input.nextLine();
				System.out.print("Password: ");
				String pass = input.nextLine();
				
				for (int i = 0; i < totalCars; i++) {
					User current = users[i];
					if (current != null) {
						String username = current.getUser();
						if (username.equals(user)) {
							if (current.getPass().equals(pass)) {
								exit = true;
								
								Log done = createLog(username + " logged in");
								updateLogs(done);
								return current;
							}
						}
					} else {
						break;
					}
				}
				System.out.println("Incorrect login");
			}
			
		}
		return null;
	}
	
	/**
	 * updates the files that have been changed upon exit
	 */
	public static void exit() {
		updateFiles();
	}
	
	/**
	 * Presents the user with 2 options:
	 * 	1. Login
	 * 	2. Exit
	 * @param input How input is read from user
	 */
	public static void runMenu(Scanner input) {
		boolean exit = false;
		while (!exit) {
			System.out.println();
			System.out.println("Mine Cars");
			System.out.println("   1. User login");
			System.out.println("   2. Login as Admin");
			System.out.println("   3. Exit");
			System.out.print("Choose an option: ");
			String choice = input.nextLine();
			if (choice.equals("1")) {
				User loggedUser = login(input);
				if (loggedUser != null) {
					runUserMenu(loggedUser, input);
				}
			} else if (choice.equals("2")) {
				Log done = createLog("Admin logged in");
				updateLogs(done);
				runAdminMenu(input);
			} else if (choice.equals("3")) {
				exit = true;
			}
		}
		
	}
	
	/**
	 * Presents the user with 6 options after logging in:
	 * 	1. Display all cars
	 * 	2. Filter Cars (used / new)
	 * 	3. Buy a car
	 * 	4. View tickets
	 *  5. Return a car
	 * 	6. Sign out
	 * 
	 * @param client The user file to use through the following interactions
	 * @param input How input is read from user
	 */
	public static void runUserMenu(User client, Scanner input) {
		boolean signOut = false;
		while (!signOut) {
			
			printUserMenu(client);
			
			String choice = input.nextLine();
			if (choice.equals("1")) {
				Log done = createLog(client.getUser() + " displayed cars");
				updateLogs(done);
				displayCars();
			} else if (choice.equals("2")) {
				System.out.println("Type '1' for Used and any other key for New");
				String choice2 = input.nextLine();
				if (choice2.equals("1")) {
					Log done = createLog(client.getUser() + " displayed used cars");
					updateLogs(done);
					displayByUsed(true);
				} else {
					Log done = createLog(client.getUser() + " displayed new cars");
					updateLogs(done);
					displayByUsed(false);
				}
			} else if (choice.equals("3")) {
				initiatePurchase(client, input);
			} else if (choice.equals("4")) {
				Log done = createLog(client.getUser() + " viewed tickets");
				updateLogs(done);
				System.out.println("\nTickets for " + client.getName() + ":");
				client.showTickets();
			} else if (choice.equals("5")) {
				refundCar(client, input);
			} else if (choice.equals("6")) {
				Log done = createLog(client.getUser() + " logged out");
				updateLogs(done);
				signOut = true;
			}
		}
	}
	
	/**
	 * Prints the User menu
	 * @param client The user in the current setting
	 */
	public static void printUserMenu(User client) {
		System.out.println();
		System.out.println("Welcome, " + client.getName());
		System.out.println("   1. Display all cars");
		System.out.println("   2. Filter Cars (used / new)");
		System.out.println("   3. Buy a car");
		System.out.println("   4. View tickets");
		System.out.println("   5. Return a car");
		System.out.println("   6. Sign out");
		System.out.print("Choose an option: ");
	}
	
	/**
	 * Presents the Admin with 5 options after logging in:
	 * 	1. Add car to system
	 * 	2. Remove car from system
	 * 	3. Add User
	 * 	4. View car revenue
	 * 	5. Sign out
	 * 
	 * @param input How input is read from user
	 */
	public static void runAdminMenu(Scanner input) {
		boolean signOut = false;
		
		while (!signOut) {
			
			printAdminMenu();
			String choice = input.nextLine();
			
			if (choice.equals("1")) {
				addCarData(input);
			} else if (choice.equals("2")) {
				displayCars();
				int removeID = getRemovedCar(input);
				removeCarData(removeID);
				Log done = createLog("Admin removed car #" + removeID);
				updateLogs(done);
			} else if (choice.equals("3")) {
				addUserData(input);
			} else if (choice.equals("4")) {
				String revenueType = admin.getRevenueChoice(input, totalCars);
				admin.viewRevenue(revenueType);
				Log done = createLog("Admin viewed revenue");
				updateLogs(done);
			} else if (choice.equals("5")) {
				Log done = createLog("Admin logged out");
				updateLogs(done);
				signOut = true;
			}
		}
	}
	
	/**
	 * Displays the Admin menu
	 */
	public static void printAdminMenu() {
		System.out.println();
		System.out.println("*****ADMIN MENU*****");
		System.out.println("   1. Add car to system");
		System.out.println("   2. Remove car from system");
		System.out.println("   3. Add User");
		System.out.println("   4. View car revenue");
		System.out.println("   5. Sign out");
		System.out.print("Choose an option: ");
	}

	
	/*
	 * 
	 * Data Reading methods
	 * 
	 */
	
	
	/**
	 * Get the order of features in a data file by tracking the header and index it appears
	 * @param header The string array created from the header of the CSV file
	 * @return A HashMap with the indices of each car features to help sort data
	 */
	public static HashMap<String, Integer> getIndices(String[] header) {
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		
		for (int i = 0; i < header.length; i++) {
			result.put(header[i], i);
		}
		return result;
	}
	
	/**
	 * Get the values from a String array of values and use the found
	 * indices to assign the values properly to a HashMap. Allows the 
	 * values the values to be accessed by name
	 * @param indices A hasmap relating the indices of the header to 
	 * the names of values 
	 * @param values The values mapped to the names based on the header
	 * @return A HashMap with all of the values of either a car or user
	 */
	public static HashMap<String, String> getValues(HashMap<String, Integer> indices, String[] values) {
		HashMap<String, String> result = new HashMap<String, String>();
		
		for (String i : indices.keySet()) {
			if (indices.get(i) == 12 && values.length == 12) {
				result.put(i, "No");
			} else {
				result.put(i, values[indices.get(i)]);
			}
		}
		return result;
	}
	
	/**
	 * @return the data from the car_data.csv in an array of Car
	 */
	public static void loadCars() {
		int carsInFile = getNumValues(CAR_DATA) - 1;
		totalCars = carsInFile;
		int size = 	 carsInFile* 2; //extra size for edits
		Car[] data = new Car[size];
		int index = 0;
		
		try (Scanner reader = new Scanner(new File(CAR_DATA))) {
			
			String header = reader.nextLine();
			String[] headerVals = header.split(",");
			HashMap<String, Integer> indices = getIndices(headerVals);
			
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				String[] values = line.split(",");
				HashMap<String, String> dataBus = getValues(indices, values);
				CarFactory producer = new CarFactory();
				data[index] = producer.getCar(dataBus);
				index++;
			}
			reader.close();
		} catch (Exception e) {
			System.out.println("Failed to retrieve car data");
			System.out.println(e);
		}
		cars = data;
	}
	
	
	/**
	 * @return the data from the user_data.csv in an array of User
	 */
	public static void loadUsers() {
		int usersInFile = 	getNumValues(USER_DATA) - 1;
		totalUsers = usersInFile;
		int size = usersInFile * 2; //extra size for edits
		User[] data = new User[size];
		int index = 0;
		try (Scanner reader = new Scanner(new File(USER_DATA))) {
			String header = reader.nextLine();
			String[] headerVals = header.split(",");
			HashMap<String, Integer> indices = getIndices(headerVals);
					
			while (reader.hasNextLine()) {
				String[] values = reader.nextLine().split(",");
				HashMap<String, String> dataBus = getValues(indices, values);
				data[index] = createUser(dataBus);
				index++;
			}
			reader.close();
		} catch (Exception e) {
			System.out.print("Failed to retrieve user data");
		}
		users = data;
	}
	
	/**
	 * Uses the data given to create a User object.
	 * @param dataBus The hashmap containing all of the user data
	 * @return A user with the given data instantiated into it
	 */
	public static User createUser(HashMap<String, String> dataBus) throws InvalidFieldException {
		
		//parse values
		try {
			int id = Integer.parseInt(dataBus.get("ID"));
			String first = dataBus.get("First Name");                         
			String last = dataBus.get("Last Name");
			double bal = Double.parseDouble(dataBus.get("Money Available"));
			int purchased = Integer.parseInt(dataBus.get("Cars Purchased"));
			boolean member = Boolean.parseBoolean(dataBus.get("MinerCars Membership"));
			String user = dataBus.get("Username");
			String pass = dataBus.get("Password");
			return new User(id, first, last, bal, purchased, member, user, pass);
		} catch (NumberFormatException e) {
			throw new InvalidFieldException();
		}
		
		
	}
	
	
	/*
	 * 
	 * Car related methods
	 * 
	 */
	
	
	/**
	 * Displays all of the cars in order of ID
	 */
	public static void displayCars() {
		System.out.println();
		for (int i = 0; i < cars.length; i++) {
			Car current = cars[i];
			if (current != null) {
				System.out.println(current.getID() + ": " + current.toString());
			} else {
				break;
			}
		}
	}
	
	/**
	 * Displays all the cars in order of ID, but only displays
	 * the ones that are used if isUsed is true,
	 * and the ones that are New if isUsed is false
	 * @param isUsed Whether or not the car is used
	 */
	public static void displayByUsed(boolean isUsed) {
		if (isUsed) {
			for (int i = 0; i < cars.length; i++) {
				Car current = cars[i];
				if (current != null) {
					if (current.getUsed()) {
						System.out.println(current.getID() + ": " + current.toString());
					}
				} else {
					break;
				}
			}
		} else {
			for (int i = 0; i < cars.length; i++) {
				Car current = cars[i];
				if (current != null) {
					if (!current.getUsed()) {
						System.out.println(current.getID() + ": " + current.toString());
					}
				} else {
					break;
				}
			}
		}
	}
	
	/**
	 * Prompts the admin to enter a valid ID to remove a car.
	 * Ensures a valid choice to remove.
	 * @param input How input is read from user
	 * @param totalCars the total numbers
	 * @return the entered choice
	 */
	public static int getRemovedCar(Scanner input) {

		int result = -1;
		boolean valid = false;
		while (!valid) {
			System.out.print("\nCurrently there are " + totalCars + 
					" cars. \nChoose an ID of a car to remove: ");

			try {
				result = Integer.parseInt(input.nextLine());
				if (result < 1 || getCarIndex(result) == -1) {
					result = -1;
					throw new Exception();
				}
				valid = true;
			} catch (NumberFormatException e) {
				System.out.println("Enter a valid number.");
			} catch (Exception e) {
				System.out.println("Enter a valid ID.");
			}
		}
		return result;
	}
	
	
	/**
	 * Remove a car from the car list given the ID
	 * The car's will all shift down in ID in the array and it will update 
	 * the csv accordingly when the program exits.
	 * Assumes the passed id is valid
	 * @param id The id of the car to remove
	 */
	public static void removeCarData(int id) {
		//find car index
		int index = -1;
		for (int i = 0; i < totalCars; i++) {
			if (cars[i].getID() == id) {
				index = i;
				break;
			}
		}
		for (int i = index; i < totalCars; i++) {
			cars[i] = cars[i + 1];
		}
		totalCars--;
		System.out.println("Removed car #" + id);
	}
	
	/**
	 * Add a new car to the current cat data
	 * @param input How input is read from user
	 */
	public static void addCarData(Scanner input) {
		checkTotalCars();
		Car newCar = null;
		boolean valid = false;
		while (!valid) {
			try {
				newCar = admin.getNewCar(input);
				valid = true;
			} catch (InvalidFieldException e) {
				valid = false;
				System.out.println("One or more entries were invalid, please try again.");
			}
		}
		
		//find first empty spot (null) to place car
		cars[totalCars] = newCar;
		totalCars++;
		Log done = new Log("Added the " + newCar.getModel() + " to the system");
		updateLogs(done);
	}

	
	/*
	 * 
	 * Other
	 * 
	 */
	
	
	/**
	 * Add a new user to the current user data
	 * @param input How input is read from user
	 */
	public static void addUserData(Scanner input) {
		checkTotalUsers();
		boolean valid  = false;
		while (!valid) {
			try {
				User newUser = createUser(admin.newUserData(input));
				users[totalUsers] = newUser;
				totalUsers++;
				System.out.println("\nWelcome to the dealership, " + newUser.getName() + "!");
				Log done = new Log("Added " + newUser.getUser() + " to the system");
				updateLogs(done);
				valid = true;
			} catch (InvalidFieldException e) {
				System.out.println("One or more entries were invalid, please try again.");
			}
		}
	}

	/**
	 * Prompts the user to choose a car to purchase and purchases the car if funds are sufficient.
	 * @param client The user account to initiate the purchase from
	 * @param input How input is read from user
	 */
	public static void initiatePurchase(User client, Scanner input) {
		
		int choice = getPurchaseChoice(input);
		Car chosenCar = cars[choice - 1];
		double balance = client.getBalance();
		double total = client.getTotal(chosenCar);
		
		if (balance > total) {
			if (chosenCar.hasInventory()) {
				//write to logs
				Log done = createLog(client.getUser() + " purchased " + chosenCar.getModel());
				updateLogs(done);
				
				client.purchase(chosenCar);
				Ticket tk = new Ticket(chosenCar.getID(), chosenCar.getClass().getName(), 
						chosenCar.getModel(), total, client.getUser());
				client.addTicket(tk);
				admin.addTicket(tk);
				viewCheckout(client, tk, total, input);
			} else {
				System.out.println("Out of stock!");
			}
		} else {
			System.out.println("Insufficient funds!");
		}
	}
	
	/**
	 * Asks if the user would like the checkout details to be displayed
	 * Shows the discount applied, the taxes, the cost before taxes, and
	 * the cost after everything has been applied
	 * Also shows the Car purchased
	 * @param client The user of the current purchase
	 * @param tk The ticket from the current purchase
	 * @param input How input is read from user
	 */
	public static void viewCheckout(User client, Ticket tk, double total, Scanner input) {
		System.out.println("Type '1' to view Checkout. Otherwise, press anything else to continue");
		String answer = input.nextLine();
		
		if (answer.equals("1")) {
			System.out.println("Customer Name: " + client.getName());
			System.out.println(tk);
			double price = tk.getPrice();
			if (client.getMember()) {
				price = price * .9;
				System.out.println("Applied Member Discount: -" + String.format("%.2f", tk.getPrice() - price));
			}
			System.out.println("Tax: " +  String.format("%.2f", (price * .0625)));
			System.out.println("Total: " +  String.format("%.2f", total));
		}
	}
	
	/**
	 * Prompts the user to enter a valid choice of car
	 * @param input How input is read from user
	 * @return The user's choice of car or their choice to cancel the purchase
	 */
	public static int getPurchaseChoice(Scanner input) {

		int choice = -1;
		boolean validChoice = false;
		while (!validChoice) {
			try {
				System.out.println("\nWhich car would you like to purchase?");
				System.out.print("Enter the ID of the Car you would like (Cancel = -1): ");
				choice = Integer.parseInt(input.nextLine());
				if (choice >= -1 && choice <= totalCars) {
					validChoice = true;
				} else {
					throw new Exception();
				}
			} catch (Exception e) {
				System.out.println("\nInvalid choice. You may still enter -1 to cancel.");
			}
		}
		
		return choice;
	}
	
	/**
	 * Adds the newLog to the current iteration's logs
	 * @param newLog The Log just created from the last action
	 */
	public static void updateLogs(Log newLog) {
		try {
			FileWriter fr = new FileWriter("src/Log.txt", true);
			fr.write(newLog.toString()); //header
			fr.write("\n");
			fr.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	/**
	 * Updates the car and user data files with the current iteration's info
	 */
	public static void updateFiles() {
		
		//update car file
		try {
			FileWriter fr = new FileWriter("src/car_updated.csv", false);
			//Scanner reader = new Scanner(new File(CAR_DATA));
			//reader.nextLine();
			String header = "ID,Car Type,Model,Condition,Color,Capacity,Fuel Type,Transmission,VIN,Price,Cars Available,hasTurbo";
			fr.write(header);
			fr.write("\n");
			for (int i = 0; i < cars.length; i++) {
				if (cars[i] == null) {
					break;
				}
				fr.write(cars[i].toCSV());
				fr.write("\n");
			}
			
			fr.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		//update user file
		try {
			FileWriter fr = new FileWriter("src/user_updated.csv", false);
			//Scanner reader = new Scanner(new File(USER_DATA));
			//reader.nextLine();
			String header = "ID,First Name,Last Name,Money Available,Cars Purchased,MinerCars Membership,Username,Password";
			fr.write(header);
			fr.write("\n");
			for (int i = 0; i < users.length; i++) {
				if (users[i] == null) {
					break;
				}
				fr.write(users[i].toCSV());
				fr.write("\n");
			}
			
			fr.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Creates a Log based on the given action
	 * @param action A brief description of the action being recorded
	 * @return Log created from the given action
	 */
	public static Log createLog(String action) {
		Log result = new Log(action);
		return result;
	}
	
	
	/**
	 * Gets how many values are in a file so that an array may be created
	 * using the returned size
	 * @param filename The name of the file to check
	 * @return The number of lines/values in a file
	 */
	public static int getNumValues(String filename) {
		int result = 0;
		
		try (Scanner reader = new Scanner(new File(filename))) {
			while (reader.hasNextLine()) {
				reader.nextLine();
				result++;
			}
			reader.close();
		} catch (Exception e) {
			System.out.print(e);
		}
		
		return result;
	}
	
	/**
	 * Check how many cars there are in the respective array
	 * If the array needs expansion, the array will be deep copied and
	 * the size will be doubled
	 */
	public static void checkTotalCars() {
		if (totalCars == cars.length) {
			int newSize = cars.length * 2;
			Car[] newArray = new Car[newSize];
			for (int i = 0; i < totalCars; i++) {
				newArray[i] = cars[i];
			}
			cars = newArray;
		}
	}
	
	/**
	 * Check how many users there are in the respective array
	 * If the array needs expansion, the array will be deep copied and
	 * the size will be doubled
	 */
	public static void checkTotalUsers() {
		if (totalUsers == users.length) {
			int newSize = users.length * 2;
			User[] newArray = new User[newSize];
			for (int i = 0; i < totalUsers; i++) {
				newArray[i] = users[i];
			}
			users = newArray;
		}
	}
	
	/**
	 * Prompts the user for a car to refund, and removes the car from the user's
	 * account, refunds them the purchase, and adds the refunded car back to the system
	 * @param client The User account to initiate a refund from
	 * @param input How input is read from user
	 */
	public static void refundCar(User client, Scanner input) {
		if (client.tickets[0] == null) {
			System.out.println("You have no cars to refund at this time.");
		} else {
			int refundID = getRefundChoice(client, input);
			if (refundID != -1) { //refund process
				Ticket toRefund = client.tickets[refundID];
				client.refundTicket(refundID);
				admin.refundTicket(client.getUser(), toRefund.getID());
				int refundIndex = getCarIndex(toRefund.getID());
				cars[refundIndex].addCar();
				Log done = createLog(client.getUser() + " returned their " 
						+ cars[refundIndex].getModel());
				updateLogs(done);
				System.out.println("You returned your " 
						+ cars[refundIndex].getModel());
			}
		}
	}
	
	/**
	 * Gets the choice of the user in refunding their car.
	 * The user may choose a car to refund if they have a ticket.
	 * They may also exist if they choose to do so.
	 * @param client The user account to view the tickets of
	 * @param input How input is read from user
	 * @return The choice of the user. The ID is the index of the ticket in the tickets array.
	 */
	public static int getRefundChoice(User client, Scanner input) {
		int entry = 0;
		boolean valid = false;
		String response = "";
		while (!valid) {
			System.out.println("\nWhich car would you like to refund? (Type 'exit' to return)");
			client.showTickets();
			System.out.print("\nEnter the ticket number of the car: ");
			
			try {
				response = input.nextLine();
				entry = Integer.parseInt(response);
				if (entry > 0 & entry < client.tickets.length & 
						client.tickets[entry - 1] != null) {
					valid = true;
				}
			} catch (Exception e) {
				if (response.toLowerCase().equals("exit")) {
					return -1;
				}
				System.out.println("Please enter a valid ticket");
				entry = 0;
			}
		}
		return entry - 1;
	}
	
	/**
	 * Returns the index of the car with the given ID
	 * No car should be given the ID of 0
	 * @param id The id to look for in the system
	 * @return the index of the car with the given id
	 */
	public static int getCarIndex(int id) {
		int result = -1;
		for (int i = 0; i < cars.length; i++) {
			if (cars[i] == null) {
				break;
			}
			if (cars[i].getID() == id) {
				return i;
			}
		}
		return result;
	}

	
}
