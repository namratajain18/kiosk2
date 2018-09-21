package com.cusbee.kiosk.repository;

import com.cusbee.kiosk.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepository extends JpaRepository<Payments, Long> {
}