package INT20H.task.services.impl;

import INT20H.task.model.dto.PhotoDto;
import INT20H.task.model.dto.PhotoSizeDto;
import INT20H.task.services._interfaces.FacePlusPlusService;
import INT20H.task.services._interfaces.FlickrService;
import INT20H.task.utils.FaceAPI;
import com.flickr4java.flickr.photos.Size;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static INT20H.task.resources.Configs.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class FacePlusPlusServiceImpl implements FacePlusPlusService {

    private Map<String, List<PhotoSizeDto>> emogiesMap; //extract dto

    private final FlickrService flickrService;

    public FacePlusPlusServiceImpl(FlickrService flickrService) {
        this.flickrService = flickrService;
    }

    @Override
    @Scheduled(initialDelay = 10*1000, fixedDelay = 1000)
    public void cacheEmogies(){

        try {
            Map<String, List<PhotoSizeDto>> emogiesMapBuffer = new HashMap<>(); //extract dto

            PhotoDto photoDto = new PhotoDto(i20HphotosetId_, tag_, defaultLabel_);
            Map<PhotoDto, List<PhotoSizeDto>> urlCache = flickrService.getUrlCache();
            if (urlCache == null || urlCache.size() == 0) return;
            List<PhotoSizeDto> photoSizeDtos = urlCache.get(photoDto);
            for (PhotoSizeDto photoSizeDto : photoSizeDtos) {
                List<Size> listOfSizes = photoSizeDto.getListOfSizes();
                if (listOfSizes == null) continue;
                String source = listOfSizes.stream().filter(e -> e.getLabel() == 4).findFirst().get().getSource();
                List<String> emotionsByUrl = getEmotionsByUrl(source);
                for (String emogy : emotionsByUrl) {
                    List<PhotoSizeDto> listOfSizesByEmogy = emogiesMapBuffer.get(emogy);
                    if (listOfSizesByEmogy == null) {
                        listOfSizesByEmogy = new ArrayList<>();
                        emogiesMapBuffer.put(emogy, listOfSizesByEmogy);
                    } else {
                        listOfSizesByEmogy.add(new PhotoSizeDto(photoSizeDto.getId(), photoSizeDto.getListOfSizes()));
                    }
                }
            }
            emogiesMap = emogiesMapBuffer; //TODO optimize cache, don't cache already parsed image
        } catch (Exception e){
            log.error(e);
        }
    }

    @Override
    public List<PhotoSizeDto> getAllEmogies(String emogie) {
        return emogiesMap.get(emogie);
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
