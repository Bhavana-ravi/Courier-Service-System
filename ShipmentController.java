package jsp.springboot.courier.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jsp.springboot.courier.management.dto.ResponseStructure;
import jsp.springboot.courier.management.dto.ShipmentStatusEnum;
import jsp.springboot.courier.management.entity.Shipment;
import jsp.springboot.courier.management.entity.TrackingHistory;
import jsp.springboot.courier.management.service.ShipmentService;

@RestController
@RequestMapping("/api/shipment")
public class ShipmentController {

	@Autowired
	private ShipmentService shipmentService;

	// 1. Create Shipment
	@PostMapping
	public ResponseEntity<ResponseStructure<Shipment>> createShipment(@RequestBody Shipment shipment) {
		return new ResponseEntity<>(shipmentService.createShipment(shipment), HttpStatus.CREATED);
	}

	// 2. Get all shipments
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Shipment>>> getAllShipments() {
		return new ResponseEntity<>(shipmentService.getAllShipments(), HttpStatus.OK);
	}

	// 3. Get shipment by Id
	@GetMapping("/shipmentId/{shipmentId}")
	public ResponseEntity<ResponseStructure<Shipment>> getShipmentById(@PathVariable Integer shipmentId) {
		return new ResponseEntity<>(shipmentService.getShipmentById(shipmentId), HttpStatus.OK);
	}

	// 4. Get shipment by tracking number
	@GetMapping("/tracking/{shipmentTrackingNumber}")
	public ResponseEntity<ResponseStructure<Shipment>> getShipmentByTrackingNumber(@PathVariable Integer shipmentTrackingNumber) {
		return new ResponseEntity<>(shipmentService.getShipmentByTrackingNumber(shipmentTrackingNumber),
				HttpStatus.OK);
	}

	// 5. Update shipment status
	@PutMapping("/status/{shipmentId}/{shipmentStatus}")
	public ResponseEntity<ResponseStructure<Shipment>> updateShipmentStatus(@PathVariable Integer shipmentId,@PathVariable ShipmentStatusEnum shipmentStatus) {
		return new ResponseEntity<>(shipmentService.updateShipmentStatus(shipmentId, shipmentStatus), HttpStatus.OK);
	}

	// 6. Assign delivery agent to shipment
	@PutMapping("/assign-agent/{shipmentId}/{deliveryAgentId}")
	public ResponseEntity<ResponseStructure<Shipment>> assignDeliveryAgent(@PathVariable Integer shipmentId, @PathVariable Integer deliveryAgentId) {
		return new ResponseEntity<>(shipmentService.assignDeliveryAgent(shipmentId, deliveryAgentId), HttpStatus.OK);
	}

	// 7. Delete shipment
	@DeleteMapping("/delete/{shipmentId}")
	public ResponseEntity<ResponseStructure<String>> deleteShipment(@PathVariable Integer shipmentId) {
		return new ResponseEntity<>(shipmentService.deleteShipment(shipmentId), HttpStatus.OK);
	}

	// 8. Get shipments by customer id
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<ResponseStructure<List<Shipment>>> getShipmentsByCustomerId(@PathVariable Integer customerId) {
		return new ResponseEntity<>(shipmentService.getShipmentsByCustomerId(customerId), HttpStatus.OK);
	}

	// 9. Get shipments by pagination and sorting
	@GetMapping("/pagination/{pageNum}/{pageSize}/{fieldName}")
	public ResponseEntity<ResponseStructure<Page<Shipment>>> getShipmentsByPaginationAndSorting(
			@PathVariable Integer pageNum, @PathVariable Integer pageSize, @PathVariable String fieldName) {
		return new ResponseEntity<>(shipmentService.getShipmentsByPaginationAndSorting(pageNum, pageSize, fieldName), HttpStatus.OK);
	}

	// 10. Add tracking history to shipment
	@PostMapping("/tracking-history/{shipmentId}")
	public ResponseEntity<ResponseStructure<Shipment>> addTrackingHistory(@PathVariable Integer shipmentId,@RequestBody TrackingHistory trackingHistory) {
		return new ResponseEntity<>(shipmentService.addTrackingHistory(shipmentId, trackingHistory),
				HttpStatus.CREATED);
	}
}