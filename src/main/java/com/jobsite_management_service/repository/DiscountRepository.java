package com.jobsite_management_service.repository;

import com.jobsite_management_service.model.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Optional<Discount> findByCode(String code);

    List<Discount> findByProviderId(Long providerId);

    List<Discount> findByItemId(Long itemId);

    //everything currently in force for a provider, item-scoped rows included
    @Query("""
            SELECT d FROM Discount d
            WHERE d.active = TRUE
              AND (d.provider.id = :providerId OR d.provider IS NULL)
              AND (d.startsAt IS NULL OR d.startsAt <= :at)
              AND (d.endsAt IS NULL OR d.endsAt >= :at)
            """)
    List<Discount> findActiveForProvider(@Param("providerId") Long providerId,
                                         @Param("at") OffsetDateTime at);
}
