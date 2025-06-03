package com.dci.full_mvc.controller;

import com.dci.full_mvc.exceptions.ResourceNotFound;
import com.dci.full_mvc.model.User;
import com.dci.full_mvc.repository.UserRepository;
import com.dci.full_mvc.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token, Model model){
        System.out.println("in Veirfy Route");
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(()-> new ResourceNotFound("User not found with verification token: " + token));

        user.setVerified(true);
        user.setVerificationToken(null);

        userRepository.save(user);

        return "redirect:/auth/login?verified";


    }



    @PostMapping("/delete-account")
    public String deleteAccount(Authentication authentication){
        User user = userRepository.findByEmailAndIsDeletedFalse(authentication.getName())
                .orElseThrow(()-> new ResourceNotFound("User not found with email: " + authentication.getName()));

        user.setDeleted(true);
        user.setDeletedAt(LocalDate.now());

        userRepository.save(user);

        return "redirect:/auth/login?accountDeleted";
    }


    @GetMapping("/login")
    public String loginPage(){

        return "auth/login";
    }

}
