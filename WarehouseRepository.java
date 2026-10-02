package jsp.springboot.courier.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jsp.springboot.courier.management.entity.Warehouse;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer>{

	Optional<Warehouse> findByWarehousePhno(String warehousePhno);

	List<Warehouse> findByWarehouseLocation(String warehouseLocation);

	List<Warehouse> findByWarehouseCapacityGreaterThan(Double capacity);

}
