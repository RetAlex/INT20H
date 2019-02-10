package INT20H.task.utils;

import INT20H.task._main.TaskApplication;
import INT20H.task.utils.exceptions.FaceAPIException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TaskApplication.class)
public class FaceAPITest {

    private String key = "JNsr371qG2YY0jYB8MLs5M_E9QYsDOt4";
    private String secret = "PO04I-3jZxB1BbBeWc6VqQxhmNCFjJFZ";

    FaceAPI api = new FaceAPI();

    @Test
    public void getFacesTokens() {
        System.out.println(api.getFacesTokens(key, secret, "https://i.ytimg.com/vi/4VQcx2mpX3M/hqdefault.jpg"));
    }

    @Test
    public void getEmotionsByFaceTokens() throws FaceAPIException {
        List<String> tokens = new ArrayList<String>(Arrays.asList("da89a2c98fc7dedbf4eeb9a8e6babe2a"));
        System.out.println(api.getEmotionsByFaceTokens(key, secret, tokens));
    }
}