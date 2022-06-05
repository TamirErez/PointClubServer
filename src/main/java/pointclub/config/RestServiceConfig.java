package pointclub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pointclub.service.restservice.RestService;
import pointclub.service.restservice.RestServiceImpl;

@Configuration
public class RestServiceConfig {
    @Value("${rest.path}")
    private String path;

    @Bean
    public RestService restService(){
        return new RestServiceImpl(path);
    }
}
