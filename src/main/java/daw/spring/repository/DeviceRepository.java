package daw.spring.repository;

import daw.spring.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    long countDevicesByActivatedIsTrue();

    long countDevicesByActivatedIsFalse();

    @Query(value = "SELECT cost FROM device WHERE type = ?1 limit 1", nativeQuery = true)
    public int findCostByType(String type);
}
