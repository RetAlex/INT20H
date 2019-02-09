package INT20H.task.services._interfaces;

import INT20H.task.model.dto.PhotoSizeDto;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

public interface FacePlusPlusService {
    @Scheduled
    void cacheEmotions();

    List<String> getEmotionsByUrl(String url);

    List<PhotoSizeDto> getAllEmogies(String emogie, int page);

    Map<String, List<PhotoSizeDto>> getEmotionsMap();
}
