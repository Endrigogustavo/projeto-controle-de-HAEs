package br.com.fateczl.apihae.domain.factory;

import java.time.LocalDateTime;

import br.com.fateczl.apihae.domain.entity.EmailVerification;

public class EmailVerificationFactory {

    public static EmailVerification create(String name, String email, String course, String encryptedPassword,
            String institutionName, String verificationToken) {
        EmailVerification newVerification = new EmailVerification();
        newVerification.setEmail(email);
        newVerification.setName(name);
        newVerification.setCourse(course);
        newVerification.setPassword(encryptedPassword);
        newVerification.setCode(verificationToken);
        newVerification.setExpiresAt(LocalDateTime.now().plusMinutes(15));

        return newVerification;
    }

}
