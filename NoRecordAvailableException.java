package jsp.springboot.courier.management.exception;

@SuppressWarnings("serial")
public class NoRecordAvailableException extends RuntimeException{
	
	public NoRecordAvailableException(String message) {
		
		super(message);
	}

}
