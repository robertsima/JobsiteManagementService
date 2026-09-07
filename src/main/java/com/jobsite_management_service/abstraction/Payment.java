package com.jobsite_management_service.abstraction;

import com.jobsite_management_service.abstraction.enums.PaymentStatusType;
import com.jobsite_management_service.abstraction.enums.PaymentType;

//Idempotent
public interface Payment {
    Double getPaymentAmount();
    Double getPaymentDate();
    PaymentType getPaymentType();
    PaymentStatusType getPaymentStatus();
}
