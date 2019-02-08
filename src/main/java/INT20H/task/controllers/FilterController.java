package INT20H.task.controllers;

import INT20H.task.services._interfaces.FacePlusPlusService;
import com.flickr4java.flickr.photos.Size;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FilterController {
    private final FacePlusPlusService facePlusPlusService;

    public FilterController(FacePlusPlusService facePlusPlusService) {
        this.facePlusPlusService = facePlusPlusService;
    }


    @GetMapping("/getListOfSizesByEmogy") //todo validate page >= 0
    public List<List<Size>> getAllImages(@RequestParam(name = "emogy") String emogy) throws Exception {
        return facePlusPlusService.getAllEmogies(emogy);
    }
}
