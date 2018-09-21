package com.cusbee.kiosk.bean;

import com.cusbee.kiosk.entity.Devices;
import com.cusbee.kiosk.entity.Locations;
import com.cusbee.kiosk.entity.Menus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ahorbat on 02.07.17.
 */
public class LocationBean {
    private Long id;
    private int tables = 1;
    private String name;
    private List<Devices> devices;
    private List<MenuBean> menus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTables() {
        return tables;
    }

    public void setTables(int tables) {
        if (tables == 0) {
            return;
        }
        this.tables = tables;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Devices> getDevices() {
        return devices;
    }

    public void setDevices(List<Devices> devices) {
        this.devices = devices;
    }

    public List<MenuBean> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuBean> menus) {
        this.menus = menus;
    }

    public static List<LocationBean> toBeanIDNList(List<Locations> locations) {
        return locations.stream().map(LocationBean::toBean).collect(Collectors.toList());
    }

    public static LocationBean toBean(Locations location, List<Devices> devices, List<Menus> menus) {
        LocationBean bean = new LocationBean();
        bean.setId(location.getId());
        bean.setName(location.getName());
        bean.setMenus(MenuBean.toBeanList(menus));
        bean.setDevices(devices);
        bean.setTables(location.getTables());
        return bean;
    }

    public static LocationBean toBean(Locations location) {
        LocationBean bean = new LocationBean();
        bean.setId(location.getId());
        bean.setName(location.getName());
        List<Devices> devices = new ArrayList<>();
        bean.setDevices(devices);
        List<MenuBean> menusBeanList = new ArrayList<>();
        bean.setMenus(menusBeanList);
        bean.setTables(location.getTables());
        return bean;
    }

}
