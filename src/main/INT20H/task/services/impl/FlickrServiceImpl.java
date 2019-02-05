package INT20H.task.services.impl;

import INT20H.task.services._interfaces.FlickrService;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.*;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class FlickrServiceImpl implements FlickrService {

    private @Value("${flickr.api.key}") String apiKey;
    private @Value("${flickr.api.secret}") String apiSecret;
    private @Value("${flickr.photoset.limit}") int photosPerPage;

    @Override
    public List<String> getImagesUrlFromAlbum(String albumId, int page) throws Exception {
        Flickr f = new Flickr(apiKey, apiSecret, new REST());
        PhotoList<Photo> photos = f.getPhotosetsInterface().getPhotos(albumId, photosPerPage, page);

        return photos.stream().map(FlickrServiceImpl::getUrlFromPhoto).distinct().collect(Collectors.toList());
    }

    @Override
    public List<String> getImagesUrlByTag(String tag, int page) throws Exception {
        Flickr f = new Flickr(apiKey, apiSecret, new REST());
        SearchParameters params = new SearchParameters();
        params.setTags(new String[]{tag});
        PhotoList<Photo> search = f.getPhotosInterface().search(params, photosPerPage, page);
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
        return null;
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
