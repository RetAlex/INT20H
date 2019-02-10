package INT20H.task.services._interfaces;

import INT20H.task.model.dto.EmotionsDto;
import INT20H.task.model.dto.ImageFaceDto;
import INT20H.task.model.dto.PhotoSizeDto;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

public interface FacePlusPlusService {
    @Scheduled
    void cacheEmotions();

    EmotionsDto getAllEmogies(String emotion, int page);

    Map<String, List<PhotoSizeDto>> getEmotionsMap();

    List<ImageFaceDto> setEmotionsForImageFaceDto(List<ImageFaceDto> listOfImageFaceDto);
}
