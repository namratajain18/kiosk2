package com.cusbee.kiosk.controller.api.admin.menu;

import com.cusbee.kiosk.bean.CategoryBean;
import com.cusbee.kiosk.bean.MenuItemBean;
import com.cusbee.kiosk.controller.ResponseContainer;
import com.cusbee.kiosk.entity.Categories;
import com.cusbee.kiosk.entity.MenuItems;
import com.cusbee.kiosk.repository.CategoriesRepository;
import com.cusbee.kiosk.repository.MenuItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by ahorbat on 26.04.17.
 */
@RestController
@RequestMapping(value = "/api/admin/image")
public class ImageController {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private MenuItemsRepository menuItemsRepository;

    //private static String UPLOADED_FOLDER = "/Users/ahorbat/Projects/cusbee/kiosk-server/img/";
    private static final String UPLOAD_CATEGORY_FOLDER = "/img/";
    private static final String UPLOADED_FOLDER = "/var/www/html/img/";

    @RequestMapping(value = "/upload-menu-item/{itemId}", method = RequestMethod.POST)
    public ResponseContainer<MenuItemBean> imageUploadByMenuItem(
                                                    @RequestParam("file") MultipartFile file,
                                                    @PathVariable("itemId") long itemId){
        ResponseContainer<MenuItemBean> response = new ResponseContainer<>();

        if(file.isEmpty() || file == null) {
            response.setMessage("File is empty or does not exist");
            return response;
        }
        try {
            MenuItems menue = menuItemsRepository.findById(itemId);
            if(menue != null) {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                Files.write(path, bytes);
                menue.setUrl(UPLOAD_CATEGORY_FOLDER + file.getOriginalFilename());
                menuItemsRepository.save(menue);
                response.setData(MenuItemBean.toBean(menue));
                return response;
            } else {
                response.setMessage("Menu item by id: " + itemId + " was not found");
                return response;
            }
            // Get the file and save it somewhere

        } catch (IOException e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            return response;
        }
    }

    @RequestMapping(value = "/upload-category/{categoryId}", method = RequestMethod.POST)
    public ResponseContainer<CategoryBean> imageUploadByCatId(@RequestParam("file") MultipartFile file,
                                     @PathVariable("categoryId") long categoryId
                                     ){
        ResponseContainer<CategoryBean> response = new ResponseContainer<>();

        if(file.isEmpty() || file == null) {
            response.setMessage("File is empty or does not exist");
            return response;
        }
        try {
            Categories category = categoriesRepository.findById(categoryId);
            if(category != null) {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                Files.write(path, bytes);
                category.setUrl(UPLOAD_CATEGORY_FOLDER + file.getOriginalFilename());
                categoriesRepository.save(category);
                response.setData(CategoryBean.toBean(category));
                return response;
            } else {
                response.setMessage("Category by id: " + categoryId + " was not found");
                return response;
            }
            // Get the file and save it somewhere

        } catch (IOException e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            return response;
        }
    }
}
