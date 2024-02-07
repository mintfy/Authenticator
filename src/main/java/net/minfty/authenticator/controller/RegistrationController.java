package net.minfty.authenticator.controller;

import net.minfty.authenticator.entity.RegistrationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class RegistrationController {
    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody RegistrationDTO registrationDTO) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Benutzer erfolgreich registriert");
        response.put("user: ", registrationDTO);
        return ResponseEntity.ok(response);
    }
}
