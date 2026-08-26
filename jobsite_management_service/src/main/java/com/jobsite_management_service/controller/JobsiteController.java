package com.jobsite_management_service.controller;

import com.jobsite_management_service.repository.CustomerRepository;
import com.jobsite_management_service.repository.JobsiteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JobsiteController {
    CustomerRepository customerRepository;
    JobsiteRepository jobsiteRepository;

    //constructor dependency injection
    public JobsiteController (CustomerRepository customerRepository, JobsiteRepository jobsiteRepository) {
        this.customerRepository = customerRepository;
        this.jobsiteRepository = jobsiteRepository;
    }

    @GetMapping
    public ResponseEntity<List<String>> testGetAllLocationsByCustomerEmail(String email) {
//        String response = "";
        Long customerId = customerRepository.getIdByEmail(email);
        List<String> locations = jobsiteRepository.getLocationsByCustomerId(customerId);

        if (locations.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        return ResponseEntity.ok(locations);
    }

}
