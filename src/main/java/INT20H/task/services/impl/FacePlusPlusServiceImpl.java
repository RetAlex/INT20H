package INT20H.task.services.impl;

import INT20H.task.model.dto.PhotoSizeDto;
import INT20H.task.services._interfaces.FacePlusPlusService;
import INT20H.task.services._interfaces.FlickrService;
import INT20H.task.utils.FaceAPI;
import com.flickr4java.flickr.photos.Size;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static INT20H.task.resources.Configs.*;
import static INT20H.task.utils.Pagination.getByPage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class FacePlusPlusServiceImpl implements FacePlusPlusService {

    private Map<String, List<PhotoSizeDto>> emotionsMap = new HashMap<>();
    private final FlickrService flickrService;

    private final static int defaultFaceLabel = 4;

    public FacePlusPlusServiceImpl(FlickrService flickrService) {
        this.flickrService = flickrService;
    }

    @Override
    @Scheduled(initialDelay = 20*1000, fixedDelay = 1000)
    public void cacheEmotions(){
        Set<String> listOfCachedId = null;
        if(emotionsMap != null && emotionsMap.size() > 0) {
             listOfCachedId = emotionsMap.entrySet().stream().flatMap(e -> e.getValue().stream()).map(PhotoSizeDto::getId).collect(Collectors.toSet());
        }

        try {
            List<PhotoSizeDto> photoCache = flickrService.getPhotoCache();
            if (photoCache == null || photoCache.size() == 0){
                log.info("photoCache null or zero size, can not cache emotions");
                return;
            }

            for (PhotoSizeDto photoSizeDto : photoCache) {
                if (isAlreadyCached(listOfCachedId, photoSizeDto)) continue;
                addNewPhotoForEmotions(photoSizeDto);
            }
            log.info("Emotions cache size = " + emotionsMap.size());
        } catch (Exception e){
            log.error(e);
        }
    }

    private void addNewPhotoForEmotions(PhotoSizeDto photoSizeDto) throws Exception {
        for (String emotion : getListOfEmotions(photoSizeDto)) {
            List<PhotoSizeDto> listOfSizesByEmotion = emotionsMap.get(emotion);
            if (listOfSizesByEmotion == null) {
                listOfSizesByEmotion = new ArrayList<>();
                emotionsMap.put(emotion, listOfSizesByEmotion);
            } else {
                listOfSizesByEmotion.add(new PhotoSizeDto(photoSizeDto.getId(), photoSizeDto.getListOfSizes()));
            }
        }
    }

    private List<String> getListOfEmotions(PhotoSizeDto photoSizeDto) throws Exception {
        String source = photoSizeDto.getListOfSizes().stream().filter(e -> e.getLabel() == defaultFaceLabel).findFirst().orElseThrow(() -> new Exception("Default label " + defaultFaceLabel + " not found!")).getSource();

        return getEmotionsByUrl(source);
    }

    private boolean isAlreadyCached(Set<String> listOfCachedId, PhotoSizeDto photoSizeDto) {
        if(listOfCachedId != null && listOfCachedId.contains(photoSizeDto.getId())){
            return true;
        }
        return false;
    }

    @Override
    public List<PhotoSizeDto> getAllEmogies(String emogie, int page) {
        List<PhotoSizeDto> listOfPhotoSizeDto = emotionsMap.get(emogie);

        return getByPage(page, listOfPhotoSizeDto);
    }

    @Override
    public Map<String, List<PhotoSizeDto>> getEmotionsMap() {
        return emotionsMap;
    }

    private List<String> getFaceTokensByUrl(String url) {
        List<String> tokens = null;
        try {
            FaceAPI api = new FaceAPI();
            tokens = api.getFacesTokens(faceApiKey_, faceApiSecret_, url);
        } catch (Exception e) {
            log.error(e);
        }
        return tokens;
    }


    private List<String> getEmoutionsByTokens(List<String> tokens) {
        List<String> emotions = null;
        try {
            FaceAPI api = new FaceAPI();
            emotions = api.getEmotionsByFaceTokens(faceApiKey_, faceApiSecret_, tokens);
        } catch (Exception e) {
            log.error(e);
        }
        return emotions;
    }

    @Override
    public List<String> getEmotionsByUrl(String url) {
        log.info("executing getEmotionsByUrl...");
        List<String> emotions = null;
        try {
            List<String> tokens = getFaceTokensByUrl(url);
            emotions = getEmoutionsByTokens(tokens);
        } catch (Exception e) {
            log.error(e);
        }
        return emotions;
    }
}
