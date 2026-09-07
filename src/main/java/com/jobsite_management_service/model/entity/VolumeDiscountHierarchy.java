package com.jobsite_management_service.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

//singleton per provider - exactly one hierarchy backs a supplier at any time
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"provider", "tiers"})
@Table(name = "volume_discount_hierarchies")
public class VolumeDiscountHierarchy implements com.jobsite_management_service.abstraction.VolumeDiscountHierarchy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false, unique = true)
    Provider provider;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", length = 1024)
    String description;

    @Column(name = "active", nullable = false)
    Boolean active = Boolean.TRUE;

    @Column(name = "created_at")
    OffsetDateTime createdAt;

    @Column(name = "updated_at")
    OffsetDateTime updatedAt;

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "hierarchy", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("level ASC")
    List<VolumeDiscountTier> tiers = new ArrayList<>();

    public List<VolumeDiscountTier> getTiers() {
        return List.copyOf(tiers);
    }

    public void addTier(VolumeDiscountTier tier) {
        tiers.add(tier);
        tier.setHierarchy(this);
    }

    @Override
    public Integer getLevels() {
        return tiers.size();
    }

    @Override
    public Map<Integer, Integer> getVolumeMap() {
        return sortedTiers().collect(Collectors.toMap(
                VolumeDiscountTier::getLevel,
                VolumeDiscountTier::getMinQuantity,
                (a, b) -> a,
                LinkedHashMap::new));
    }

    @Override
    public Map<Integer, Double> getDiscountMap() {
        return sortedTiers().collect(Collectors.toMap(
                VolumeDiscountTier::getLevel,
                t -> t.getDiscountPercentage().doubleValue(),
                (a, b) -> a,
                LinkedHashMap::new));
    }

    //highest tier whose minimum quantity the order satisfies, or null when none apply
    public VolumeDiscountTier resolveTier(int quantity) {
        return sortedTiers()
                .filter(t -> quantity >= t.getMinQuantity())
                .reduce((first, second) -> second)
                .orElse(null);
    }

    private java.util.stream.Stream<VolumeDiscountTier> sortedTiers() {
        return tiers.stream().sorted(Comparator.comparing(VolumeDiscountTier::getLevel));
    }
}
