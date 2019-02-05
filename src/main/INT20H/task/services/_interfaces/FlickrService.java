package INT20H.task.services._interfaces;

import com.flickr4java.flickr.FlickrException;

import java.util.List;

public interface FlickrService {
    /**
     * Retrieves distinct list of picture urls. Similar pictures (with equal url) shouldn't be included to response list for memory efficiency.
     * @param page page of iterable images list, starts from 0
     * @return list of url addresses of images
     */

    List<String> getAllImagesUrl(int page);
}
