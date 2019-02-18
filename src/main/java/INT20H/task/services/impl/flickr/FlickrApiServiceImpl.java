package INT20H.task.services.impl.flickr;

import INT20H.task.model.dto.PhotoSizeDto;
import INT20H.task.model.dto.RequestPhotoDto;
import INT20H.task.model.properties.FlickrProperties;
import INT20H.task.services.interfaces.FlickrApiService;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.Size;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class FlickrApiServiceImpl implements FlickrApiService {
    private Flickr client;
    private int counter;
    private final static int ZERO = 0;

    public FlickrApiServiceImpl(FlickrProperties properties) {
        this.client = new Flickr(properties.getApiKey(), properties.getApiSecret(), new REST());
    }

    @Override
    public PhotoList<Photo> getPhotosByAlbumId(RequestPhotoDto photo, int amount) throws FlickrException {
        return client.getPhotosetsInterface().getPhotos(photo.getAlbumId(), amount, ZERO);
    }

    @Override
    public PhotoSizeDto getPhotoSizeDto(String photoId) {
        try {
            Thread.sleep(50);
            log.info("Processing getPhotoSizeDto " + counter++);
            return new PhotoSizeDto(photoId, (List<Size>) client.getPhotosInterface().getSizes(photoId));
        } catch (FlickrException | InterruptedException ex) {
            log.error(ex);
        }
        return null;
    }

    @Override
    public PhotoList<Photo> searchByTag(RequestPhotoDto requestPhotoDto, int amount) throws FlickrException {
        SearchParameters params = new SearchParameters();
        params.setTags(new String[]{requestPhotoDto.getTag()});
        return client.getPhotosInterface().search(params, amount, ZERO);
    }

}
