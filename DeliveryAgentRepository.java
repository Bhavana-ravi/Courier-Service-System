package jsp.springboot.courier.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jsp.springboot.courier.management.entity.DeliveryAgent;

@Repository
public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, Integer>{

	Optional<DeliveryAgent> findByDeliveryAgentPhno(String deliveryAgentPhno);

	Optional<DeliveryAgent> findByDeliveryAgentVehicleNo(String deliveryAgentVehicleNo);

	List<DeliveryAgent> findByDeliveryAgentRatingsGreaterThan(Double rating);

}
