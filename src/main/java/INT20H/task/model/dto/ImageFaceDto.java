package INT20H.task.model.dto;

import com.flickr4java.flickr.photos.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class ImageFaceDto {
    private String id;
    private String url;
    private List<String> listOfTokens;
    private Set<String> emotion;
    private List<Size> listOfSizes;

    public ImageFaceDto(String id, String url,  List<Size> listOfSizes){
        this.id = id;
        this.url = url;
        this.listOfSizes = listOfSizes;
        listOfTokens = new ArrayList<>();
        emotion = new HashSet<>();
    }
}
