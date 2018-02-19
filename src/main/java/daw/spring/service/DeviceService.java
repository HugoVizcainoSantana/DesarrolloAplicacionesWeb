package daw.spring.service;


import daw.spring.model.Device;
import daw.spring.model.Device.DeviceType;
import daw.spring.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.PostConstruct;

@Service
public class DeviceService {

    @Autowired
    DeviceRepository deviceRepository;

    public Device findOneById(Long id){

        return deviceRepository.findOne(id);
    }

    public List<Device> findAllDevices(){
        return deviceRepository.findAll();
    }


    public void saveDevice(Device product){
        deviceRepository.save(product);
    }

    public void deleteDevice(Device product){
        deviceRepository.delete(product);
    }

    public Page<Device> findAllDevicePage(PageRequest pageRequest){
        return deviceRepository.findAll(pageRequest);
    }

    public void save(Device product1) {
        deviceRepository.save(product1);
    }
    
    
    @PostConstruct
    public void prueba() {
    	saveDevice(new Device(0, "Descripcion", 1234, Device.DeviceType.LIGHT,  Device.StateType.OFF, null));
    }



}
