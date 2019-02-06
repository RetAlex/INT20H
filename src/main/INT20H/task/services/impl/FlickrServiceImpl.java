package INT20H.task.services.impl;

import INT20H.task.resources.AfterContextInitilized;
import INT20H.task.services._interfaces.FlickrService;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.Size;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.instrument.Instrumentation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class FlickrServiceImpl implements FlickrService {

    private List<String> listOfUrl;
    private static final int ZERO = 0;
    private @Value("${flickr.api.key}") String apiKey;
    private @Value("${flickr.api.secret}") String apiSecret;
    private @Value("${flickr.photoset.id}") String photosetId;
    private @Value("${flickr.photoset.tag}") String tag;
    private @Value("${flickr.photoset.limit}") int photoLimit;

    //    @AfterContextInitilized TODO implement
    @PostConstruct //test
    public void loadCache() throws Exception {
        List<String> imagesUrlFromAlbum = getImagesUrlFromAlbum(photosetId, ZERO, Integer.MAX_VALUE);
        List<String> imagesUrlByTag = getImagesUrlByTag(tag, ZERO, Integer.MAX_VALUE);
        imagesUrlFromAlbum.removeAll(imagesUrlByTag);
        imagesUrlFromAlbum.addAll(imagesUrlByTag);

        listOfUrl = imagesUrlFromAlbum;
        log.info("Cache size = " + listOfUrl.size());
    }

    @Override
    public List<String> getAllImagesUrl(int page) {
        int toIndex = photoLimit * (page + 1) > listOfUrl.size() ? listOfUrl.size() : photoLimit * (page + 1);
        return listOfUrl.subList(page * photoLimit, toIndex);
    }

    public List<String> getImagesUrlFromAlbum(String albumId, int page, int amount) throws Exception {
        Flickr f = new Flickr(apiKey, apiSecret, new REST());
        PhotoList<Photo> photos = f.getPhotosetsInterface().getPhotos(albumId, amount, page);

        return photos.stream().map(FlickrServiceImpl::getUrlFromPhoto).distinct().collect(Collectors.toList());
    }

    public List<String> getImagesUrlByTag(String tag, int page, int amount) throws Exception {
        Flickr f = new Flickr(apiKey, apiSecret, new REST());
        SearchParameters params = new SearchParameters();
        params.setTags(new String[]{tag});
        PhotoList<Photo> search = f.getPhotosInterface().search(params, amount, page);
        return search.stream().map(e -> getSourceUrlFromSize(f, e)).collect(Collectors.toList());
    }

    private String getSourceUrlFromSize(Flickr f, Photo photo) {
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
