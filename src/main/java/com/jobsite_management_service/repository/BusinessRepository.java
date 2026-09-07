package com.jobsite_management_service.repository;

import com.jobsite_management_service.abstraction.enums.BusinessType;
import com.jobsite_management_service.model.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    Optional<Business> findByName(String name);

    Optional<Business> findByTaxId(String taxId);

    List<Business> findByBusinessType(BusinessType businessType);

    List<Business> findByActiveTrue();
}
