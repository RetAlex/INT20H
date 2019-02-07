package INT20H.task.services._interfaces;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface FacePlusPlusService {
    @Scheduled
    void cacheEmogies();

    public List<String> getEmotionsByUrl(String url);
}
