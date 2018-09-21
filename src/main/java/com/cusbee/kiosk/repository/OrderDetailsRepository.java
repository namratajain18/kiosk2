package com.cusbee.kiosk.repository;

import com.cusbee.kiosk.entity.OrderDetails;
import com.cusbee.kiosk.entity.Orders;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
	List<OrderDetails> findByOrders(Orders arg0);
}