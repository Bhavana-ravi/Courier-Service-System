package jsp.springboot.courier.management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jsp.springboot.courier.management.dto.PaymentMethodEnum;
import jsp.springboot.courier.management.dto.PaymentStatusEnum;
import jsp.springboot.courier.management.dto.ResponseStructure;
import jsp.springboot.courier.management.entity.Payment;
import jsp.springboot.courier.management.entity.Shipment;
import jsp.springboot.courier.management.exception.IdNotFoundException;
import jsp.springboot.courier.management.exception.NoRecordAvailableException;
import jsp.springboot.courier.management.repository.PaymentRepository;
import jsp.springboot.courier.management.repository.ShipmentRepository;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private ShipmentRepository shipmentRepository;

	// Note: Payment records are created only along with a Shipment (cascade from Shipment),
	// so this service does not expose a standalone create operation, only read/update/delete.

	// 1. Get All Payments
	public ResponseStructure<List<Payment>> getAllPayments() {

		List<Payment> payments = paymentRepository.findAll();

		// check if any payment exists
		if (payments.isEmpty()) {
			throw new NoRecordAvailableException("No payments available");
		}

		ResponseStructure<List<Payment>> response = new ResponseStructure<>();
		response.setData(payments);
		response.setMessage("Payments fetched successfully");
		response.setStatusCode(HttpStatus.OK.value());
		return response;
	}

	// 2. Get Payment By Id
	public ResponseStructure<Payment> getPaymentById(Integer paymentId) {

		Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);

		// check if payment exists with given id
		if (optionalPayment.isEmpty()) {
			throw new IdNotFoundException("Payment not found with id: " + paymentId);
		}

		ResponseStructure<Payment> response = new ResponseStructure<>();
		response.setData(optionalPayment.get());
		response.setMessage("Payment fetched successfully");
		response.setStatusCode(HttpStatus.OK.value());
		return response;
	}

	// 3. Get Payment By Shipment Id
	public ResponseStructure<Payment> getPaymentByShipmentId(Integer shipmentId) {

		Optional<Payment> optionalPayment = paymentRepository.findByShipment_ShipmentId(shipmentId);

		// check if payment exists for given shipment id
		if (optionalPayment.isEmpty()) {
			throw new IdNotFoundException("Payment not found for shipment id: " + shipmentId);
		}

		ResponseStructure<Payment> response = new ResponseStructure<>();
		response.setData(optionalPayment.get());
		response.setMessage("Payment fetched successfully");
		response.setStatusCode(HttpStatus.OK.value());
		return response;
	}

	// 4. Get Payments By Payment Status
	public ResponseStructure<List<Payment>> getPaymentsByStatus(PaymentStatusEnum paymentStatus) {

		List<Payment> payments = paymentRepository.findByPaymentStatus(paymentStatus);

		// check if any payment exists with given status
		if (payments.isEmpty()) {
			throw new NoRecordAvailableException("No payments available with status: " + paymentStatus);
		}

		ResponseStructure<List<Payment>> response = new ResponseStructure<>();
		response.setData(payments);
		response.setMessage("Payments fetched successfully");
		response.setStatusCode(HttpStatus.OK.value());
		return response;
	}

	// 5. Update Payment Status
	public ResponseStructure<Payment> updatePaymentStatus(Integer paymentId, PaymentStatusEnum paymentStatus) {

		Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);

		// check if payment exists with given id
		if (optionalPayment.isEmpty()) {
			throw new IdNotFoundException("Payment not found with id: " + paymentId);
		}

		Payment payment = optionalPayment.get();

		// update the payment status
		payment.setPaymentStatus(paymentStatus);
		Payment updatedPayment = paymentRepository.save(payment);

		ResponseStructure<Payment> response = new ResponseStructure<>();
		response.setData(updatedPayment);
		response.setMessage("Payment status updated successfully");
		response.setStatusCode(HttpStatus.OK.value());
		return response;
	}

	// 6. Update Payment Method
	public ResponseStructure<Payment> updatePaymentMethod(Integer paymentId, PaymentMethodEnum paymentMethod) {

		Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);

		// check if payment exists with given id
		if (optionalPayment.isEmpty()) {
			throw new IdNotFoundException("Payment not found with id: " + paymentId);
		}

		Payment payment = optionalPayment.get();

		// update the payment method
		payment.setPaymentMethod(paymentMethod);
		Payment updatedPayment = paymentRepository.save(payment);

		ResponseStructure<Payment> response = new ResponseStructure<>();
		response.setData(updatedPayment);
		response.setMessage("Payment method updated successfully");
		response.setStatusCode(HttpStatus.OK.value());
		return response;
	}

	// 7. Get Payments By Pagination And Sorting
	public ResponseStructure<Page<Payment>> getPaymentsByPaginationAndSorting(Integer pageNum, int pageSize,
			String fieldName) {

		Page<Payment> paymentPage = paymentRepository
				.findAll(PageRequest.of(pageNum, pageSize, Sort.by(fieldName).descending()));

		// check if any payment exists on this page
		if (paymentPage.isEmpty()) {
			throw new NoRecordAvailableException("Data not available");
		}

		ResponseStructure<Page<Payment>> response = new ResponseStructure<>();
		response.setData(paymentPage);
		response.setMessage("Payments fetched successfully");
		response.setStatusCode(HttpStatus.OK.value());
		return response;
	}

	// 8. Delete Payment
	public ResponseStructure<String> deletePayment(Integer paymentId) {

		Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);

		// check if payment exists with given id
		if (optionalPayment.isEmpty()) {
			throw new IdNotFoundException("Payment not found with id: " + paymentId);
		}

		Payment payment = optionalPayment.get();

		// detach the payment from its shipment before deleting, since shipment owns the join column
		Shipment shipment = payment.getShipment();
		if (shipment != null) {
			shipment.setPayment(null);
			shipmentRepository.save(shipment);
		}

		paymentRepository.deleteById(paymentId);

		ResponseStructure<String> response = new ResponseStructure<>();
		response.setData("Deleted payment with id: " + paymentId);
		response.setMessage("Payment deleted successfully");
		response.setStatusCode(HttpStatus.OK.value());
		return response;
	}
}