package main.java.ua.nure.bogun.apiproject.service.databaseservice;

import main.java.ua.nure.bogun.apiproject.database.DBException;
import main.java.ua.nure.bogun.apiproject.database.DeviceDBManager;
import main.java.ua.nure.bogun.apiproject.entity.Device;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class DeviceService {
    private static DeviceService instance;
    private static final Logger logger = Logger.getLogger(DeviceService.class);

    public static DeviceService getInstance() {
        if (instance == null) {
            instance = new DeviceService();
        }
        return instance;
    }
    public Device getDeviceById(Long userId){
        return DeviceDBManager.getInstance().findDeviceById(userId);
    }
    public List<Device> getAllDevices(){
        try {
            return DeviceDBManager.getInstance().findAllDevices();
        } catch (DBException e) {
            e.printStackTrace();
            logger.error("Error getAllDevices: ",e);
            return new ArrayList<>();
        }
    }
    public List<Device> getAllDevicesByUserId(Long userId){
        try {
            return DeviceDBManager.getInstance().findDevicesByUserId(userId);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error("Error getAllDevicesByUserId: ",e);
            return new ArrayList<>();
        }
    }
    public boolean updateData(Long deviceId, String temperature, String humidity, String currentDate){
        Device device = new Device();
        device.setId(deviceId);
        device.setTemperature(temperature);
        device.setHumidity(humidity);
        device.setLastDate(currentDate);
        try {
            return DeviceDBManager.getInstance().updateData(device);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error("Error updateData: ",e);
            return false;
        }
    }
    public boolean updatePhone(String phone, Long deviceId){
        try {
            return DeviceDBManager.getInstance().updatePhone(phone,deviceId);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error("Error updatePhone: ",e);
            return false;
        }
    }
    public boolean deleteDevice(Long deviceId){
        try {
            return DeviceDBManager.getInstance().delete(deviceId);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error("Error updatePhone: ",e);
            return false;
        }
    }
}
