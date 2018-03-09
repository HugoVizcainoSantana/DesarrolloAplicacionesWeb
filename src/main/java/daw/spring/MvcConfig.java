package daw.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        String resourcePath = Application.UPLOADED_FILES_PATH.toAbsolutePath().toUri().toString();
        log.info(resourcePath);
        registry.addResourceHandler("/uploaded/**/*").addResourceLocations(resourcePath);
    }


}
