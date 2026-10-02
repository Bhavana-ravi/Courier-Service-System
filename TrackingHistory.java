package jsp.springboot.courier.management.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jsp.springboot.courier.management.dto.TrackingHistoryStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackingHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer trackingId;
	
	private String trackingLocation;
	
	private String trackingRemarks;
	
	@Enumerated(EnumType.STRING)
	private TrackingHistoryStatusEnum trackingStatus;
	
	@ManyToOne
	private Shipment shipment;

}
