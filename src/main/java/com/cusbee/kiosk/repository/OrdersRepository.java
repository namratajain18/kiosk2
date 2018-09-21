package com.cusbee.kiosk.repository;

import com.cusbee.kiosk.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by ahorbat on 12.02.17.
 */
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    Orders findById(Long id);
    List<Orders> findTop5ByPhoneOrderByIdDesc(String phone);

    @Query(value = "SELECT o FROM Orders o WHERE o.name LIKE :query OR o.phone LIKE :query")
    Page<Orders> findByNameOrPhoneLike(@Param("query") String query, Pageable pageable);

    Page<Orders> findByIdLike(Long id, Pageable pageable);

    Page<Orders> findByOrderDateBetween(Date from, Date to, Pageable pageable);

    List<Orders> findByOrderDateBetween(Date from, Date to);

    Page<Orders> findAll(Pageable pageable);
}
