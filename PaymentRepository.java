package jsp.springboot.courier.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jsp.springboot.courier.management.dto.PaymentStatusEnum;
import jsp.springboot.courier.management.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer>{

	List<Payment> findByPaymentStatus(PaymentStatusEnum paymentStatus);

	Optional<Payment> findByShipment_ShipmentId(Integer shipmentId);

}
