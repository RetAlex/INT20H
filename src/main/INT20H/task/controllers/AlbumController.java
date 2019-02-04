package INT20H.task.controllers;

import INT20H.task.configs.Configurations;
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
    public List<String> getAllImages(@RequestParam(name = "page", defaultValue = "0", required = false) int page){
        return flickrService.getImagesFromAlbumOrByTag(Configurations.tag, Configurations.albumId, page);
    }
}
