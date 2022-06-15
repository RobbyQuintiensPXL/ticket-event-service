package be.jevent.eventservice.service;

import org.apache.commons.fileupload.FileUploadException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FileStorageServiceTests {

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    public void saveTest() throws FileUploadException {
        String image = "image.jpg";
        MockMultipartFile imageFile = new MockMultipartFile("banner", image,
                "text/plain", "test data".getBytes());
        fileStorageService.save(imageFile);

        assertEquals(imageFile.getOriginalFilename(), image);
    }

    @Test
    public void loadTest() {
        String image = "image.jpg";
        Resource resource = fileStorageService.load(image);

        assertEquals(resource.getFilename(), image);
    }

}
