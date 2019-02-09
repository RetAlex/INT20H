package INT20H.task.services.impl;

import INT20H.task.model.dto.RequestPhotoDto;
import INT20H.task.model.dto.PhotoSizeDto;
import INT20H.task.services._interfaces.FlickrService;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.Size;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

import static INT20H.task.resources.Configs.*;

@Service
@Log4j2
@Data
public class FlickrServiceImpl implements FlickrService {

    private List<PhotoSizeDto> urlCache = new ArrayList<>();
    Set<String> listOfCachedPhotoId = new HashSet<>();

    private static final int ZERO = 0;
    private static volatile int k = 0;
    int amount = 5;

    @PostConstruct
    public void init(){

    }

    @Scheduled(initialDelay = 0, fixedDelay = 1000)
    public void loadCache() throws Exception {
        if(urlCache.size() > 0) {
            listOfCachedPhotoId = urlCache.stream().map(PhotoSizeDto::getId).collect(Collectors.toSet());
        }

        RequestPhotoDto requestPhotoDto = new RequestPhotoDto(i20HphotosetId_, tag_, defaultLabel_); //extract
        getUrlByAlbumIdAndTag(requestPhotoDto);
        log.info("Cache size = " + urlCache.size());
    }

    private void getUrlByAlbumIdAndTag(RequestPhotoDto requestPhotoDto) throws Exception {
        List<PhotoSizeDto> imagesUrlFromAlbum = new ArrayList<>();
        if(requestPhotoDto.getAlbumId() != null) imagesUrlFromAlbum = getImagesUrlFromAlbum(requestPhotoDto, ZERO, amount);

        List<PhotoSizeDto> imagesUrlByTag = getImagesUrlByTag(requestPhotoDto, ZERO, amount);
        imagesUrlByTag.addAll(imagesUrlFromAlbum);

        urlCache.addAll(imagesUrlByTag.stream().distinct().collect(Collectors.toList()));
    }

    @Override
    public List<PhotoSizeDto> getAllImagesUrl(int page) throws Exception { //todo extract
        int toIndex = photoLimit_ * (page + 1) > urlCache.size() ? urlCache.size() : photoLimit_ * (page + 1);
        int fromIndex = (page * photoLimit_) > urlCache.size() ? urlCache.size() : (page * photoLimit_);

        return urlCache.subList(fromIndex, toIndex);
    }

    public List<PhotoSizeDto> getImagesUrlFromAlbum(RequestPhotoDto photo, int page, int amount) throws Exception {
        Flickr f = new Flickr(flickrApiKey_, flickrApiSecret_, new REST());
        PhotoList<Photo> photos = f.getPhotosetsInterface().getPhotos(photo.getAlbumId(), amount, page);
        System.out.println("start getImagesUrlFromAlbum");

        return convertToPhotoSizeDtoList(f, photos);
    }

    private List<PhotoSizeDto> convertToPhotoSizeDtoList(Flickr f, PhotoList<Photo> photos) {
        removeAlreadyCached(photos);
        return photos.stream().map(Photo::getId).map(id -> getPhotoSizeDto(f, id)).collect(Collectors.toList());
    }

    private void removeAlreadyCached(PhotoList<Photo> photos) {
        if(listOfCachedPhotoId.size() > 0){
            photos.removeIf(next -> listOfCachedPhotoId.contains(next.getId()));
        }
    }

    public List<PhotoSizeDto> getImagesUrlByTag(RequestPhotoDto requestPhotoDto, int page, int amount) throws Exception {
        Flickr f = new Flickr(flickrApiKey_, flickrApiSecret_, new REST());
        SearchParameters params = new SearchParameters();
        params.setTags(new String[]{requestPhotoDto.getTag()});
        PhotoList<Photo> search = f.getPhotosInterface().search(params, amount, page);

        System.out.println("start getImagesUrlByTag");
        return convertToPhotoSizeDtoList(f, search);
    }

    private static PhotoSizeDto getPhotoSizeDto(Flickr f, String photoId) {
        try {
            Thread.sleep(50);
            System.out.println("processing  " + ++k);
            return new PhotoSizeDto(photoId, (List<Size>) f.getPhotosInterface().getSizes(photoId));
        } catch (FlickrException | InterruptedException e1) {
            e1.printStackTrace();
        }
        return null;
    }


}
