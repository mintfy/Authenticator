package net.minfty.authenticator.service;

import net.minfty.authenticator.database.DBAccessHelper;
import net.minfty.authenticator.entity.User;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class RegistrationService {

    private final DBAccessHelper dbAccessHelper;

    public RegistrationService(DBAccessHelper dbAccessHelper) {
        this.dbAccessHelper = dbAccessHelper;
    }

    public void validate(User registrationData) throws InvalidUserDataException{
        validateEmail(registrationData.getEmail());
        validateUsernameAndEmailUniqueness(registrationData.getUsername(), registrationData.getEmail());
        validateNameAndPassword(registrationData.getFirstName(), registrationData.getLastName(), registrationData.getPassword());
    }

    private void validateEmail(String email) throws InvalidUserDataException {
        EmailValidator validator = EmailValidator.getInstance();
        if(!validator.isValid(email)) {
            throw new InvalidUserDataException("Ungültige E-Mail Adresse");
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
        if (firstName == null || lastName == null || password == null) {
            throw new InvalidUserDataException("Vor- und Nachname sowie Passwort dürfen nicht null sein");
        }
    }
}
