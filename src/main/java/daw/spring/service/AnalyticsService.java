package daw.spring.service;

import daw.spring.model.Analytics;
import daw.spring.model.Device;
import daw.spring.repository.AnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class AnalyticsService {

    private final AnalyticsRepository analyticsRepository;

    @Autowired
    public AnalyticsService(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
        
    }

    public Analytics findOneById(long id) {
        return analyticsRepository.findOne(id);
    }

    public List<Analytics> findAnalyticsByDevice(Device device) {
        return analyticsRepository.findByDeviceOrderByDateAsc(device);
    }

    public List<Analytics> findAnalyticsByDevice(Device device, Date date) {
        return analyticsRepository.findByDeviceAndDateAfter(device, date);
    }

    public List<Analytics> findAllAnalytics() {
        return analyticsRepository.findAll();
    }

    public void saveAnalytics(Analytics analytics) {
        analyticsRepository.save(analytics);
    }

    public void deleteAnalytics(Analytics analytics) {
        analyticsRepository.delete(analytics);
    }

    public Page<Analytics> findAllAnalyticsPage(PageRequest pageRequest) {
        return analyticsRepository.findAll(pageRequest);
    }

    public List<Analytics> findAllByDate() {
        Date oneDayAgo = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));

        return analyticsRepository.findAllByDateAfter(oneDayAgo);
    }

}
