package jsp.springboot.courier.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import jsp.springboot.courier.management.entity.Customer;
import jsp.springboot.courier.management.service.CustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // 1. Create customer
    @PostMapping
    public ResponseEntity<ResponseStructure<Customer>> createCustomer(@RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.createCustomer(customer), HttpStatus.CREATED);
    }

    // 2. Get all customers
    @GetMapping
    public ResponseEntity<ResponseStructure<List<Customer>>> getAllCustomers() {
        return new ResponseEntity<>(customerService.getAllCustomers(), HttpStatus.OK);
    }

    // 3. Get customer by Id
    @GetMapping("/{customerId}")
    public ResponseEntity<ResponseStructure<Customer>> getCustomerById(@PathVariable Integer customerId) {
        return new ResponseEntity<>(customerService.getCustomerById(customerId), HttpStatus.OK);
    }

    // 4. Get customer by email
    @GetMapping("/email/{customerEmail}")
    public ResponseEntity<ResponseStructure<Customer>> getCustomerByEmail(@PathVariable String customerEmail) {
        return new ResponseEntity<>(customerService.getCustomerByEmail(customerEmail), HttpStatus.OK);
    }

    // 5. Update customer
    @PutMapping("/{customerId}")
    public ResponseEntity<ResponseStructure<Customer>> updateCustomer(@PathVariable Integer customerId, @RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.updateCustomer(customerId, customer), HttpStatus.OK);
    }

    // 6. Delete customer
    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<ResponseStructure<String>> deleteCustomer(@PathVariable Integer customerId) {
        return new ResponseEntity<>(customerService.deleteCustomer(customerId), HttpStatus.OK);
    }

    // 7. Get customer by contact number
    @GetMapping("/contact/{customerPhno}")
    public ResponseEntity<ResponseStructure<Customer>> getCustomerByContact(@PathVariable String customerPhno) {
        return new ResponseEntity<>(customerService.getCustomerByContact(customerPhno), HttpStatus.OK);
    }

    // 8. Get customers by pagination and sorting
    @GetMapping("/pagination/{pageNum}/{pageSize}/{fieldName}")
    public ResponseEntity<ResponseStructure<Page<Customer>>> getCustomersByPaginationAndSorting(@PathVariable Integer pageNum, @PathVariable Integer pageSize, @PathVariable String fieldName) {
        return new ResponseEntity<>(customerService.getCustomersByPaginationAndSorting(pageNum, pageSize, fieldName), HttpStatus.OK);
    }
}