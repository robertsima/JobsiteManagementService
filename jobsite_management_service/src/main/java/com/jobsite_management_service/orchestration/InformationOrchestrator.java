package com.jobsite_management_service.orchestration;

import com.jobsite_management_service.service.CustomerService;
import com.jobsite_management_service.service.JobsiteService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class InformationOrchestrator {
    private CustomerService customerService;
    private JobsiteService jobsiteService;

    public InformationOrchestrator(CustomerService customerService, JobsiteService jobsiteService) {
        this.customerService = customerService;
        this.jobsiteService = jobsiteService;
    }

    //take email, call customer service and jobsite service to create output
    public List<String> getAllLocationsByCustomerEmail(String email){
        Long customerId = customerService.getCustomerIdByEmail(email);

        //simple return here since JPA will return a list
        return jobsiteService.getJobSitesByCustomerId(customerId);
    }
}
