package INT20H.task.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagesDto {
    private int amount;
    private List<PhotoSizeDto> photoSizeDtos;
}
