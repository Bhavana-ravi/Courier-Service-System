package jsp.springboot.courier.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jsp.springboot.courier.management.dto.ResponseStructure;


@ControllerAdvice
public class GlobalExceptionHandler {

	
	@ExceptionHandler(IdNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleINFE(IdNotFoundException exception){
		
		ResponseStructure<String> res=new ResponseStructure<>();
		res.setStatusCode(HttpStatus.NOT_FOUND.value());
		res.setMessage(exception.getMessage());
		res.setData("Failure");
		return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<ResponseStructure<String>> handleIIE(InvalidInputException exception){
		
		ResponseStructure<String> res=new ResponseStructure<>();
		res.setStatusCode(HttpStatus.NOT_FOUND.value());
		res.setMessage(exception.getMessage());
		res.setData("Failure");
		return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(NoRecordAvailableException.class)
	public ResponseEntity<ResponseStructure<String>> handleNRAE(NoRecordAvailableException exception){
		
		ResponseStructure<String> res=new ResponseStructure<>();
		res.setStatusCode(HttpStatus.NOT_FOUND.value());
		res.setMessage(exception.getMessage());
		res.setData("Failure");
		return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(OperationNotAllowedException.class)
	public ResponseEntity<ResponseStructure<String>> handleONAE(OperationNotAllowedException exception){
		ResponseStructure<String> res=new ResponseStructure<>();
		res.setStatusCode(HttpStatus.NOT_FOUND.value());
		res.setMessage(exception.getMessage());
		res.setData("Illegal Operation");
		return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(DeliveryAgentUnavailableException.class)
	public ResponseEntity<ResponseStructure<String>> handleDAUE(DeliveryAgentUnavailableException exception){
		ResponseStructure<String> res=new ResponseStructure<>();
		res.setStatusCode(HttpStatus.NOT_FOUND.value());
		res.setMessage(exception.getMessage());
		res.setData("Delivery agent unavailable");
		return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
	}
	
}
