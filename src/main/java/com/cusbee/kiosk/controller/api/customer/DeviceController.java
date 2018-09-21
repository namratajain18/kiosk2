package com.cusbee.kiosk.controller.api.customer;

import com.cusbee.kiosk.controller.ResponseContainer;
import com.cusbee.kiosk.entity.Devices;
import com.cusbee.kiosk.entity.Locations;
import com.cusbee.kiosk.entity.Menus;
import com.cusbee.kiosk.repository.DevicesRepository;
import com.cusbee.kiosk.repository.LocationRepository;
import com.cusbee.kiosk.repository.MenusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by ahorbat on 24.06.17.
 */
@RestController
@RequestMapping(value = "/api/device")
public class DeviceController {

    private  enum DeviceFilter{
        BY_LOCATION, ASSIGNED_TO_MENU, NOT_ASSIGNED, ALL ;
    }

    @Autowired
    private DevicesRepository devicesRepository;

    @Autowired
    private MenusRepository menusRepository;

    @Autowired
    private LocationRepository locationRepository;

    @RequestMapping(value = "/by-menu/{menuId}", method = RequestMethod.GET)
    public ResponseContainer<List<Devices>> retrieveDevicesByMenuId(@PathVariable("menuId") Long menuId) {
        ResponseContainer<List<Devices>> response = new ResponseContainer<>();
        Menus menu = menusRepository.findById(menuId);
        List<Devices> devices = devicesRepository.findByMenus(menu);
        response.setCode(200);
        response.setData(devices);
        response.setMessage("Success");
        return response;
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public ResponseContainer<List<Devices>> filterBy(@RequestParam("filter") DeviceFilter filter) {
        ResponseContainer<List<Devices>> response = new ResponseContainer<>();

        switch (filter) {
            case BY_LOCATION: {
                response.setCode(200);
                response.setData(devicesRepository.findByLocationsNotNull());
                break;
            }
            case ASSIGNED_TO_MENU: {
                response.setCode(200);
                response.setData(devicesRepository.findByMenusNotNull());
                break;
            }
            case NOT_ASSIGNED:
                response.setCode(200);
                response.setData(devicesRepository.findByMenusIsNullOrLocationsIsNull());
                break;
            case ALL:
                response.setCode(200);
                response.setData(devicesRepository.findAll());
            }
        return response;
    }

    @RequestMapping(value = "/assign-menu/{menuId}", method = RequestMethod.POST)
    public ResponseContainer<List<Devices>> assignMenu(@PathVariable("menuId") Long menuId,
                                                       @RequestParam("deviceIds") Set<Long> deviceIds){
        ResponseContainer<List<Devices>> response = new ResponseContainer<>();
        Menus menus = menusRepository.findById(menuId);
        if(menus != null) {
            Iterable<Long> ids = deviceIds;
            List<Devices> devices = devicesRepository.findAll(ids);
            List<Devices> assignedForSave = new ArrayList<>();
            devices.stream().forEach(el->{
                el.setAssignStatus(Devices.AssignStatus.ASSIGNED);
                el.setMenus(menus);
                assignedForSave.add(el);
            });
            if(!assignedForSave.isEmpty()) {
                Iterable<Devices> entity = assignedForSave;
                devicesRepository.save(entity);
                response.setCode(200);
                response.setData(assignedForSave);
                response.setMessage("Current devices were successfully assigned to menu: " + menus.getName());
                return response;
            } else {
                response.setCode(404);
                response.setMessage("Devices with id: " + deviceIds + " were not found");
                return response;
            }
        } else {
            response.setCode(404);
            response.setMessage("Menu with id:" + menuId + " does not exist");
            return response;
        }

    }

    @RequestMapping(value = "/assign-location/{locationId}", method = RequestMethod.POST)
    public ResponseContainer<List<Devices>> assignLocation(@PathVariable("locationId") Long locationId,
                                                     @RequestParam("deviceIds") Set<Long> deviceIds){
        ResponseContainer<List<Devices>> response = new ResponseContainer<>();
        Iterable<Long> ids = deviceIds;
        Locations location = locationRepository.findById(locationId);
        if(location != null) {
            List<Devices> devices = devicesRepository.findAll(ids);
            List<Devices> assignedForSave = new ArrayList<>();
            devices.stream().forEach(el->{
                el.setLocations(location);
                assignedForSave.add(el);
            });
            if(!assignedForSave.isEmpty()) {
                Iterable<Devices> entity = assignedForSave;
                devicesRepository.save(entity);
                response.setCode(200);
                response.setData(assignedForSave);
                response.setMessage("Current devices were successfully assigned to location: " + location.getName());
                return response;
            } else {
                response.setCode(404);
                response.setMessage("Devices with id: " + deviceIds + " were not found");
                return response;
            }

        } else {
            response.setCode(404);
            response.setMessage("Location with id:" + locationId + " does not exist");
            return response;
        }


    }

    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public ResponseContainer<Devices> init(@RequestBody Devices device){
        ResponseContainer<Devices> response = new ResponseContainer<>();
        if(device.getSysId() == null || device.getUid() == null) {
            response.setMessage("Some parameters data are missing System(assigned to device) id is: "+device.getSysId() +
                    " and uID is: " + device.getUid());
            response.setCode(404);
            return response;
        }
        Devices updatedDevice = devicesRepository.findBySysId(device.getSysId());
        if(updatedDevice != null) {
            updatedDevice.setDeviceType(device.getDeviceType());
            updatedDevice.setLastTimeActive(new Date());
            updatedDevice.setSysId(device.getSysId());
            devicesRepository.save(updatedDevice);
            response.setData(updatedDevice);

            if(updatedDevice.getUid() != null && updatedDevice.getUid() != device.getUid()){
                response.setMessage("Assign warning: UID("+updatedDevice.getUid()+") and SysID("+
                        device.getSysId()+") are not from the same device");
                response.setCode(201);
                return response;
            }
            response.setMessage("Device inited successfully");
            response.setCode(200);
            return response;
        } else {
            device.setLastTimeActive(new Date());
            devicesRepository.save(device);
            response.setData(device);
            response.setMessage("Device inited successfully");
            response.setCode(200);
            return response;
        }
    }
}
