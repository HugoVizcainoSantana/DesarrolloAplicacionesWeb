package daw.spring.repository;

import daw.spring.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    long countDevicesByActivatedIsTrue();
    long countDevicesByActivatedIsFalse();
}
