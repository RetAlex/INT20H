package INT20H.task.services._interfaces;

import INT20H.task.model.dto.ImagesDto;
import INT20H.task.model.dto.PhotoSizeDto;

import java.util.List;

public interface FlickrService {

    ImagesDto getAllImages(int page) throws Exception;

    List<PhotoSizeDto> getPhotoCache();
}
