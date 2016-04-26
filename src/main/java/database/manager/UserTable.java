package database.manager;

import database.ConnectionFactory;
import org.apache.log4j.Logger;
import utils.PropertyLoader;

import java.sql.*;

/**
 * @author Ivan Gladush
 * @since 21.04.16.
 */
public class UserTable {
    private static final Logger logger = Logger.getLogger(UserTable.class);
    private static final PropertyLoader PROPERTY_LOADER = PropertyLoader.getPropertyLoader("database.configuration.properties");

    private static final String EXCEPTION_CHECK_CONTAINS_USER = "Exception when I check user contains in db or not contains %s";
    private static final String EXCEPTION_ADDED_USER = "Exception when I added new user %s";

    private static final String QUERY_FOR_COUNT_USER_WITH_ID = PROPERTY_LOADER.property("get.count.user.with.id");
    private static final String QUERY_CONTAINS_USER = PROPERTY_LOADER.property("contains.user");

    private static Connection connection = ConnectionFactory.getConnection();
    private PreparedStatement addUserStatement;

    public UserTable() throws SQLException {
        addUserStatement = connection.prepareStatement(PROPERTY_LOADER.property("add.user.query"));
    }

    public boolean addNewUser(String userName, String userPassword) {
        try {
            addUserStatement.setString(1, userName);
            addUserStatement.setString(2, userPassword);
            addUserStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error(String.format(EXCEPTION_ADDED_USER, e.getMessage()));
        }
        return false;
    }

    public boolean isValidUser(String session) {
        try (Statement s = connection.createStatement()) {
            ResultSet resultSet = s.executeQuery(String.format(QUERY_FOR_COUNT_USER_WITH_ID, session));
            resultSet.next();
            return resultSet.getInt(1) == 1;
        } catch (SQLException e) {
            logger.error(String.format(EXCEPTION_CHECK_CONTAINS_USER, e.getMessage()));
        }
        return false;
    }

    public boolean containsUser(String userName, String userPassword) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(String.format(QUERY_CONTAINS_USER, userName, userPassword));
            resultSet.next();
            return resultSet.getInt(1) == 1;
        } catch (SQLException e) {
            logger.error(String.format(EXCEPTION_CHECK_CONTAINS_USER, e.getMessage()));
        }
        return false;
    }

    public void close() throws SQLException {
        addUserStatement.close();
    }
}
