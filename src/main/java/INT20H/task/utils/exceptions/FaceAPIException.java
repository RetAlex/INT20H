package INT20H.task.utils.exceptions;

import INT20H.task.services._interfaces.FacePlusPlusService;
import INT20H.task.utils.FaceAPI;
import lombok.Data;

import java.io.IOException;

@Data
public class FaceAPIException extends IOException {

    private String error;

    public FaceAPIException(String error) {
        this.error = error;
    }
}
