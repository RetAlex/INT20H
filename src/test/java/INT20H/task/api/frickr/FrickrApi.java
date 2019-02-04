package INT20H.task.api.frickr;


import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.*;
import com.flickr4java.flickr.tags.ClusterList;
import com.flickr4java.flickr.tags.RelatedTagsList;
import com.flickr4java.flickr.tags.TagRaw;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;

public class FrickrApi {

    public static void main(String[] args) throws Exception {
        String userId = "72157674388093532";
        String apikey = "bfd4c6af1cec67be3a5aa2b4c296b3f0";
        String secret = "10a56939cf4647fd";
        Flickr f = new Flickr(apikey, secret, new REST());
//        PhotoList list = f.getPhotosetsInterface().getPhotos(userId, 100, 1);

        PhotosInterface pi = new PhotosInterface(apikey, secret, new REST());
        PhotoList<Photo> photos = f.getPhotosetsInterface().getPhotos("72157674388093532", 1, 1);
        for (Iterator iterator = photos.iterator(); iterator.hasNext();) {
            Photo photo = (Photo) iterator.next();
            System.out.println(photo.getOriginalUrl());
            photo.getTags().stream().forEach(System.out::print);
            BufferedImage bufferedImage = pi.getImage(photo, Size.SQUARE);
            File outputfile = new File("image.jpg");
            ImageIO.write(bufferedImage, "jpg", outputfile);
        }

    }

    private static void searchByTag(Flickr f) throws FlickrException {
        SearchParameters params = new SearchParameters();
        params.setTags(new String[]{"4"});
        PhotoList<Photo> search = f.getPhotosInterface().search(params, 1, 1);
        for (Photo photo : search) {
            System.out.println(photo.getUrl());
        }
    }

}
