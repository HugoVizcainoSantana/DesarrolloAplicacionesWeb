package daw.spring.service;

import daw.spring.model.Analytics;
import daw.spring.repository.AnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

    public List<Analytics> findAnalyticsByDeviceId(long id) {
        return analyticsRepository.findByDeviceId(id);
    }

    public List<Analytics> findAnalyticsByDeviceId(long id, Date date) {
        return analyticsRepository.findByDeviceIdAndDateAfter(id, date);
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

}
