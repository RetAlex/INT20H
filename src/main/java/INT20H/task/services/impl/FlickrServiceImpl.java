package INT20H.task.services.impl;

import INT20H.task.model.dto.PhotoDto;
import INT20H.task.services._interfaces.FlickrService;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.SearchParameters;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static INT20H.task.resources.Configs.*;

@Service
@Log4j2
public class FlickrServiceImpl implements FlickrService {

    private Map<PhotoDto, List<String>> urlCache = new HashMap<>();
    private static final int ZERO = 0;
    private static volatile int k = 0;
    int amount = 2;

    @Scheduled(fixedRate = 120*1000)
    public void loadCache() throws Exception {
        PhotoDto photoDto = new PhotoDto(i20HphotosetId_, tag_, 5); //TODO label
        List<String> imagesUrlFromAlbum = getUrlByAlbumIdAndTag(photoDto);
        urlCache.put(photoDto, imagesUrlFromAlbum.stream().distinct().collect(Collectors.toList()));
        log.info("Cache size = " + urlCache.get(photoDto).size());
    }

    private List<String> getUrlByAlbumIdAndTag(PhotoDto photoDto) throws Exception {
        List<String> imagesUrlFromAlbum = new ArrayList<>();
        if(photoDto.getAlbumId() != null) imagesUrlFromAlbum = getImagesUrlFromAlbum(photoDto, ZERO, amount);

        List<String> imagesUrlByTag = getImagesUrlByTag(photoDto, ZERO, amount);
        imagesUrlFromAlbum.addAll(imagesUrlByTag);
        return imagesUrlFromAlbum;
    }

    @Override
    public List<String> getAllImagesUrl(PhotoDto photoDto, int page, int label) throws Exception {
        List<String> urls = getFromCacheOrSite(photoDto);

        int toIndex = photoLimit_ * (page + 1) > urls.size() ? urls.size() : photoLimit_ * (page + 1);
        int fromIndex = page * photoLimit_;

        return urls.subList(fromIndex, toIndex);
    }

    private List<String> getFromCacheOrSite(PhotoDto photoDto) throws Exception {
        List<String> urls = urlCache.get(photoDto);
        if(urls == null){
            urls = getUrlByAlbumIdAndTag(photoDto);
            urlCache.put(photoDto, urls);
        }
        return urls;
    }

    public List<String> getImagesUrlFromAlbum(PhotoDto photo, int page, int amount) throws Exception {
        Flickr f = new Flickr(flickrApiKey_, flickrApiSecret_, new REST());
        PhotoList<Photo> photos = f.getPhotosetsInterface().getPhotos(photo.getAlbumId(), amount, page);
        System.out.println("start getImagesUrlFromAlbum");

        return photos.stream().map(Photo::getId).map(e -> getLabelSizeFromPhoto(photo, f, e)).collect(Collectors.toList());
    }

    public List<String> getImagesUrlByTag(PhotoDto photoDto, int page, int amount) throws Exception {
        Flickr f = new Flickr(flickrApiKey_, flickrApiSecret_, new REST());
        SearchParameters params = new SearchParameters();
        params.setTags(new String[]{photoDto.getTag()});
        PhotoList<Photo> search = f.getPhotosInterface().search(params, amount, page);
        System.out.println("start getImagesUrlByTag");
        return search.stream().map(Photo::getId).map(e -> getLabelSizeFromPhoto(photoDto, f, e)).collect(Collectors.toList());
    }

    private static String getLabelSizeFromPhoto(PhotoDto photoDto, Flickr f, String photoId) {
        try {
            Thread.sleep(50);
            System.out.println("processing  " + ++k);
            return f.getPhotosInterface().getSizes(photoId).stream().filter(e1 -> e1.getLabel() == photoDto.getLabel()).findFirst().get().getSource();
        } catch (FlickrException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static String getSourceUrlFromSize(Flickr f, String id, PhotoDto photoDto) {
//        try {
//            return (f.getPhotosInterface().getSizes(id)).stream().peek(e -> getLabelSizeFromPhoto(photoDto, f, e.)).getSource();
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
