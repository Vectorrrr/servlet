package database.manager;

import database.ConnectionFactory;
import org.apache.log4j.Logger;
import utils.PropertyLoader;

import java.sql.*;

/**
 * This class is designed to work with the user table.
 * It allows you to add a new user. Check does the login
 * to the database. As well as checking whether the user
 * is contained in the database
 * @author Ivan Gladush
 * @since 21.04.16.
 */
public class UserTable {
    private static final Logger logger = Logger.getLogger(UserTable.class);
    private static final PropertyLoader PROPERTY_LOADER = PropertyLoader.getPropertyLoader("users.database.query.properties");

    private static final String EXCEPTION_CHECK_CONTAINS_USER = "Exception when I check user contains in db or not contains %s";
    private static final String EXCEPTION_ADDED_USER = "Exception when I added new user %s";

    private static final String QUERY_FOR_COUNT_USER_WITH_ID = PROPERTY_LOADER.property("get.count.user.with.id");
    private static final String QUERY_CONTAINS_USER = PROPERTY_LOADER.property("contains.user");

    private static Connection connection = ConnectionFactory.getConnection();

    private PreparedStatement addUserStatement;

    public UserTable() throws SQLException {
        addUserStatement = connection.prepareStatement(PROPERTY_LOADER.property("add.user.query"));
    }

    /**
     * Method adds a user into the database and
     * returns the user whether or not was added
     */
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

    /**
     * Method checks whether the currently
     * logged into the database or not
     */
    public boolean thisLoginExist(String login) {
        try (Statement s = connection.createStatement()) {
            ResultSet resultSet = s.executeQuery(String.format(QUERY_FOR_COUNT_USER_WITH_ID, login));
            resultSet.next();
            return resultSet.getInt(1) == 1;
        } catch (SQLException e) {
            logger.error(String.format(EXCEPTION_CHECK_CONTAINS_USER, e.getMessage()));
        }
        return false;
    }

    /**
     * The method checks whether the active user in the database or not
     */
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
