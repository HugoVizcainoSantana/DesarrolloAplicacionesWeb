package daw.spring.service;

import daw.spring.model.Analytics;
import daw.spring.model.Device;
import daw.spring.model.Home;
import daw.spring.model.User;
import daw.spring.repository.AnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Service
public class AnalyticsService {

    private final AnalyticsRepository analyticsRepository;
    private final DeviceService deviceService;
    private final UserService userService;
    private final HomeService homeService;


    @Autowired
    public AnalyticsService(AnalyticsRepository analyticsRepository, DeviceService deviceService, UserService userService, HomeService homeService) {
        this.analyticsRepository = analyticsRepository;
        this.deviceService = deviceService;
        this.userService = userService;
        this.homeService = homeService;
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

    private void saveAnalytics(Analytics analytics) {
        //deviceService.saveDevice(analytics.getDevice());
        analyticsRepository.save(analytics);
    }

    public void deleteAnalytics(Analytics analytics) {
        analyticsRepository.delete(analytics);
    }

    public Page<Analytics> findAllAnalyticsPage(PageRequest pageRequest) {
        return analyticsRepository.findAll(pageRequest);
    }

    @PostConstruct
    public void init() {

        /*User user = userRepository.findUserById(1L);

        String[] months = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
        Integer[] data = {50,45,65,55,35,15,10,15,35,40,50,60};
        Integer[] dataAverage = {60,55,45,25,15,5,5,5,25,30,55,75};

        ArrayList<String> monthsArrayList = new ArrayList<>(Arrays.asList(months));
        ArrayList<Integer> dataArrayList = new ArrayList<>(Arrays.asList(data));
        ArrayList<Integer> dataAverageArrayList = new ArrayList<>(Arrays.asList(dataAverage));

        Analytics analitycsYearUser1 = new Analytics(user, USERS, "Consumo del año 2017", "Consumo del año 2017 del usuario: ", monthsArrayList,dataArrayList,dataAverageArrayList);

        saveAnalytics(analitycsYearUser1);*/


        User user = userService.findOneUserByEmail("hugo@santana.com");
        Home home = user.getHomeList().get(0);
        Device device = home.getDeviceList().get(0);
        Analytics analytics1 = new Analytics(device, new Date(), Device.StateType.OFF, Device.StateType.ON, null);
        analytics1.setDate(new Date(2018 - 1900, 2, 25, 12, 0, 0));
        Analytics analytics2 = new Analytics(device, new Date(), Device.StateType.ON, Device.StateType.OFF, analytics1);
        analytics2.setDate(new Date(2018 - 1900, 1, 25, 16, 0, 0));
        saveAnalytics(analytics1);
        saveAnalytics(analytics2);
    }

}
