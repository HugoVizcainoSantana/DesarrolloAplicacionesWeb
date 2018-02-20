package daw.spring.service;


import daw.spring.model.Device;
import daw.spring.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

    public void deleteDevice(Device product) {
        deviceRepository.delete(product);
    }

    public Page<Device> findAllDevicePage(PageRequest pageRequest) {
        return deviceRepository.findAll(pageRequest);
    }

    @PostConstruct
    public void init() {
        saveDevice(new Device("una bombilla de 4000k", 30, Device.DeviceType.LIGHT, Device.StateType.ON, null, true));
        saveDevice(new Device("una persiana", 150, Device.DeviceType.BLIND, Device.StateType.UP, null, true));
        saveDevice(new Device("una bombilla de 6000k", 30, Device.DeviceType.LIGHT, Device.StateType.OFF, null, false));
    }

}
