package INT20H.task.services._interfaces;

import INT20H.task.resources.Filters;

import java.util.List;

public interface FacePlusPlusService {
    /**
     * This method should return list of filtered photos
     * @param filters list of filters to apply to photo
     * @param page looked page starting from 0
     * @return list of photo urls
     */
    List<String> applyFilter(List<Filters> filters, int page);
}
