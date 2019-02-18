package INT20H.task.utils.exceptions;

import lombok.Data;

import java.io.IOException;

@Data
public class FaceAPIException extends IOException {

    private String error;

    public FaceAPIException(String error) {
        this.error = error;
    }
}
