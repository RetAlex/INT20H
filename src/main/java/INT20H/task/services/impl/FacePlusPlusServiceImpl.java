package INT20H.task.services.impl;

import INT20H.task.model.dto.PhotoDto;
import INT20H.task.model.dto.PhotoSizeDto;
import INT20H.task.services._interfaces.FacePlusPlusService;
import INT20H.task.services._interfaces.FlickrService;
import INT20H.task.utils.FaceAPI;
import org.springframework.scheduling.annotation.Scheduled;
import static INT20H.task.resources.Configs.*;

import java.util.List;
import java.util.Map;

public class FacePlusPlusServiceImpl implements FacePlusPlusService {

    private String key = "JNsr371qG2YY0jYB8MLs5M_E9QYsDOt4";
    private String secret = "PO04I-3jZxB1BbBeWc6VqQxhmNCFjJFZ";

    private Map<String, List<String>> emogiesMap;

    private final FlickrService flickrService;

    public FacePlusPlusServiceImpl(FlickrService flickrService) {
        this.flickrService = flickrService;
    }

    @Override
    @Scheduled
    public void cacheEmogies(){
        for (Map.Entry<PhotoDto, List<PhotoSizeDto>> entry : flickrService.getUrlCache().entrySet()) {

        }
    }

    @Override
    public List<String> getFaceTokensByUrl(String url) {
        List<String> tokens = null;
        try {
            FaceAPI api = new FaceAPI();
            tokens = api.getFacesTokens(key, secret, url);
        } catch (Exception e) {
        }
        return tokens;
    }

    @Override
    public List<String> getEmoutionsByTokens(List<String> tokens) {
        List<String> emotions = null;
        try {
            FaceAPI api = new FaceAPI();
            emotions = api.getEmotionsByFaceTokens(key, secret, tokens);
        } catch (Exception e) {
        }
        return emotions;
    }
}
