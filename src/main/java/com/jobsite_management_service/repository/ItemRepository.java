package com.jobsite_management_service.repository;

import com.jobsite_management_service.model.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

//JpaSpecificationExecutor backs the dynamic inventory search/filter criteria
@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    Optional<Item> findByInventoryIdAndSku(Long inventoryId, String sku);

    Page<Item> findByInventoryId(Long inventoryId, Pageable pageable);

    List<Item> findByInventoryProviderId(Long providerId);

    List<Item> findByCategoryIgnoreCase(String category);

    List<Item> findByInventoryIdAndStockQuantityGreaterThan(Long inventoryId, Integer stockQuantity);

    //the CAST calls give Postgres a type for the optional parameters when they come through null
    @Query("""
            SELECT i FROM Item i
            WHERE i.inventory.id = :inventoryId
              AND i.active = TRUE
              AND (CAST(:category AS String) IS NULL
                   OR LOWER(i.category) = LOWER(CAST(:category AS String)))
              AND (CAST(:maxPrice AS BigDecimal) IS NULL
                   OR i.price <= CAST(:maxPrice AS BigDecimal))
              AND (LOWER(i.name) LIKE LOWER(CONCAT('%', CAST(:term AS String), '%'))
                   OR LOWER(i.sku) LIKE LOWER(CONCAT('%', CAST(:term AS String), '%')))
            """)
    List<Item> search(@Param("inventoryId") Long inventoryId,
                      @Param("term") String term,
                      @Param("category") String category,
                      @Param("maxPrice") BigDecimal maxPrice);
}
