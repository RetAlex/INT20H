package INT20H.task.services.impl;

import INT20H.task.model.dto.PhotoDto;
import INT20H.task.services._interfaces.FlickrService;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.Size;
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

    @Scheduled(fixedRate = 60*1000)
    public void loadCache() throws Exception {
        PhotoDto photoDto = new PhotoDto(i20HphotosetId_, tag_);
        List<String> imagesUrlFromAlbum = getUrlByAlbumIdAndTag(photoDto);
        urlCache.put(photoDto, imagesUrlFromAlbum.stream().distinct().collect(Collectors.toList()));
        log.info("Cache size = " + urlCache.get(photoDto).size());
    }

    private List<String> getUrlByAlbumIdAndTag(PhotoDto photoDto) throws Exception {
        List<String> imagesUrlFromAlbum = new ArrayList<>();
        if(photoDto.getAlbumId() != null) imagesUrlFromAlbum = getImagesUrlFromAlbum(photoDto.getAlbumId(), ZERO, Integer.MAX_VALUE);

        List<String> imagesUrlByTag = getImagesUrlByTag(photoDto.getTag(), ZERO, Integer.MAX_VALUE);
        imagesUrlFromAlbum.addAll(imagesUrlByTag);
        return imagesUrlFromAlbum;
    }

    @Override
    public List<String> getAllImagesUrl(PhotoDto photoDto, int page) throws Exception {
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

    public List<String> getImagesUrlFromAlbum(String albumId, int page, int amount) throws Exception {
        Flickr f = new Flickr(flickrApiKey_, flickrApiSecret_, new REST());
        PhotoList<Photo> photos = f.getPhotosetsInterface().getPhotos(albumId, amount, page);

        return photos.stream().map(FlickrServiceImpl::getUrlFromPhoto).distinct().collect(Collectors.toList());
    }

    public List<String> getImagesUrlByTag(String tag, int page, int amount) throws Exception {
        Flickr f = new Flickr(flickrApiKey_, flickrApiSecret_, new REST());
        SearchParameters params = new SearchParameters();
        params.setTags(new String[]{tag});
        PhotoList<Photo> search = f.getPhotosInterface().search(params, amount, page);
        return search.stream().map(e -> getSourceUrlFromSize(f, e)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static String getSourceUrlFromSize(Flickr f, Photo photo) {
        try {
            return ((List<Size>)f.getPhotosInterface().getSizes(photo.getId())).get(0).getSource();
        } catch (FlickrException e) {
            log.error(e);
            return null;
        }
    }

    private static String getUrlFromPhoto(Photo photo){
        try {
            return photo.getOriginalUrl();
        } catch (FlickrException e) {
            log.error(e);
            return null;
        }
    }


}