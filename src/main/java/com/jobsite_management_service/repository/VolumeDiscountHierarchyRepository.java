package com.jobsite_management_service.repository;

import com.jobsite_management_service.model.entity.VolumeDiscountHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VolumeDiscountHierarchyRepository extends JpaRepository<VolumeDiscountHierarchy, Long> {

    //singleton per provider - at most one row can ever come back
    Optional<VolumeDiscountHierarchy> findByProviderId(Long providerId);

    Optional<VolumeDiscountHierarchy> findByProviderCode(String providerCode);

    boolean existsByProviderId(Long providerId);
}
