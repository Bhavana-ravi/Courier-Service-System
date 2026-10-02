package jsp.springboot.courier.management.service;

import java.util.List; 

import org.springframework.transaction.annotation.Transactional;
import jsp.springboot.courier.management.dto.TrackingHistoryStatusEnum;
import jsp.springboot.courier.management.repository.TrackingHistoryRepository;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jsp.springboot.courier.management.dto.ResponseStructure;
import jsp.springboot.courier.management.dto.ShipmentStatusEnum;
import jsp.springboot.courier.management.entity.Customer;
import jsp.springboot.courier.management.entity.DeliveryAgent;
import jsp.springboot.courier.management.entity.Shipment;
import jsp.springboot.courier.management.entity.TrackingHistory;
import jsp.springboot.courier.management.entity.Warehouse;
import jsp.springboot.courier.management.exception.DeliveryAgentUnavailableException;
import jsp.springboot.courier.management.exception.IdNotFoundException;
import jsp.springboot.courier.management.exception.InvalidInputException;
import jsp.springboot.courier.management.exception.NoRecordAvailableException;
import jsp.springboot.courier.management.repository.CustomerRepository;
import jsp.springboot.courier.management.repository.DeliveryAgentRepository;
import jsp.springboot.courier.management.repository.ShipmentRepository;
import jsp.springboot.courier.management.repository.WarehouseRepository;

@Service
public class ShipmentService {

	@Autowired
	private ShipmentRepository shipmentRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private DeliveryAgentRepository deliveryAgentRepository;

	@Autowired
	private WarehouseRepository warehouseRepository;
	@Autowired
	private TrackingHistoryRepository trackingHistoryRepository;

	private static final double RATE_PER_KG = 50.0;
	private static final double FRAGILE_SURCHARGE = 150.0;
	
	// 1. Create Shipment
	@Transactional
	public ResponseStructure<Shipment> createShipment(Shipment shipment) {

		// check tracking number is not already used
		if (shipment.getShipmentTrackingNumber() != null) {
			Optional<Shipment> existingShipment = shipmentRepository
					.findByShipmentTrackingNumber(shipment.getShipmentTrackingNumber());
			if (existingShipment.isPresent()) {
				throw new InvalidInputException(
						"Tracking number already exists: " + shipment.getShipmentTrackingNumber());
			}
		}

		// validate customer id is provided
		if (shipment.getCustomer() == null || shipment.getCustomer().getCustomerId() == null) {
			throw new InvalidInputException("Customer id is required to create shipment");
		}

		Optional<Customer> optionalCustomer = customerRepository.findById(shipment.getCustomer().getCustomerId());
		if (optionalCustomer.isEmpty()) {
			throw new IdNotFoundException("Customer not found with id: " + shipment.getCustomer().getCustomerId());
		}
		shipment.setCustomer(optionalCustomer.get());

		// validate warehouse id is provided
		if (shipment.getWarehouse() == null || shipment.getWarehouse().getWarehouseId() == null) {
			throw new InvalidInputException("Warehouse id is required to create shipment");
		}

		Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(shipment.getWarehouse().getWarehouseId());
		if (optionalWarehouse.isEmpty()) {
			throw new IdNotFoundException("Warehouse not found with id: " + shipment.getWarehouse().getWarehouseId());
		}
		shipment.setWarehouse(optionalWarehouse.get());

		// validate package details and weight
		if (shipment.getPackageEntity() == null || shipment.getShipmentHeight() == null) {
			throw new InvalidInputException("Package details and weight are required to create shipment");
		}

		// validate payment block
		if (shipment.getPayment() == null) {
			throw new InvalidInputException("Payment details are required to create shipment");
		}

		// check the delivery agent 
		DeliveryAgent agent = null;
		if (shipment.getDeliveryAgent() != null && shipment.getDeliveryAgent().getDeliveryAgentId() != null) {

			Optional<DeliveryAgent> optionalAgent = deliveryAgentRepository
					.findById(shipment.getDeliveryAgent().getDeliveryAgentId());
			if (optionalAgent.isEmpty()) {
				throw new IdNotFoundException(
						"Delivery agent not found with id: " + shipment.getDeliveryAgent().getDeliveryAgentId());
			}

			agent = optionalAgent.get();

			if (agent.getDeliveryAgentAvailability() == null || !agent.getDeliveryAgentAvailability()) {
				throw new DeliveryAgentUnavailableException(
						"Delivery agent is not available with id: " + agent.getDeliveryAgentId());
			}
		}

		// calculate price
		double calculatedAmount = shipment.getShipmentHeight() * RATE_PER_KG;
		if (shipment.getPackageEntity().getFragile() != null && shipment.getPackageEntity().getFragile()) {
			calculatedAmount = calculatedAmount + FRAGILE_SURCHARGE;
		}
		shipment.getPayment().setAmount(calculatedAmount);
		shipment.setDeliveryAgent(agent);
		Shipment savedShipment = shipmentRepository.save(shipment);
		saveTrackingHistory(savedShipment, TrackingHistoryStatusEnum.ORDER_PLACED, savedShipment.getShipmentSource(),"Shipment created");

		if (agent != null) {
			agent.setDeliveryAgentAvailability(false);
			deliveryAgentRepository.save(agent);
		}

		ResponseStructure<Shipment> response = new ResponseStructure<>();
		response.setData(savedShipment);
		response.setMessage("Shipment created successfully");
		response.setStatusCode(HttpStatus.CREATED.value());
		return response;
	}

