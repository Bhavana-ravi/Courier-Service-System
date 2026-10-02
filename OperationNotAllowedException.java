package jsp.springboot.courier.management.exception;

@SuppressWarnings("serial")
public class OperationNotAllowedException extends RuntimeException{
	
	
	public OperationNotAllowedException(String message) {
		
		super(message);
	}

}
