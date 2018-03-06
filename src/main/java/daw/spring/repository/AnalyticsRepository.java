package daw.spring.repository;

import daw.spring.model.Analytics;
import daw.spring.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {

    List<Analytics> findByDeviceOrderByDateAsc(Device device);

    List<Analytics> findByDeviceAndDateAfter(Device device, Date date);

    List<Analytics> findAllByDateAfter(Date date);
}
