package com.unitrack.controller;

import com.unitrack.entity.Collaborator;
import com.unitrack.entity.Skill;
import com.unitrack.service.CollaboratorService;
import com.unitrack.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @GetMapping("/")
    public List<Skill> getAllSkills() {
        return skillService.getAll();
    }

    @PostMapping("/")
    public void addSkill(String skillName) {
        skillService.add(skillName);
    }

    @DeleteMapping("/{id}")
    public void deleteSkill(@PathVariable Long id) {
        skillService.delete(id);
    }

}
