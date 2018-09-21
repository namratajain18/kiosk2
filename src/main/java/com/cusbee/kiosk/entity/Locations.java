package com.cusbee.kiosk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by ahorbat on 21.06.17.
 */
@Entity
@Table(name = "locations")
public class Locations extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -5082963031422538663L;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "available_tables", nullable = false)
    private int tables = 1;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "locations", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Menus> menus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "locations", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Devices> devices;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Menus> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menus> menus) {
        this.menus = menus;
    }

    public Set<Devices> getDevices() {
        return devices;
    }

    public void setDevices(Set<Devices> devices) {
        this.devices = devices;
    }

    public int getTables() {
        return tables > 0 ? tables : 1;
    }

    public void setTables(int tables) {
        if (tables == 0) {
            return;
        }
        this.tables = tables;
    }
}
