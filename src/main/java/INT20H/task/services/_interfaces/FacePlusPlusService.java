package INT20H.task.services._interfaces;

import INT20H.task.model.dto.ImageFaceDto;
import INT20H.task.model.dto.PhotoSizeDto;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface FacePlusPlusService {
    @Scheduled
    void cacheEmogies();

    List<PhotoSizeDto> getAllEmogies(String emogie, int page);

    List<ImageFaceDto> getEmogiesByListOfFaceDto(List<ImageFaceDto> listOfImageFaceDto);
}
