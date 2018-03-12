package daw.spring.restcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

public class UserDashboardRestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/test")
    public Map<String, Long> test() {
        log.error("Test");
        Map<String, Long> map = new HashMap<>();
        map.put("Test1", 1L);
        return map;
    }
}
