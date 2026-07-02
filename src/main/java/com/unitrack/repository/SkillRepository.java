package com.unitrack.repository;

import com.unitrack.dto.SkillDto;
import com.unitrack.entity.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    Optional<Skill> findByName(String name);

    @Query("SELECT new com.unitrack.dto.SkillDto(s.id, s.name) FROM Skill s " +
            "WHERE UPPER(s.name) LIKE CONCAT('%', UPPER(:name), '%')")
    Page<SkillDto> findByNameLike(@Param("name") String name, Pageable pageable);
}
