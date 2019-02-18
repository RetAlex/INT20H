package INT20H.task.services.impl.flickr;

import INT20H.task.resources.ApiConfig;
import INT20H.task.services.interfaces.FlickrApiService;
import INT20H.task.services.interfaces.FlickrService;
import INT20H.task.utils.CacheUtils;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ApiConfig.class)
public class FlickrTestConfig {

    @MockBean
    private FlickrApiService flickrApiService;

    @MockBean
    private CacheUtils cacheUtils;

    @Bean
    public FlickrService flickrService(FlickrApiService flickrApiService){
        return new FlickrServiceImpl(flickrApiService, cacheUtils);
    }
}