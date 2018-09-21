package com.cusbee.kiosk.bean;

import com.cusbee.kiosk.entity.Categories;
import com.cusbee.kiosk.entity.MenuItems;
import com.cusbee.kiosk.enums.CategoryType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.hibernate.collection.internal.PersistentList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahorbat on 07.02.17.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"id", "name", "description", "categoryType", "tax", "imgUrl", "subCategories", "categoryBelong"})
public class CategoryBean {

    private Long id;
    private String name;
    private String description;
    private String imgUrl;
    private Integer tax;
    private List<?> menuItems;
    List<CategoryBean> subCategories;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private CategoryType categoryType;

    private String categoryBelong;
    
    
    
    public String getCategoryBelong() {
		return categoryBelong;
	}

	public void setCategoryBelong(String categoryBelong) {
		this.categoryBelong = categoryBelong;
	}

	public Integer getTax() {
        return tax;
    }

    public void setTax(Integer tax) {
        this.tax = tax;
    }

    public List<?> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<?> menuItems) {
        this.menuItems = menuItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<CategoryBean> getSubCategories() {
        return subCategories == null ? Lists.newArrayList() : subCategories;
    }

    public void setSubCategories(List<CategoryBean> subCategories) {
        this.subCategories = subCategories;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public static CategoryBean toBean(Categories categories) {
        CategoryBean bean = new CategoryBean();
        bean.setId(categories.getId());
        bean.setName(categories.getName());
        bean.setDescription(categories.getDescription());
        bean.setImgUrl(categories.getUrl());
        bean.setCategoryType(categories.getCategoryType());
        List<Categories> subCategories = categories.getSubcategories();
        if (subCategories != null && (((PersistentList) subCategories).wasInitialized() && !subCategories.isEmpty())) {
            bean.setSubCategories(CategoryBean.toBeanList(subCategories));
        }
        return bean;
    }

    public static CategoryBean toBean(Categories categories, Integer tax) {
        CategoryBean bean = new CategoryBean();
        bean.setId(categories.getId());
        bean.setName(categories.getName());
        bean.setDescription(categories.getDescription());
        bean.setImgUrl(categories.getUrl());
        bean.setCategoryType(categories.getCategoryType());
        bean.setTax(tax);
        bean.setCategoryBelong(categories.getCategoryBelong());

        List<Categories> subCategories = categories.getSubcategories();
        if (subCategories != null && (((PersistentList) subCategories).wasInitialized() && !subCategories.isEmpty())) {
            bean.setSubCategories(CategoryBean.toBeanList(subCategories, tax));
        }
        return bean;
    }

    public static CategoryBean toBeanWithIMenutems(Categories categories, List<MenuItems> menuItems) {
        CategoryBean bean = new CategoryBean();
        bean.setId(categories.getId());
        bean.setName(categories.getName());
        bean.setDescription(categories.getDescription());
        bean.setImgUrl(categories.getUrl());
        bean.setCategoryType(categories.getCategoryType());

        List<Categories> subCategories = categories.getSubcategories();
        if (subCategories != null && (((PersistentList) subCategories).wasInitialized() && !subCategories.isEmpty())) {
            bean.setSubCategories(CategoryBean.toBeanList(subCategories));
        }

        bean.setMenuItems(Lists.newArrayList(menuItems));
        return bean;
    }

    public static List<CategoryBean> toBeanList(List<Categories> categoriesList, Integer tax) {
        List<CategoryBean> catBeanList = new ArrayList<>();
        for (Categories categories : categoriesList) {
            catBeanList.add(CategoryBean.toBean(categories, tax));
        }
        return catBeanList;
    }

    public static List<CategoryBean> toBeanList(List<Categories> categoriesList) {
        List<CategoryBean> catBeanList = new ArrayList<>();
        for (Categories categories : categoriesList) {
            catBeanList.add(CategoryBean.toBean(categories));
        }
        return catBeanList;
    }
}
