package com.unitrack.service;

import com.unitrack.dto.request.ClientDto;
import com.unitrack.entity.Client;
import com.unitrack.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public void add(ClientDto dto) {
        Client client = new Client(dto.name(), dto.email(), dto.phoneNumber());
        clientRepository.save(client);
    }

    public Client getByName(String name) {
        return clientRepository.findByName(name).orElse(null);
    }

    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }

    public Client updateById(Long id, ClientDto dto) {
        Client client = clientRepository.findById(id).orElseThrow(); //retrieve the client from the database
        //write dto data
        client.setName(dto.name());
        client.setEmail(dto.email());
        client.setPhoneNumber(dto.phoneNumber());
        clientRepository.save(client); //persist
        return client;
    }

    public Client getByNameOrCreate(String name) {
        Optional<Client> optional = clientRepository.findByName(name);
        Client client = null;
        if (optional.isPresent()) {
            client = optional.get();
            log.debug("Client {} is fetched. Client id: {}", name, client.getId());
        } else {
            client = new Client(name);
            clientRepository.save(client);
        }
        return client;
    }
}
