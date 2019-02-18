package INT20H.task.services.impl.flickr;

import INT20H.task.model.dto.ImagesDto;
import INT20H.task.model.dto.PhotoSizeDto;
import INT20H.task.model.dto.RequestPhotoDto;
import INT20H.task.services.interfaces.FlickrApiService;
import INT20H.task.services.interfaces.FlickrService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

import static INT20H.task.resources.configuration.FlickrConfig.*;
import static INT20H.task.utils.CacheUtils.*;
import static INT20H.task.utils.Pagination.getByPage;

@Service
@Log4j2
@Data
public class FlickrServiceImpl implements FlickrService {
    private List<PhotoSizeDto> photoCache;
    private Set<String> setOfCachedPhotoId = new HashSet<>();
    private final FlickrApiService flickrApiService;

    public FlickrServiceImpl(FlickrApiService flickrApiService) {
        this.flickrApiService = flickrApiService;
        photoCache = (List<PhotoSizeDto>) loadCacheFromFile(photoCacheDir, new TypeReference<List<PhotoSizeDto>>() {});
        if(photoCache == null) photoCache = new ArrayList<>();
        log.info("Successfully read list of " + photoCache.size() + " elements from file!");
    }

    @Scheduled(initialDelay = 0, fixedDelay = 10000)
    @Override
    public void loadCache() {
        try {
            if (photoCache.size() > 0) {
                setOfCachedPhotoId = photoCache.stream().map(PhotoSizeDto::getId).collect(Collectors.toSet());
            }

            RequestPhotoDto searchPhotoDto = new RequestPhotoDto(i20HphotosetId_, tag_);
            getUrlByAlbumIdAndTag(searchPhotoDto);
            storeCache(photoCache, photoCacheDir);
            log.info("Cache size = " + photoCache.size());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public ImagesDto getAllImages(int page) {
        return new ImagesDto(photoCache.size(), getByPage(page, photoCache));
    }

    private void getUrlByAlbumIdAndTag(RequestPhotoDto requestPhotoDto) throws Exception {
        List<PhotoSizeDto> listOfPhotoSizeDto = getListOfNewPhotoSizeDto(requestPhotoDto, Integer.MAX_VALUE);

        photoCache.addAll(removeNotUniqOrNull(listOfPhotoSizeDto));
    }

    private List<PhotoSizeDto> removeNotUniqOrNull(List<PhotoSizeDto> listOfPhotoSizeDto) {
        List<PhotoSizeDto> list = listOfPhotoSizeDto.stream().filter(dto -> Objects.nonNull(dto.getListOfSizes())).distinct().collect(Collectors.toList());
        list.removeIf(dto -> list.stream().filter(e -> e.getId().equals(dto.getId())).collect(Collectors.toList()).size() > 1);
        return list;
    }

    private List<PhotoSizeDto> getListOfNewPhotoSizeDto(RequestPhotoDto requestPhotoDto, int amount) throws Exception {
        List<PhotoSizeDto> imagesUrlFromAlbum = new ArrayList<>();
        if(requestPhotoDto.getAlbumId() != null) imagesUrlFromAlbum = getImagesUrlFromAlbum(requestPhotoDto, amount);

        List<PhotoSizeDto> imagesUrlByTag = getImagesUrlByTag(requestPhotoDto, amount);
        imagesUrlByTag.addAll(imagesUrlFromAlbum);
        return imagesUrlByTag;
    }

    private List<PhotoSizeDto> getImagesUrlFromAlbum(RequestPhotoDto photo, int amount) throws Exception {
        PhotoList<Photo> photos = flickrApiService.getPhotosByAlbumId(photo, amount);
        log.info("start getImagesUrlFromAlbum");

        return convertToPhotoSizeDtoList(photos);
    }

    private List<PhotoSizeDto> getImagesUrlByTag(RequestPhotoDto requestPhotoDto, int amount) throws Exception {
        PhotoList<Photo> search = flickrApiService.searchByTag(requestPhotoDto, amount);

        log.info("start getImagesUrlByTag");
        return convertToPhotoSizeDtoList(search);
    }

    private void removeAlreadyCached(PhotoList<Photo> photos) {
        if(setOfCachedPhotoId.size() > 0){
            photos.removeIf(next -> setOfCachedPhotoId.contains(next.getId()));
        }
    }

    private List<PhotoSizeDto> convertToPhotoSizeDtoList(PhotoList<Photo> photos) {
        removeAlreadyCached(photos);
        return photos.stream().map(Photo::getId).map(flickrApiService::getPhotoSizeDto).collect(Collectors.toList());
    }

}
