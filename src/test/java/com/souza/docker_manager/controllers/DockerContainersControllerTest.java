package com.souza.docker_manager.controllers;

import com.github.dockerjava.api.model.Container;
import com.souza.docker_manager.service.DockerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
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
    @DisplayName("Deve iniciar um container com sucesso")
    void startContainer1() throws Exception {
        String containerId = "abc123def456";
        doNothing().when(dockerService).startContainer(containerId);

        mockMvc.perform(post("/api/containers/{id}/start", containerId))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Container " + containerId + "started"));
        verify(dockerService, times(1)).startContainer(containerId);

    }
    @Test
    @DisplayName("Deve retornar erro 500 ao tentar criar container")
    void startContainer2() throws Exception {
        String containerId = "abc123def456";

        doThrow(new RuntimeException("Docker daemon offline"))
                .when(dockerService)
                .startContainer(containerId);

        mockMvc.perform(post("/api/containers/{id}/start", containerId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to start container: Docker daemon offline"));

        verify(dockerService, times(1)).startContainer(containerId);
    }

    @Test
    @DisplayName("Deve parar um container com sucesso")
    void stopContainer() throws Exception {
        String containerId = "abc123def456";

        doNothing().when(dockerService).stopContainer(containerId);

        mockMvc.perform(post("/api/containers/{id}/stop", containerId))
                .andExpect(status().isOk())
                .andExpect(content().string("Container " + containerId + " stopped successfully."));

        verify(dockerService, times(1)).stopContainer(containerId);

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
    @DisplayName("Deve criar um container com sucesso")
    void createContainer() throws Exception {
        String testContainerId = "abc123def456";

        doNothing().when(dockerService).createContainer(testContainerId);

        mockMvc.perform(post("/api/containers/create").contentType(MediaType.APPLICATION_JSON).content(testContainerId))
                .andExpect(status().isOk());

        verify(dockerService, times(1)).createContainer(testContainerId);
    }
}