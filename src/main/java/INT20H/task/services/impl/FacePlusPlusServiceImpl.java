package INT20H.task.services.impl;

import INT20H.task.model.dto.EmotionsDto;
import INT20H.task.model.dto.ImageFaceDto;
import INT20H.task.model.dto.PhotoSizeDto;
import INT20H.task.services._interfaces.FacePlusPlusService;
import INT20H.task.services._interfaces.FlickrService;
import INT20H.task.utils.FaceAPI;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static INT20H.task.resources.Configs.*;
import static INT20H.task.utils.CacheUtils.*;
import static INT20H.task.utils.Pagination.getByPage;

@Service
@Log4j2
public class FacePlusPlusServiceImpl implements FacePlusPlusService {
    private final FlickrService flickrService;

    private Map<String, List<PhotoSizeDto>> emotionsMap;

    private Set<String> listOfCachedId = new HashSet<>();
    private final static String UNKNOWN_EMOTION = "unknown";

    public FacePlusPlusServiceImpl(FlickrService flickrService) {
        emotionsMap = (Map<String, List<PhotoSizeDto>>) loadCacheFromFile(emotionCacheDir, new TypeReference<Map<String, List<PhotoSizeDto>>>() {});
        if(emotionsMap == null) emotionsMap = new HashMap<>();
        this.flickrService = flickrService;
    }

    @Override
    @Scheduled(initialDelay = 20*1000, fixedDelay = 10000)
    public void cacheEmotions(){
        listOfCachedId = getListOfCachedId();

        try {
            List<PhotoSizeDto> photoCache = flickrService.getPhotoCache();
            if (photoCache == null || photoCache.size() == 0){
                log.warn("Photo Cache null or zero size, can not cache emotions");
                return;
            }

            List<ImageFaceDto> listOfImageFaceDto = photoCache.stream().map(e -> new ImageFaceDto(e.getId(), getDefaultSource(e), e.getListOfSizes())).collect(Collectors.toList());
            if(listOfCachedId != null) listOfImageFaceDto.removeIf(e -> listOfCachedId.contains(e.getId()));
            for (ImageFaceDto imageFaceDto : listOfImageFaceDto) {
                List<String> emotionsByUrl = getEmotionsByUrl(imageFaceDto.getUrl());
                imageFaceDto.setEmotion(new HashSet<>(emotionsByUrl));
            }

            for (ImageFaceDto imageFaceDto : listOfImageFaceDto) {
                addNewImageByEmotion(imageFaceDto);
            }

            if(emotionsMap.entrySet().size() > 0) storeCache(emotionsMap, emotionCacheDir);

            log.info("Emotions cache size = " + emotionsMap.size() + "; " + "Photo with filtered emotion count = " + listOfCachedId.size());
        } catch (Exception e){
            log.error(e.getMessage());
        }
    }

    private void addNewImageByEmotion(ImageFaceDto imageFaceDto) {
        for (String emotion : imageFaceDto.getEmotion()) {
            putOrUpdate(imageFaceDto, emotion);
        }

        processNullableEmotion(imageFaceDto);
    }

    private void putOrUpdate(ImageFaceDto imageFaceDto, String emotion) {
        List<PhotoSizeDto> listOfSizesByEmotion = emotionsMap.get(emotion);
        if (listOfSizesByEmotion == null) {
            listOfSizesByEmotion = new ArrayList<>();
            listOfSizesByEmotion.add(new PhotoSizeDto(imageFaceDto.getId(), imageFaceDto.getListOfSizes()));
            emotionsMap.put(emotion, listOfSizesByEmotion);
        } else {
            listOfSizesByEmotion.add(new PhotoSizeDto(imageFaceDto.getId(), imageFaceDto.getListOfSizes()));
        }
    }

    private void processNullableEmotion(ImageFaceDto imageFaceDto) {
        if(imageFaceDto.getEmotion().size() == 0){
            putOrUpdate(imageFaceDto, UNKNOWN_EMOTION);
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
        return photoSizeDto.getListOfSizes().stream().filter(e -> e.getLabel() == defaultFaceLabel_).findFirst().orElseThrow(() -> new RuntimeException("Can not find defaul label!")).getSource();
    }

    @Override
    public EmotionsDto getAllEmogies(String emotion, int page) {
        List<PhotoSizeDto> listOfPhotoSizeDto = emotionsMap.get(emotion);
        return new EmotionsDto(listOfPhotoSizeDto.size(), getByPage(page, listOfPhotoSizeDto));
    }

    @Override
    public Map<String, List<PhotoSizeDto>> getEmotionsMap() {
        return emotionsMap;
    }

    private List<String> getFaceTokensByUrl(String url) {
        log.info("Starting getFaceTokensByUrl");
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
        log.info("Starting getEmoutionsByTokens");
        List<String> emotions = null;
        try {
            FaceAPI api = new FaceAPI();
            emotions = api.getEmotionsByFaceTokens(faceApiKey_, faceApiSecret_, tokens);
        } catch (Exception e) {
            log.error(e);
        }
        return emotions;
    }


//    public List<ImageFaceDto> setEmotionsForImageFaceDto(List<ImageFaceDto> listOfImageFaceDto) {
//        if(listOfCachedId != null) listOfImageFaceDto.removeIf(e -> listOfCachedId.contains(e.getId()));
//
//        List<String> response;
//        try {
//            listOfImageFaceDto = getTokensForListOfImage(listOfImageFaceDto);
//
//            Map<Integer, List<String>> indexes = new HashMap<>();
//            for (int i = 0; i < listOfImageFaceDto.size(); i++) {
//                List<String> tokens = listOfImageFaceDto.get(i).getListOfTokens();
//                for (String token : tokens) {
//                    indexes.computeIfAbsent(i, k -> new ArrayList<>());
//                    indexes.get(i).add(token);
//                    if (indexes.values().stream().mapToInt(List::size).sum() == 5 || i == listOfImageFaceDto.size() - 1) {
//                        response = getEmoutionsByTokens(getValuesOfMapOfLists(indexes));
//                        listOfImageFaceDto = setGotEmotionsToImages(listOfImageFaceDto, response, indexes);
//                        indexes = new HashMap<>();
//                    }
//                }
//            }
//        } catch (Exception e) {
//            log.error(e);
//        }
//        return listOfImageFaceDto;
//    }

    private List<String> getValuesOfMapOfLists(Map<Integer, List<String>> indexes) {
        List<String> list = new ArrayList<>();
        try {
            indexes.values().forEach(list::addAll);
        } catch (Exception e) {
            log.error(e);
        }
        return list;
    }

    private List<ImageFaceDto> setGotEmotionsToImages(List<ImageFaceDto> images, List<String> emotions, Map<Integer, List<String>> indexes) {
        try {
            int f = 0;
            int count = 0;
            for (Integer t : indexes.keySet()) {
                count += indexes.get(t).size();
                images.get(t).setEmotion(new HashSet<>(emotions.subList(f, count)));
                f = count;
            }
        } catch (Exception e) {
            log.error(e);
        }
        return images;
    }


    private List<ImageFaceDto> getTokensForListOfImage(List<ImageFaceDto> images) {
        try {
            images.forEach(image -> {
                image.setListOfTokens(getFaceTokensByUrl(image.getUrl()));
            });
        } catch (Exception e) {
            log.error(e);
        }
        return images;
    }

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
