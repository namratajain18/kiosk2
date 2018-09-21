package com.cusbee.kiosk.repository;

import com.cusbee.kiosk.entity.IngredientConstraints;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientConstraintRepository extends JpaRepository<IngredientConstraints, Long> {
	IngredientConstraints findByIngredientId(Long arg0);
}