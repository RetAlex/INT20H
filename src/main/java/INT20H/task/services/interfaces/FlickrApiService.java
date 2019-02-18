package INT20H.task.services.interfaces;

import INT20H.task.model.dto.PhotoSizeDto;
import INT20H.task.model.dto.RequestPhotoDto;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;

public interface FlickrApiService {
    PhotoList<Photo> getPhotosByAlbumId(RequestPhotoDto photo, int amount) throws FlickrException;

    PhotoSizeDto getPhotoSizeDto(String photoId);

    PhotoList<Photo> searchByTag(RequestPhotoDto requestPhotoDto, int amount) throws FlickrException;
}
