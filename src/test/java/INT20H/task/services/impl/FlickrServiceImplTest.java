package INT20H.task.services.impl;

import INT20H.task._main.TaskApplication;
import INT20H.task.services._interfaces.FlickrService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TaskApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FlickrServiceImplTest {

    @Autowired
    private FlickrService flickrService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testAll() throws Exception {
        System.out.println(mockMvc.perform(get("/api/getAllImages?albumId=72157674388093532&tag=int20h")).andDo(print()).andReturn().getResponse().getContentAsString());
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