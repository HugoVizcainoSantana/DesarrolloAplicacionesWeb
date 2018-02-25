package daw.spring.repository;

import daw.spring.model.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {

    public List<Analytics> findByDeviceId(long id);

    public List<Analytics> findByDeviceIdAndDateAfterOrderByDateAsc(long id, Date date);
}
