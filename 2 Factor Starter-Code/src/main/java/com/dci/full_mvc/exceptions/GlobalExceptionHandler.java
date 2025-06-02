package com.dci.full_mvc.exceptions;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFound.class)
    public String handleNotFound(ResourceNotFound ex, Model model){
        model.addAttribute("error",ex.getMessage());
        return "errors/404";
    }
}
