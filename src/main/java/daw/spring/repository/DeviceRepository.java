package daw.spring.repository;

import daw.spring.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    //public int countDevicesByActivate();
    //public int countDevicesByActivateNot();
}
