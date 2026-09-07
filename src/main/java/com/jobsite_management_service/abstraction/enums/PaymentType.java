package com.jobsite_management_service.abstraction.enums;

public enum PaymentType {
    /** Payment Card - Credit and debit cards (Visa, Mastercard, Amex, etc.) */
    PC,

    /** Digital wallets (Apple Pay, Google Pay, Link) */
    DIGITAL_WALLET,

    /** Automated Clearing House - bank transfers (ACH, SEPA, iDEAL) */
    ACH,
}
