package INT20H.task.services._interfaces;

import INT20H.task.model.dto.RequestPhotoDto;
import INT20H.task.model.dto.PhotoSizeDto;

import java.util.List;
import java.util.Map;

public interface FlickrService {

    List<PhotoSizeDto> getAllImagesUrl(int page) throws Exception;

    Map<RequestPhotoDto, List<PhotoSizeDto>> getUrlCache();
}
