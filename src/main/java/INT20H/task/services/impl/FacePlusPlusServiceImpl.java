package INT20H.task.services.impl;

import INT20H.task.model.dto.ImageFaceDto;
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
import java.util.stream.Collectors;

@Service
@Log4j2
public class FacePlusPlusServiceImpl implements FacePlusPlusService {

    private Map<String, List<PhotoSizeDto>> emogiesMap; //extract dto

    private final FlickrService flickrService;

    public FacePlusPlusServiceImpl(FlickrService flickrService) {
        this.flickrService = flickrService;
    }
    private String key = "JNsr371qG2YY0jYB8MLs5M_E9QYsDOt4";
    private String secret = "PO04I-3jZxB1BbBeWc6VqQxhmNCFjJFZ";
    @Override
    @Scheduled(initialDelay = 10 * 1000, fixedDelay = 1000)
    public void cacheEmogies() {

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
//                List<String> emotionsByUrl = getEmogiesByListOfFaceDto(source);
//                for (String emogy : emotionsByUrl) {
//                    List<PhotoSizeDto> listOfSizesByEmogy = emogiesMapBuffer.get(emogy);
//                    if (listOfSizesByEmogy == null) {
//                        listOfSizesByEmogy = new ArrayList<>();
//                        emogiesMapBuffer.put(emogy, listOfSizesByEmogy);
//                    } else {
//                        listOfSizesByEmogy.add(new PhotoSizeDto(photoSizeDto.getId(), photoSizeDto.getListOfSizes()));
//                    }
//                }
            }
            emogiesMap = emogiesMapBuffer; //TODO optimize cache, don't cache already parsed image
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public List<PhotoSizeDto> getAllEmogies(String emogie, int page) {
        List<PhotoSizeDto> listOfPhotoSizeDto = emogiesMap.get(emogie);

        int toIndex = photoLimit_ * (page + 1) > listOfPhotoSizeDto.size() ? listOfPhotoSizeDto.size() : photoLimit_ * (page + 1);
        int fromIndex = (page * photoLimit_) > listOfPhotoSizeDto.size() ? listOfPhotoSizeDto.size() : (page * photoLimit_);

        return listOfPhotoSizeDto.subList(fromIndex, toIndex);
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
    public List<ImageFaceDto> getEmogiesByListOfFaceDto(List<ImageFaceDto> listOfImageFaceDto) {
        System.out.println("getEmogiesByListOfFaceDto");
        List<String> responce = null;
        try {
            listOfImageFaceDto.stream().forEach(image -> {
                image.setListOfTokens(getFaceTokensByUrl(image.getUrl()));
            });
            Map<Integer, List<String>> indexes = new HashMap<>();
            for (int i = 0; i < listOfImageFaceDto.size(); i++) {
                List<String> tokens = listOfImageFaceDto.get(i).getListOfTokens();
                for (int j = 0; j < tokens.size(); j++) {
                    if (indexes.get(i) == null) indexes.put(i, new ArrayList<>());
                    indexes.get(i).add(tokens.get(j));
                    if (indexes.values().stream().mapToInt(List::size).sum() == 5 || i == listOfImageFaceDto.size()-1) {
                        List<String> list = new ArrayList<>();
                        indexes.values().forEach(list::addAll);
                        responce = getEmoutionsByTokens(list);
                        int f = 0;
                        int count = 0;
                        for (Integer t : indexes.keySet()) {
                             count += indexes.get(t).size();
                            listOfImageFaceDto.get(t).setEmogies(responce.subList(f, count).stream().collect(Collectors.toSet()));
                            f = count;
                        }
                       indexes = new HashMap<>();
                    }
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return listOfImageFaceDto;
    }


}
