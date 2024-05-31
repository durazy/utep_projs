
public class User implements AsCSV {
	
	/** The ID of the user in the user_data file */
	private int userID;
	
	/** Class holding the user's personal info */
	private Person person;
	
	/** The amount of funds available for User */
	private double balance;
	
	/** The number of cars purchased by the user */
	private int carsPurchased;
	
	/** The user's current MinerCars Membership status */
	private boolean isMember;
	
	/** The user's username */
	private String username;
	
	/** The user's password */
	private String password;
	
	/** There may be 10 tickets per session */
	public Ticket[] tickets = new Ticket[10];
	
	// private Ticket[] tickets;
	
	
	//constructor
	public User(int id, String first, String last, double bal, int purchased, 
			boolean membership, String user, String pass) {
		userID = id;
		person = new Person(first, last);
		balance = bal;
		carsPurchased = purchased;
		isMember = membership;
		username = user;
		password = pass;
	}
	
	//getters and setters
	
	/**
	 * The user ID is based on the order it appears in the user_data.csv
	 * @return the id for user
	 */
	public int getUserID() {
		return userID;
	}
	
	/**
	 * Gets the full name of the user
	 * @return user's full name
	 */
	public String getName() {
		return person.getFirst() + " " + person.getLast();
	}
	
	/**
	 * @return the user's balance
	 */
	public double getBalance() {
		return balance;
	}
	
	/**
	 * Sets the user's balance to the given amount
	 * @param amount The value in dollars to set the amount to
	 */
	public void setBalance(double amount) {
		balance = amount;
	}
	
	/**
	 * @return the number of cars the user has purchased
	 */
	public int getPurchased() {
		return carsPurchased;
	}
	
	/**
	 * Sets the user's amount of purchased cars to the given amount
	 * @param amount The amount in dollars
	 */
	public void setPurchased(int amount) {
		carsPurchased = amount;
	}
	
	/**
	 * @return whether or not the user is a MinerCars member
	 */
	public boolean getMember() {
		return isMember;
	}
	
	/**
	 * Sets the user's membership to the given boolean
	 * @param isMember True if user is a member 
	 */
	public void setMember(boolean isMember) {
		this.isMember = isMember;
	}
	
	/**
	 * @return the username for user
	 */
	public String getUser() {
		return this.username;
	}
	
	/**
	 * @return the username for user
	 */
	public String getPass() {
		return this.password;
	}
	
	/**
	 * Sets the username for user
	 * @param user The string to set the username to
	 */
	public void setUser(String user) {
		username = user;
	}
	
	/**
	 * Sets the password for user
	 * @param pass The string to set the password to
	 */
	public void setPass(String pass) {
		password = pass;
	}
	
	//methods
	
	/**
	 * Get the price of the car
	 * @param chosen The car chosen for purchase
	 */
	public void purchase(Car chosen) {
		double price = getTotal(chosen);
		increasePurchase();
		setBalance(balance - price);
		chosen.removeCar();
	}
	
	/**
	 * Get the total price for a given car
	 * Includes membership and taxes.
	 * Truncates the decimals after the first two
	 * @param chosen The car to calculate the total price from
	 * @return The total price of a car after all discounts and taxes
	 */
	public double getTotal(Car chosen) {
		if (isMember) {
			return chosen.getPrice() * .90 * 1.0625;
		}
		double untruncated = chosen.getPrice() * 1.0625;
		return ((int)(untruncated*100))/100.0;
	}
	/**
	 * Increments the users purchased cars by 1. Method called after purchasing 
	 */
	public void increasePurchase() {
		carsPurchased++;
	}
	
	/**
	 * Find the first empty slot for tickets and insert the given car ticket
	 * @param t The ticket to add to the user's account
	 */
	public void addTicket(Ticket t) {
		for (int i = 0; i < tickets.length; i++) {
			if (tickets[i] == null) {
				tickets[i] = t;
				break;
			}
		}
	}
	
	/**
	 * Prints a list of the user's current tickets in the system
	 */
	public void showTickets() {
		
		for (int i = 0; i < tickets.length; i++) {
			if (tickets[i] != null) {
				StringBuilder result = new StringBuilder();
				result.append((i + 1));
				result.append(": ");
				result.append(tickets[i]);
				System.out.println(result.toString());
			}
		}
		
	}
	
	/**
	 * Returns the csv string of Car
	 */
	public String toCSV() {
		StringBuilder builder = new StringBuilder();
		builder.append(userID);
		builder.append(",");
		builder.append(person.getFirst());
		builder.append(",");
		builder.append(person.getLast());
		builder.append(",");
		builder.append(balance);
		builder.append(",");
		builder.append(carsPurchased);
		builder.append(",");
		builder.append(isMember ? "TRUE" : "FALSE");
		builder.append(",");
		builder.append(username);
		builder.append(",");
		builder.append(password);

		return builder.toString();
	}
	
	/**
	 * Refunds the given ticket index.
	 * Restores the balance spent on the ticket and the
	 * tickets in the class is updated with the
	 * refunded ticket removed.
	 * @param id ID of the car's ticket that is being refunded
	 */
	public void refundTicket(int id) {
		Ticket toRefund = tickets[id];
		balance += toRefund.getPrice();
		carsPurchased--;
		removeTicket(id);
		
	}
	
	/**
	 * Updates the user's tickets by removing a ticket
	 * with the given index
	 * Moves all tickets to the left.
	 * @param id index of the car's ticket that is being removed
	 */
	public void removeTicket(int id) {
		for (int i = id; i < tickets.length - 1; i++) {
			Ticket current = tickets[i];
			if (current != null) {
				tickets[i] = tickets[i+1];
			}
		}
	}
}
