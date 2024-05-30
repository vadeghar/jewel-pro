package com.billing.service;

import com.billing.dto.Error;
import com.billing.dto.ErrorResponse;
import com.billing.entity.Customer;
import com.billing.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.billing.constant.ErrorCode.DATA_ERROR_CUSTOMER;

@Slf4j
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer customer) {
        if (customerRepository.existsById(id)) {
            customer.setId(id);
            return customerRepository.save(customer);
        }
        return null; // or throw an exception indicating customer not found
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
    public List<Customer> findCustomerByNameLike(String name) {
        return customerRepository.findAllByNameLike(name);
    }
    public ErrorResponse validateCustomer(Customer customer) {
        log.debug("Validating customer {}", customer);
        List<Error> errors = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse(errors, LocalDateTime.now(), DATA_ERROR_CUSTOMER.getCode());

        // Check if customer has a first name
        if (customer.getFirstName() == null || customer.getFirstName().isEmpty()) {
            errorResponse.getErrors().add(new Error("First name is required.", "Error"));
        }

        // Check if customer has a phone number
        if (customer.getPhone() == null || customer.getPhone().isEmpty()) {
            errorResponse.getErrors().add(new Error("Phone number is required.", "Error"));
        }

        // Check if customer has a city
//        if (customer.getAddress() == null || customer.getAddress().getCity() == null || customer.getAddress().getCity().isEmpty()) {
//            errorResponse.getErrors().add(new Error("City is required.", "Error"));
//        }

        log.debug("Exiting validate customer, Found errors: {}", errorResponse.getErrors().size());
        return errorResponse;
    }


    public Customer getById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }
}

