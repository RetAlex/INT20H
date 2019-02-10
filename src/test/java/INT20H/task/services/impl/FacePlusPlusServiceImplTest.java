package INT20H.task.services.impl;

import INT20H.task._main.TaskApplication;
import INT20H.task.model.dto.ImageFaceDto;
import INT20H.task.model.dto.PhotoSizeDto;
import INT20H.task.services._interfaces.FacePlusPlusService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TaskApplication.class)
public class FacePlusPlusServiceImplTest {

    @Test
    public void getEmogiesByListOfFaceDto() {
        List<ImageFaceDto> imageFaceDtos = new ArrayList<>();
        imageFaceDtos.add(new ImageFaceDto("id1", "https://uroki4mam.ru/wp-content/uploads/2014/06/rebenok-plachet-na-krovati1.jpg"));
        imageFaceDtos.add(new ImageFaceDto("id2", "https://liza.ua/wp-content/uploads/2016/02/smile.jpg"));
        imageFaceDtos.add(new ImageFaceDto("id3", "https://uroki4mam.ru/wp-content/uploads/2014/06/rebenok-plachet-na-krovati1.jpg"));
        imageFaceDtos.add(new ImageFaceDto("id4", "http://n1s1.hsmedia.ru/3d/8a/74/3d8a74cb85894692486d99211a303f09/440x326_21_9d069aaa35802d7d471d05379ebb9410@690x460_0xc0a8392b_4912642831512120021.jpeg"));
        imageFaceDtos.add(new ImageFaceDto("id5", "https://uroki4mam.ru/wp-content/uploads/2014/06/rebenok-plachet-na-krovati1.jpg"));

        FacePlusPlusService service = new FacePlusPlusServiceImpl(null);
        List<ImageFaceDto> emogiesByListOfFaceDto = service.getEmogiesByListOfFaceDto(imageFaceDtos);
        System.out.println(emogiesByListOfFaceDto);
    }
}