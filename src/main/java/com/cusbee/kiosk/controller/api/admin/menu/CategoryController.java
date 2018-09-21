package com.cusbee.kiosk.controller.api.admin.menu;

import com.cusbee.kiosk.bean.CategoryBean;
import com.cusbee.kiosk.bean.MenuItemBean;
import com.cusbee.kiosk.controller.ResponseContainer;
import com.cusbee.kiosk.controller.api.customer.CustomerOrderController;
import com.cusbee.kiosk.entity.*;
import com.cusbee.kiosk.repository.*;
import com.google.common.collect.Lists;
import org.hibernate.collection.internal.PersistentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by ahorbat on 07.02.17.
 */
@RestController
@RequestMapping(value = "/api")
public class CategoryController {
	private final static Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private MenuItemsRepository menuItemsRepository;

    @Autowired
    private MenusRepository menusRepository;

    @Autowired
    private DevicesRepository devicesRepository;

    @Autowired
    private LocationRepository locationRepository;

    @RequestMapping(value = {"/mobile/category/all"}, method = RequestMethod.GET)
    public ResponseContainer<List<CategoryBean>> mobileCatList(@RequestParam(value = "sysId", required = false) Long sysId,
                                                               @RequestParam(value = "locId", required = false) Long locId) {
        return catList(sysId,locId);
    }

    @Transactional
    @RequestMapping(value = "/mobile/category/{id}", method = RequestMethod.GET)
    public ResponseContainer<CategoryBean> mobileCatWithItems(@PathVariable("id") long id) {
        return catWithItems(id, false);
    }

    @Transactional
    @RequestMapping(value = "/admin/category/{menuId}", method = RequestMethod.POST)
    public ResponseContainer<CategoryBean> createCategory(@RequestBody Categories categories, @PathVariable("menuId") Long menuId) {
        ResponseContainer<CategoryBean> response = new ResponseContainer<>();
        Menus menu = null;
        if (menuId != null) {
            menu = menusRepository.findById(menuId);
            if (menu == null) {
                response.setCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Menu with id: " + menuId + " not found");
                return response;
            }
        }

        Categories existingCat = categoriesRepository.findByName(categories.getName());
        if (existingCat == null) {
            categories.setMenus(menu);
            categoriesRepository.save(categories);
            response.setData(CategoryBean.toBean(categories));
            response.setCode(HttpStatus.OK.value());
            response.setMessage("Category successfully created");
            return response;
        } else {
            response.setData(CategoryBean.toBean(existingCat));
            response.setCode(HttpStatus.ALREADY_REPORTED.value());
            response.setMessage("Category with such name already exist");
            return response;
        }
    }

    @RequestMapping(value = "/admin/category/by-menu/{menuId}", method = RequestMethod.GET)
    public ResponseContainer<List<CategoryBean>> catByMenu(@PathVariable("menuId") Long menuId) {
        ResponseContainer<List<CategoryBean>> response = new ResponseContainer<>();
        Menus menu = menusRepository.findById(menuId);
        if (menu == null) {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Menu with id: " + menuId + " not found");
            return response;
        } else {
            response.setData(CategoryBean.toBeanList(categoriesRepository.findByMenusAndParentCategoryIsNull(menu), menu.getTax()));
            response.setMessage("Success");
            response.setCode(200);
            return response;
        }
    }

