package com.cusbee.kiosk.repository;

import com.cusbee.kiosk.entity.Devices;
import com.cusbee.kiosk.entity.Locations;
import com.cusbee.kiosk.entity.Menus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;


/**
 * Created by ahorbat on 08.06.17.
 */
public interface MenusRepository extends JpaRepository<Menus, Long> {

    Menus findByName(String name);

    Menus findById(Long id);

    Menus findByDevices(Set<Devices> devices);

    List<Menus> findByPublish(Boolean isPublish);

    List<Menus> findByLocations(Locations locations);

    Page<Menus> findAll(Pageable pageable);
}
