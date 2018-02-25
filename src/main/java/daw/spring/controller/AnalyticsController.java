package daw.spring.controller;

import daw.spring.model.Analytics;
import daw.spring.model.Device;
import daw.spring.model.Home;
import daw.spring.service.AnalyticsService;
import daw.spring.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller

@RequestMapping("/dashboard")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private HomeService homeService;

    @RequestMapping("/analytics/{homeId}")
    public List<Analytics> getAnalytics(@PathVariable long homeId){
        Home home = homeService.findOneById(homeId);
        //List<List<Analytics>>

        for (Device d:home.getDeviceList()){
            d.
        }

        return
    }
}
