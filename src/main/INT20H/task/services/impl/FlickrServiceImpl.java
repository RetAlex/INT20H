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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class FlickrServiceImpl implements FlickrService {

    private List<String> listOfUrl;
    private @Value("${flickr.api.key}") String apiKey;
    private @Value("${flickr.api.secret}") String apiSecret;
    private @Value("${flickr.photoset.id}") String photosetId;
    private @Value("${flickr.photoset.limit}") int photoLimit;

    @AfterContextInitilized
    public void loadCache() throws Exception {
        List<String> imagesUrlFromAlbum = getImagesUrlFromAlbum(photosetId, 0, 999999);
        List<String> imagesUrlByTag = getImagesUrlByTag(photosetId, 0, 999999);
        imagesUrlFromAlbum.removeAll(imagesUrlByTag);
        imagesUrlFromAlbum.addAll(imagesUrlByTag);

        listOfUrl = imagesUrlFromAlbum;
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

    @Override
    public List<String> getAllImagesUrl(String tag, String albumId, int page) {
        int toIndex = photoLimit * (page + 1) > listOfUrl.size() ? listOfUrl.size() : photoLimit * (page + 1);
        return listOfUrl.subList(page * photoLimit, toIndex);
    }

    private static String getUrlFromPhoto(Photo photo){
        try {
            return photo.getOriginalUrl();
        } catch (FlickrException e) {
            log.error(e);
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String userId = "72157674388093532";
        String apikey = "bfd4c6af1cec67be3a5aa2b4c296b3f0";
        String secret = "10a56939cf4647fd";
        Flickr f = new Flickr(apikey, secret, new REST());
        PhotoList<Photo> photos = f.getPhotosetsInterface().getPhotos("72157674388093532", 999999, 1);
        System.out.println(photos);
    }
}
