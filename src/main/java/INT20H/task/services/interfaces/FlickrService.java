package INT20H.task.services.interfaces;

import INT20H.task.model.dto.ImagesDto;
import INT20H.task.model.dto.PhotoSizeDto;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface FlickrService {

    @Scheduled(initialDelay = 0, fixedDelay = 10000)
    void loadCache();

    ImagesDto getAllImages(int page) throws Exception;

    List<PhotoSizeDto> getPhotoCache();
}
