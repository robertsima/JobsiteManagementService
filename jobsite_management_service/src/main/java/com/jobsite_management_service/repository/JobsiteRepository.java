package com.jobsite_management_service.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobsiteRepository extends JpaRepository {

    @Query("SELECT jobsites.address as address," +
            " jobsites.city as city," +
            "  jobsites.state as state," +
            " jobsites.zipcode as zipcode," +
            " jobsites.country as country " +
            "FROM jobsites " +
            "WHERE customerId= :customerId")
    List<String> getLocationsByCustomerId(Long customerId);
}
