package INT20H.task.services._interfaces;

import INT20H.task.model.dto.PhotoDto;
import INT20H.task.model.dto.PhotoSizeDto;

import java.util.List;
import java.util.Map;

public interface FlickrService {
    /**
     * Retrieves distinct list of picture urls. Similar pictures (with equal url) shouldn't be included to response list for memory efficiency.
     * @param page page of iterable images list, starts from 0
     * @param label
     * @return list of url addresses of images
     */

    List<PhotoSizeDto> getAllImagesUrl(PhotoDto photoDto, int page, int label) throws Exception;

    Map<PhotoDto, List<PhotoSizeDto>> getUrlCache();
}
