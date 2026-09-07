package com.jobsite_management_service.model.entity;

import com.jobsite_management_service.abstraction.enums.PaymentStatusType;
import com.jobsite_management_service.abstraction.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

//idempotent - transaction_reference is the dedupe key for a retried capture
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "order")
@Table(name = "payments")
public class Payment implements com.jobsite_management_service.abstraction.Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    @Column(name = "transaction_reference", nullable = false, unique = true)
    String transactionReference;

    @Column(name = "payment_amount", nullable = false, precision = 19, scale = 4)
    BigDecimal paymentAmount = BigDecimal.ZERO;

    @Column(name = "currency", nullable = false, length = 3)
    String currency = "USD";

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    PaymentStatusType paymentStatus = PaymentStatusType.PENDING;

    @Column(name = "payment_date", nullable = false)
    OffsetDateTime paymentDate;

    @Column(name = "processed_at")
    OffsetDateTime processedAt;

    @Column(name = "failure_reason")
    String failureReason;

    @Column(name = "created_at")
    OffsetDateTime createdAt;

    public boolean isCaptured() {
        return paymentStatus == PaymentStatusType.SUCCESSFUL;
    }
}
