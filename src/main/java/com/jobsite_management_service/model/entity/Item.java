package com.jobsite_management_service.model.entity;

import com.jobsite_management_service.abstraction.enums.UnitOfMeasure;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "inventory")
@Table(name = "items",
        uniqueConstraints = @UniqueConstraint(name = "uq_items_inventory_sku",
                columnNames = {"inventory_id", "sku"}))
public class Item implements com.jobsite_management_service.abstraction.Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    Inventory inventory;

    @Column(name = "sku", nullable = false)
    String sku;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", length = 1024)
    String description;

    //searchable/filterable facets
    @Column(name = "category")
    String category;

    @Column(name = "brand")
    String brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_of_measure", nullable = false)
    UnitOfMeasure unitOfMeasure = UnitOfMeasure.EACH;

    @Column(name = "price", nullable = false, precision = 19, scale = 4)
    BigDecimal price = BigDecimal.ZERO;

    @Column(name = "currency", nullable = false, length = 3)
    String currency = "USD";

    @Column(name = "color")
    String color;

    @Column(name = "material")
    String material;

    @Column(name = "dimensions")
    String dimensions;

    @Column(name = "weight")
    String weight;

    @Getter(AccessLevel.NONE)
    @Column(name = "stock_quantity", nullable = false)
    Integer stockQuantity = 0;

    @Column(name = "active", nullable = false)
    Boolean active = Boolean.TRUE;

    @Column(name = "created_at")
    OffsetDateTime createdAt;

    @Column(name = "updated_at")
    OffsetDateTime updatedAt;

    //null-safe view used by the inventory stock arithmetic
    public int getStockQuantity() {
        return stockQuantity == null ? 0 : stockQuantity;
    }
}
