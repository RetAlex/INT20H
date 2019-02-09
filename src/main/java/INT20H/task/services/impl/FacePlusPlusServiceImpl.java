package INT20H.task.services.impl;

import INT20H.task.model.dto.RequestPhotoDto;
import INT20H.task.model.dto.PhotoSizeDto;
import INT20H.task.services._interfaces.FacePlusPlusService;
import INT20H.task.services._interfaces.FlickrService;
import INT20H.task.utils.FaceAPI;
import com.flickr4java.flickr.photos.Size;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static INT20H.task.resources.Configs.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class FacePlusPlusServiceImpl implements FacePlusPlusService {

    private Map<String, List<PhotoSizeDto>> emogiesMap = new HashMap<>(); //no needs for using thread-safe collection
    private final FlickrService flickrService;

    private final static int defaultFaceLabel = 4;

    public FacePlusPlusServiceImpl(FlickrService flickrService) {
        this.flickrService = flickrService;
    }

    @Override
    @Scheduled(initialDelay = 10*1000, fixedDelay = 1000)
    public void cacheEmogies(){
        Set<String> listOfCachedId = null;
        if(emogiesMap != null && emogiesMap.size() > 0) {
             listOfCachedId = emogiesMap.entrySet().stream().flatMap(e -> e.getValue().stream()).map(e -> e.getId()).collect(Collectors.toSet());
        }

        try {
            RequestPhotoDto requestPhotoDto = new RequestPhotoDto(i20HphotosetId_, tag_, defaultLabel_);
            Map<RequestPhotoDto, List<PhotoSizeDto>> urlCache = flickrService.getUrlCache();
            if (urlCache == null || urlCache.size() == 0) return;

            List<PhotoSizeDto> photoSizeDtos = urlCache.get(requestPhotoDto);
            for (PhotoSizeDto photoSizeDto : photoSizeDtos) {
                if (isAlreadyCached(listOfCachedId, photoSizeDto)) continue;

                List<Size> listOfSizes = photoSizeDto.getListOfSizes();
                if(listOfSizes == null){
                    log.info("Empty listOfSizes");
                    continue;
                }
                String source = listOfSizes.stream().filter(e -> e.getLabel() == defaultFaceLabel).findFirst().orElseThrow(() -> new Exception("Default label " + defaultFaceLabel + " not found!")).getSource();

                List<String> emotionsByUrl = getEmotionsByUrl(source);

                for (String emotion : emotionsByUrl) {
                    List<PhotoSizeDto> listOfSizesByEmotion = emogiesMap.get(emotion);
                    if (listOfSizesByEmotion == null) {
                        listOfSizesByEmotion = new ArrayList<>();
                        emogiesMap.put(emotion, listOfSizesByEmotion);
                        System.out.println("put");
                    } else {
                        listOfSizesByEmotion.add(new PhotoSizeDto(photoSizeDto.getId(), photoSizeDto.getListOfSizes()));
                    }
                }
            }
        } catch (Exception e){
            log.error(e);
        }
    }

    private boolean isAlreadyCached(Set<String> listOfCachedId, PhotoSizeDto photoSizeDto) {
        if(listOfCachedId != null && listOfCachedId.contains(photoSizeDto.getId())){
            System.out.println("continue");
            return true;
        }
        return false;
    }

    @Override
    public List<PhotoSizeDto> getAllEmogies(String emogie, int page) {
        List<PhotoSizeDto> listOfPhotoSizeDto = emogiesMap.get(emogie);

        return getPhotoSizeDtos(page, listOfPhotoSizeDto);
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
        System.out.println("getEmotionsByUrl");
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
