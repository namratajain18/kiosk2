package com.cusbee.kiosk.entity;

import com.cusbee.kiosk.enums.DeviceType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ahorbat on 21.06.17.
 */
@Entity
@Table(name = "devices")
public class Devices extends AbstractEntity {

    private static final long serialVersionUID = -5804488169708183460L;

    public enum AssignStatus {
        ASSIGNED, UNASSIGNED;
    }

    @Column(name = "system_id")
    private Long sysId;

    @Column(name = "uID")
    private String uid;

    @Column(name = "last_time_activated")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonIgnore
    private Date lastTimeActive = new Date();

    @Column(name = "type")
    private DeviceType deviceType = DeviceType.TABLET;

    @Column(name = "assign")
    private AssignStatus assignStatus = AssignStatus.UNASSIGNED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locations_id")
    @JsonIgnore
    private Locations locations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    @JsonIgnore
    private Menus menus;

    public Long getSysId() {
        return sysId;
    }

    public void setSysId(Long sysId) {
        this.sysId = sysId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getLastTimeActive() {
        return lastTimeActive;
    }

    public void setLastTimeActive(Date lastTimeActive) {
        this.lastTimeActive = lastTimeActive;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public AssignStatus getAssignStatus() {
        return assignStatus;
    }

    public void setAssignStatus(AssignStatus assignStatus) {
        this.assignStatus = assignStatus;
    }

    public Locations getLocations() {
        return locations;
    }

    public void setLocations(Locations locations) {
        this.locations = locations;
    }

    public Menus getMenus() {
        return menus;
    }

    public void setMenus(Menus menus) {
        this.menus = menus;
    }
}
