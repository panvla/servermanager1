package com.vladimirpandurov.servermanagerB.repository;

import com.vladimirpandurov.servermanagerB.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository<Server, Long> {

    Server findServerByIpAddress(String ipAddress);

}
