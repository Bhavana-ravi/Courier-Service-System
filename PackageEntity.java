package jsp.springboot.courier.management.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jsp.springboot.courier.management.dto.PackageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PackageEntity {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer packageEntityId;
	
	@Enumerated(EnumType.STRING)
	private PackageTypeEnum packageType;
	
	private Boolean fragile;
	
	@NotBlank(message = "Dimensions are required in this format: LxBxH e.g. 30x20x15")
	private String dimensions;
	
	@JsonIgnore
    @OneToOne(mappedBy = "packageEntity", cascade = CascadeType.ALL)
	private Shipment shipment;


	}
