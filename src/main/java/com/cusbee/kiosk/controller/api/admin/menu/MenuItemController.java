package com.cusbee.kiosk.controller.api.admin.menu;

import com.cusbee.kiosk.bean.MenuItemBean;
import com.cusbee.kiosk.bean.OrderDetailsBean;
import com.cusbee.kiosk.controller.PageContainer;
import com.cusbee.kiosk.controller.ResponseContainer;
import com.cusbee.kiosk.controller.api.customer.CustomerOrderController;
import com.cusbee.kiosk.entity.*;
import com.cusbee.kiosk.entity.Ingredients;
import com.cusbee.kiosk.entity.MenuItems;
import com.cusbee.kiosk.entity.OrderDetails;
import com.cusbee.kiosk.enums.KitchenStatus;
import com.cusbee.kiosk.repository.*;
import com.cusbee.kiosk.repository.IngredientsRepository;
import com.cusbee.kiosk.repository.MenuItemsRepository;
import com.cusbee.kiosk.repository.OrderDetailsRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ahorbat on 10.02.17.
 */
@RestController
@RequestMapping(value = "/api")
public class MenuItemController {

	private final static Logger LOGGER = LoggerFactory.getLogger(MenuItemController.class);
	@Autowired
	private MenuItemsRepository menuItemsRepository;

	@Autowired
	private CategoriesRepository categoriesRepository;

	@Autowired
	private IngredientsRepository ingredientsRepository;

	@Autowired
	private OrderDetailsRepository orderDetailsRepository;

	@RequestMapping(value = "/mobile/menu-item/{menuItemId}", method = RequestMethod.GET)
	public ResponseContainer<MenuItemBean> mobileMenuWithIngredients(@PathVariable("menuItemId") long menuItemId) {
		return menuWithIngredients(menuItemId);
	}

	@RequestMapping(value = "/admin/menu-item/{categoryId}", method = RequestMethod.POST)
	public ResponseContainer<MenuItemBean> createMenuItem(@PathVariable("categoryId") long categoryId,
			@RequestBody MenuItems items) {
		ResponseContainer<MenuItemBean> response = new ResponseContainer<>();
		Categories category = categoriesRepository.findById(categoryId);
		if (category == null) {
			response.setMessage("Category not found");
			response.setCode(HttpStatus.NO_CONTENT.value());
			return response;
		} else {
			items.setCategory(category);
			menuItemsRepository.save(items);
			response.setData(MenuItemBean.toBean(items));
			response.setCode(HttpStatus.OK.value());
			response.setMessage("Menu item successfully added to category " + category.getName());
			return response;
		}
	}

	@RequestMapping(value = "/admin/menu-item/{menuItemId}", method = RequestMethod.PUT)
	public ResponseContainer<MenuItemBean> updateMenuItem(@PathVariable("menuItemId") long menuItemId,
			@RequestBody MenuItems items) {
		ResponseContainer<MenuItemBean> response = new ResponseContainer<>();
		MenuItems existingItem = menuItemsRepository.findById(menuItemId);
		if (existingItem != null) {
			existingItem.setUrl(items.getUrl());
			existingItem.setName(items.getName());
			existingItem.setPrice(items.getPrice());
			existingItem.setDescription(items.getDescription());
			existingItem.setSpecialities(items.isSpecialities());
			existingItem.setTopSeller(items.isTopSeller());
			menuItemsRepository.save(existingItem);
			response.setData(MenuItemBean.toBean(existingItem));
			response.setCode(HttpStatus.OK.value());
			response.setMessage("Menu item successfully updated");
			return response;
		} else {
			response.setMessage("Menu item not found");
			response.setCode(HttpStatus.NO_CONTENT.value());
			return response;
		}
	}

