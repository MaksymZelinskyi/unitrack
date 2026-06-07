package com.unitrack.repository;


import com.unitrack.entity.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    @Query("SELECT w FROM Workspace w WHERE w.name LIKE %:search% OR w.description LIKE %:search%")
    Page<Workspace> findByNameContainingOrDescriptionContaining(String search, Pageable pageable);
}
