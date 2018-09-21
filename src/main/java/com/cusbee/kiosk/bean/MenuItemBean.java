package com.cusbee.kiosk.bean;

import java.util.ArrayList;
import java.util.List;

import com.cusbee.kiosk.entity.Ingredients;
import com.cusbee.kiosk.entity.ItemDimentions;
import com.cusbee.kiosk.entity.MenuItems;
import com.cusbee.kiosk.enums.CookType;

/**
 * Created by ahorbat on 10.02.17.
 */
public class MenuItemBean {
    private Long id;
    private String name;
    private String categoryName;
    private String description;
    private String url;
    private double price;
    private boolean topSellers;
    private boolean specialities;
    private List<CookType> cookType;
    private List<Ingredients> ingredients;
    private List<Ingredients> extras;
    private List<ItemDimentions> itemDimentions;
    private String size;
    private String serve;


    
    
    public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getServe() {
		return serve;
	}

	public void setServe(String serve) {
		this.serve = serve;
	}

	public List<CookType> getCookType() {
        return cookType;
    }

    public void setCookType(List<CookType> cookType) {
        this.cookType = cookType;
    }

    public List<Ingredients> getExtras() {
        return extras;
    }

    public void setExtras(List<Ingredients> extras) {
        this.extras = extras;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(final String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isTopSellers() {
        return topSellers;
    }

    public void setTopSellers(boolean topSellers) {
        this.topSellers = topSellers;
    }

    public boolean isSpecialities() {
        return specialities;
    }

    public void setSpecialities(boolean specialities) {
        this.specialities = specialities;
    }
    
	public List<ItemDimentions> getItemDimentions() {
		return itemDimentions;
	}

	public void setItemDimentions(List<ItemDimentions> itemDimentions) {
		this.itemDimentions = itemDimentions;
	}

	public static MenuItemBean toBean(MenuItems item) {
        MenuItemBean bean = new MenuItemBean();
        bean.setId(item.getId());
        bean.setName(item.getName());
        bean.setCategoryName(item.getCategory().getName());
        bean.setDescription(item.getDescription());
        bean.setPrice(item.getPrice().doubleValue());
        bean.setUrl(item.getUrl());
        bean.setSpecialities(bean.isSpecialities());
        bean.setTopSellers(bean.isSpecialities());
		if (item.getItemDimentions() != null && !item.getItemDimentions().isEmpty()) {
			bean.setItemDimentions(item.getItemDimentions());
		}
		
		bean.setSize(item.getSize());
		bean.setServe(item.getServe());
        return bean;
    }

    public static MenuItemBean toBeanWithIngredients(MenuItems item, List<Ingredients> ingredientsList) {
        MenuItemBean bean = new MenuItemBean();
        bean.setId(item.getId());
        bean.setName(item.getName());
        bean.setCategoryName(item.getCategory().getName());
        bean.setDescription(item.getDescription());
        bean.setPrice(item.getPrice().doubleValue());
        bean.setUrl(item.getUrl());
        bean.setSpecialities(item.isSpecialities());
        bean.setTopSellers(item.isSpecialities());
        bean.setSize(item.getSize());
		bean.setServe(item.getServe());
        //sorry for this hardcode
        if (item.isCookRules()) {
            if (item.getId() == 23 ||
                    item.getId() == 24 ||
                    item.getId() == 25 ||
                    item.getId() == 46 ||
                    item.getId() == 47 ||
                    item.getId() == 48 ||
                    item.getId() == 68 ||
                    item.getId() == 80 ||
                    item.getId() == 81 ||
                    item.getId() == 82 ||
                    item.getId() == 83 ||
                    item.getId() == 84 ||
                    item.getId() == 85 ||
                    item.getId() == 135 ||
                    item.getId() == 136 ||
                    item.getId() == 137 ||
                    item.getId() == 138 ||
                    item.getId() == 139 ||
                    item.getId() == 161 ||
                    item.getId() == 173 ||
                    item.getId() == 174 ||
                    item.getId() == 175 ||
                    item.getId() == 176 ||
                    item.getId() == 177) {

                List<CookType> list = new ArrayList<>();
                list.add(CookType.Tossed);
                list.add(CookType.OnSide);
                bean.setCookType(list);
            } else {
                List<CookType> list = new ArrayList<>();
                list.add(CookType.Medium);
                list.add(CookType.MediumRare);
                list.add(CookType.MediumWell);
                list.add(CookType.WellDone);
                bean.setCookType(list);
            }

        }
        List<Ingredients> ingred = new ArrayList<>();
        List<Ingredients> extras = new ArrayList<>();

        if(ingredientsList  != null) {
        	for (Ingredients ingredient: ingredientsList ){
        		if(ingredient == null || ingredient.getIngredientType() == null)
        			continue;
                if(ingredient.getIngredientType().ordinal() == 0){
                    ingred.add(ingredient);
                } else {
                    extras.add(ingredient);
                }
            }	
        }
        
        //bean.setIngredients(ingredientsList);
        bean.setIngredients(ingred);
        bean.setExtras(extras);
		if (item.getItemDimentions() != null && !item.getItemDimentions().isEmpty()) {
			bean.setItemDimentions(item.getItemDimentions());
		}
        return bean;
    }


}