	@RequestMapping(value = "/admin/menu-item/{menuItemId}", method = RequestMethod.GET)
	public ResponseContainer<MenuItemBean> menuWithIngredients(@PathVariable("menuItemId") long menuItemId) {
		ResponseContainer<MenuItemBean> response = new ResponseContainer<>();

		MenuItems menuItems = menuItemsRepository.findById(menuItemId);
		LOGGER.info("Menu Items: " + menuItems);

		if (menuItems != null) {
			LOGGER.info("Menu Items: " + menuItems.getName());
			Page<Ingredients> ingredients = ingredientsRepository.findByMenuItemsOrderByNameAsc(menuItems,
					new PageRequest(0, 30));
			if (ingredients != null) {
				LOGGER.info("ingredients: " + ingredients.getSize());

			}
			else
				LOGGER.info("ingredients can be Null: " + ingredients);
			List<Ingredients> ingredientList = ingredients.getContent();
			if (ingredientList != null && !ingredientList.isEmpty()) {
				LOGGER.info("ingredientList: " + ingredients.getContent());

			}
			else
				LOGGER.info("ingredientList can be Null or empty: " + ingredientList);
		
			response.setData(MenuItemBean.toBeanWithIngredients(menuItems, ingredientList));
			response.setCode(HttpStatus.OK.value());
			response.setMessage(HttpStatus.OK.name());
			response.setPageContainer(new PageContainer(ingredients.getTotalElements(), ingredients.getTotalPages()));
			return response;
		} else {
			response.setMessage("Menu item is not found in the system");
			response.setCode(HttpStatus.NO_CONTENT.value());
			return response;
		}
	}

	@RequestMapping(value = "/admin/menu-item/cooking-status/{itemDetailsId}", method = RequestMethod.POST)
	public ResponseContainer<OrderDetailsBean> changeCookingStatus(@PathVariable("itemDetailsId") long itemDetailsId,
			@RequestParam(value = "status") String status) {
		ResponseContainer<OrderDetailsBean> response = new ResponseContainer<>();
		try {
			KitchenStatus kitchenStatus = KitchenStatus.valueOf(status);

			OrderDetails orderDetail = orderDetailsRepository.findOne(itemDetailsId);
			if (orderDetail != null) {
				orderDetail.setItemStatus(kitchenStatus);
				orderDetailsRepository.save(orderDetail);
				response.setData(OrderDetailsBean.toOrderDetailsBean(orderDetail));
				response.setMessage("Item " + orderDetail.getTitle() + " changed status to "
						+ orderDetail.getItemStatus().getDescription());
				response.setCode(HttpStatus.OK.value());
				return response;
			} else {
				response.setMessage("Order detail item with id: " + itemDetailsId + " was not found in the system");
				response.setCode(HttpStatus.NO_CONTENT.value());
				return response;
			}

		} catch (Exception e) {
			response.setMessage("Kitchen status: " + status + " is incorrect");
			response.setCode(HttpStatus.NOT_ACCEPTABLE.value());
			return response;
		}
	}

	@RequestMapping(value = "/admin/menu-item/{menuItemId}", method = RequestMethod.DELETE)
	public ResponseContainer<String> removeMenueItemWithIngredients(@PathVariable("menuItemId") long menuItemId) {
		ResponseContainer<String> response = new ResponseContainer<>();
		MenuItems existItem = menuItemsRepository.findById(menuItemId);
		if (existItem != null) {
			List<Ingredients> existIngredients = ingredientsRepository.findByMenuItems(existItem);
			if (existIngredients != null) {
				ingredientsRepository.delete(existIngredients);
				menuItemsRepository.delete(existItem);
				response.setCode(HttpStatus.OK.value());
				response.setMessage("Successfully removed menu item");
				return response;
			} else {
				menuItemsRepository.delete(existItem);
				response.setCode(HttpStatus.OK.value());
				response.setMessage("Successfully removed menu item");
				return response;
			}
		} else {
			response.setCode(HttpStatus.NO_CONTENT.value());
			response.setMessage("Menu item not found");
			return response;
		}
	}
}
