package INT20H.task.services._interfaces;

import java.util.List;

public interface FlickrService {
    /**
     * Retrieves distinct list of picture urls. Similar pictures (with equal url) shouldn't be included to response list for memory efficiency.
     * @param albumId album id to retrieve picturesFrom
     * @param tag tag to retrieve pictures by
     * @param page page of iterable images list, starts from 0
     * @return list of url addresses of images
     */
    List<String> getImagesFromAlbumOrByTag(String albumId, String tag, int page);
}
