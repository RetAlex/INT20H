package INT20H.task.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class IndexController {

    @GetMapping("/")
    public @ResponseBody byte[] renderIndex() throws IOException {
        return getClass().getClassLoader().getResourceAsStream("index.html").readAllBytes();
    }
}
