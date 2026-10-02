package jsp.springboot.courier.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jsp.springboot.courier.management.entity.Shipment;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {

	Optional<Shipment> findByShipmentTrackingNumber(Integer shipmentTrackingNumber);

	List<Shipment> findByCustomer_CustomerId(Integer customerId);

}