package net.minfty.authenticator.controller;

import net.minfty.authenticator.entity.RegistrationData;
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
    public ResponseEntity<?> receiveData(@RequestBody RegistrationData registrationData) {
        registrationService.validate(registrationData);

        String test = "i am the body";
        HttpHeaders responseHeaders = new HttpHeaders();

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .headers(responseHeaders)
                .body(test);
    }

}
