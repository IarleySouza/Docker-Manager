package com.souza.docker_manager.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.Container;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DockerServiceTest {

    @Mock
    private DockerClient dockerClient;
    @Mock
    private ListContainersCmd listContainersCmd;
    @Mock
    private StartContainerCmd startContainerCmd;
    @Mock
    private StopContainerCmd stopContainerCmd;
    @Mock
    private ListImagesCmd listImagesCmd;
    @Mock
    private RemoveContainerCmd removeContainerCmd;
    @Mock
    private CreateContainerCmd createContainerCmd;

    @InjectMocks
    private DockerService dockerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Deve consultar os containers quando recebe showAlL=true")
    public void testListContainers1() {
        // Arrange
        List<Container> mockContainers = Collections.emptyList();
        when(dockerClient.listContainersCmd()).thenReturn(listContainersCmd);
        when(listContainersCmd.withShowAll(true)).thenReturn(listContainersCmd);
        when(listContainersCmd.exec()).thenReturn(mockContainers);

        // Act
        List<Container> result = dockerService.listContainers(true);

        // Assert
        assertEquals(mockContainers, result);
        verify(dockerClient).listContainersCmd();
        verify(listContainersCmd).withShowAll(true);
        verify(listContainersCmd).exec();
    }

    @Test
    @DisplayName("Deve consultar os containers quando receber showAlL=false")
    public void testListContainers2() {
        // Arrange
        List<Container> mockContainers = Collections.emptyList();
        when(dockerClient.listContainersCmd()).thenReturn(listContainersCmd);
        when(listContainersCmd.withShowAll(false)).thenReturn(listContainersCmd);
        when(listContainersCmd.exec()).thenReturn(mockContainers);

        // Act
        List<Container> result = dockerService.listContainers(false);

        // Assert
        assertEquals(mockContainers, result);
        verify(dockerClient).listContainersCmd();
        verify(listContainersCmd).withShowAll(false);
        verify(listContainersCmd).exec();
    }

    @Test
    @DisplayName("Deve iniciar o container quando receber um ID válido")
    public void testStartContainer1() {
        // Arrange
        String containerId = UUID.randomUUID().toString();
        when(dockerClient.startContainerCmd(eq(containerId))).thenReturn(startContainerCmd);

        // Act
        dockerService.startContainer(containerId);

        // Assert
        verify(dockerClient).startContainerCmd(containerId);
        verify(dockerClient.startContainerCmd(containerId)).exec();
    }

    @Test
    @DisplayName("Deve iniciar o exceção ContainerNotFound quando container nao existir")
    public void testStartContainer2() {
        // Arrange
        String containerId = UUID.randomUUID().toString();
        when(dockerClient.startContainerCmd(eq(containerId))).thenReturn(startContainerCmd);
        when(startContainerCmd.exec()).thenThrow(new RuntimeException("Container not found"));

        // Act
        assertThrows(
                RuntimeException.class,
                () -> dockerService.startContainer(containerId),
                "Expected startContainer to throw, but it didn't"
        );

        // Assert
        verify(dockerClient).startContainerCmd(containerId);
        verify(dockerClient.startContainerCmd(containerId)).exec();
    }

    @Test
    @DisplayName("Deve parar o container quando receber um ID válido")
    public void testStopContainer1() {
        // Arrange
        String containerId = UUID.randomUUID().toString();
        when(dockerClient.stopContainerCmd(eq(containerId))).thenReturn(stopContainerCmd);

        // Act
        dockerService.stopContainer(containerId);

        // Assert
        verify(dockerClient).stopContainerCmd(containerId);
        verify(dockerClient.stopContainerCmd(containerId)).exec();
    }

    @Test
    @DisplayName("Deve deletar o container quando receber um ID válido")
    public void testDeleteContainer1() {
        // Arrange
        String containerId = UUID.randomUUID().toString();
        when(dockerClient.removeContainerCmd(eq(containerId))).thenReturn(removeContainerCmd);

        // Act
        dockerService.deleteContainer(containerId);

        // Assert
        verify(dockerClient).removeContainerCmd(containerId);
        verify(dockerClient.removeContainerCmd(containerId)).exec();
    }

    @Test
    @DisplayName("Deve criar um container quando receber um ID válido")
    public void testCreateContainer1() {
        // Arrange
        String containerId = UUID.randomUUID().toString();
        when(dockerClient.createContainerCmd(eq(containerId))).thenReturn(createContainerCmd);

        // Act
        dockerService.createContainer(containerId);

        // Assert
        verify(dockerClient).createContainerCmd(containerId);
        verify(dockerClient.createContainerCmd(containerId)).exec();
    }

}