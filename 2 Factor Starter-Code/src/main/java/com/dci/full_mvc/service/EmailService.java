package com.dci.full_mvc.service;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;


    public void sendGenericEmail(){

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

        String htmlContent = "<h1>Welcome to my site</h1>";

        try {
            mimeMessageHelper.setTo("omaraakamal@gmail.com");
            mimeMessageHelper.setSubject("Welcome to my site");
            mimeMessageHelper.setText(htmlContent,true);
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendVerificationEmail(String recipientEmail, String token){
        String verificationUrl = "http://localhost:8080/auth/verify?token=" + token;

        try {
            String htmlContent = loadHtmlTemplate("email-templates/verification-email.html");
            htmlContent = htmlContent.replace("{{VERIFICATION_URL}}",verificationUrl);

            System.out.println(htmlContent);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessageHelper.setTo(recipientEmail);
            mimeMessageHelper.setSubject("Email Verification");
            mimeMessageHelper.setText(htmlContent,true);
            javaMailSender.send(mimeMessage);


        } catch (IOException | MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    //    loads html file and converts it to a string
    private String loadHtmlTemplate(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        byte[] bytes = Files.readAllBytes(Path.of(resource.getURI()));
        return new String(bytes, StandardCharsets.UTF_8);
    }


//    @PostConstruct
//    public void init(){
//        sendVerificationEmail("omar@gmail.com","token12345");
//    }

}
