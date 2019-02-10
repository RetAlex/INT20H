package INT20H.task.resources;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "int20h")
@Component
public class Configs {
    private String flickrApiKey;
    private String flickrApiSecret;

    private String faceApiKey;


    private String faceApiSecret;

    private String detectAPI;
    private String faceAnalyzeAPI;

    private String i20HphotosetId;
    private String tag;
    private int photoLimit;
    private int defaultLabel;

    public static String flickrApiKey_;

    public static String detectAPI_;
    public static String faceAnalyzeAPI_;

    public static String flickrApiSecret_;
    public static String i20HphotosetId_;
    public static String tag_;
    public static int photoLimit_;
    public static int defaultLabel_;

    public static String faceApiKey_; //  = "JNsr371qG2YY0jYB8MLs5M_E9QYsDOt4";
    public static String faceApiSecret_; // = "3jZxB1BbBeWc6VqQxhmNCFjJFZ";

    public void setFlickrApiSecret(String flickrApiSecret) {
        flickrApiSecret_ = this.flickrApiSecret = flickrApiSecret;
    }

    public void setI20HphotosetId(String i20HphotosetId) {
        i20HphotosetId_ = this.i20HphotosetId = i20HphotosetId;
    }

    public void setTag(String tag) {
        tag_ = this.tag = tag;
    }

    public void setPhotoLimit(int photoLimit) {
        photoLimit_ = this.photoLimit = photoLimit;
    }

    public void setFlickrApiKey(String flickrApiKey) {
        flickrApiKey_ = this.flickrApiKey = flickrApiKey;
    }

    public void setDefaultLabel_(int defaultLabel) {
        defaultLabel_ = this.defaultLabel = defaultLabel;
    }

    public void setFaceApiKey(String faceApiKey) {
        faceApiKey_ = this.faceApiKey = faceApiKey;
    }

    public void setFaceApiSecret(String faceApiSecret) {
        faceApiSecret_ = this.faceApiSecret = faceApiSecret;
    }


    public void setDetectAPI(String detectAPI) {
        detectAPI_ = this.detectAPI = detectAPI;
    }

    public void setFaceAnalyzeAPI(String faceAnalyzeAPI) {
        faceApiSecret_ = this.faceAnalyzeAPI = faceAnalyzeAPI;
    }
}
