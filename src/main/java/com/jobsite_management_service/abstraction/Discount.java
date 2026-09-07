package com.jobsite_management_service.abstraction;

public interface Discount {
    //Decorator for payments
    Double calculateDiscount();
    Double applyDiscount();
}
