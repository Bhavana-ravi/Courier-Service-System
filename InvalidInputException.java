package jsp.springboot.courier.management.exception;

@SuppressWarnings("serial")
public class InvalidInputException extends RuntimeException{
	
	public InvalidInputException(String message) {
		super(message);
	}

}
