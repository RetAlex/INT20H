package INT20H.task.resources.configuration;

import INT20H.task.model.properties.FlickrProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "flickr")
@Component
@Data
public class FlickrConfig {
    private String i20HphotosetId;
    private String tag;
    private int photoLimit;
    private int defaultFaceLabel;
    private String rootCacheDir_;

    private FlickrProperties int20h;

    public static String flickrApiKey_;
    public static String flickrApiSecret_;
    public static String i20HphotosetId_;
    public static String tag_;
    public static String faceApiKey_;
    public static String faceApiSecret_;
    public static String rootCacheDir;
    public static int photoLimit_;



    public void setI20HphotosetId(String i20HphotosetId) {
        i20HphotosetId_ = this.i20HphotosetId = i20HphotosetId;
    }

    public void setTag(String tag) {
        tag_ = this.tag = tag;
    }

    public void setPhotoLimit(int photoLimit) {
        photoLimit_ = this.photoLimit = photoLimit;
    }

    public void setRootCacheDir_(String cacheDir) {
        this.rootCacheDir = cacheDir;
    }
}
