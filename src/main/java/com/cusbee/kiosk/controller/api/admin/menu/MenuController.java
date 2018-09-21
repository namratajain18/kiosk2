package com.cusbee.kiosk.controller.api.admin.menu;

import com.cusbee.kiosk.bean.MenuBean;
import com.cusbee.kiosk.controller.ResponseContainer;
import com.cusbee.kiosk.entity.Devices;
import com.cusbee.kiosk.entity.Locations;
import com.cusbee.kiosk.entity.Menus;
import com.cusbee.kiosk.repository.DevicesRepository;
import com.cusbee.kiosk.repository.LocationRepository;
import com.cusbee.kiosk.repository.MenusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ahorbat on 08.06.17.
 */
@RestController
@RequestMapping(value = "/api/admin/menu")
public class MenuController {

    @Autowired
    private MenusRepository menusRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private DevicesRepository devicesRepository;

    @RequestMapping(value = "/{locationId}", method = RequestMethod.POST)
    public ResponseContainer<Menus> createMenu(@RequestBody Menus menus, @PathVariable("locationId") Long locationId) {
        ResponseContainer<Menus> response = new ResponseContainer<>();
        Locations location = locationRepository.findById(locationId);
        Menus existingMenu = menusRepository.findByName(menus.getName());
        if (existingMenu == null) {
            menus.setLocations(location);

            response.setData(menusRepository.save(menus));
            response.setCode(HttpStatus.OK.value());
            response.setMessage("Menu successfully created");
            return response;
        } else {
            response.setData(existingMenu);
            response.setCode(HttpStatus.ALREADY_REPORTED.value());
            response.setMessage("Menu with such name already exist");
            return response;
        }
    }

    @RequestMapping(value = "/assign-location/{locationId}/{menuId}", method = RequestMethod.POST)
    public ResponseContainer<Menus> assign(@PathVariable("locationId") Long locationId,
                                           @PathVariable("menuId") Long menuId) {
        ResponseContainer<Menus> response = new ResponseContainer<>();
        Menus menus = menusRepository.findById(menuId);
        Locations locations = locationRepository.findById(locationId);
        menus.setLocations(locations);
        menusRepository.save(menus);
        response.setCode(200);
        response.setData(menus);
        response.setMessage("Success");

        return response;
    }

    @RequestMapping(value = "/publish/{menuId}", method = RequestMethod.GET)
    public ResponseContainer<Menus> publish(@PathVariable("menuId") Long menuId) {
        ResponseContainer<Menus> response = new ResponseContainer<>();
        Menus menu = menusRepository.findById(menuId);

        if (menu == null) {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(String.format("Menu with id: %d not found", menuId));
            return response;
        }
        List<Devices> assignedDevices = devicesRepository.findByMenus(menu);
        if (!assignedDevices.isEmpty()) {
            menu.setPublish(true);
            response.setCode(200);
            response.setData(menusRepository.save(menu));
            response.setMessage(String.format("Menu with id: %d published", menuId));
            return response;
        } else {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(String.format("Menu with id: %d has no assigned devices", menuId));
            return response;
        }
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseContainer<List<Menus>> catList() {
        ResponseContainer<List<Menus>> response = new ResponseContainer<>();
        response.setData(menusRepository.findAll(new PageRequest(0, 10)).getContent());
        response.setCode(HttpStatus.OK.value());
        response.setMessage(HttpStatus.OK.name());
        return response;
    }
}
