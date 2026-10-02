package jsp.springboot.courier.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jsp.springboot.courier.management.dto.PackageTypeEnum;
import jsp.springboot.courier.management.dto.ResponseStructure;
import jsp.springboot.courier.management.entity.PackageEntity;
import jsp.springboot.courier.management.service.PackageEntityService;

@RestController
@RequestMapping("/api/package")
public class PackageEntityController {

    @Autowired
    private PackageEntityService packageEntityService;

    // 1. Get all packages
    @GetMapping
    public ResponseEntity<ResponseStructure<List<PackageEntity>>> getAllPackages() {
        return new ResponseEntity<>(packageEntityService.getAllPackages(), HttpStatus.OK);
    }

    // 2. Get package by Id
    @GetMapping("/{packageEntityId}")
    public ResponseEntity<ResponseStructure<PackageEntity>> getPackageById(@PathVariable Integer packageEntityId) {
        return new ResponseEntity<>(packageEntityService.getPackageById(packageEntityId), HttpStatus.OK);
    }

    // 3. Get packages by type
    @GetMapping("/type/{packageType}")
    public ResponseEntity<ResponseStructure<List<PackageEntity>>> getPackagesByType(@PathVariable PackageTypeEnum packageType) {
        return new ResponseEntity<>(packageEntityService.getPackagesByType(packageType), HttpStatus.OK);
    }
}