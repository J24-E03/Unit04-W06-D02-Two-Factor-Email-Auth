package com.dci.full_mvc.service;

import com.dci.full_mvc.exceptions.ResourceNotFound;
import com.dci.full_mvc.exceptions.UserNotVerifiedException;
import com.dci.full_mvc.model.User;
import com.dci.full_mvc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

// this class is responsible for fetching the user from the DB to log them in

@Service
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(()-> new ResourceNotFound("User not found with email: " + email));

        if(!user.isVerified()){
            throw new UserNotVerifiedException("You are not verfied yet. Please check your email for verification link.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
