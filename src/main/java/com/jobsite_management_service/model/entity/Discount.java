package com.jobsite_management_service.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

//decorator class to be used on price
@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Discount implements com.jobsite_management_service.abstraction.Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @Override
    public Double calculateDiscount() {
        return 0.0;
    }

    @Override
    public Double applyDiscount() {
        return 0.0;
    }
}
