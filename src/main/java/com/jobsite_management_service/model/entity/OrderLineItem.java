package com.jobsite_management_service.model.entity;

import com.jobsite_management_service.abstraction.enums.UnitOfMeasure;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

//priced line on an order - snapshots the item at the moment the order was placed
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"order", "item"})
@Table(name = "order_line_items")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    Item item;

    @Column(name = "sku", nullable = false)
    String sku;

    @Column(name = "description", nullable = false)
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_of_measure", nullable = false)
    UnitOfMeasure unitOfMeasure = UnitOfMeasure.EACH;

    @Column(name = "quantity", nullable = false)
    Integer quantity = 0;

    @Column(name = "quantity_fulfilled", nullable = false)
    Integer quantityFulfilled = 0;

    @Column(name = "unit_price", nullable = false, precision = 19, scale = 4)
    BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(name = "discount_amount", nullable = false, precision = 19, scale = 4)
    BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "line_total", nullable = false, precision = 19, scale = 4)
    BigDecimal lineTotal = BigDecimal.ZERO;

    @Column(name = "created_at")
    OffsetDateTime createdAt;

    public BigDecimal getExtendedPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity == null ? 0 : quantity));
    }

    public void recalculateLineTotal() {
        lineTotal = getExtendedPrice().subtract(discountAmount == null ? BigDecimal.ZERO : discountAmount);
    }
}
