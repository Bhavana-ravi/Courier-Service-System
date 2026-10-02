package jsp.springboot.courier.management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jsp.springboot.courier.management.dto.ResponseStructure;
import jsp.springboot.courier.management.dto.ShipmentStatusEnum;
import jsp.springboot.courier.management.entity.Customer;
import jsp.springboot.courier.management.entity.Shipment;
import jsp.springboot.courier.management.exception.IdNotFoundException;
import jsp.springboot.courier.management.exception.InvalidInputException;
import jsp.springboot.courier.management.exception.NoRecordAvailableException;
import jsp.springboot.courier.management.exception.OperationNotAllowedException;
import jsp.springboot.courier.management.repository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // 1. Create customer
    public ResponseStructure<Customer> createCustomer(Customer customer) {
        if (customer.getCustomerName() == null) {
            throw new InvalidInputException("Customer name is required");
        } else if (customer.getCustomerEmail() == null) {
            throw new InvalidInputException("Customer email is required");
        } else if (customer.getCustomerPhno() == null || !customer.getCustomerPhno().matches("^[0-9]{10}$")) {
            throw new InvalidInputException("Customer phone number must be exactly 10 digits");
        } else if (customerRepository.findByCustomerEmail(customer.getCustomerEmail()).isPresent()) {
            throw new InvalidInputException("Customer email already exists");
        } else if (customerRepository.findByCustomerPhno(customer.getCustomerPhno()).isPresent()) {
            throw new InvalidInputException("Customer phone number already exists");
        }

        Customer saved = customerRepository.save(customer);
        ResponseStructure<Customer> res = new ResponseStructure<>();
        res.setData(saved);
        res.setMessage("Customer created successfully");
        res.setStatusCode(HttpStatus.CREATED.value());
        return res;
    }

    // 2. Get all customers
    public ResponseStructure<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) {
            throw new NoRecordAvailableException("No customers available");
        }
        ResponseStructure<List<Customer>> res = new ResponseStructure<>();
        res.setData(customers);
        res.setMessage("Customers fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 3. Get customer by Id
    public ResponseStructure<Customer> getCustomerById(Integer customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            throw new IdNotFoundException("Customer not found with id: " + customerId);
        }
        ResponseStructure<Customer> res = new ResponseStructure<>();
        res.setData(optionalCustomer.get());
        res.setMessage("Customer fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 4. Get customer by email
    public ResponseStructure<Customer> getCustomerByEmail(String customerEmail) {
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerEmail(customerEmail);
        if (optionalCustomer.isEmpty()) {
            throw new IdNotFoundException("Customer not found with email: " + customerEmail);
        }
        ResponseStructure<Customer> res = new ResponseStructure<>();
        res.setData(optionalCustomer.get());
        res.setMessage("Customer fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 5. Update customer
    public ResponseStructure<Customer> updateCustomer(Integer customerId, Customer customer) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            throw new IdNotFoundException("Customer not found with id: " + customerId);
        }

        Customer existing = optionalCustomer.get();
        existing.setCustomerName(customer.getCustomerName());
        existing.setCustomerEmail(customer.getCustomerEmail());
        existing.setCustomerPhno(customer.getCustomerPhno());
        existing.setCustomerAddress(customer.getCustomerAddress());

        Customer updated = customerRepository.save(existing);
        ResponseStructure<Customer> res = new ResponseStructure<>();
        res.setData(updated);
        res.setMessage("Customer updated successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 6. Delete customer (blocked if an active shipment exists)
    public ResponseStructure<String> deleteCustomer(Integer customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            throw new IdNotFoundException("Customer not found with id: " + customerId);
        }

        Customer customer = optionalCustomer.get();
        if (hasActiveShipment(customer)) {
            throw new OperationNotAllowedException("Cannot delete customer with an active shipment");
        }

        customerRepository.deleteById(customerId);
        ResponseStructure<String> res = new ResponseStructure<>();
        res.setData("Deleted customer with id: " + customerId);
        res.setMessage("Customer deleted successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 7. Get customer by contact number
    public ResponseStructure<Customer> getCustomerByContact(String customerPhno) {
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerPhno(customerPhno);
        if (optionalCustomer.isEmpty()) {
            throw new IdNotFoundException("Customer not found with contact no: " + customerPhno);
        }
        ResponseStructure<Customer> res = new ResponseStructure<>();
        res.setData(optionalCustomer.get());
        res.setMessage("Customer fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // 8. Get customers by pagination and sorting
    public ResponseStructure<Page<Customer>> getCustomersByPaginationAndSorting(Integer pageNum, Integer pageSize, String fieldName) {
        Page<Customer> customerPage = customerRepository.findAll(PageRequest.of(pageNum, pageSize, Sort.by(fieldName).descending()));
        if (customerPage.isEmpty()) {
            throw new NoRecordAvailableException("Data not available");
        }
        ResponseStructure<Page<Customer>> res = new ResponseStructure<>();
        res.setData(customerPage);
        res.setMessage("Customers fetched successfully");
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    // Helper: checks if customer has any shipment that is not yet delivered or cancelled
    private boolean hasActiveShipment(Customer customer) {
        List<Shipment> shipments = customer.getShipments();
        for (Shipment shipment : shipments) {
            if (shipment.getShipmentStatus() != ShipmentStatusEnum.DELIVERED
                    && shipment.getShipmentStatus() != ShipmentStatusEnum.CANCELLED) {
                return true;
            }
        }
        return false;
    }
}