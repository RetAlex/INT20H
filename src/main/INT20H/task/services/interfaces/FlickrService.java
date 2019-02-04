package INT20H.task.services.interfaces;

import java.util.List;

public interface FlickrService {
    /**
     * Retrieves distinct list of pictures. Similar pictures shouldn't be included to response list for memory efficiency.
     * @param albumId album id to retrieve picturesFrom
     * @param tag tag to retrieve pictures by
     * @param page page of iterable images list, starts from 0
     * @return list of images
     */
    List<byte[]> getImagesFromAlbumOrByTag(String albumId, String tag, int page);
}