	// 2. Get All Shipments
	public ResponseStructure<List<Shipment>> getAllShipments() {

		List<Shipment> shipments = shipmentRepository.findAll();
		if (shipments.isEmpty()) {
			throw new NoRecordAvailableException("No shipments available");
		}

		ResponseStructure<List<Shipment>> response = new ResponseStructure<>();
		response.setData(shipments);
		response.setMessage("Shipments fetched successfully");
		response.setStatusCode(HttpStatus.OK.value());
		return response;
	}

	// 3. Get Shipment By Id
	public ResponseStructure<Shipment> getShipmentById(Integer shipmentId) {

		Optional<Shipment> optionalShipment = shipmentRepository.findById(shipmentId);
		if (optionalShipment.isEmpty()) {
			throw new IdNotFoundException("Shipment not found with id: " + shipmentId);
		}

		ResponseStructure<Shipment> response = new ResponseStructure<>();
		response.setData(optionalShipment.get());
		response.setMessage("Shipment fetched successfully");
		response.setStatusCode(HttpStatus.OK.value());
		return response;
	}

	// 4. Get Shipment By Tracking Number
	public ResponseStructure<Shipment> getShipmentByTrackingNumber(Integer shipmentTrackingNumber) {

		Optional<Shipment> optionalShipment = shipmentRepository.findByShipmentTrackingNumber(shipmentTrackingNumber);
		if (optionalShipment.isEmpty()) {
			throw new IdNotFoundException("Shipment not found with tracking number: " + shipmentTrackingNumber);
		}

		ResponseStructure<Shipment> response = new ResponseStructure<>();
		response.setData(optionalShipment.get());
		response.setMessage("Shipment fetched successfully");
		response.setStatusCode(HttpStatus.OK.value());
		return response;
	}

	// 5. Update Shipment Status
	@Transactional
	public ResponseStructure<Shipment> updateShipmentStatus(Integer shipmentId, ShipmentStatusEnum shipmentStatus) {

		Optional<Shipment> optionalShipment = shipmentRepository.findById(shipmentId);

		if (optionalShipment.isEmpty()) {
			throw new IdNotFoundException("Shipment not found with id: " + shipmentId);
		}

		Shipment shipment = optionalShipment.get();
		shipment.setShipmentStatus(shipmentStatus);
		TrackingHistoryStatusEnum historyStatus = mapToTrackingStatus(shipmentStatus);
		saveTrackingHistory(shipment, historyStatus, shipment.getShipmentDestination(),"Shipment status changed to " + shipmentStatus);
		if (shipment.getDeliveryAgent() != null) {
			if (shipmentStatus == ShipmentStatusEnum.DELIVERED || shipmentStatus == ShipmentStatusEnum.CANCELLED) {
				DeliveryAgent agent = shipment.getDeliveryAgent();
				agent.setDeliveryAgentAvailability(true);
				deliveryAgentRepository.save(agent);
			}
		}

		Shipment updatedShipment = shipmentRepository.save(shipment);

		ResponseStructure<Shipment> response = new ResponseStructure<>();
		response.setData(updatedShipment);
		response.setMessage("Shipment status updated successfully");
		response.setStatusCode(HttpStatus.OK.value());
		return response;
	}
	
	
	// 6. Assign Delivery Agent To Shipment
	public ResponseStructure<Shipment> assignDeliveryAgent(Integer shipmentId, Integer deliveryAgentId) {

		Optional<Shipment> optionalShipment = shipmentRepository.findById(shipmentId);
		if (optionalShipment.isEmpty()) {
			throw new IdNotFoundException("Shipment not found with id: " + shipmentId);
		}

		Optional<DeliveryAgent> optionalAgent = deliveryAgentRepository.findById(deliveryAgentId);
		if (optionalAgent.isEmpty()) {
			throw new IdNotFoundException("Delivery agent not found with id: " + deliveryAgentId);
		}

		DeliveryAgent agent = optionalAgent.get();
		if (agent.getDeliveryAgentAvailability() == null || !agent.getDeliveryAgentAvailability()) {
			throw new DeliveryAgentUnavailableException(
					"Delivery agent is not available with id: " + deliveryAgentId);
		}

		Shipment shipment = optionalShipment.get();
		shipment.setDeliveryAgent(agent);
		agent.setDeliveryAgentAvailability(false);
		deliveryAgentRepository.save(agent);

		Shipment updatedShipment = shipmentRepository.save(shipment);

		ResponseStructure<Shipment> response = new ResponseStructure<>();
		response.setData(updatedShipment);
		response.setMessage("Delivery agent assigned successfully");
		response.setStatusCode(HttpStatus.OK.value());
		return response;
	}

