package INT20H.task.services.impl;

import INT20H.task.model.dto.PhotoDto;
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

import java.util.*;
import java.util.stream.Collectors;

import static INT20H.task.resources.Configs.*;

@Service
@Log4j2
@Data
public class FlickrServiceImpl implements FlickrService {

    private Map<PhotoDto, List<PhotoSizeDto>> urlCache = new HashMap<>();
    private static final int ZERO = 0;
    private static volatile int k = 0;
    int amount = 20;

    @Scheduled(initialDelay = 0, fixedDelay = 1000)
    public void loadCache() throws Exception {
        Map<PhotoDto, List<PhotoSizeDto>> urlCacheBuffer = new HashMap<>();
        PhotoDto photoDto = new PhotoDto(i20HphotosetId_, tag_, defaultLabel_);
        List<PhotoSizeDto> imagesUrlFromAlbum = getUrlByAlbumIdAndTag(photoDto);
        urlCacheBuffer.put(photoDto, imagesUrlFromAlbum.stream().distinct().collect(Collectors.toList()));
        log.info("Cache size = " + urlCacheBuffer.get(photoDto).size());
        urlCache = urlCacheBuffer;
    }

    private List<PhotoSizeDto> getUrlByAlbumIdAndTag(PhotoDto photoDto) throws Exception {
        List<PhotoSizeDto> imagesUrlFromAlbum = new ArrayList<>();
        if(photoDto.getAlbumId() != null) imagesUrlFromAlbum = getImagesUrlFromAlbum(photoDto, ZERO, amount);

        List<PhotoSizeDto> imagesUrlByTag = getImagesUrlByTag(photoDto, ZERO, amount);
        imagesUrlByTag.addAll(imagesUrlFromAlbum);

        return imagesUrlByTag;
    }

    @Override
    public List<PhotoSizeDto> getAllImagesUrl(PhotoDto photoDto, int page, int label) throws Exception { //todo extract
        List<PhotoSizeDto> urls = getFromCacheOrSite(photoDto);

        int toIndex = photoLimit_ * (page + 1) > urls.size() ? urls.size() : photoLimit_ * (page + 1);
        int fromIndex = (page * photoLimit_) > urls.size() ? urls.size() : (page * photoLimit_);

        return urls.subList(fromIndex, toIndex);
    }

    private List<PhotoSizeDto> getFromCacheOrSite(PhotoDto photoDto) throws Exception {
        List<PhotoSizeDto>urls = urlCache.get(photoDto);
        if(urls == null){
            urls = getUrlByAlbumIdAndTag(photoDto);
            urlCache.put(photoDto, urls);
        }
        return urls;
    }

    public List<PhotoSizeDto> getImagesUrlFromAlbum(PhotoDto photo, int page, int amount) throws Exception {
        Flickr f = new Flickr(flickrApiKey_, flickrApiSecret_, new REST());
        PhotoList<Photo> photos = f.getPhotosetsInterface().getPhotos(photo.getAlbumId(), amount, page);
        System.out.println("start getImagesUrlFromAlbum");

        return photos.stream().map(Photo::getId).map(id -> getPhotoSizeDto(f, id)).collect(Collectors.toList());
    }

    public List<PhotoSizeDto> getImagesUrlByTag(PhotoDto photoDto, int page, int amount) throws Exception {
        Flickr f = new Flickr(flickrApiKey_, flickrApiSecret_, new REST());
        SearchParameters params = new SearchParameters();
        params.setTags(new String[]{photoDto.getTag()});
        PhotoList<Photo> search = f.getPhotosInterface().search(params, amount, page);
        System.out.println("start getImagesUrlByTag");
        return search.stream().map(Photo::getId).map(id -> getPhotoSizeDto(f, id)).collect(Collectors.toList());
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

//    public static String getSourceUrlFromSize(Flickr f, String id, PhotoDto photoDto) {
//        try {
//            return (f.getPhotosInterface().getSizes(id)).stream().peek(e -> getPhotoSizeDto(photoDto, f, e.)).getSource();
//        } catch (FlickrException e) {
//            log.error(e);
//            return null;
//        }
//    }

    private static String getUrlFromPhoto(Photo photo){
        try {
            return photo.getOriginalUrl();
        } catch (FlickrException e) {
            log.error(e);
            return null;
        }
    }


}
