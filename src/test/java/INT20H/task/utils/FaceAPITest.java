package INT20H.task.utils;

import INT20H.task._main.TaskApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TaskApplication.class)
public class FaceAPITest {

    private String key = "JNsr371qG2YY0jYB8MLs5M_E9QYsDOt4";
    private String secret = "PO04I-3jZxB1BbBeWc6VqQxhmNCFjJFZ";

    FaceAPI api = new FaceAPI();

    @Test
    public void getFacesTokens() {
        System.out.println(api.getFacesTokens(key, secret, "https://zub.ru/upload/resize_cache/iblock/3fb/400_400_1/3fbb5092e65142007fb8971a1abbf9d6.jpg"));
    }
}