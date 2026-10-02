package jsp.springboot.courier.management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jsp.springboot.courier.management.dto.ResponseStructure;
import jsp.springboot.courier.management.entity.Warehouse;
import jsp.springboot.courier.management.exception.IdNotFoundException;
import jsp.springboot.courier.management.exception.InvalidInputException;
import jsp.springboot.courier.management.exception.NoRecordAvailableException;
import jsp.springboot.courier.management.exception.OperationNotAllowedException;
import jsp.springboot.courier.management.repository.WarehouseRepository;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    // 1. Create warehouse
    public ResponseStructure<Warehouse> createWarehouse(Warehouse warehouse) {
        if (warehouse.getWarehouseName() == null) {
            throw new InvalidInputException("Warehouse name is required");
        } else if (warehouse.getWarehouseLocation() == null) {
            throw new InvalidInputException("Warehouse location is required");
        } else if (warehouse.getWarehousePhno() == null || !warehouse.getWarehousePhno().matches("^[0-9]{10}$")) {
            throw new InvalidInputException("Warehouse phone number must be exactly 10 digits");
        } else if (warehouseRepository.findByWarehousePhno(warehouse.getWarehousePhno()).isPresent()) {
            throw new InvalidInputException("Warehouse phone number already exists");
        }

        Warehouse saved = warehouseRepository.save(warehouse);
        ResponseStructure<Warehouse> res = new ResponseStructure<>();
        res.setData(saved);
        res.setMessage("Warehouse created successfully");
        res.setStatusCode(HttpStatus.CREATED.value());
        return res;
    }

    // 2. Get all warehouses
    public ResponseStructure<List<Warehouse>> getAllWarehouses() {
        List<Warehouse> warehouses = warehouseRepository.findAll();
        if (warehouses.isEmpty()) {
            throw new NoRecordAvailableException("No warehouses available");
        }
        ResponseStructure<List<Warehouse>> res = new ResponseStructure<>();
        res.setData(warehouses);
        res.setMessage("Warehouses fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 3. Get warehouse by Id
    public ResponseStructure<Warehouse> getWarehouseById(Integer warehouseId) {
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(warehouseId);
        if (optionalWarehouse.isEmpty()) {
            throw new IdNotFoundException("Warehouse not found with id: " + warehouseId);
        }
        ResponseStructure<Warehouse> res = new ResponseStructure<>();
        res.setData(optionalWarehouse.get());
        res.setMessage("Warehouse fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 4. Get warehouse by location
    public ResponseStructure<List<Warehouse>> getWarehouseByLocation(String warehouseLocation) {
        List<Warehouse> warehouses = warehouseRepository.findByWarehouseLocation(warehouseLocation);
        if (warehouses.isEmpty()) {
            throw new NoRecordAvailableException("No warehouses found at location: " + warehouseLocation);
        }
        ResponseStructure<List<Warehouse>> res = new ResponseStructure<>();
        res.setData(warehouses);
        res.setMessage("Warehouses fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 5. Get warehouses by capacity greater than
    public ResponseStructure<List<Warehouse>> getWarehousesByCapacityGreaterThan(Double capacity) {
        List<Warehouse> warehouses = warehouseRepository.findByWarehouseCapacityGreaterThan(capacity);
        if (warehouses.isEmpty()) {
            throw new NoRecordAvailableException("No warehouses found with capacity greater than: " + capacity);
        }
        ResponseStructure<List<Warehouse>> res = new ResponseStructure<>();
        res.setData(warehouses);
        res.setMessage("Warehouses fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 6. Update warehouse
    public ResponseStructure<Warehouse> updateWarehouse(Integer warehouseId, Warehouse warehouse) {
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(warehouseId);
        if (optionalWarehouse.isEmpty()) {
            throw new IdNotFoundException("Warehouse not found with id: " + warehouseId);
        }

        Warehouse existing = optionalWarehouse.get();
        existing.setWarehouseName(warehouse.getWarehouseName());
        existing.setWarehouseLocation(warehouse.getWarehouseLocation());
        existing.setWarehouseCapacity(warehouse.getWarehouseCapacity());
        existing.setWarehousePhno(warehouse.getWarehousePhno());

        Warehouse updated = warehouseRepository.save(existing);
        ResponseStructure<Warehouse> res = new ResponseStructure<>();
        res.setData(updated);
        res.setMessage("Warehouse updated successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 7. Delete warehouse (blocked if a shipment is linked to it)
    public ResponseStructure<String> deleteWarehouse(Integer warehouseId) {
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(warehouseId);
        if (optionalWarehouse.isEmpty()) {
            throw new IdNotFoundException("Warehouse not found with id: " + warehouseId);
        }

        Warehouse warehouse = optionalWarehouse.get();
        if (!warehouse.getShipments().isEmpty()) {
            throw new OperationNotAllowedException("Cannot delete warehouse with existing shipments");
        }

        warehouseRepository.deleteById(warehouseId);
        ResponseStructure<String> res = new ResponseStructure<>();
        res.setData("Deleted warehouse with id: " + warehouseId);
        res.setMessage("Warehouse deleted successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }
}