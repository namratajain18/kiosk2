package com.cusbee.kiosk.controller.api.admin.menu;

import com.cusbee.kiosk.bean.IngredientBean;
import com.cusbee.kiosk.controller.ResponseContainer;
import com.cusbee.kiosk.entity.Ingredients;
import com.cusbee.kiosk.entity.MenuItems;
import com.cusbee.kiosk.repository.IngredientsRepository;
import com.cusbee.kiosk.repository.MenuItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ahorbat on 11.02.17.
 */
@RestController
//@CrossOrigin(origins = "http://138.197.127.24:8080", maxAge = 3600)
@RequestMapping(value = "/api/admin")
public class IngredientController {

    @Autowired
    private MenuItemsRepository menuItemsRepository;

    @Autowired
    private IngredientsRepository ingredientsRepository;

    @RequestMapping(value = "/ingredient/{menuItemId}", method = RequestMethod.POST)
    public ResponseContainer<IngredientBean> createIngredient(@PathVariable("menuItemId") long menuItemId,
                                                              @RequestBody Ingredients ingredients){
        ResponseContainer<IngredientBean> response = new ResponseContainer<>();
        MenuItems menuItems = menuItemsRepository.findById(menuItemId);
        if (menuItems != null) {
            ingredients.setMenuItems(menuItems);
            ingredientsRepository.save(ingredients);
            response.setData(IngredientBean.toBean(ingredients));
            response.setCode(HttpStatus.OK.value());
            response.setMessage("Ingredient successfully added to item " + menuItems.getName());
            return response;
        } else {
            response.setMessage("Menu item not found");
            response.setCode(HttpStatus.NO_CONTENT.value());
            return response;
        }
    }

    @RequestMapping(value = "/ingredient/{ingredientId}", method = RequestMethod.PUT)
    public ResponseContainer<IngredientBean> updateIngredientById(@PathVariable("ingredientId") long ingredientId,
                                                                  @RequestBody Ingredients ingredients) {

        ResponseContainer<IngredientBean> response = new ResponseContainer<>();
        Ingredients existIngredient = ingredientsRepository.findOne(ingredientId);
        if(existIngredient != null) {
            existIngredient.setName(ingredients.getName());
            existIngredient.setRemove(ingredients.isRemove());
            ingredientsRepository.save(existIngredient);
            response.setData(IngredientBean.toBean(existIngredient));
            response.setCode(HttpStatus.OK.value());
            response.setMessage("Ingredient " + ingredients.getName() + " successfuly updated");
            return response;
        } else {
            response.setMessage("Ingredient not found");
            response.setCode(HttpStatus.NO_CONTENT.value());
            return response;
        }
    }

    @RequestMapping(value = "/ingredient/{ingredientId}", method = RequestMethod.DELETE)
    public ResponseContainer<String> removeIngredientById(@PathVariable("ingredientId") long ingredientId) {
        ResponseContainer<String> response = new ResponseContainer<>();
        Ingredients ingredient = ingredientsRepository.findOne(ingredientId);
        if(ingredient != null) {
            ingredientsRepository.delete(ingredientId);
            response.setMessage("Ingredient removed successfully");
            response.setCode(HttpStatus.OK.value());
            return response;
        } else {
            response.setMessage("Ingredient not found");
            response.setCode(HttpStatus.NO_CONTENT.value());
            return response;
        }
    }
}
