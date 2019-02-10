package INT20H.task.services.impl;

import INT20H.task.model.dto.ImageFaceDto;
import INT20H.task.model.dto.PhotoSizeDto;
import INT20H.task.services._interfaces.FacePlusPlusService;
import INT20H.task.services._interfaces.FlickrService;
import INT20H.task.utils.FaceAPI;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static INT20H.task.resources.Configs.faceApiKey_;
import static INT20H.task.resources.Configs.faceApiSecret_;
import static INT20H.task.utils.Pagination.getByPage;

@Service
@Log4j2
public class FacePlusPlusServiceImpl implements FacePlusPlusService {

    private final FlickrService flickrService;

    private Map<String, List<PhotoSizeDto>> emotionsMap = new HashMap<>();
    private Set<String> listOfCachedId;
    private final static int defaultFaceLabel = 4; //todo

    public FacePlusPlusServiceImpl(FlickrService flickrService) {
        this.flickrService = flickrService;
    }

    @Override
    @Scheduled(initialDelay = 20*1000, fixedDelay = 1000)
    public void cacheEmotions(){
        listOfCachedId = getListOfCachedId();

        try {
            List<PhotoSizeDto> photoCache = flickrService.getPhotoCache();
            if (photoCache == null || photoCache.size() == 0){
                log.info("photoCache null or zero size, can not cache emotions");
                return;
            }

            List<ImageFaceDto> listOfImageFaceDto = photoCache.stream().map(e -> new ImageFaceDto(e.getId(), getDefaultSource(e), e.getListOfSizes())).collect(Collectors.toList());
            setEmotionsForImageFaceDto(listOfImageFaceDto);
            for (ImageFaceDto imageFaceDto : listOfImageFaceDto) {
                for (String emotion : imageFaceDto.getEmotion()) {
                    List<PhotoSizeDto> listOfSizesByEmotion = emotionsMap.get(emotion);
                    if (listOfSizesByEmotion == null) {
                        listOfSizesByEmotion = new ArrayList<>();
                        listOfSizesByEmotion.add(new PhotoSizeDto(imageFaceDto.getId(), imageFaceDto.getListOfSizes()));
                        emotionsMap.put(emotion, listOfSizesByEmotion);
                    } else {
                        listOfSizesByEmotion.add(new PhotoSizeDto(imageFaceDto.getId(), imageFaceDto.getListOfSizes()));
                    }
                }
            }

            log.info("Emotions cache size = " + emotionsMap.size());
        } catch (Exception e){
            log.error(e);
        }
    }

    private Set<String> getListOfCachedId() {
        Set<String> listOfCachedId = null;
        if(emotionsMap != null && emotionsMap.size() > 0) {
             listOfCachedId = emotionsMap.entrySet().stream().flatMap(e -> e.getValue().stream()).map(PhotoSizeDto::getId).collect(Collectors.toSet());
        }
        return listOfCachedId;
    }



    private String getDefaultSource(PhotoSizeDto photoSizeDto) {
        return photoSizeDto.getListOfSizes().stream().filter(e -> e.getLabel() == defaultFaceLabel).findFirst().orElseThrow(() -> new RuntimeException("Can not find defaul label!")).getSource();
    }

    private boolean isAlreadyCached(Set<String> listOfCachedId, PhotoSizeDto photoSizeDto) {
        return listOfCachedId != null && listOfCachedId.contains(photoSizeDto.getId());
    }

    @Override
    public List<PhotoSizeDto> getAllEmogies(String emotion, int page) {
        List<PhotoSizeDto> listOfPhotoSizeDto = emotionsMap.get(emotion);

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


    public List<ImageFaceDto> setEmotionsForImageFaceDto(List<ImageFaceDto> listOfImageFaceDto) {
        if(listOfCachedId != null) listOfImageFaceDto.removeIf(e -> listOfCachedId.contains(e.getId()));

        List<String> response = null;
        try {
            listOfImageFaceDto.forEach(image -> {
                image.setListOfTokens(getFaceTokensByUrl(image.getUrl()));
            });
            Map<Integer, List<String>> indexes = new HashMap<>();
            for (int i = 0; i < listOfImageFaceDto.size(); i++) {
                List<String> tokens = listOfImageFaceDto.get(i).getListOfTokens();
                for (int j = 0; j < tokens.size(); j++) {
                    indexes.computeIfAbsent(i, k -> new ArrayList<>());
                    indexes.get(i).add(tokens.get(j));
                    if (indexes.values().stream().mapToInt(List::size).sum() == 5 || i == listOfImageFaceDto.size()-1) {
                        List<String> list = new ArrayList<>();
                        indexes.values().forEach(list::addAll);
                        response = getEmoutionsByTokens(list);
                        int f = 0;
                        int count = 0;
                        for (Integer t : indexes.keySet()) {
                             count += indexes.get(t).size();
                            listOfImageFaceDto.get(t).setEmotion(new HashSet<>(response.subList(f, count)));
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
