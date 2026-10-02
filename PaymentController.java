package jsp.springboot.courier.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jsp.springboot.courier.management.dto.PaymentMethodEnum;
import jsp.springboot.courier.management.dto.PaymentStatusEnum;
import jsp.springboot.courier.management.dto.ResponseStructure;
import jsp.springboot.courier.management.entity.Payment;
import jsp.springboot.courier.management.service.PaymentService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	// 1. Get all payments
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Payment>>> getAllPayments() {
		return new ResponseEntity<>(paymentService.getAllPayments(), HttpStatus.OK);
	}

	// 2. Get payment by Id
	@GetMapping("/{paymentId}")
	public ResponseEntity<ResponseStructure<Payment>> getPaymentById(@PathVariable Integer paymentId) {
		return new ResponseEntity<>(paymentService.getPaymentById(paymentId), HttpStatus.OK);
	}

	// 3. Get payment by shipment id
	@GetMapping("/shipment/{shipmentId}")
	public ResponseEntity<ResponseStructure<Payment>> getPaymentByShipmentId(@PathVariable Integer shipmentId) {
		return new ResponseEntity<>(paymentService.getPaymentByShipmentId(shipmentId), HttpStatus.OK);
	}

	// 4. Get payments by payment status
	@GetMapping("/status/{paymentStatus}")
	public ResponseEntity<ResponseStructure<List<Payment>>> getPaymentsByStatus(
			@PathVariable PaymentStatusEnum paymentStatus) {
		return new ResponseEntity<>(paymentService.getPaymentsByStatus(paymentStatus), HttpStatus.OK);
	}

	// 5. Update payment status
	@PutMapping("/status/{paymentId}/{paymentStatus}")
	public ResponseEntity<ResponseStructure<Payment>> updatePaymentStatus(@PathVariable Integer paymentId,
			@PathVariable PaymentStatusEnum paymentStatus) {
		return new ResponseEntity<>(paymentService.updatePaymentStatus(paymentId, paymentStatus), HttpStatus.OK);
	}

	// 6. Update payment method
	@PutMapping("/method/{paymentId}/{paymentMethod}")
	public ResponseEntity<ResponseStructure<Payment>> updatePaymentMethod(@PathVariable Integer paymentId,
			@PathVariable PaymentMethodEnum paymentMethod) {
		return new ResponseEntity<>(paymentService.updatePaymentMethod(paymentId, paymentMethod), HttpStatus.OK);
	}

	// 7. Get payments by pagination and sorting
	@GetMapping("/pagination/{pageNum}/{pageSize}/{fieldName}")
	public ResponseEntity<ResponseStructure<Page<Payment>>> getPaymentsByPaginationAndSorting(
			@PathVariable Integer pageNum, @PathVariable Integer pageSize, @PathVariable String fieldName) {
		return new ResponseEntity<>(
				paymentService.getPaymentsByPaginationAndSorting(pageNum, pageSize, fieldName), HttpStatus.OK);
	}

	// 8. Delete payment
	@DeleteMapping("/delete/{paymentId}")
	public ResponseEntity<ResponseStructure<String>> deletePayment(@PathVariable Integer paymentId) {
		return new ResponseEntity<>(paymentService.deletePayment(paymentId), HttpStatus.OK);
	}
}