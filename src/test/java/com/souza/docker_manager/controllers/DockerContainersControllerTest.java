package com.souza.docker_manager.controllers;

import com.github.dockerjava.api.model.Container;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class DockerContainersControllerTest {

    @Mock
    private DockerService dockerService;

    @InjectMocks
    private DockerContainersController dockerContainersController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(dockerContainersController).build();
    }

    @Test
    @DisplayName("Deve listar os containers passando o valor default de 'all' como true")
    void listContainers1() throws Exception {
        List<Container> MockContainers = Collections.emptyList();
        when(dockerService.listContainers(true)).thenReturn(MockContainers);

        mockMvc.perform(get("/api/containers"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(dockerService, times(1)).listContainers(true);
    }
    @Test
    @DisplayName("Deve listar os containers passando o valor de 'all' como false")
    void listContainers2() throws Exception {
        List<Container> MockContainers = Collections.emptyList();
        when(dockerService.listContainers(false)).thenReturn(MockContainers);

        mockMvc.perform(get("/api/containers").param("all", "false"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(dockerService, times(1)).listContainers(false);
    }

    @Test
    void startContainer() {
    }

    @Test
    void stopContainer() {
    }

    @Test
    @DisplayName("Deve deletar um container com sucesso")
    void deleteContainer() throws Exception {
        String containerId = "abc123def456";

        doNothing().when(dockerService).deleteContainer(containerId);

        mockMvc.perform(delete("/api/containers/{id}", containerId))
                .andExpect(status().isOk());

        verify(dockerService, times(1)).deleteContainer(containerId);
    }

    @Test
    void createContainer() {
    }
}