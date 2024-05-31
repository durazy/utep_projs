
public class Person {

	/** First name of a person */
	private String firstName;
	
	/** Last name of a person */
	private String lastName;
	
	
	// constructors
	public Person(String first, String last) {
		firstName = first;
		lastName = last;
	}
	
	public Person() {}	
	
	
	// getters and setters
	
	/**
	 * @return person's first name
	 */
	public String getFirst() {
		return firstName;
	}
	
	/**
	 * @return person's last name
	 */
	public String getLast() {
		return lastName;
	}
	
	/**
	 * Sets firstName to the given string
	 * @param name The first name to use
	 */
	public void setFirst(String name) {
		firstName = name;
	}
	
	/**
	 * Sets lastName to the given string
	 * @param name The last name to use
	 */
	public void setLast(String name) {
		lastName = name;
	}
	
	
}
