package com.vladimirpandurov.servermanagerB.service.implementation;

import com.vladimirpandurov.servermanagerB.enumeration.Status;
import com.vladimirpandurov.servermanagerB.model.Server;
import com.vladimirpandurov.servermanagerB.repository.ServerRepository;
import com.vladimirpandurov.servermanagerB.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;

    @Override
    public Server create(Server server) {
        log.info("Saving new server: {}", server.getName());
        server.setImageUrl(setServerImageUrl());
        return this.serverRepository.save(server);
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging server IP: {}", ipAddress);
        Server server = this.serverRepository.findServerByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(1000)? Status.SERVER_UP : Status.SERVER_DOWN);
        this.serverRepository.save(server);
        return server;
    }

    @Override
    public Collection<Server> list(int limit) {
        log.info("Fetching all servers");
        return this.serverRepository.findAll(PageRequest.of(0, limit)).toList();
    }

    @Override
    public Server get(Long id) {
        log.info("Fetching server by id: {}", id);
        return this.serverRepository.findById(id).get();
    }

    @Override
    public Server update(Server server) {
        log.info("Updating server: {}", server.getName());
        return this.serverRepository.save(server);
    }

    @Override
    public Boolean delete(Long id) {
        log.info("Deleteing server by ID: {}", id);
        this.serverRepository.deleteById(id);
        return Boolean.TRUE;
    }

    private String setServerImageUrl() {
        String[] imageNames = {"server1.png","server2.png", "server3.png","server4.png"};
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/servers/image/" + imageNames[new Random().nextInt(4)]).toUriString();
    }
}
