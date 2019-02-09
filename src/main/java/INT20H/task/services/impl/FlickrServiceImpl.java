package INT20H.task.services.impl;

import INT20H.task.model.dto.RequestPhotoDto;
import INT20H.task.model.dto.PhotoSizeDto;
import INT20H.task.services._interfaces.FlickrService;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.Size;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static INT20H.task.resources.Configs.*;
import static INT20H.task.utils.Pagination.getByPage;

@Service
@Log4j2
@Data
public class FlickrServiceImpl implements FlickrService {

    private List<PhotoSizeDto> urlCache = new ArrayList<>();
    private Set<String> listOfCachedPhotoId = new HashSet<>();

    private static final int ZERO = 0;
    private static volatile int k = 0;
    private int amount = 5;

    @Scheduled(initialDelay = 0, fixedDelay = 1000)
    public void loadCache() throws Exception {
        if(urlCache.size() > 0) {
            listOfCachedPhotoId = urlCache.stream().map(PhotoSizeDto::getId).collect(Collectors.toSet());
        }

        RequestPhotoDto requestPhotoDto = new RequestPhotoDto(i20HphotosetId_, tag_);
        getUrlByAlbumIdAndTag(requestPhotoDto);
        log.info("Cache size = " + urlCache.size());
    }

    @Override
    public List<PhotoSizeDto> getAllImagesUrl(int page) throws Exception { //todo extract
        return getByPage(page, urlCache);
    }

    private void getUrlByAlbumIdAndTag(RequestPhotoDto requestPhotoDto) throws Exception {
        List<PhotoSizeDto> imagesUrlByTag = getListOfNewPhotoSizeDto(requestPhotoDto);

        urlCache.addAll(imagesUrlByTag.stream().distinct().collect(Collectors.toList()));
    }

    private List<PhotoSizeDto> getListOfNewPhotoSizeDto(RequestPhotoDto requestPhotoDto) throws Exception {
        List<PhotoSizeDto> imagesUrlFromAlbum = new ArrayList<>();
        if(requestPhotoDto.getAlbumId() != null) imagesUrlFromAlbum = getImagesUrlFromAlbum(requestPhotoDto, amount);

        List<PhotoSizeDto> imagesUrlByTag = getImagesUrlByTag(requestPhotoDto, amount);
        imagesUrlByTag.addAll(imagesUrlFromAlbum);
        return imagesUrlByTag;
    }

    private List<PhotoSizeDto> getImagesUrlFromAlbum(RequestPhotoDto photo, int amount) throws Exception {
        Flickr f = getNewFlickrObject();
        PhotoList<Photo> photos = f.getPhotosetsInterface().getPhotos(photo.getAlbumId(), amount, ZERO);
        log.info("start getImagesUrlFromAlbum");

        return convertToPhotoSizeDtoList(f, photos);
    }

    private List<PhotoSizeDto> convertToPhotoSizeDtoList(Flickr f, PhotoList<Photo> photos) {
        removeAlreadyCached(photos);
        return photos.stream().map(Photo::getId).map(id -> getPhotoSizeDto(f, id)).filter(e -> e != null).collect(Collectors.toList());
    }

    private void removeAlreadyCached(PhotoList<Photo> photos) {
        if(listOfCachedPhotoId.size() > 0){
            photos.removeIf(next -> listOfCachedPhotoId.contains(next.getId()));
        }
    }

    private List<PhotoSizeDto> getImagesUrlByTag(RequestPhotoDto requestPhotoDto, int amount) throws Exception {
        Flickr f = getNewFlickrObject();
        PhotoList<Photo> search = searchByTag(requestPhotoDto, amount, f);

        log.info("start getImagesUrlByTag");
        return convertToPhotoSizeDtoList(f, search);
    }

    private PhotoList<Photo> searchByTag(RequestPhotoDto requestPhotoDto, int amount, Flickr f) throws FlickrException {
        SearchParameters params = new SearchParameters();
        params.setTags(new String[]{requestPhotoDto.getTag()});
        return f.getPhotosInterface().search(params, amount, ZERO);
    }

    private Flickr getNewFlickrObject() {
        return new Flickr(flickrApiKey_, flickrApiSecret_, new REST());
    }

    private static PhotoSizeDto getPhotoSizeDto(Flickr f, String photoId) {
        try {
            Thread.sleep(50);
            log.info("Processing getPhotoSizeDto " + k++);
            return new PhotoSizeDto(photoId, (List<Size>) f.getPhotosInterface().getSizes(photoId));
        } catch (FlickrException | InterruptedException ex) {
            log.error(ex);
        }
        return null;
    }


}
