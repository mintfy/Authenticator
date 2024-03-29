package net.minfty.authenticator.service;

import net.minfty.authenticator.database.DBAccessHelper;
import net.minfty.authenticator.entity.Role;
import net.minfty.authenticator.entity.User;
import net.minfty.authenticator.repository.RoleRepository;
import net.minfty.authenticator.repository.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegistrationService {

    private final DBAccessHelper dbAccessHelper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public RegistrationService(DBAccessHelper dbAccessHelper, UserRepository userRepository, RoleRepository roleRepository) {
        this.dbAccessHelper = dbAccessHelper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public void registerUser(User registrationData) throws InvalidUserDataException {
        validate(registrationData);
        Role studentRole = roleRepository.findByName("STUDENT").orElseThrow(() -> new RuntimeException("Rolle STUDENT nicht gefunden"));
        registrationData.setRole(studentRole);
        userRepository.save(registrationData);
    }

    public void validate(User registrationData) throws InvalidUserDataException {
        validateEmail(registrationData.getEmail());
        validateUsernameAndEmailUniqueness(registrationData.getUsername(), registrationData.getEmail());
        validateNameAndPassword(registrationData.getFirstName(), registrationData.getLastName(), registrationData.getPassword());
    }

    private void validateEmail(String email) throws InvalidUserDataException {
        EmailValidator validator = EmailValidator.getInstance();
        if(!validator.isValid(email)) {
            throw new InvalidUserDataException("Ungültige E-Mail Adresse");
        }

        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
        } catch (AddressException exception) {
            throw new InvalidUserDataException("Ungültige E-Mail-Domain");
        }
    }

    private void validateUsernameAndEmailUniqueness(String username, String email) throws InvalidUserDataException {
        try (Connection connection = dbAccessHelper.getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM user WHERE username = ? OR email = ?")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    if (count > 0) {
                        throw new InvalidUserDataException("Benutzername oder E-Mail Adresse ist schon vorhanden");
                    }
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Fehler bei der Datenbankabfrage", exception);
        }
    }

    private void validateNameAndPassword(String firstName, String lastName, String password) throws InvalidUserDataException {
        if (isValidName(firstName) || isValidName(lastName)) {
            throw new InvalidUserDataException("Ungültiger Vor- oder Nachname");
        }

        if (!isValidPassword(password)) {
            throw new InvalidUserDataException("Ungültiges Passwort");
        }
    }

    private boolean isValidName(String name) {
        return name == null || name.isEmpty() || !name.matches("[a-zA-ZäöüÄÖÜß\\\\s]+");
    }

    private boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{12,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
