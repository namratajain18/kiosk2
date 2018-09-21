package com.cusbee.kiosk.repository;

import com.cusbee.kiosk.entity.Devices;
import com.cusbee.kiosk.entity.Locations;
import com.cusbee.kiosk.entity.Menus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevicesRepository extends JpaRepository<Devices, Long> {
	Devices findBySysId(Long arg0);

	List<Devices> findByLocationsNotNull();

	List<Devices> findByMenusNotNull();

	List<Devices> findByMenusIsNullOrLocationsIsNull();

	List<Devices> findByLocations(Locations arg0);

	List<Devices> findByMenus(Menus arg0);
}