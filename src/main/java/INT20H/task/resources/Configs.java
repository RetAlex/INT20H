package INT20H.task.resources;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "int20h")
@PropertySource("classpath:application.properties")
@Component
public class Configs {
    private String flickrApiKey;

    public static String flickrApiKey_;


    public void setFlickrApiKey(String flickrApiKey) {
        System.out.println("Setted " + flickrApiKey);
        this.flickrApiKey = flickrApiKey;
        flickrApiKey_ = flickrApiKey;
    }
}
