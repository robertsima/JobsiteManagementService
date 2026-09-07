package com.jobsite_management_service.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Item implements com.jobsite_management_service.abstraction.Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @Override
    public String getSku() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public double getPrice() {
        return 0;
    }

    @Override
    public String getColor() {
        return "";
    }

    @Override
    public String getMaterial() {
        return "";
    }

    @Override
    public String getDimensions() {
        return "";
    }

    @Override
    public String getDescripton() {
        return "";
    }

    @Override
    public String getWeight() {
        return "";
    }
}
