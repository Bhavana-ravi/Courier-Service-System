package jsp.springboot.courier.management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jsp.springboot.courier.management.dto.ResponseStructure;
import jsp.springboot.courier.management.dto.ShipmentStatusEnum;
import jsp.springboot.courier.management.entity.DeliveryAgent;
import jsp.springboot.courier.management.entity.Shipment;
import jsp.springboot.courier.management.exception.IdNotFoundException;
import jsp.springboot.courier.management.exception.InvalidInputException;
import jsp.springboot.courier.management.exception.NoRecordAvailableException;
import jsp.springboot.courier.management.exception.OperationNotAllowedException;
import jsp.springboot.courier.management.repository.DeliveryAgentRepository;

@Service
public class DeliveryAgentService {

    @Autowired
    private DeliveryAgentRepository deliveryAgentRepository;

    // 1. Create delivery agent
    public ResponseStructure<DeliveryAgent> createDeliveryAgent(DeliveryAgent deliveryAgent) {
        if (deliveryAgent.getDeliveryAgentName() == null) {
            throw new InvalidInputException("Delivery agent name is required");
        } else if (deliveryAgent.getDeliveryAgentPhno() == null || !deliveryAgent.getDeliveryAgentPhno().matches("^[0-9]{10}$")) {
            throw new InvalidInputException("Delivery agent phone number must be exactly 10 digits");
        } else if (deliveryAgent.getDeliveryAgentVehicleNo() == null) {
            throw new InvalidInputException("Delivery agent vehicle number is required");
        } else if (deliveryAgentRepository.findByDeliveryAgentPhno(deliveryAgent.getDeliveryAgentPhno()).isPresent()) {
            throw new InvalidInputException("Delivery agent phone number already exists");
        } else if (deliveryAgentRepository.findByDeliveryAgentVehicleNo(deliveryAgent.getDeliveryAgentVehicleNo()).isPresent()) {
            throw new InvalidInputException("Delivery agent vehicle number already exists");
        }

        if (deliveryAgent.getDeliveryAgentAvailability() == null) {
            deliveryAgent.setDeliveryAgentAvailability(true);
        }

        DeliveryAgent saved = deliveryAgentRepository.save(deliveryAgent);
        ResponseStructure<DeliveryAgent> res = new ResponseStructure<>();
        res.setData(saved);
        res.setMessage("Delivery agent created successfully");
        res.setStatusCode(HttpStatus.CREATED.value());
        return res;
    }

