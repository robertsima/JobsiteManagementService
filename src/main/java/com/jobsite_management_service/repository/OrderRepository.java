package com.jobsite_management_service.repository;

import com.jobsite_management_service.abstraction.enums.OrderStatusType;
import com.jobsite_management_service.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByBusinessId(Long businessId);

    List<Order> findByJobsiteId(Long jobsiteId);

    List<Order> findByRequestorId(Long userId);

    List<Order> findByProviderId(Long providerId);

    List<Order> findByStatus(OrderStatusType status);

    List<Order> findByBusinessIdAndStatus(Long businessId, OrderStatusType status);

    //the order a quote was converted into, if any
    Optional<Order> findByQuoteId(Long quoteId);

    //reporting window
    List<Order> findBySubmittedAtBetween(OffsetDateTime from, OffsetDateTime to);
}
