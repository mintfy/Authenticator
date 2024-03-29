package net.minfty.authenticator.controller;

import net.minfty.authenticator.entity.User;
import net.minfty.authenticator.service.InvalidUserDataException;
import net.minfty.authenticator.service.RegistrationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/api/auth")
    public ResponseEntity<?> receiveData(@RequestBody User registrationData) {
        String responseBody = "Registrierung erfolgreich!";
        HttpHeaders responseHeaders = new HttpHeaders();

        try {
            registrationService.registerUser(registrationData);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .headers(responseHeaders)
                    .body(responseBody);
        } catch (InvalidUserDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .headers(responseHeaders)
                    .body(e.getMessage());
        }
    }

}
