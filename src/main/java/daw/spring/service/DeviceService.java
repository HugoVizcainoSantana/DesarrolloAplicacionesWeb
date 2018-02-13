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

    @Autowired
    DeviceRepository deviceRepository;

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

    public void save(Device product1) {
        deviceRepository.save(product1);
    }


}