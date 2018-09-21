package com.cusbee.kiosk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * Created by ahorbat on 08.06.17.
 */
@Entity
@Table(name = "menus")
public class Menus extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -3220936752408059613L;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "publish")
    private boolean publish = false;

    @Column(name = "tax")
    private Integer tax;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "location_id")
    @JsonIgnore
    private Locations locations;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menus", cascade = CascadeType.ALL)
    private Set<Categories> categories;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menus", cascade = CascadeType.ALL)
    private Set<Devices> devices;

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

    public Integer getTax() {
        return tax;
    }

    public void setTax(Integer tax) {
        this.tax = tax;
    }

    public Locations getLocations() {
        return locations;
    }

    public void setLocations(Locations locations) {
        this.locations = locations;
    }

    public Set<Categories> getCategories() {
        return categories;
    }

    public void setCategories(Set<Categories> categories) {
        this.categories = categories;
    }

    public Set<Devices> getDevices() {
        return devices;
    }

    public void setDevices(Set<Devices> devices) {
        this.devices = devices;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Menus)) {
            return false;
        }
        return super.equals(obj) && Objects.equals(((Menus) obj).name, name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
