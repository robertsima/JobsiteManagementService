package com.jobsite_management_service.repository;

import com.jobsite_management_service.model.entity.QuoteLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteLineItemRepository extends JpaRepository<QuoteLineItem, Long> {

    List<QuoteLineItem> findByQuoteId(Long quoteId);

    List<QuoteLineItem> findByItemId(Long itemId);

    void deleteByQuoteId(Long quoteId);
}
