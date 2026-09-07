package com.jobsite_management_service.repository;

import com.jobsite_management_service.abstraction.enums.JobsiteType;
import com.jobsite_management_service.model.entity.Jobsite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobsiteRepository extends JpaRepository<Jobsite, Long> {

    List<Jobsite> findByBusinessId(Long businessId);

    List<Jobsite> findByRequestorId(Long userId);

    List<Jobsite> findByJobsiteType(JobsiteType jobsiteType);

    //one printable location line per jobsite belonging to the given requestor
    @Query("""
            SELECT CONCAT(j.address, ', ', j.city, ', ', j.state, ' ', j.zipcode, ', ', j.country)
            FROM Jobsite j
            WHERE j.requestor.id = :userId
            """)
    List<String> getLocationsByUserId(@Param("userId") Long userId);
}
