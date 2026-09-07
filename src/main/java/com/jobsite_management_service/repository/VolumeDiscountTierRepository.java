package com.jobsite_management_service.repository;

import com.jobsite_management_service.model.entity.VolumeDiscountTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolumeDiscountTierRepository extends JpaRepository<VolumeDiscountTier, Long> {

    List<VolumeDiscountTier> findByHierarchyIdOrderByLevelAsc(Long hierarchyId);

    Optional<VolumeDiscountTier> findByHierarchyIdAndLevel(Long hierarchyId, Integer level);

    //highest tier the given quantity qualifies for
    @Query("""
            SELECT t FROM VolumeDiscountTier t
            WHERE t.hierarchy.id = :hierarchyId
              AND t.minQuantity <= :quantity
            ORDER BY t.minQuantity DESC
            """)
    List<VolumeDiscountTier> findApplicableTiers(@Param("hierarchyId") Long hierarchyId,
                                                 @Param("quantity") Integer quantity);
}
