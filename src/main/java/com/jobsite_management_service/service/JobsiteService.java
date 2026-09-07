package com.jobsite_management_service.service;

import com.jobsite_management_service.repository.JobsiteRepository;

import java.util.List;

public class JobsiteService {
    JobsiteRepository jobsiteRepository;

    public JobsiteService (JobsiteRepository jobsiteRepository) {
        this.jobsiteRepository = jobsiteRepository;
    }

    //this function returns all jobsites given a userID where
    //a jobsite is one combination of address, city, state, zip, country
    public List<String> getJobSitesByUserId(Long userId) {
        return jobsiteRepository.getLocationsByUserId(userId);
    }

}
