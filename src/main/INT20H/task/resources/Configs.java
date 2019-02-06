package INT20H.task.resources;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@ConfigurationProperties(prefix = "int20h")
//@Component
public class Configs {
    private String flickrApiKey;

    public static String flickrApiKey_;

    public void setFlickrApiKey(String flickrApiKey) {
        this.flickrApiKey = flickrApiKey;
        flickrApiKey_ = flickrApiKey;
    }
}
