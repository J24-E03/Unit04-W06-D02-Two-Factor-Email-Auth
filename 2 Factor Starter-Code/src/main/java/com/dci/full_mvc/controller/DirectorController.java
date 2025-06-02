package com.dci.full_mvc.controller;

import com.dci.full_mvc.model.Director;
import com.dci.full_mvc.repository.DirectorRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorRepository directorRepository;

    @GetMapping("/new")
    public String newDirectorForm(Model model){
        model.addAttribute("director", new Director());
        return "directors/director-form";
    }

    @PostMapping("/new")
    public String createDirector(@ModelAttribute Director director){
        directorRepository.save(director);
        return "redirect:/movies/new";
    }
}

