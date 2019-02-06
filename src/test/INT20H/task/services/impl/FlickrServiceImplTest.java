package INT20H.task.services.impl;

import INT20H.task._main.TaskApplication;
import INT20H.task.services._interfaces.FlickrService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TaskApplication.class)
public class FlickrServiceImplTest {

    @Autowired
    private FlickrService flickrService;

    @Test
    public void testAll(){
//        List<String> allImagesUrl = (List<String>) flickrService.getAllImagesUrl(new Object());
//        assert allImagesUrl.size() == photosPerPage;
//        allImagesUrl.forEach(e -> {
//            assert !StringUtils.isEmpty(e);
//        });

    }
//    @Test TODO
//    public void getImagesUrlFromAlbum() throws Exception {
//        List<String> imagesUrlFromAlbum = flickrService.getImagesUrlFromAlbum(photosetId, 1);
//        assert imagesUrlFromAlbum.size() == photosPerPage;
//        imagesUrlFromAlbum.forEach(e -> {
//            assert !StringUtils.isEmpty(e);
//        });
//    }
//
//    @Test
//    public void getImagesUrlByTag() throws Exception {
//        List<String> imagesUrlByTag = flickrService.getImagesUrlByTag("4", 1);
//        assert imagesUrlByTag.size() == photosPerPage;
//        imagesUrlByTag.forEach(e -> {
//            assert !StringUtils.isEmpty(e);
//        });
//    }


}