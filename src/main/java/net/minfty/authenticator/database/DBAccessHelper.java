package net.minfty.authenticator.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Service
public class DBAccessHelper {

    private static final Logger LOGGER = LogManager.getLogger(DBAccessHelper.class);
    private final Properties properties;

    public DBAccessHelper() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream("src/main/resources/application.properties"));
        } catch (IOException exception) {
            LOGGER.error("Fehler beim Laden der JDBC-Properties", exception);
            throw new RuntimeException("Fehler beim Laden der JDBC-Properties", exception);
        }
    }

    public Connection getDatabaseConnection() throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("spring.datasource.url"),
                properties.getProperty("spring.datasource.username"),
                properties.getProperty("spring.datasource.password")
        );
    }
}
