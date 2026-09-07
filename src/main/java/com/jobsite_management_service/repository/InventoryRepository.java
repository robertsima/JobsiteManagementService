package com.jobsite_management_service.repository;

import com.jobsite_management_service.model.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    //one inventory per provider
    Optional<Inventory> findByProviderId(Long providerId);

    Optional<Inventory> findByProviderCode(String providerCode);
}
