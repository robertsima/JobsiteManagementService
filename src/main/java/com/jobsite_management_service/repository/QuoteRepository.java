package com.jobsite_management_service.repository;

import com.jobsite_management_service.abstraction.enums.QuoteStatusType;
import com.jobsite_management_service.model.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long>, JpaSpecificationExecutor<Quote> {

    Optional<Quote> findByQuoteNumber(String quoteNumber);

    List<Quote> findByBusinessId(Long businessId);

    List<Quote> findByJobsiteId(Long jobsiteId);

    List<Quote> findByRequestorId(Long userId);

    List<Quote> findByProviderId(Long providerId);

    List<Quote> findByStatus(QuoteStatusType status);

    List<Quote> findByBusinessIdAndStatus(Long businessId, QuoteStatusType status);

    //quotes that can still be converted into an order
    List<Quote> findByStatusAndValidUntilAfter(QuoteStatusType status, OffsetDateTime at);
}
