package com.jobsite_management_service.model.entity;

import com.jobsite_management_service.abstraction.enums.PaymentStatusType;
import com.jobsite_management_service.abstraction.enums.PaymentType;
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
public class Payment implements com.jobsite_management_service.abstraction.Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @Override
    public Double getPaymentAmount() {
        return 0.0;
    }

    @Override
    public Double getPaymentDate() {
        return 0.0;
    }

    @Override
    public PaymentType getPaymentType() {
        return null;
    }

    @Override
    public PaymentStatusType getPaymentStatus() {
        return null;
    }
}
