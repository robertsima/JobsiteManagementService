package com.jobsite_management_service.model.entity;

import com.jobsite_management_service.abstraction.enums.DiscountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

//decorator class to be used on price
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"provider", "item"})
@Table(name = "discounts")
public class Discount implements com.jobsite_management_service.abstraction.Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    //a discount is scoped to a whole provider, to a single item, or to neither (global)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    Item item;

    @Column(name = "code", nullable = false, unique = true)
    String code;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", length = 1024)
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    DiscountType discountType = DiscountType.PERCENTAGE;

    //percent when PERCENTAGE (12.50 -> 12.5% off), currency amount when FIXED_AMOUNT
    @Column(name = "value", nullable = false, precision = 19, scale = 4)
    BigDecimal value = BigDecimal.ZERO;

    @Column(name = "minimum_order_total", precision = 19, scale = 4)
    BigDecimal minimumOrderTotal;

    @Column(name = "active", nullable = false)
    Boolean active = Boolean.TRUE;

    @Column(name = "starts_at")
    OffsetDateTime startsAt;

    @Column(name = "ends_at")
    OffsetDateTime endsAt;

    @Column(name = "created_at")
    OffsetDateTime createdAt;

    @Column(name = "updated_at")
    OffsetDateTime updatedAt;

    @Override
    public BigDecimal calculateDiscount(BigDecimal basePrice) {
        if (basePrice == null || !isApplicableTo(basePrice)) {
            return BigDecimal.ZERO;
        }

        BigDecimal cut = discountType == DiscountType.PERCENTAGE
                ? basePrice.multiply(value).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
                : value;

        //never discount below zero
        return cut.min(basePrice).max(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal applyDiscount(BigDecimal basePrice) {
        if (basePrice == null) {
            return BigDecimal.ZERO;
        }

        return basePrice.subtract(calculateDiscount(basePrice));
    }

    public boolean isApplicableTo(BigDecimal basePrice) {
        if (!Boolean.TRUE.equals(active)) {
            return false;
        }

        OffsetDateTime now = OffsetDateTime.now();

        if (startsAt != null && now.isBefore(startsAt)) {
            return false;
        }

        if (endsAt != null && now.isAfter(endsAt)) {
            return false;
        }

        return minimumOrderTotal == null || basePrice.compareTo(minimumOrderTotal) >= 0;
    }
}
