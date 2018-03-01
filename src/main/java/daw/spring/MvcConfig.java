package daw.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.file.Paths;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // TODO Auto-generated method stub
        super.addResourceHandlers(registry);
        String resourcePath = Paths.get("upload").toAbsolutePath().toUri().toString();
        log.info(resourcePath);
        registry.addResourceHandler("/upload/**").addResourceLocations(resourcePath);
    }


}
