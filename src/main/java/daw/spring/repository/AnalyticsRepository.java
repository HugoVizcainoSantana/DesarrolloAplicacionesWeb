package daw.spring.repository;

import daw.spring.model.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {

    List<Analytics> findByDeviceId(long id);

    List<Analytics> findByDeviceIdAndDateAfter(long id, Date date);
}
