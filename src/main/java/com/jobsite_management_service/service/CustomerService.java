package com.jobsite_management_service.service;

import com.jobsite_management_service.repository.CustomerRepository;

public class CustomerService {
    CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    //grab a customerId by email
    public Long getCustomerIdByEmail(String email) {
        return customerRepository.getIdByEmail(email);
    }
}
