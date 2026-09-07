package com.jobsite_management_service.abstraction.enums;

public enum DiscountType {
    /** Takes a percentage off the base price, e.g. 12.5 -> 12.5% off. */
    PERCENTAGE,

    /** Takes a flat currency amount off the base price. */
    FIXED_AMOUNT
}
