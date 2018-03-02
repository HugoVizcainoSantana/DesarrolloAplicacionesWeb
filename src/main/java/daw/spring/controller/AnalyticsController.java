package daw.spring.controller;

import daw.spring.model.Analytics;
import daw.spring.model.Device;
import daw.spring.model.Home;
import daw.spring.service.AnalyticsService;
import daw.spring.service.HomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
@RequestMapping("/dashboard")
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final HomeService homeService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final double LIGHT_CONSUMPTION = 0.20;
    private final double BLIND_CONSUMPTION = 1;
    private final double RASPBERRY_CONSUMPTION = 5;

    @Autowired
    public AnalyticsController(AnalyticsService analyticsService, HomeService homeService) {
        this.analyticsService = analyticsService;
        this.homeService = homeService;
    }

    @RequestMapping(value = "/analytics/{homeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Double> getAnalytics(@PathVariable long homeId) throws ParseException {
        log.info("Requested Analytics for home " + homeId);
        Home home = homeService.findOneById(homeId);
        List<Analytics> analyticsList = new LinkedList<>();
        Date oneDayAgo = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));
        for (Device d : home.getDeviceList()) {
            analyticsList.addAll(analyticsService.findAnalyticsByDevice(d, oneDayAgo));
        }
        if (analyticsList.isEmpty()) { //If it's empty, return empty map
            return new HashMap<>();
        } else {
            analyticsList.sort(Comparator.comparing(Analytics::getDate));
            LinkedList<Date> graphDomain = new LinkedList<>();
            LinkedList<Double> graphValues = new LinkedList<>();
            for (int i = 0; i < analyticsList.size(); i++) {
                Analytics currentAnalytic = analyticsList.get(i);
                Analytics previousRecord;
                if (i > 0) {
                    previousRecord = analyticsList.get(i - 1);
                } else {
                    previousRecord = null;
                }
                graphDomain.add(currentAnalytic.getDate());
                Double lastValue = graphValues.peekLast();
                double deviceConsumption = 0;
                switch (currentAnalytic.getDevice().getType()) {
                    case LIGHT:
                        deviceConsumption = LIGHT_CONSUMPTION;
                        break;
                    case BLIND:
                        deviceConsumption = BLIND_CONSUMPTION;
                        break;
                    case RASPBERRYPI:
                        break;
                }
                if (currentAnalytic.getDevice().getType() == Device.DeviceType.BLIND || currentAnalytic.getPreviousState().equals(Device.StateType.OFF) && currentAnalytic.getNewState().equals(Device.StateType.ON)) {
                    if (lastValue != null) {
                        graphValues.addLast(lastValue + deviceConsumption);
                    } else {
                        graphValues.addLast(deviceConsumption);
                    }
                } else if (currentAnalytic.getDevice().getType() != Device.DeviceType.BLIND && //Blinds dont have off action per se
                        currentAnalytic.getPreviousState().equals(Device.StateType.ON) &&
                        currentAnalytic.getNewState().equals(Device.StateType.OFF)) {
                    if (lastValue != null) {
                        graphValues.addLast(lastValue - deviceConsumption);
                    } else {
                        graphValues.addLast(0.0);
                    }
                }
                if (previousRecord != null && previousRecord.getDevice().getType() == Device.DeviceType.BLIND) { //Read last with blind consumption also removed
                    lastValue = graphValues.pollLast();
                    graphValues.addLast(lastValue - BLIND_CONSUMPTION);
                }
            }
            graphDomain.addLast(Timestamp.from(Instant.now()));
            //If last record is from a Blind, we gotta remove it too
            if (analyticsList.get(analyticsList.size() - 1).getDevice().getType() == Device.DeviceType.BLIND) {
                graphValues.addLast(graphValues.peekLast() - BLIND_CONSUMPTION);
            } else {
                graphValues.addLast(graphValues.peekLast());
            }
            HashMap<String, Double> graphData = new HashMap<>();
            if (graphDomain.size() != graphValues.size()) {
                throw new RuntimeException("THIS SHOULD NOT BE HAPPENING! Different sized arrays on analytics controller");
            }
            for (int i = 0; i < graphDomain.size(); i++) {
                graphData.put(formatDomain(graphDomain.get(i)), graphValues.get(i));
            }
            return new TreeMap<>(graphData);
        }
    }

    private String formatDomain(Date d) throws ParseException {
        String date_s = d.toString();
        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd HH:mm:ss");
        Date date = dt.parse(date_s);
        SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm:ss a");

        return dt1.format(date);
    }

}
