package database;

import utils.PropertyLoader;

import java.sql.*;
import java.util.Properties;

import org.apache.log4j.Logger;
/**
 * This class checks the database if the database is not available,
 * it tries to create it. If it is not
 * possible to create a database or spreadsheet exception
 * occurs emissions
 * @author Ivan Gladush
 * @since 19.04.16.
 */
public class ConnectionFactory {
    private static final Logger logger = Logger.getLogger(ConnectionFactory.class);
    private static final PropertyLoader PROPERTY_LOADER=PropertyLoader.getPropertyLoader("database.configuration.properties");

    private static final String DB_DRIVER = PROPERTY_LOADER.property("db.driver.name");
    private static final String DATABASE_NAME = PROPERTY_LOADER.property("database.name");
    private static final String SHOW_DATABASE = PROPERTY_LOADER.property("show.database.query");
    private static final String CREATE_DB_QUERY = PROPERTY_LOADER.property("create.database.query");

    private static final String DB_USER_NAME = PROPERTY_LOADER.property("db.user.name");
    private static final String DB_PASSWORD = PROPERTY_LOADER.property("db.user.password");
    private static final String DB_URL_ADDRESS = PROPERTY_LOADER.property("db.url.address");

    private static final String EXCEPTION_WHEN_CREATE_DB = "Exception when I create db %s";
    private static final String EXCEPTION_INIT_DB = "I can't init db because %s";

    private static Connection connectionToDataBase;



    static {
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL_ADDRESS, initProperties());
            createDbIfNotExist(connection);
            connection.close();
            connectionToDataBase = createConnection();
        } catch (Exception e) {
            logger.error(String.format(EXCEPTION_WHEN_CREATE_DB,e.getMessage()));
            throw new IllegalStateException(String.format(EXCEPTION_INIT_DB , e.getMessage()));


        }
    }

    /**
     * Method returns a connection to the database
     */
    public static Connection getConnection() {
        return connectionToDataBase;
    }

    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL_ADDRESS + DATABASE_NAME, DB_USER_NAME, DB_PASSWORD);
    }

    private static Properties initProperties() {
        Properties connectionProperties = new Properties();
        connectionProperties.put("driver", DB_DRIVER);
        connectionProperties.put("user", DB_USER_NAME);
        connectionProperties.put("password", DB_PASSWORD);
        return connectionProperties;
    }

    private static void createDbIfNotExist(Connection connection) throws SQLException {
        try (Statement sql = connection.createStatement()) {
            ResultSet resultSet = sql.executeQuery(SHOW_DATABASE);
            while (resultSet.next()) {
                if (DATABASE_NAME.equals(resultSet.getString(1))) {
                    return;
                }
            }
            sql.execute(CREATE_DB_QUERY);
        }
    }

}
