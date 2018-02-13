package daw.spring.repository;

import daw.spring.model.Analytics;
import daw.spring.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {
}
