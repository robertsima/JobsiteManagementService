package com.jobsite_management_service.repository;

import com.jobsite_management_service.model.entity.Item;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    /**
     * Dynamic catalog search across one inventory. Every argument except {@code inventoryId} is
     * optional - a null or blank value simply drops that filter.
     * <p>
     * Built as a {@link Specification} rather than a JPQL string on purpose: a static query has to
     * express "ignore this filter" as {@code :param IS NULL}, and Postgres cannot infer a type for
     * a null bind parameter, so it falls back to {@code bytea} and the comparison fails. Adding
     * predicates only when a filter is present avoids binding the null at all.
     */
    default List<Item> search(Long inventoryId, String term, String category, BigDecimal maxPrice) {
        return findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("inventory").get("id"), inventoryId));
            predicates.add(cb.isTrue(root.get("active")));

            if (term != null && !term.isBlank()) {
                String like = "%" + term.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), like),
                        cb.like(cb.lower(root.get("sku")), like),
                        cb.like(cb.lower(root.get("description")), like)));
            }

            if (category != null && !category.isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("category")), category.toLowerCase()));
            }

            if (maxPrice != null) {
                predicates.add(cb.le(root.get("price"), maxPrice));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}
