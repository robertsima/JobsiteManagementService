package com.jobsite_management_service.controller;

import com.jobsite_management_service.repository.UserRepository;
import com.jobsite_management_service.repository.JobsiteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JobsiteController {
    UserRepository userRepository;
    JobsiteRepository jobsiteRepository;

    //constructor dependency injection
    public JobsiteController (UserRepository userRepository, JobsiteRepository jobsiteRepository) {
        this.userRepository = userRepository;
        this.jobsiteRepository = jobsiteRepository;
    }

    @GetMapping
    public ResponseEntity<List<String>> testGetAllLocationsByUserEmail(String email) {
//        String response = "";
        Long userId = userRepository.getIdByEmail(email);
        List<String> locations = jobsiteRepository.getLocationsByUserId(userId);

        if (locations.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        return ResponseEntity.ok(locations);
    }

}