    //todo: move out from admin
    @Transactional
    @RequestMapping(value = {"/admin/category/all"}, method = RequestMethod.GET)
    public ResponseContainer<List<CategoryBean>> catList(
            @RequestParam(value = "sysId", required = false) Long sysId,
            @RequestParam(value = "locId", required = false) Long locId) {
        ResponseContainer<List<CategoryBean>> response = new ResponseContainer<>();
        List<Menus> activeMenus = menusRepository.findByPublish(true);

        if(locId !=null){
        	LOGGER.info("locId: ", locId);
			System.out.println("locId: "+ locId);
			Long val = 2L;
            Locations location = locationRepository.findById(locId);
            if (location == null) {
                response.setCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Location does not exist");
                return response;
            }
        	LOGGER.info("location: ", location);
			System.out.println("location: "+ location.getId());
            List<Menus> menu = menusRepository.findByLocations(location);
        	LOGGER.info("menu.get(0): ", menu.get(0));
			System.out.println("menu.get(0): "+ menu.get(0).getId());
			
            List<Categories> categories = categoriesRepository.findByMenus(menu.get(0));
            System.out.println("test1");
            List<Categories> sortList = new LinkedList<Categories>(categories);
            System.out.println("test2");
            Comparator<Categories>comparator = Comparator.comparing(Categories::getDisplayPosition,Comparator.nullsFirst(Comparator.naturalOrder()));
            Collections.sort(sortList,comparator);
        	LOGGER.info("activeMenus.get(0): ", activeMenus.get(0));
			System.out.println("activeMenus.get(0): "+ activeMenus.get(0));
            response.setData(CategoryBean.toBeanList(sortList, activeMenus.get(0).getTax()));
            response.setCode(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.name());
            return response;
        }

        if (sysId == null) {
            if (activeMenus.size() == 0) {
                response.setCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No active menu for now");
                return response;
            } else {
                List<Categories> list = categoriesRepository.findByMenusAndParentCategoryIsNull(new PageRequest(0, 20), activeMenus.get(0)).getContent();
                List<Categories> sortList = new LinkedList<Categories>(list);
                Comparator<Categories>comparator = Comparator.comparing(Categories::getDisplayPosition,Comparator.nullsFirst(Comparator.naturalOrder()));
                Collections.sort(sortList,comparator);

                response.setData(CategoryBean.toBeanList(sortList, activeMenus.get(0).getTax()));
                response.setCode(HttpStatus.OK.value());
                response.setMessage(HttpStatus.OK.name());
                return response;
            }
        } else {
            Devices device = devicesRepository.findBySysId(sysId);
            Set<Devices> d = new HashSet<>();
            d.add(device);
            Menus m = menusRepository.findByDevices(d);
            response.setData(CategoryBean.toBeanList(categoriesRepository.findByMenusAndParentCategoryIsNull(new PageRequest(0, 20), m).getContent(), m.getTax()));
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("WARNING: Using url with out sys ID");
            return response;
        }


    }

    @Transactional
    @RequestMapping(value = "/admin/category/{id}", method = RequestMethod.GET)
    public ResponseContainer<CategoryBean> catWithItems(
        @PathVariable("id") long id,
        @RequestParam(value = "ingradients", defaultValue = "false") boolean withIngradients
    ) {
        ResponseContainer<CategoryBean> response = new ResponseContainer<>();
        Categories category = categoriesRepository.findById(id);
        if (category != null) {
            List<Categories> categories = flattenSubCategories(category);
            List<MenuItems> menuItems = menuItemsRepository.findByCategoryIn(categories);
            if (withIngradients) menuItems.forEach(MenuItems::getIngredients);

            CategoryBean data = CategoryBean.toBean(category);

            response.setData(applyMenuItemsToCategories(data, menuItems, withIngradients));
            response.setCode(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.name());

            return response;
        } else {
            response.setMessage("Category is not found in the system");
            response.setCode(HttpStatus.NO_CONTENT.value());
            return response;
        }
    }

    @RequestMapping(value = "/admin/category", method = RequestMethod.PUT)
    public ResponseContainer<CategoryBean> updateCategory(@RequestParam(value = "catName", required = true) String catName, @RequestBody Categories categories) {
        Categories existingCat = categoriesRepository.findByName(catName);
        ResponseContainer<CategoryBean> response = new ResponseContainer<>();
        if (existingCat != null) {
            existingCat.setName(categories.getName());
            existingCat.setDescription(categories.getDescription());
            existingCat.setUrl(categories.getUrl());
          //  existingCat.setNewUrl(categories.getNewUrl());
            categoriesRepository.save(existingCat);
            response.setData(CategoryBean.toBean(existingCat));
            response.setMessage("Category updated successfully");
            response.setCode(HttpStatus.OK.value());
            return response;
        } else {
            response.setMessage("Category is not found in the system");
            response.setCode(HttpStatus.NO_CONTENT.value());
            return response;
        }
    }

