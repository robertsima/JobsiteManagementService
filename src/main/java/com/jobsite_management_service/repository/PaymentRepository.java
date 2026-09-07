package com.jobsite_management_service.repository;

import com.jobsite_management_service.abstraction.enums.PaymentStatusType;
import com.jobsite_management_service.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    //idempotency guard - a retried capture resolves to the same row
    Optional<Payment> findByTransactionReference(String transactionReference);

    boolean existsByTransactionReference(String transactionReference);

    List<Payment> findByOrderId(Long orderId);

    List<Payment> findByPaymentStatus(PaymentStatusType paymentStatus);

    @Query("""
            SELECT COALESCE(SUM(p.paymentAmount), 0)
            FROM Payment p
            WHERE p.order.id = :orderId
              AND p.paymentStatus = com.jobsite_management_service.abstraction.enums.PaymentStatusType.SUCCESSFUL
            """)
    BigDecimal sumCapturedByOrderId(@Param("orderId") Long orderId);
}
