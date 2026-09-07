package com.jobsite_management_service.controller;

import com.jobsite_management_service.orchestration.InformationOrchestrator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InformationManagementController {
    InformationOrchestrator informationOrchestrator;

    public InformationManagementController(InformationOrchestrator informationOrchestrator) {
        this.informationOrchestrator = informationOrchestrator;
    }


    @GetMapping("/api/v1/customers/locations")
    public ResponseEntity<List<String>> getAllLocationsByCustomerEmail(@RequestParam("email") String email) {
        List<String> locations = informationOrchestrator.getAllLocationsByCustomerEmail(email);

        if (locations.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        return ResponseEntity.ok(locations);
    }
}
