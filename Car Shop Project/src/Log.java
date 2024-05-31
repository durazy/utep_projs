
public class Log {
	
	private String action;
	private String time;
	
	
	//constructor
	public Log(String action) {
		this.action = action;
		time = getCurrentTime();
	}
	
	
	//methods
	
	/**
	 * Calculates the current time and date and returns a string
	 * containing the information
	 * @return The date and time as hh:mm:ss month/day/year
	 */
	public String getCurrentTime() {
		return java.time.LocalDateTime.now().toString();
	}
	
	/**
	 * Returns the log as string to write to Log.txt
	 * @return The log in the format: hh:mm:ss month/day/year - Action
	 */
	public String toString() {
		return time + " - " + action;
	}
}
