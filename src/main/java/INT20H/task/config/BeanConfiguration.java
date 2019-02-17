package INT20H.task.config;

import INT20H.task.resources.ApiConfig;
import INT20H.task.services._interfaces.FacePlusPlusService;
import INT20H.task.services._interfaces.FlickrApiService;
import INT20H.task.services.impl.FacePlusPlusServiceImpl;
import INT20H.task.services.impl.FlickrApiServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    private final ApiConfig apiConfig;

    public BeanConfiguration(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    @Bean
    public FlickrApiService flickrApi(){
        return new FlickrApiServiceImpl(apiConfig.getFlickrConfig().getInt20h());
    }

    @Bean
    public FacePlusPlusService faceApiService(){
        return new FacePlusPlusServiceImpl(apiConfig.getFaceConfig().getInt20h());
    }
}
