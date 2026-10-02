package jsp.springboot.courier.management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jsp.springboot.courier.management.dto.PackageTypeEnum;
import jsp.springboot.courier.management.dto.ResponseStructure;
import jsp.springboot.courier.management.entity.PackageEntity;
import jsp.springboot.courier.management.exception.IdNotFoundException;
import jsp.springboot.courier.management.exception.NoRecordAvailableException;
import jsp.springboot.courier.management.repository.PackageEntityRepository;

@Service
public class PackageEntityService {

    @Autowired
    private PackageEntityRepository packageEntityRepository;

    // 1. Get all packages
    public ResponseStructure<List<PackageEntity>> getAllPackages() {
        List<PackageEntity> packages = packageEntityRepository.findAll();
        if (packages.isEmpty()) {
            throw new NoRecordAvailableException("No packages available");
        }
        ResponseStructure<List<PackageEntity>> res = new ResponseStructure<>();
        res.setData(packages);
        res.setMessage("Packages fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 2. Get package by Id
    public ResponseStructure<PackageEntity> getPackageById(Integer packageEntityId) {
        Optional<PackageEntity> optionalPackage = packageEntityRepository.findById(packageEntityId);
        if (optionalPackage.isEmpty()) {
            throw new IdNotFoundException("Package not found with id: " + packageEntityId);
        }
        ResponseStructure<PackageEntity> res = new ResponseStructure<>();
        res.setData(optionalPackage.get());
        res.setMessage("Package fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 3. Get packages by type
    public ResponseStructure<List<PackageEntity>> getPackagesByType(PackageTypeEnum packageType) {
        List<PackageEntity> packages = packageEntityRepository.findByPackageType(packageType);
        if (packages.isEmpty()) {
            throw new NoRecordAvailableException("No packages found with type: " + packageType);
        }
        ResponseStructure<List<PackageEntity>> res = new ResponseStructure<>();
        res.setData(packages);
        res.setMessage("Packages fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }
}