package exceptions;

public class UserNameIsNullException extends Exception {
	public UserNameIsNullException() {
		super("Your name cannot be empty or null");
	}

	public UserNameIsNullException(String name) {
		super("Your name cannot be empty or null" + name);
	}

}
