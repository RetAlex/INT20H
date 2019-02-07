package INT20H.task.model.dto;

import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PhotoDto {
    private String albumId;
    private String tag;
    private Integer label;
}
