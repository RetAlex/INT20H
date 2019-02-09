package INT20H.task.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
public class ImageFaceDto {
    private String id;
    private String url;
    private List<String> listOfTokens;
    private Set<String> emogies;

    public ImageFaceDto(String id, String url){
        this.id = id;
        this.url = url;
        listOfTokens = new ArrayList<>();
        emogies = new HashSet<>();
    }
}
