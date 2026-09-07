package com.jobsite_management_service.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

//one 1-indexed level of a provider volume hierarchy: buy N or more, take X percent off
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "hierarchy")
@Table(name = "volume_discount_tiers",
        uniqueConstraints = @UniqueConstraint(name = "uq_volume_tier_level",
                columnNames = {"hierarchy_id", "level"}))
public class VolumeDiscountTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hierarchy_id", nullable = false)
    VolumeDiscountHierarchy hierarchy;

    @Column(name = "level", nullable = false)
    Integer level;

    @Column(name = "min_quantity", nullable = false)
    Integer minQuantity;

    @Column(name = "discount_percentage", nullable = false, precision = 5, scale = 2)
    BigDecimal discountPercentage = BigDecimal.ZERO;

    @Column(name = "label")
    String label;
}
