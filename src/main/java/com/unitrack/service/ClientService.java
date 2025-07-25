package com.unitrack.service;

import com.unitrack.dto.request.ClientDto;
import com.unitrack.entity.Client;
import com.unitrack.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        Client client = clientRepository.findById(id).orElseThrow();
        client.setName(dto.name());
        client.setEmail(dto.email());
        client.setPhoneNumber(dto.phoneNumber());
        clientRepository.save(client);
        return client;
    }
}
