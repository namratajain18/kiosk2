package com.cusbee.kiosk.repository;

import com.cusbee.kiosk.entity.Locations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Locations, Long> {
	Locations findById(Long arg0);
}