package net.minfty.authenticator.service;

import net.minfty.authenticator.entity.RegistrationData;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    public boolean validate(RegistrationData registrationData) {
        // TODO email validieren
        // TODO überprüfung ob der Username oder die Email schon vorhanden ist
        // TODO vor und nachname + password != null
        System.out.println(registrationData.getEmail());
        return false;
    }
}
