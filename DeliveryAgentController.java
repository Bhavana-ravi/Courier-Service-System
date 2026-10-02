package jsp.springboot.courier.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import jsp.springboot.courier.management.entity.DeliveryAgent;
import jsp.springboot.courier.management.service.DeliveryAgentService;

@RestController
@RequestMapping("/api/delivery-agent")
public class DeliveryAgentController {

    @Autowired
    private DeliveryAgentService deliveryAgentService;

    // 1. Create delivery agent
    @PostMapping
    public ResponseEntity<ResponseStructure<DeliveryAgent>> createDeliveryAgent(@RequestBody DeliveryAgent deliveryAgent) {
        return new ResponseEntity<>(deliveryAgentService.createDeliveryAgent(deliveryAgent), HttpStatus.CREATED);
    }

    // 2. Get all delivery agents
    @GetMapping
    public ResponseEntity<ResponseStructure<List<DeliveryAgent>>> getAllDeliveryAgents() {
        return new ResponseEntity<>(deliveryAgentService.getAllDeliveryAgents(), HttpStatus.OK);
    }

    // 3. Get delivery agent by Id
    @GetMapping("/{deliveryAgentId}")
    public ResponseEntity<ResponseStructure<DeliveryAgent>> getDeliveryAgentById(@PathVariable Integer deliveryAgentId) {
        return new ResponseEntity<>(deliveryAgentService.getDeliveryAgentById(deliveryAgentId), HttpStatus.OK);
    }

    // 4. Get delivery agent by vehicle number
    @GetMapping("/vehicle/{vehicleNo}")
    public ResponseEntity<ResponseStructure<DeliveryAgent>> getDeliveryAgentByVehicleNo(@PathVariable String vehicleNo) {
        return new ResponseEntity<>(deliveryAgentService.getDeliveryAgentByVehicleNo(vehicleNo), HttpStatus.OK);
    }

    // 5. Get delivery agent by contact number
    @GetMapping("/contact/{phno}")
    public ResponseEntity<ResponseStructure<DeliveryAgent>> getDeliveryAgentByContactNo(@PathVariable String phno) {
        return new ResponseEntity<>(deliveryAgentService.getDeliveryAgentByContactNo(phno), HttpStatus.OK);
    }

    // 6. Get delivery agents by rating greater than
    @GetMapping("/rating/{rating}")
    public ResponseEntity<ResponseStructure<List<DeliveryAgent>>> getDeliveryAgentsByRatingGreaterThan(@PathVariable Double rating) {
        return new ResponseEntity<>(deliveryAgentService.getDeliveryAgentsByRatingGreaterThan(rating), HttpStatus.OK);
    }

    // 7. Update delivery agent
    @PutMapping("/{deliveryAgentId}")
    public ResponseEntity<ResponseStructure<DeliveryAgent>> updateDeliveryAgent(@PathVariable Integer deliveryAgentId, @RequestBody DeliveryAgent deliveryAgent) {
        return new ResponseEntity<>(deliveryAgentService.updateDeliveryAgent(deliveryAgentId, deliveryAgent), HttpStatus.OK);
    }

    // 8. Delete delivery agent
    @DeleteMapping("/delete/{deliveryAgentId}")
    public ResponseEntity<ResponseStructure<String>> deleteDeliveryAgent(@PathVariable Integer deliveryAgentId) {
        return new ResponseEntity<>(deliveryAgentService.deleteDeliveryAgent(deliveryAgentId), HttpStatus.OK);
    }

    // 9. Update delivery agent availability
    @PutMapping("/availability/{deliveryAgentId}/{availability}")
    public ResponseEntity<ResponseStructure<DeliveryAgent>> updateAgentAvailability(@PathVariable Integer deliveryAgentId, @PathVariable Boolean availability) {
        return new ResponseEntity<>(deliveryAgentService.updateAgentAvailability(deliveryAgentId, availability), HttpStatus.OK);
    }
}