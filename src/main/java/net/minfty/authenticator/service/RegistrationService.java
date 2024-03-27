package net.minfty.authenticator.service;

import net.minfty.authenticator.entity.User;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    public void validate(User registrationData) throws InvalidUserDataException{
        if (isValidEmail(registrationData.getEmail())) {
            System.out.println("Email ist valide");
        } else {
            throw new InvalidUserDataException("Ungültige E-Mail-Adresse");
        }
    }

    private boolean isValidEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }
}
