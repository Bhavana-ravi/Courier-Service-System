package jsp.springboot.courier.management.entity;

import java.util.ArrayList; 
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAgent {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer deliveryAgentId;
	
	private String deliveryAgentName;
	

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    @Column(unique = true)
    private String deliveryAgentPhno;
    
    @Column(unique = true)
    private String deliveryAgentVehicleNo;
    
    private Boolean deliveryAgentAvailability;
    
    private Double deliveryAgentRatings;
    
    @JsonIgnore
    @OneToMany(mappedBy = "deliveryAgent", cascade = CascadeType.ALL)
    private List<Shipment> shipments = new ArrayList<>();


	
}
