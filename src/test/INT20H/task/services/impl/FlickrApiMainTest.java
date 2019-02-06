package INT20H.task.services.impl;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class FlickrApiMainTest {

    @Test
    public void test() throws Exception {
        String userId = "72157674388093532";
        String apikey = "bfd4c6af1cec67be3a5aa2b4c296b3f0";
        String secret = "10a56939cf4647fd";
        Flickr f = new Flickr(apikey, secret, new REST());

        PhotoList<Photo> photos = f.getPhotosetsInterface().getPhotos(null, 6, 1);
        System.out.println();

    }
}
