package com.souza.docker_manager.config;

import com.github.dockerjava.api.DockerClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class DockerClientConfigTest {


    @Test
    void buildDockerClient() {
        DockerClientConfig config = new DockerClientConfig();

        ReflectionTestUtils.setField(
                config,
                "dockerSocketPath",
                "unix:///var/run/docker.sock"
        );

        DockerClient client = config.buildDockerClient();

        assertNotNull(client);
    }
}