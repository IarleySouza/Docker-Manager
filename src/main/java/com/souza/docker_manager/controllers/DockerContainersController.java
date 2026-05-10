package com.souza.docker_manager.controllers;

import com.github.dockerjava.api.model.Container;
import com.souza.docker_manager.service.DockerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/containers")
@Slf4j
public class DockerContainersController {

    private final DockerService dockerService;

    @Autowired
    public DockerContainersController(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    @GetMapping
    public List<Container> listContainers(@RequestParam(required = false, defaultValue = "true") boolean all) {
        log.info("Listing containers: {}", all);
        return dockerService.listContainers(all);
    }

    @PostMapping(value = "/{id}/start")
    public ResponseEntity<String> startContainer(@PathVariable String id) {
        try {
            dockerService.startContainer(id);
            log.info("Container {} started.", id);
            return new ResponseEntity<>( "Container " + id + "started", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to start container: " + e.getMessage());
        }
    }
    @PostMapping(value = "/{id}/stop")
    public ResponseEntity<String> stopContainer(@PathVariable String id) {
        dockerService.stopContainer(id);
        return ResponseEntity.ok("Container " + id + " stopped successfully.");
    }

    @DeleteMapping(value = "/{id}")
    public void deleteContainer(@PathVariable String id) {
        dockerService.deleteContainer(id);
    }
    @PostMapping("/create")
    public void createContainer(@RequestBody String container) {
        dockerService.createContainer(container);
    }
}
