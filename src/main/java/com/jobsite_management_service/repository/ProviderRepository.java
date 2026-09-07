package com.jobsite_management_service.repository;

import com.jobsite_management_service.model.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    Optional<Provider> findByCode(String code);

    Optional<Provider> findByName(String name);

    List<Provider> findByActiveTrue();

    //providers a given business has been integrated with
    @Query("SELECT p FROM Provider p JOIN p.businesses b WHERE b.id = :businessId")
    List<Provider> findByBusinessId(@Param("businessId") Long businessId);
}
