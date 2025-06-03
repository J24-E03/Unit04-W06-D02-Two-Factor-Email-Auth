package com.dci.full_mvc.controller;

import com.dci.full_mvc.model.User;
import com.dci.full_mvc.repository.UserRepository;
import com.dci.full_mvc.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


//    @GetMapping /login
//    @Getmapping /signup
//    @PostMapping /signup


    @GetMapping("/signup")
    public String signupForm(Model model){
        model.addAttribute("user",new User());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String registerUser(@Valid  @ModelAttribute User user, BindingResult result, Model model){

        List<String> errors = new ArrayList<>();
//        validation

//        email is not already taken in the DB
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            model.addAttribute("emailTaken", true);
            return "auth/signup";
        }

        //        check if password works
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$";
        if (!user.getPassword().matches(passwordPattern)) {
            System.out.println("Password does not match the pattern");
            result.rejectValue("password", "error.password",
                    "Password must be at least 6 characters and contain uppercase, lowercase, number and special character");
        }


        if(result.hasErrors()){
            return "auth/signup";
        }


        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setPassword(passwordEncoder.encode(user.getPassword()));



        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(),token);

        return "redirect:/auth/login?registered";
    }




    @GetMapping("/login")
    public String loginPage(){

        return "auth/login";
    }

}
