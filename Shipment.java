	package jsp.springboot.courier.management.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jsp.springboot.courier.management.dto.ShipmentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer shipmentId;
		
		@Column(unique = true)
		private Integer shipmentTrackingNumber;
		private String shipmentSource;
		private String shipmentDestination;
		private Double shipmentHeight;
		
		@CreationTimestamp
		private LocalDateTime shipmentDateTime;
		
		private LocalDate shipmentDeliveryDate;
		
		@Enumerated(EnumType.STRING)
		private ShipmentStatusEnum shipmentStatus;
	
	
	 	@ManyToOne
	    private Customer customer;

	    @ManyToOne
	    private DeliveryAgent deliveryAgent;

	    @ManyToOne
	    private Warehouse warehouse;


	    @OneToOne(cascade = CascadeType.ALL)
	    @JoinColumn(unique = true)
	    private Payment payment;
	

	    @OneToOne(cascade = CascadeType.ALL)
	    @JoinColumn(unique = true)
	    private PackageEntity packageEntity;

	    @JsonIgnore
	    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL)
	    private List<TrackingHistory> trackingHistories = new ArrayList<>();
	    
}
