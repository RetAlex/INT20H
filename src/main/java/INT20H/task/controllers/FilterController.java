package INT20H.task.controllers;

import INT20H.task.exception.IncorrectRequestParamException;
import INT20H.task.model.dto.PhotoSizeDto;
import INT20H.task.services._interfaces.FacePlusPlusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FilterController {
    private final FacePlusPlusService facePlusPlusService;

    public FilterController(FacePlusPlusService facePlusPlusService) {
        this.facePlusPlusService = facePlusPlusService;
    }

    @GetMapping("/getListOfSizesByEmotion")
    public List<PhotoSizeDto> getAllImages(@RequestParam(name = "emotion") String emotion,
                                           @RequestParam(name = "page", defaultValue = "0", required = false) int page) throws Exception {
        if(page < 0) throw new IncorrectRequestParamException("Page can not lowest than 0!");

        return facePlusPlusService.getAllEmogies(emotion, page);
    }

    @GetMapping("/getImagesAmountByEmotion")
    public int getAllImagesAmount(@RequestParam(name = "emotion") String emotion) {

        List<PhotoSizeDto> listOfPhoto = facePlusPlusService.getEmotionsMap().get(emotion);
        return listOfPhoto == null ? 0 : listOfPhoto.size();
    }

    @GetMapping("/getAvailableEmotions")
    public List<String> getAvailableEmotions() {
        return new ArrayList<>(facePlusPlusService.getEmotionsMap().keySet());
    }
}
