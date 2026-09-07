package com.jobsite_management_service.repository;

import com.jobsite_management_service.model.entity.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    List<OrderLineItem> findByOrderId(Long orderId);

    List<OrderLineItem> findByItemId(Long itemId);

    void deleteByOrderId(Long orderId);
}
