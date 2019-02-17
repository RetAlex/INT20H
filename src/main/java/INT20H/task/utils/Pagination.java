package INT20H.task.utils;

import INT20H.task.model.dto.PhotoSizeDto;

import java.util.List;

import static INT20H.task.resources.configuration.FlickrConfig.photoLimit_;

public class Pagination {

    public static List<PhotoSizeDto> getByPage(int page, List<PhotoSizeDto> urlCache) {
        int toIndex = photoLimit_ * (page + 1) > urlCache.size() ? urlCache.size() : photoLimit_ * (page + 1);
        int fromIndex = (page * photoLimit_) > urlCache.size() ? urlCache.size() : (page * photoLimit_);

        return urlCache.subList(fromIndex, toIndex);
    }
}
