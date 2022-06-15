package be.jevent.eventservice.controller;

import be.jevent.eventservice.service.FileStorageService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class ImageControllerTests {

    @InjectMocks
    ImageController imageController;

    @Mock
    FileStorageService fileStorageService;

    @Test
    public void getImageTest() {
        String mockFile = "file.jpg";
        Resource mockResource = Mockito.mock(Resource.class);

        when(fileStorageService.load(anyString())).thenReturn(mockResource);

        ResponseEntity<Resource> responseEntity = imageController.getImage(mockFile);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }
}
