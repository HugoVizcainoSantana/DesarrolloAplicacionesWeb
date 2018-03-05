package daw.spring.service;


import daw.spring.model.Device;
import daw.spring.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Device findOneById(Long id) {
        return deviceRepository.findOne(id);
    }

    public List<Device> findAllDevices() {
        return deviceRepository.findAll();
    }

    public void saveDevice(Device product) {
        deviceRepository.save(product);
    }

    public void deleteDevice(Device device) {
        deviceRepository.delete(device);
    }

    public Page<Device> findAllDevicePage(PageRequest pageRequest) {
        return deviceRepository.findAll(pageRequest);
    }

    public long countActivatedDevices(){
        return deviceRepository.countDevicesByActivatedIsTrue();
    }

    public long countNotActivatedDevices(){
        return deviceRepository.countDevicesByActivatedIsFalse();
    }

    public void activeOneDevice(long deviceId, String serialNumber) {
        Device deviceUpdate=deviceRepository.findOne(deviceId);
        deviceUpdate.setActivated(true);
        deviceUpdate.setSerialNumber(serialNumber);
        saveDevice(deviceUpdate);
    }

    public void cancelOneDevice(long deviceId) {
        deviceRepository.delete(deviceId);
    }
    
    public int findCost(String type) {
    		return deviceRepository.findCostByType(type);
    }

}