    // 2. Get all delivery agents
    public ResponseStructure<List<DeliveryAgent>> getAllDeliveryAgents() {
        List<DeliveryAgent> agents = deliveryAgentRepository.findAll();
        if (agents.isEmpty()) {
            throw new NoRecordAvailableException("No delivery agents available");
        }
        ResponseStructure<List<DeliveryAgent>> res = new ResponseStructure<>();
        res.setData(agents);
        res.setMessage("Delivery agents fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 3. Get delivery agent by Id
    public ResponseStructure<DeliveryAgent> getDeliveryAgentById(Integer deliveryAgentId) {
        Optional<DeliveryAgent> optionalAgent = deliveryAgentRepository.findById(deliveryAgentId);
        if (optionalAgent.isEmpty()) {
            throw new IdNotFoundException("Delivery agent not found with id: " + deliveryAgentId);
        }
        ResponseStructure<DeliveryAgent> res = new ResponseStructure<>();
        res.setData(optionalAgent.get());
        res.setMessage("Delivery agent fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 4. Get delivery agent by vehicle number
    public ResponseStructure<DeliveryAgent> getDeliveryAgentByVehicleNo(String vehicleNo) {
        Optional<DeliveryAgent> optionalAgent = deliveryAgentRepository.findByDeliveryAgentVehicleNo(vehicleNo);
        if (optionalAgent.isEmpty()) {
            throw new IdNotFoundException("Delivery agent not found with vehicle no: " + vehicleNo);
        }
        ResponseStructure<DeliveryAgent> res = new ResponseStructure<>();
        res.setData(optionalAgent.get());
        res.setMessage("Delivery agent fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 5. Get delivery agent by contact number
    public ResponseStructure<DeliveryAgent> getDeliveryAgentByContactNo(String phno) {
        Optional<DeliveryAgent> optionalAgent = deliveryAgentRepository.findByDeliveryAgentPhno(phno);
        if (optionalAgent.isEmpty()) {
            throw new IdNotFoundException("Delivery agent not found with contact no: " + phno);
        }
        ResponseStructure<DeliveryAgent> res = new ResponseStructure<>();
        res.setData(optionalAgent.get());
        res.setMessage("Delivery agent fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 6. Get delivery agents by rating greater than
    public ResponseStructure<List<DeliveryAgent>> getDeliveryAgentsByRatingGreaterThan(Double rating) {
        List<DeliveryAgent> agents = deliveryAgentRepository.findByDeliveryAgentRatingsGreaterThan(rating);
        if (agents.isEmpty()) {
            throw new NoRecordAvailableException("No delivery agents found with rating greater than: " + rating);
        }
        ResponseStructure<List<DeliveryAgent>> res = new ResponseStructure<>();
        res.setData(agents);
        res.setMessage("Delivery agents fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 7. Update delivery agent
    public ResponseStructure<DeliveryAgent> updateDeliveryAgent(Integer deliveryAgentId, DeliveryAgent deliveryAgent) {
        Optional<DeliveryAgent> optionalAgent = deliveryAgentRepository.findById(deliveryAgentId);
        if (optionalAgent.isEmpty()) {
            throw new IdNotFoundException("Delivery agent not found with id: " + deliveryAgentId);
        }

        DeliveryAgent existing = optionalAgent.get();
        existing.setDeliveryAgentName(deliveryAgent.getDeliveryAgentName());
        existing.setDeliveryAgentPhno(deliveryAgent.getDeliveryAgentPhno());
        existing.setDeliveryAgentVehicleNo(deliveryAgent.getDeliveryAgentVehicleNo());
        existing.setDeliveryAgentAvailability(deliveryAgent.getDeliveryAgentAvailability());
        existing.setDeliveryAgentRatings(deliveryAgent.getDeliveryAgentRatings());

        DeliveryAgent updated = deliveryAgentRepository.save(existing);
        ResponseStructure<DeliveryAgent> res = new ResponseStructure<>();
        res.setData(updated);
        res.setMessage("Delivery agent updated successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

 // 8. Delete delivery agent (blocked if busy / shipment assigned)
    public ResponseStructure<String> deleteDeliveryAgent(Integer deliveryAgentId) {
        Optional<DeliveryAgent> optionalAgent = deliveryAgentRepository.findById(deliveryAgentId);
        if (optionalAgent.isEmpty()) {
            throw new IdNotFoundException("Delivery agent not found with id: " + deliveryAgentId);
        }

        DeliveryAgent agent = optionalAgent.get();

        // Block if agent is marked unavailable (i.e. allotted a shipment)
        if (!agent.getDeliveryAgentAvailability()) {
            throw new OperationNotAllowedException("Cannot delete delivery agent: agent is unavailable / has an allotted shipment");
        }

        // Block if any active shipment is still linked
        if (hasAssignedShipment(agent)) {
            throw new OperationNotAllowedException("Cannot delete delivery agent with an assigned shipment");
        }

        deliveryAgentRepository.deleteById(deliveryAgentId);
        ResponseStructure<String> res = new ResponseStructure<>();
        res.setData("Deleted delivery agent with id: " + deliveryAgentId);
        res.setMessage("Delivery agent deleted successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }
    
    
    // 9. Update delivery agent availability
    public ResponseStructure<DeliveryAgent> updateAgentAvailability(Integer deliveryAgentId, Boolean availability) {
        Optional<DeliveryAgent> optionalAgent = deliveryAgentRepository.findById(deliveryAgentId);
        if (optionalAgent.isEmpty()) {
            throw new IdNotFoundException("Delivery agent not found with id: " + deliveryAgentId);
        }

        DeliveryAgent agent = optionalAgent.get();
        agent.setDeliveryAgentAvailability(availability);
        DeliveryAgent updated = deliveryAgentRepository.save(agent);

        ResponseStructure<DeliveryAgent> res = new ResponseStructure<>();
        res.setData(updated);
        res.setMessage("Delivery agent availability updated successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // Helper: checks if agent has a shipment that is not yet delivered or cancelled
    private boolean hasAssignedShipment(DeliveryAgent agent) {
        List<Shipment> shipments = agent.getShipments();
        for (Shipment shipment : shipments) {
            if (shipment.getShipmentStatus() != ShipmentStatusEnum.DELIVERED
                    && shipment.getShipmentStatus() != ShipmentStatusEnum.CANCELLED) {
                return true;
            }
        }
        return false;
    }
}