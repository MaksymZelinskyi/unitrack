package com.unitrack.service;

import com.unitrack.dto.AddCollaboratorDto;
import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Project;
import com.unitrack.repository.CollaboratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;


    public void add(AddCollaboratorDto dto) {
        Collaborator collaborator = new Collaborator(dto.firstName(), dto.lastName(), dto.email(), dto.password());
        collaboratorRepository.save(collaborator);
    }

    public List<Collaborator> getAll() {
        return collaboratorRepository.findAll();
    }

    public Collaborator getById(Long id) {
        return collaboratorRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        collaboratorRepository.deleteById(id);
    }
}
