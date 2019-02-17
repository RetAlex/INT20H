package INT20H.task.resources.configuration;

import INT20H.task.model.properties.FaceProperties;
import INT20H.task.model.properties.FlickrProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "face")
@Component
@Data
public class FaceConfig {
    private int defaultLabel_;
    private FaceProperties int20h;

    public static int defaultLabel;

    public static void setDefaultLabel(int defaultLabel_) {
        defaultLabel = defaultLabel_;
    }
}
