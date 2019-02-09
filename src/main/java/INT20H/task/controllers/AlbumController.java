package INT20H.task.controllers;

import INT20H.task.exception.IncorrectRequestParamException;
import INT20H.task.model.dto.PhotoSizeDto;
import INT20H.task.services._interfaces.FlickrService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AlbumController {
    private final FlickrService flickrService;

    public AlbumController(FlickrService flickrService) {
        this.flickrService = flickrService;
    }

    @GetMapping("/getAllImages")
    public List<PhotoSizeDto> getAllImages(@RequestParam(name = "page", defaultValue = "0", required = false) int page) throws Exception {
        if(page < 0) throw new IncorrectRequestParamException("Page can not be null!");

        return flickrService.getAllImagesUrl(page);
    }
}
