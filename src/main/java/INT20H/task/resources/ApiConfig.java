package INT20H.task.resources;

import INT20H.task.resources.configuration.FaceConfig;
import INT20H.task.resources.configuration.FlickrConfig;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ApiConfig {
    private final FlickrConfig flickrConfig;
    private final FaceConfig faceConfig;


    public ApiConfig(FlickrConfig flickrConfig, FaceConfig faceConfig) {
        this.flickrConfig = flickrConfig;
        this.faceConfig = faceConfig;
    }
}
