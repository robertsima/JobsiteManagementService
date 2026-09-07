package com.jobsite_management_service.abstraction;

import com.jobsite_management_service.abstraction.enums.PaymentStatusType;
import com.jobsite_management_service.abstraction.enums.PaymentType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

//Idempotent
public interface Payment {
    BigDecimal getPaymentAmount();
    OffsetDateTime getPaymentDate();
    PaymentType getPaymentType();
    PaymentStatusType getPaymentStatus();
}
