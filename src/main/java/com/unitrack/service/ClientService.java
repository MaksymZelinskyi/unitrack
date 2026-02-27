package com.unitrack.service;

import com.unitrack.dto.request.UpdateClientDto;
import com.unitrack.entity.Client;
import com.unitrack.entity.Project;
import com.unitrack.entity.Workspace;
import com.unitrack.exception.ClientNotFoundException;
import com.unitrack.exception.WorkspaceException;
import com.unitrack.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final WorkspaceService workspaceService;

    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    public List<Client> getAll(String currentUserEmail) {
        Workspace workspace = workspaceService.getUserWorkspace(currentUserEmail);
        return clientRepository.findAllByWorkspace(workspace);
    }

    public void add(UpdateClientDto dto, String currentUserEmail) {
        Workspace workspace = workspaceService.getUserWorkspace(currentUserEmail);
        Client client = new Client(dto.name(), dto.email(), dto.phoneNumber());
        client.setWorkspace(workspace);
        clientRepository.save(client);
    }

    public Client getByName(String name) {
        return clientRepository.findByName(name).orElse(null);
    }

    public Client getById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("id", id));
    }

    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }

    public Client updateById(Long id, UpdateClientDto dto, String currentUserEmail) {
        Workspace workspace = workspaceService.getUserWorkspace(currentUserEmail);
        Client client = clientRepository.findById(id).orElseThrow(); //retrieve the client from the database

        if (!Objects.equals(client.getWorkspace(), workspace))
            throw new WorkspaceException("Can't update a client from another workspace");
        //write dto data
        client.setName(dto.name());
        client.setEmail(dto.email());
        client.setPhoneNumber(dto.phoneNumber());
        clientRepository.save(client); //persist
        return client;
    }

    public Client getByNameOrCreate(String name, String currentUserEmail) {
        Workspace workspace = workspaceService.getUserWorkspace(currentUserEmail);
        Optional<Client> optional = clientRepository.findByNameAndWorkspace(name, workspace);

        Client client = null;
        if (optional.isPresent()) {
            client = optional.get();
            log.debug("Client {} is fetched. Client id: {}", name, client.getId());
        } else {
            client = new Client(name);
            client.setWorkspace(workspace);
            clientRepository.save(client);
        }
        return client;
    }

}
