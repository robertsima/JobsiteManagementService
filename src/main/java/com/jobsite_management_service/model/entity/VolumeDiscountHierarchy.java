package com.jobsite_management_service.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Map;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class VolumeDiscountHierarchy implements com.jobsite_management_service.abstraction.VolumeDiscountHierarchy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @Override
    public Integer getLevels() {
        return 0;
    }

    @Override
    public Map<Integer, Integer> getVolumeMap() {
        return Map.of();
    }

    @Override
    public Map<Integer, Double> getDiscountMap() {
        return Map.of();
    }
}
