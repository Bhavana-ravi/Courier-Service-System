package jsp.springboot.courier.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jsp.springboot.courier.management.dto.TrackingHistoryStatusEnum;
import jsp.springboot.courier.management.entity.TrackingHistory;

@Repository
public interface TrackingHistoryRepository extends JpaRepository<TrackingHistory, Integer>{

	List<TrackingHistory> findByShipment_ShipmentTrackingNumber(Integer shipmentTrackingNumber);

	List<TrackingHistory> findByTrackingStatus(TrackingHistoryStatusEnum trackingStatus);

}
