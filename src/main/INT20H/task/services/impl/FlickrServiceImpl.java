package INT20H.task.services.impl;

import INT20H.task.services._interfaces.FlickrService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlickrServiceImpl implements FlickrService {

    @Override
    public List<String> getImagesFromAlbumOrByTag(String albumId, String tag, int page) {
        return null;
    }
}
