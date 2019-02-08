package INT20H.task.services._interfaces;

import com.flickr4java.flickr.photos.Size;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface FacePlusPlusService {
    @Scheduled
    void cacheEmogies();

    List<String> getEmotionsByUrl(String url);
    List<List<Size>> getAllEmogies(String emogie);
}
