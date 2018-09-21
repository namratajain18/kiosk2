package com.cusbee.kiosk.controller.api.admin.location;

import com.cusbee.kiosk.bean.LocationBean;
import com.cusbee.kiosk.controller.ResponseContainer;
import com.cusbee.kiosk.entity.*;
import com.cusbee.kiosk.repository.DevicesRepository;
import com.cusbee.kiosk.repository.LocationRepository;
import com.cusbee.kiosk.repository.MenusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ahorbat on 02.07.17.
 */
@RestController
@RequestMapping(value = "/api/admin/location")
public class LocationController {
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MenusRepository menusRepository;

    @Autowired
    private DevicesRepository devicesRepository;

    private static Ingredients copyIngredient(Ingredients ingredient, MenuItems newItem) {
        System.out.println("Ingredient: " + ingredient.getName());
        final Ingredients newIngredient = new Ingredients();
        newIngredient.setName(ingredient.getName());
        newIngredient.setPrice(ingredient.getPrice());
        newIngredient.setIngredientType(ingredient.getIngredientType());
        newIngredient.setMenuItems(newItem);
        newIngredient.setRemove(ingredient.isRemove());
        return newIngredient;
    }

    private static MenuItems copyMenuItem(MenuItems item, Categories newCategory) {
        final MenuItems newItem = new MenuItems();
        newItem.setName(item.getName());
        newItem.setDescription(item.getDescription());
        newItem.setCategory(newCategory);
        newItem.setCookRules(item.isCookRules());
        newItem.setPrice(item.getPrice());
        newItem.setSpecialities(item.isSpecialities());
        newItem.setTopSeller(item.isTopSeller());
        newItem.setUrl(item.getUrl());
        newItem.setIngredients(item.getIngredients()
            .stream()
            .map(ingredient -> LocationController.copyIngredient(ingredient, newItem))
            .collect(Collectors.toList())
        );
        return newItem;
    }

    private static Categories copyCategory(Categories category, Menus newMenu) {
        final Categories newCategory = new Categories();
        newCategory.setName(category.getName());
        newCategory.setCategoryType(category.getCategoryType());
        newCategory.setDescription(category.getDescription());
        newCategory.setParentCategory(category.getParentCategory());
        newCategory.setMenus(newMenu);
        newCategory.setTax(category.getTax());
        newCategory.setUrl(category.getUrl());
        newCategory.setDisplayPosition(category.getDisplayPosition());
        newCategory.setMenuItems(category.getMenuItems()
            .stream()
            .map(item -> {
                final MenuItems menuItem = LocationController.copyMenuItem(item, newCategory);
                System.out.println("Count: " + menuItem.getIngredients().size());
                return menuItem;
            })
            .collect(Collectors.toSet())
        );
        return newCategory;
    }

    private static Menus copyMenu(Menus menu, Locations newLocation) {
        final Menus newMenu = new Menus();
        newMenu.setName("Usual menu - " + newLocation.getName());
        newMenu.setDescription(newLocation.getName());
        newMenu.setTax(menu.getTax());
        newMenu.setPublish(menu.isPublish());
        newMenu.setLocations(newLocation);
        newMenu.setCategories(menu.getCategories()
            .stream()
            .map(category -> LocationController.copyCategory(category, newMenu))
            .collect(Collectors.toSet())
        );
        return newMenu;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseContainer<List<LocationBean>> all() {
        ResponseContainer<List<LocationBean>> response = new ResponseContainer<>();
        response.setData(LocationBean.toBeanIDNList(locationRepository.findAll()));
        return response;
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public ResponseContainer<Locations> addLocation (@RequestBody Locations location) {
        ResponseContainer<Locations> response = new ResponseContainer<>();
        locationRepository.save(location);
        response.setCode(200);
        response.setData(location);
        response.setMessage("Location with name: " + location.getName() + " sucsessfully created");
        return response;
    }

    @RequestMapping(value = "/{locationId}", method = RequestMethod.GET)
    public ResponseContainer<LocationBean> byLocationId(@PathVariable("locationId") Long locationId) {
        ResponseContainer<LocationBean> response = new ResponseContainer<>();
        Locations location = locationRepository.findById(locationId);
        List<Menus> menus = menusRepository.findByLocations(location);
        List<Devices> devices = devicesRepository.findByLocations(location);
        response.setData(LocationBean.toBean(location,devices,menus));
        return response;
    }

    @RequestMapping(value = "/menu/{menuId}", method = RequestMethod.GET)
    public ResponseContainer<List<Devices>> byMenuId(@PathVariable("menuId") Long menuId) {
        ResponseContainer<List<Devices>> response = new ResponseContainer<>();
        Menus menus = menusRepository.findById(menuId);
        List<Devices> devices = devicesRepository.findByMenus(menus);
        response.setCode(200);
        response.setData(devices);
        response.setMessage("Success");
        return response;
    }

    @RequestMapping(value = "/duplicate/{locationId}", method = RequestMethod.GET)
    @Transactional
    public ResponseContainer<LocationBean> duplicateByLocationId(@PathVariable("locationId") Long locationId,
                                                                 @RequestParam(name = "name") String newName) {
        final ResponseContainer<LocationBean> response = new ResponseContainer<>();

        final Locations location = locationRepository.findById(locationId);
        final Locations newLocation = new Locations();
        newLocation.setName(newName);
        newLocation.setTables(location.getTables());
        newLocation.setMenus(location.getMenus()
            .stream()
            .map(menu -> LocationController.copyMenu(menu, newLocation))
            .collect(Collectors.toSet())
        );
        locationRepository.save(newLocation);

        response.setCode(200);
        response.setData(LocationBean.toBean(newLocation));
        response.setMessage("Success");
        return response;
    }
}
