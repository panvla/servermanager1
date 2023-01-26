package com.vladimirpandurov.servermanagerB.controller;

import com.vladimirpandurov.servermanagerB.enumeration.Status;
import com.vladimirpandurov.servermanagerB.model.Response;
import com.vladimirpandurov.servermanagerB.model.Server;
import com.vladimirpandurov.servermanagerB.service.implementation.ServerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
@CrossOrigin
public class ServerController {

    private final ServerServiceImpl serverService;

    @GetMapping
    public ResponseEntity<Response> getServers() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        return ResponseEntity.ok(
              Response.builder()
               .timeStamp(LocalDateTime.now())
               .data(Map.of("servers", this.serverService.list(30)))
               .message("Servers retrieved")
               .status(HttpStatus.OK)
               .statusCode(HttpStatus.OK.value())
               .build()
       );
    }
    @GetMapping("/{id}")
    public ResponseEntity<Response> getServer(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("server", this.serverService.get(id)))
                .message("Server retrieved")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

    @GetMapping("/ping/{idAddress}")
    public ResponseEntity<Response> pingServer(@PathVariable("idAddress") String ipAddress) throws IOException, InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        Server server =  this.serverService.ping(ipAddress);
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("server", server))
                .message(server.getStatus() == Status.SERVER_UP ? "Ping success" : "Ping failed")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }
    @PostMapping
    public ResponseEntity<Response> saveServer(@RequestBody @Valid Server server){
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("server", this.serverService.create(server)))
                .message("Server created")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .build()
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteServer(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("deleted", this.serverService.delete(id)))
                .message("Server deleted")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }
    @GetMapping(path="/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public byte[] getServerImage(@PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/Downloads/png/" + fileName));
    }



}
