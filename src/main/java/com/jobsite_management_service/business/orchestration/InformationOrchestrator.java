package com.jobsite_management_service.business.orchestration;

import com.jobsite_management_service.business.service.UserService;
import com.jobsite_management_service.business.service.JobsiteService;

import java.util.List;

public class InformationOrchestrator {
    private UserService userService;
    private JobsiteService jobsiteService;

    public InformationOrchestrator(UserService userService, JobsiteService jobsiteService) {
        this.userService = userService;
        this.jobsiteService = jobsiteService;
    }

    //take email, call user service and jobsite service to create output
    public List<String> getAllLocationsByUserEmail(String email){
        Long userId = userService.getUserIdByEmail(email);

        //simple return here since JPA will return a list
        return jobsiteService.getJobSitesByUserId(userId);
    }
}
