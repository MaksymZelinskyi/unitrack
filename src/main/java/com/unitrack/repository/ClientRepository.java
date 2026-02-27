package com.unitrack.repository;

import com.unitrack.entity.Client;
import com.unitrack.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByName(String name);

    Optional<Client> findByNameAndWorkspace(String name, Workspace workspace);

    List<Client> findAllByWorkspace(Workspace workspace);
}
