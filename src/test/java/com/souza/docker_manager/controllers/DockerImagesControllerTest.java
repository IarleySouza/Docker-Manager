package com.souza.docker_manager.controllers;

import com.github.dockerjava.api.model.Image;
import com.souza.docker_manager.service.DockerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

class DockerImagesControllerTest {

    @Mock
    private DockerService dockerService;

    @InjectMocks
    private DockerImagesController dockerImagesController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(dockerImagesController).build();
    }

    @Test
    @DisplayName("Deve listar as imagens do container")
    void listImages() throws Exception {
        List<Image> images = Collections.emptyList();
        when(dockerService.listImages()).thenReturn(images);

        mockMvc.perform(get("/api/images"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(dockerService, times(1)).listImages();

    }

    @Test
    @DisplayName("Deve listar as imagens do container filtrando pelo nome")
    void testListImages() throws Exception {
        List<Image> images = Collections.emptyList();
        when(dockerService.filterImages("image")).thenReturn(images);

        mockMvc.perform(get("/api/images/filter").param("filterName", "image"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(dockerService, times(1)).filterImages("image");
    }
}