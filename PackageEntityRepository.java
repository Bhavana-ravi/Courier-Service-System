package jsp.springboot.courier.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jsp.springboot.courier.management.dto.PackageTypeEnum;
import jsp.springboot.courier.management.entity.PackageEntity;

@Repository
public interface PackageEntityRepository extends JpaRepository<PackageEntity, Integer>{

	List<PackageEntity> findByPackageType(PackageTypeEnum packageType);

}
