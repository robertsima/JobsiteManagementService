package com.jobsite_management_service.model.entity;

import com.jobsite_management_service.abstraction.Request;
import com.jobsite_management_service.abstraction.enums.QuoteStatusType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

//a quote is one request against exactly one provider; it can be converted into an order
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"business", "jobsite", "provider", "requestor", "lineItems"})
@Table(name = "quotes")
public class Quote implements Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @Column(name = "quote_number", nullable = false, unique = true)
    String quoteNumber;

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

    //typed internally, exposed as a String through Request so Quote and Order stay substitutable
    @Getter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    QuoteStatusType status = QuoteStatusType.DRAFT;

    @Column(name = "currency", nullable = false, length = 3)
    String currency = "USD";

    @Column(name = "subtotal", nullable = false, precision = 19, scale = 4)
    BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "discount_total", nullable = false, precision = 19, scale = 4)
    BigDecimal discountTotal = BigDecimal.ZERO;

    @Column(name = "tax_total", nullable = false, precision = 19, scale = 4)
    BigDecimal taxTotal = BigDecimal.ZERO;

    @Column(name = "total", nullable = false, precision = 19, scale = 4)
    BigDecimal total = BigDecimal.ZERO;

    @Column(name = "notes", length = 2048)
    String notes;

    @Column(name = "valid_until")
    OffsetDateTime validUntil;

    @Column(name = "created_at")
    OffsetDateTime createdAt;

    @Column(name = "updated_at")
    OffsetDateTime updatedAt;

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "quote", cascade = CascadeType.ALL, orphanRemoval = true)
    List<QuoteLineItem> lineItems = new ArrayList<>();

    public List<QuoteLineItem> getLineItems() {
        return List.copyOf(lineItems);
    }

    public void addLineItem(QuoteLineItem lineItem) {
        lineItems.add(lineItem);
        lineItem.setQuote(this);
    }

    public QuoteStatusType getQuoteStatus() {
        return status;
    }

    public void setQuoteStatus(QuoteStatusType status) {
        this.status = status;
    }

    @Override
    public String getStatus() {
        return status == null ? null : status.name();
    }

    //iterate the cart to recompute the running totals
    public void recalculateTotals() {
        subtotal = lineItems.stream()
                .map(QuoteLineItem::getExtendedPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        discountTotal = lineItems.stream()
                .map(QuoteLineItem::getDiscountAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        total = subtotal.subtract(discountTotal).add(taxTotal);
    }
}
