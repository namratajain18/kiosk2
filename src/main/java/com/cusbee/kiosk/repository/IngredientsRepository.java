package com.cusbee.kiosk.repository;

import com.cusbee.kiosk.entity.Ingredients;
import com.cusbee.kiosk.entity.MenuItems;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientsRepository extends JpaRepository<Ingredients, Long> {
	Page<Ingredients> findByMenuItemsOrderByNameAsc(MenuItems arg0, Pageable arg1);

	List<Ingredients> findByMenuItems(MenuItems arg0);
}