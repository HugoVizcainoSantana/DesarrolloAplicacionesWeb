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
    public void prueba() {
        saveDevice(new Device("sdasdasd", 12122, Device.DeviceType.LIGHT, Device.StateType.ON, null));
    }

}
