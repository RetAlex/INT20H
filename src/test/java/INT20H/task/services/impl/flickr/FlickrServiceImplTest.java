package INT20H.task.services.impl.flickr;

import INT20H.task.model.dto.PhotoSizeDto;
import INT20H.task.services.interfaces.FlickrApiService;
import INT20H.task.services.interfaces.FlickrService;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FlickrTestConfig.class)
public class FlickrServiceImplTest {

    private static final String NULL_LIST_OF_SIZES_ID = "nullListOfSizes";

    @Autowired
    private FlickrService flickrService;
    @Resource
    private FlickrApiService flickrApiService;

    @Test
    public void loadCache() throws FlickrException {
        PhotoList<Photo> byTag = new PhotoList<>();
        PhotoList<Photo> byAlbumId = new PhotoList<>();
        List<String> listOfExpectedId = fillPhotoLists(byTag, byAlbumId);

        Mockito.when(flickrApiService.searchByTag(any(),anyInt())).thenReturn(byTag);
        Mockito.when(flickrApiService.getPhotosByAlbumId(any(),anyInt())).thenReturn(byAlbumId);
        Mockito.when(flickrApiService.getPhotoSizeDto(any())).thenAnswer((Answer<PhotoSizeDto>) invocation -> {
            PhotoSizeDto photoSizeDto = new PhotoSizeDto();
            String photoId = (String) invocation.getArguments()[0];
            photoSizeDto.setId(photoId);
            if(!photoId.equals(NULL_LIST_OF_SIZES_ID))photoSizeDto.setListOfSizes(new ArrayList<>());
            return photoSizeDto;
        });

        flickrService.loadCache();

        assertEquals(listOfExpectedId, flickrService.getPhotoCache().stream().map(PhotoSizeDto::getId).collect(Collectors.toList()));
    }

    private List<String> fillPhotoLists(List<Photo> byTag, List<Photo> byAlbumId){
        List<String> listOfExpectedId = new ArrayList<>();
        listOfExpectedId.add("1");
        listOfExpectedId.add("2");
        listOfExpectedId.add("3");

        Photo duplicate = new Photo();
        duplicate.setId(listOfExpectedId.get(0));
        Photo e2 = new Photo();
        e2.setId(listOfExpectedId.get(1));
        byTag.add(duplicate);
        byTag.add(e2);

        Photo e3 = new Photo();
        e3.setId(listOfExpectedId.get(2));
        Photo e4 = new Photo();
        e4.setId(NULL_LIST_OF_SIZES_ID);
        byAlbumId.add(e3);
        byAlbumId.add(e4);
        byAlbumId.add(duplicate);

        return listOfExpectedId;
    }
}