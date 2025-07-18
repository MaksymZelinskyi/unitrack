package com.unitrack.repository;

import com.unitrack.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Participation, Long> {
}
