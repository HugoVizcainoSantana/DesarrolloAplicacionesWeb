package daw.spring.controller;

import daw.spring.model.Analytics;
import daw.spring.model.Device;
import daw.spring.model.Home;
import daw.spring.service.AnalyticsService;
import daw.spring.service.HomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final HomeService homeService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final float LIGHT_CONSUMPTION = (float) 0.20;
    private final float BLIND_CONSUMPTION = (float) 1;

    @Autowired
    public AnalyticsController(AnalyticsService analyticsService, HomeService homeService) {
        this.analyticsService = analyticsService;
        this.homeService = homeService;
    }

    @RequestMapping(value = "/analytics/{homeId}", produces = "application/json")
    public LinkedHashMap<Date, Float> getAnalytics(@PathVariable long homeId) {
        log.info("Requested Analytics!");
        Home home = homeService.findOneById(homeId);
        LinkedList<Date> graphDomain = new LinkedList<>();
        LinkedList<Float> graphValues = new LinkedList<>();
        Date oneDayAgo = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));
        for (Device d : home.getDeviceList()) {
            List<Analytics> analyticsListLast24 = analyticsService.findAnalyticsByDeviceId(d.getId(), oneDayAgo);
            boolean lastAnalyticWasBlindType = false;
            for (Analytics a : analyticsListLast24) {
                graphDomain.add(a.getDate());
                Float lastValue = graphValues.peekLast();
                switch (a.getDevice().getType()) {
                    case LIGHT:
                        if (a.getPreviousState().equals(Device.StateType.OFF) && a.getNewState().equals(Device.StateType.ON)) {
                            if (lastValue != null) {
                                graphValues.addLast(lastValue + LIGHT_CONSUMPTION);
                            } else {
                                graphValues.addLast(LIGHT_CONSUMPTION);
                            }
                        } else if (a.getPreviousState().equals(Device.StateType.ON) && a.getNewState().equals(Device.StateType.OFF)) {
                            if (lastValue != null) {
                                graphValues.addLast(lastValue - LIGHT_CONSUMPTION);
                            } else {
                                graphValues.addLast(0F);
                            }
                        }
                        if (lastAnalyticWasBlindType) { //Readd last with blind consumption also removed
                            graphValues.addLast(graphValues.pollLast() - BLIND_CONSUMPTION);
                        }
                        lastAnalyticWasBlindType = false;
                        break;
                    case BLIND:
                        graphValues.addLast(lastValue + BLIND_CONSUMPTION);
                        lastAnalyticWasBlindType = true;
                        break;
                }
            }
        }
        LinkedHashMap<Date, Float> graphData = new LinkedHashMap<>();
        graphData.keySet().addAll(graphDomain);
        graphData.values().addAll(graphValues);
        return graphData;
    }
}
