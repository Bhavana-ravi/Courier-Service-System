package jsp.springboot.courier.management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jsp.springboot.courier.management.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	Optional<Customer> findByCustomerEmail(String customerEmail);

	Optional<Customer> findByCustomerPhno(String customerPhno);

}
