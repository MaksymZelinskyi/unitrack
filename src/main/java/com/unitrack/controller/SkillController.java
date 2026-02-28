package com.unitrack.controller;

import com.unitrack.entity.Skill;
import com.unitrack.service.SkillService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
public class SkillController extends AuthenticatedController {

    private final SkillService skillService;

    @PostMapping("/")
    public void addSkill(String skillName) {
        skillService.add(skillName);
    }

    @DeleteMapping("/{id}")
    public void deleteSkill(@PathVariable Long id) {
        skillService.delete(id);
    }

    @GetMapping("/search")
    public List<Skill> searchSkills(String searchQuery) {
        return skillService.searchSkill(searchQuery);
    }
}