    @RequestMapping(value = "/sub-category", method = RequestMethod.PUT)
    public ResponseContainer<CategoryBean> updateCategory(
        @RequestParam(value = "parent") long parentId,
        @RequestBody Categories subCategory
    ) {
        ResponseContainer<CategoryBean> response = new ResponseContainer<>();
        try {
            Categories parentCategory = categoriesRepository.findById(parentId);
            Categories result = categoriesRepository.addSubcategory(parentCategory, subCategory);
            response.setData(CategoryBean.toBean(result));
            response.setMessage("Sub-Category added successfully");
            response.setCode(HttpStatus.OK.value());
        } catch (Exception ex) {
            response.setMessage(ex.getMessage());
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    @RequestMapping(value = "/admin/category/{id}", method = RequestMethod.DELETE)
    public ResponseContainer<String> deleteCategory(@PathVariable("id") long id) {
        ResponseContainer<String> response = new ResponseContainer<>();
        Categories existingCat = categoriesRepository.findById(id);
        if (existingCat != null) {
            categoriesRepository.delete(existingCat);
            response.setMessage("Category removed successfully");
            response.setCode(HttpStatus.OK.value());
            return response;
        } else {
            response.setMessage("Category is not found in the system");
            response.setCode(HttpStatus.NO_CONTENT.value());
            return response;
        }
    }

    @RequestMapping(value = "/sub-category/{id}/change/order/to/{order}", method = RequestMethod.GET)
    public ResponseContainer<Void> changeOrderOfSubCategory(
        @PathVariable("id") long id,
        @PathVariable("order") int order
    ) {
        ResponseContainer<Void> response = new ResponseContainer<>();
        try {
            categoriesRepository.changeOrder(id, order);
            response.setMessage("Category moved successfully");
            response.setCode(HttpStatus.OK.value());

        } catch (Exception ex) {
            response.setMessage(ex.getMessage());
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    @RequestMapping(value = "/sub-category/move/from/{oldParent}/to/{newParent}", method = RequestMethod.POST)
    public ResponseContainer<Void> moveSubCategory(
        @RequestBody Categories category,
        @PathVariable("newParent") long newParentId,
        @PathVariable("oldParent") long oldParentId
    ) {
        Categories newParent = categoriesRepository.findById(newParentId);
        Categories oldParent = categoriesRepository.findById(oldParentId);

        ResponseContainer<Void> response = new ResponseContainer<>();
        try {
            categoriesRepository.moveSubCategory(category, newParent, oldParent);
            response.setMessage("Category moved successfully");
            response.setCode(HttpStatus.OK.value());

        } catch (Exception ex) {
            response.setMessage(ex.getMessage());
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    @RequestMapping(value = "/sub-category/{id}", method = RequestMethod.DELETE)
    public ResponseContainer<String> deleteSubCategory(@PathVariable("id") long id) {
        ResponseContainer<String> response = new ResponseContainer<>();
        Categories existingCat = categoriesRepository.findById(id);
        if (existingCat != null) {
            categoriesRepository.deleteSubCategory(existingCat);
            response.setMessage("Category removed successfully");
            response.setCode(HttpStatus.OK.value());
            return response;
        } else {
            response.setMessage("Category is not found in the system");
            response.setCode(HttpStatus.NO_CONTENT.value());
            return response;
        }
    }

    private CategoryBean applyMenuItemsToCategories(CategoryBean bean, List<MenuItems> items, boolean withIngradients) {
        List<MenuItems> itemList = items != null && !items.isEmpty()
            ? items
            : Lists.newArrayList();

        bean.getSubCategories().forEach(subCategory -> applyMenuItemsToCategories(subCategory, items, withIngradients));

        Function<Long, List<MenuItems>> getMenuItemsById = id -> itemList.stream()
            .filter(item -> item.getCategory().getId().equals(id))
            .collect(Collectors.toList());

        if (withIngradients) {
            List<MenuItemBean> menuItemBeans = getMenuItemsById.apply(bean.getId())
                .stream()
                .map(menu -> MenuItemBean.toBeanWithIngredients(menu, Lists.newArrayList(menu.getIngredients())))
                .collect(Collectors.toList());
            bean.setMenuItems(menuItemBeans);
        } else {
            bean.setMenuItems(getMenuItemsById.apply(bean.getId()));
        }
        return bean;
    }

    private List<Categories> flattenSubCategories(Categories categories) {
        return flattenSubCategories(categories, new ArrayList<>());
    }

    private List<Categories> flattenSubCategories(Categories categories, List<Categories> result) {
        List<Categories> subCategories = categories.getSubcategories();
        if (subCategories != null && (((PersistentList) subCategories).wasInitialized() && !subCategories.isEmpty())) {
            subCategories.forEach(cat -> flattenSubCategories(cat, result));
        }
        result.add(categories);
        return result;
    }
}
