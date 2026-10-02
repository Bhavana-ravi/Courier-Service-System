package jsp.springboot.courier.management.entity;

import java.time.LocalDateTime;  
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jsp.springboot.courier.management.dto.PaymentMethodEnum;
import jsp.springboot.courier.management.dto.PaymentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer paymentId;
	
	private Double amount;
	
	
	@Enumerated(EnumType.STRING)		
	private PaymentMethodEnum paymentMethod;
	
	@Enumerated(EnumType.STRING)
	private PaymentStatusEnum paymentStatus;
	
	@CreationTimestamp
	private LocalDateTime paymentDateTime;
	
	 @JsonIgnore
	 @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
	 private Shipment shipment;
	
 	
}