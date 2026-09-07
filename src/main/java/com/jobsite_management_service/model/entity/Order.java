package com.jobsite_management_service.model.entity;

import com.jobsite_management_service.abstraction.Request;
import com.jobsite_management_service.abstraction.enums.OrderStatusType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

//an order targets exactly one provider and may or may not have come from a quote
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"business", "jobsite", "provider", "requestor", "quote", "lineItems", "payments"})
@Table(name = "orders")
public class Order implements Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @Column(name = "order_number", nullable = false, unique = true)
    String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobsite_id", nullable = false)
    Jobsite jobsite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User requestor;

    //nullable - orders need a site and user, but do not require a quote
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id")
    Quote quote;

    @Getter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    OrderStatusType status = OrderStatusType.DRAFT;

    @Column(name = "currency", nullable = false, length = 3)
    String currency = "USD";

    @Column(name = "subtotal", nullable = false, precision = 19, scale = 4)
    BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "discount_total", nullable = false, precision = 19, scale = 4)
    BigDecimal discountTotal = BigDecimal.ZERO;

    @Column(name = "tax_total", nullable = false, precision = 19, scale = 4)
    BigDecimal taxTotal = BigDecimal.ZERO;

    @Column(name = "shipping_total", nullable = false, precision = 19, scale = 4)
    BigDecimal shippingTotal = BigDecimal.ZERO;

    @Column(name = "total", nullable = false, precision = 19, scale = 4)
    BigDecimal total = BigDecimal.ZERO;

    @Column(name = "delivery_address")
    String deliveryAddress;

    @Column(name = "notes", length = 2048)
    String notes;

    @Column(name = "submitted_at")
    OffsetDateTime submittedAt;

    @Column(name = "expected_delivery")
    OffsetDateTime expectedDelivery;

    @Column(name = "delivered_at")
    OffsetDateTime deliveredAt;

    @Column(name = "created_at")
    OffsetDateTime createdAt;

    @Column(name = "updated_at")
    OffsetDateTime updatedAt;

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderLineItem> lineItems = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Payment> payments = new ArrayList<>();

    public List<OrderLineItem> getLineItems() {
        return List.copyOf(lineItems);
    }

    public List<Payment> getPayments() {
        return List.copyOf(payments);
    }

    public void addLineItem(OrderLineItem lineItem) {
        lineItems.add(lineItem);
        lineItem.setOrder(this);
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setOrder(this);
    }

    public OrderStatusType getOrderStatus() {
        return status;
    }

    public void setOrderStatus(OrderStatusType status) {
        this.status = status;
    }

    @Override
    public String getStatus() {
        return status == null ? null : status.name();
    }

    public void recalculateTotals() {
        subtotal = lineItems.stream()
                .map(OrderLineItem::getExtendedPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        discountTotal = lineItems.stream()
                .map(OrderLineItem::getDiscountAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        total = subtotal.subtract(discountTotal).add(taxTotal).add(shippingTotal);
    }

    //sum of every successfully captured payment on this order
    public BigDecimal getAmountPaid() {
        return payments.stream()
                .filter(Payment::isCaptured)
                .map(Payment::getPaymentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getBalanceDue() {
        return total.subtract(getAmountPaid());
    }
}
