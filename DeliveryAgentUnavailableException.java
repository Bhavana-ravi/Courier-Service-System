package jsp.springboot.courier.management.exception;


@SuppressWarnings("serial")
public class DeliveryAgentUnavailableException extends RuntimeException{
	
	public DeliveryAgentUnavailableException(String message) {
		
		super(message);
	}

}
