package INT20H.task.services.impl;

import INT20H.task.model.dto.PhotoDto;
import INT20H.task.model.dto.PhotoSizeDto;
import INT20H.task.services._interfaces.FacePlusPlusService;
import INT20H.task.services._interfaces.FlickrService;
import INT20H.task.utils.FaceAPI;
import com.flickr4java.flickr.photos.Size;
import org.springframework.scheduling.annotation.Scheduled;
import static INT20H.task.resources.Configs.*;

import java.util.List;
import java.util.Map;

import static INT20H.task.resources.Configs.*;

public class FacePlusPlusServiceImpl implements FacePlusPlusService {

    private Map<String, List<String>> emogiesMap;

    private final FlickrService flickrService;

    public FacePlusPlusServiceImpl(FlickrService flickrService) {
        this.flickrService = flickrService;
    }

    @Override
    @Scheduled
    public void cacheEmogies(){
        for (Map.Entry<PhotoDto, List<PhotoSizeDto>> entry : flickrService.getUrlCache().entrySet()) {
            Size size = entry.getValue().stream().flatMap(e -> e.getListOfSizes().stream()).filter(e -> e.getLabel() == defaultLabel_).findFirst().get();
            emogiesMap.put(entry.getValue().get(0).getId(), getEmotionsByUrl(size.getSource())); // TODO change emogiesMap, should contain <String, PhotoSizesDto>!!!
        }
    }

    private List<String> getFaceTokensByUrl(String url) {
        List<String> tokens = null;
        try {
            FaceAPI api = new FaceAPI();
            tokens = api.getFacesTokens(faceApiKey_, faceApiSecret_, url);
        } catch (Exception e) {
        }
        return tokens;
    }


    private List<String> getEmoutionsByTokens(List<String> tokens) {
        List<String> emotions = null;
        try {
            FaceAPI api = new FaceAPI();
            emotions = api.getEmotionsByFaceTokens(faceApiKey_, faceApiSecret_, tokens);
        } catch (Exception e) {
        }
        return emotions;
    }

    @Override
    public List<String> getEmotionsByUrl(String url) {
        List<String> emotions = null;
        try {
            List<String> tokens = getFaceTokensByUrl(url);
            emotions = getEmoutionsByTokens(tokens);
        } catch (Exception e) {

        }
        return emotions;
    }
}
