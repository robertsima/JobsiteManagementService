package com.jobsite_management_service.abstraction.enums;

public enum PaymentStatusType {
    /** Payment intent created, awaiting customer action/authorization. */
    PENDING,

    /** The payment has successfully cleared and funds are captured. */
    SUCCESSFUL,

    /** The payment failed due to insufficient funds, declines, or typos. */
    FAILED,

    /** The transaction was disputed or reversed by the cardholder or bank. */
    REFUNDED;
}
