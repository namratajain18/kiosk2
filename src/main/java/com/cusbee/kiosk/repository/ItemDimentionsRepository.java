package com.cusbee.kiosk.repository;

import com.cusbee.kiosk.entity.Ingredients;
import com.cusbee.kiosk.entity.MenuItems;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemDimentionsRepository extends JpaRepository<Ingredients, Long> {
	List<Ingredients> findByMenuItems(MenuItems arg0);
}