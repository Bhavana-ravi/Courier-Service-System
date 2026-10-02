package jsp.springboot.courier.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jsp.springboot.courier.management.dto.ResponseStructure;
import jsp.springboot.courier.management.entity.Warehouse;
import jsp.springboot.courier.management.service.WarehouseService;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    // 1. Create warehouse
    @PostMapping
    public ResponseEntity<ResponseStructure<Warehouse>> createWarehouse(@RequestBody Warehouse warehouse) {
        return new ResponseEntity<>(warehouseService.createWarehouse(warehouse), HttpStatus.CREATED);
    }

    // 2. Get all warehouses
    @GetMapping
    public ResponseEntity<ResponseStructure<List<Warehouse>>> getAllWarehouses() {
        return new ResponseEntity<>(warehouseService.getAllWarehouses(), HttpStatus.OK);
    }

    // 3. Get warehouse by Id
    @GetMapping("/{warehouseId}")
    public ResponseEntity<ResponseStructure<Warehouse>> getWarehouseById(@PathVariable Integer warehouseId) {
        return new ResponseEntity<>(warehouseService.getWarehouseById(warehouseId), HttpStatus.OK);
    }

    // 4. Get warehouse by location
    @GetMapping("/location/{warehouseLocation}")
    public ResponseEntity<ResponseStructure<List<Warehouse>>> getWarehouseByLocation(@PathVariable String warehouseLocation) {
        return new ResponseEntity<>(warehouseService.getWarehouseByLocation(warehouseLocation), HttpStatus.OK);
    }

    // 5. Get warehouses by capacity greater than
    @GetMapping("/capacity/{capacity}")
    public ResponseEntity<ResponseStructure<List<Warehouse>>> getWarehousesByCapacityGreaterThan(@PathVariable Double capacity) {
        return new ResponseEntity<>(warehouseService.getWarehousesByCapacityGreaterThan(capacity), HttpStatus.OK);
    }

    // 6. Update warehouse
    @PutMapping("/{warehouseId}")
    public ResponseEntity<ResponseStructure<Warehouse>> updateWarehouse(@PathVariable Integer warehouseId, @RequestBody Warehouse warehouse) {
        return new ResponseEntity<>(warehouseService.updateWarehouse(warehouseId, warehouse), HttpStatus.OK);
    }

    // 7. Delete warehouse
    @DeleteMapping("/delete/{warehouseId}")
    public ResponseEntity<ResponseStructure<String>> deleteWarehouse(@PathVariable Integer warehouseId) {
        return new ResponseEntity<>(warehouseService.deleteWarehouse(warehouseId), HttpStatus.OK);
    }
}