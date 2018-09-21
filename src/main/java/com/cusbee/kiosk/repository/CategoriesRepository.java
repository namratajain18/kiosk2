package com.cusbee.kiosk.repository;

import com.cusbee.kiosk.entity.Categories;
import com.cusbee.kiosk.entity.Menus;
import com.cusbee.kiosk.enums.CategoryType;
import com.google.common.collect.Iterables;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by ahorbat on 07.02.17.
 */
public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    Categories findByName(String name);

    Page<Categories> findByMenus(Pageable pageable, Menus menus);

    List<Categories> findByMenus(Menus menus);

    Page<Categories> findByMenusAndParentCategoryIsNull(Pageable pageable, Menus menus);

    List<Categories> findByMenusAndParentCategoryIsNull(Menus menus);

    List<Categories> findByNameContaining(String name);

    Categories findById(Long id);

    Page<Categories> findAll(Pageable pageable);

    @Transactional
    default void moveSubCategory(Categories category, Categories newParent, Categories oldParent) {
        category.setParentCategory(newParent);
        newParent.getSubcategories().add(category);
        oldParent.getSubcategories().remove(category);

        save(oldParent);
        save(newParent);
        save(category);
    }

    @Transactional
    default void deleteSubCategory(Categories category) {
        Categories parentCategory = category.getParentCategory();
        if (parentCategory != null) {
            parentCategory.getSubcategories().remove(category);
            save(parentCategory);
        } else {
            delete(category);
        }
    }

    default Categories addSubcategory(Categories parentCategory, Categories subCategory) {
        List<Categories> subcategories = parentCategory.getSubcategories();
        subcategories.add(subCategory);

        return Iterables.getLast(save(parentCategory).getSubcategories());
    }

    default void changeOrder(Long id, int order) {
        Categories subCategory = findById(id);
        Categories parent = subCategory.getParentCategory();
        if (parent != null) {
            if (Iterables.indexOf(parent.getSubcategories(), cat -> cat.getId().equals(subCategory.getId())) != order) {
                parent.getSubcategories().remove(subCategory);
                parent.getSubcategories().add(order, subCategory);
                save(parent);
            }
        }
    }
}
