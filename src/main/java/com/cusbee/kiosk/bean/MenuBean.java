package com.cusbee.kiosk.bean;

import com.cusbee.kiosk.entity.Menus;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahorbat on 08.06.17.
 */
@Dto
public class MenuBean {

    @DtoField
    private Long id;

    @DtoField
    private String name;

    @DtoField
    private String description;

    @DtoField
    private boolean publish;

    @DtoField
    private Integer tax;

    public Integer getTax() {
        return tax;
    }

    public void setTax(Integer tax) {
        this.tax = tax;
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

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    public static MenuBean toBean(Menus menus) {
        MenuBean bean = new MenuBean();
        bean.setId(menus.getId());
        bean.setName(menus.getName());
        bean.setDescription(menus.getDescription());
        bean.setPublish(menus.isPublish());
        bean.setTax(menus.getTax());
        return bean;
    }

    public static List<MenuBean> toBeanList(List<Menus> menusList){
        List<MenuBean> menusBeanList = new ArrayList<>();
        for (Menus menus : menusList) {
            menusBeanList.add(MenuBean.toBean(menus));
        }
        return menusBeanList;
    }
}
