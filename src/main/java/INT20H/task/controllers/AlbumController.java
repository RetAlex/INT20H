package INT20H.task.controllers;

import INT20H.task.model.dto.PhotoDto;
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

    @GetMapping("/getAllImages") //todo validate page >= 0
    public List<PhotoSizeDto> getAllImages(@RequestParam(name = "albumId", required = false) String albumId,
                                           @RequestParam(name = "tag", required = false) String tag,
                                           @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                           @RequestParam(name = "label", defaultValue = "5", required = false) Integer label) throws Exception {
        return flickrService.getAllImagesUrl(new PhotoDto(albumId, tag, label), page, label);
    }
}
