package INT20H.task.services._interfaces;

import com.flickr4java.flickr.FlickrException;

import java.util.List;

public interface FlickrService {
    /**
     * Retrieves distinct list of picture urls. Similar pictures (with equal url) shouldn't be included to response list for memory efficiency.
     * @param albumId album id to retrieve picturesFrom
     * @param page page of iterable images list witch contains 20 url, starts from 0
     * @return list of url addresses of images
     */
    List<String> getImagesUrlFromAlbum(String albumId, int page) throws Exception;

    List<String> getImagesUrlByTag(String tag, int page) throws Exception;

    List<String> getAllImagesUrl(String tag, String albumId, int page);
}
