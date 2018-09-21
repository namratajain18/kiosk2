package com.cusbee.kiosk.repository;

import com.cusbee.kiosk.entity.Categories;
import com.cusbee.kiosk.entity.MenuItems;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemsRepository extends JpaRepository<MenuItems, Long> {
	Page<MenuItems> findByCategory(Categories arg0, Pageable arg1);

	List<MenuItems> findByCategoryIn(List<Categories> arg0);

	List<MenuItems> findByCategory(Categories arg0);

	MenuItems findById(Long arg0);
}