	// 7. Delete Shipment
	public ResponseStructure<String> deleteShipment(Integer shipmentId) {

		Optional<Shipment> optionalShipment = shipmentRepository.findById(shipmentId);
		if (optionalShipment.isEmpty()) {
			throw new IdNotFoundException("Shipment not found with id: " + shipmentId);
		}

		Shipment shipment = optionalShipment.get();
		if (shipment.getDeliveryAgent() != null) {
			DeliveryAgent agent = shipment.getDeliveryAgent();
			agent.setDeliveryAgentAvailability(true);
			deliveryAgentRepository.save(agent);
		}

		shipmentRepository.deleteById(shipmentId);

		ResponseStructure<String> response = new ResponseStructure<>();
		response.setData("Deleted shipment with id: " + shipmentId);
		response.setMessage("Shipment deleted successfully");
		response.setStatusCode(HttpStatus.OK.value());
		return response;
	}

	// 8. Get Shipments By Customer Id
	public ResponseStructure<List<Shipment>> getShipmentsByCustomerId(Integer customerId) {
		if (!customerRepository.existsById(customerId)) {
			throw new IdNotFoundException("Customer not found with id: " + customerId);
		}

		List<Shipment> shipments = shipmentRepository.findByCustomer_CustomerId(customerId);
		if (shipments.isEmpty()) {
			throw new NoRecordAvailableException("No shipments available for customer id: " + customerId);
		}

		ResponseStructure<List<Shipment>> response = new ResponseStructure<>();
		response.setData(shipments);
		response.setMessage("Shipments fetched successfully");
		response.setStatusCode(HttpStatus.OK.value());
		return response;
	}

	// 9. Get Shipments By Pagination And Sorting
	public ResponseStructure<Page<Shipment>> getShipmentsByPaginationAndSorting(Integer pageNum, int pageSize,
			String fieldName) {

		Page<Shipment> shipmentPage = shipmentRepository.findAll(PageRequest.of(pageNum, pageSize, Sort.by(fieldName).descending()));

		if (shipmentPage.isEmpty()) {
			throw new NoRecordAvailableException("Data not available");
		}

		ResponseStructure<Page<Shipment>> response = new ResponseStructure<>();
		response.setData(shipmentPage);
		response.setMessage("Shipments fetched successfully");
		response.setStatusCode(HttpStatus.OK.value());
		return response;
	}

	// 10. Add Tracking History To Shipment
	@Transactional
	public ResponseStructure<Shipment> addTrackingHistory(Integer shipmentId, TrackingHistory trackingHistory) {

		Optional<Shipment> optionalShipment = shipmentRepository.findById(shipmentId);

		if (optionalShipment.isEmpty()) {
			throw new IdNotFoundException("Shipment not found with id: " + shipmentId);
		}

		Shipment shipment = optionalShipment.get();
		trackingHistory.setShipment(shipment);
		trackingHistoryRepository.save(trackingHistory);

		ResponseStructure<Shipment> response = new ResponseStructure<>();
		response.setData(shipment);
		response.setMessage("Tracking history added successfully");
		response.setStatusCode(HttpStatus.CREATED.value());
		return response;
	}
	// Helper: saves one tracking history row for a shipment
	private void saveTrackingHistory(Shipment shipment, TrackingHistoryStatusEnum status, String location,String remarks) {
		TrackingHistory history = new TrackingHistory();
		history.setShipment(shipment);
		history.setTrackingStatus(status);
		history.setTrackingLocation(location);
		history.setTrackingRemarks(remarks);
		trackingHistoryRepository.save(history);
	}
	
	private TrackingHistoryStatusEnum mapToTrackingStatus(ShipmentStatusEnum status) {
	    switch (status) {
	    case PENDING:
	        return TrackingHistoryStatusEnum.PENDING;
	    case ORDER_PLACED:
	        return TrackingHistoryStatusEnum.ORDER_PLACED;
	    case BOOKED:
	        return TrackingHistoryStatusEnum.BOOKED;
	    case PACKED:
	        return TrackingHistoryStatusEnum.PACKED;
	    case SHIPPED:
	        return TrackingHistoryStatusEnum.SHIPPED;
	    case IN_TRANSIT:
	        return TrackingHistoryStatusEnum.IN_TRANSIT;
	    case OUT_FOR_DELIVERY:
	        return TrackingHistoryStatusEnum.OUT_FOR_DELIVERY;
	    case DELIVERED:
	        return TrackingHistoryStatusEnum.DELIVERED;
	    case FAILED_DELIVERY:
	        return TrackingHistoryStatusEnum.DELIVERY_FAILED;
	    case CANCELLED:
	        return TrackingHistoryStatusEnum.CANCELLED;
	    case RETURNED:
	        return TrackingHistoryStatusEnum.RETURNED;
	    default:
	        throw new IllegalArgumentException("Unmapped shipment status: " + status);
	    }
	}
}