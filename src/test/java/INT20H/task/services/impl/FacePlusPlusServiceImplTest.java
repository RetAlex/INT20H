package INT20H.task.services.impl;

import INT20H.task.model.dto.ImageFaceDto;
import INT20H.task.model.dto.PhotoSizeDto;
import INT20H.task.services._interfaces.FacePlusPlusService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FacePlusPlusServiceImplTest {

    @Test
    public void getEmogiesByListOfFaceDto() {
        List<ImageFaceDto> imageFaceDtos = new ArrayList<>();
        imageFaceDtos.add(new ImageFaceDto("id1", "http://..."));
        imageFaceDtos.add(new ImageFaceDto("id2", "http://..."));
        imageFaceDtos.add(new ImageFaceDto("id3", "http://..."));
        imageFaceDtos.add(new ImageFaceDto("id4", "http://..."));
        imageFaceDtos.add(new ImageFaceDto("id5", "http://..."));

        FacePlusPlusService service = new FacePlusPlusServiceImpl(null);
        List<ImageFaceDto> emogiesByListOfFaceDto = service.getEmogiesByListOfFaceDto(imageFaceDtos);
        System.out.println(emogiesByListOfFaceDto);
    }
}