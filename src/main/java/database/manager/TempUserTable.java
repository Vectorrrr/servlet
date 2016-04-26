package database.manager;

import database.ConnectionFactory;
import org.apache.log4j.Logger;
import utils.PropertyLoader;

import java.sql.*;

/**
 * @author Ivan Gladush
 * @since 21.04.16.
 */
public class TempUserTable {
    private static final Logger logger = Logger.getLogger(TempUserTable.class);
    private static final PropertyLoader PROPERTY_LOADER = PropertyLoader.getPropertyLoader("temp.user.database.query.properties");

    private static final String QUERY_FOR_GET_COUNT_USER_WITH_ID = PROPERTY_LOADER.property("count.user.with.this.id");
    private static final String EXCEPTION_CHECK_CONTAINS_USER = "Can't check contains this user in temp table or not because %s";
    private static final String EXCEPTION_ADD_NEW_USER = "I can't add new user in temp table because %s";
    private static final String EXCEPTION_DELETE_USER = "I can't delete user because %s";

    private static Connection connection = ConnectionFactory.getConnection();
    private PreparedStatement addUserStatement;
    private PreparedStatement deleteUserStatement;

    public TempUserTable() throws SQLException {
        addUserStatement = connection.prepareStatement(PROPERTY_LOADER.property("add.new.user"));
        deleteUserStatement=connection.prepareStatement(PROPERTY_LOADER.property("delete.user.by.name"));
    }

    public boolean isTempUser(String name) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(String.format(QUERY_FOR_GET_COUNT_USER_WITH_ID,name));
            if (resultSet.next()) {
                return resultSet.getInt(1) == 1;
            }
        } catch (SQLException e) {
            logger.error(String.format(EXCEPTION_CHECK_CONTAINS_USER,e.getMessage()));
        }
        return false;
    }

    public  boolean addNewUser(String userName, String userPassword,String secretWord) {
        try {
            addUserStatement.setString(1,userName);
            addUserStatement.setString(2,userPassword);
            addUserStatement.setString(3,secretWord);
            addUserStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error(String.format(EXCEPTION_ADD_NEW_USER,e.getMessage()));
        }
        return false;
    }

    public boolean confirmUser(String userName,String password, String secretWord) {
        try {
            deleteUserStatement.setString(1,userName);
            deleteUserStatement.setString(2,password);
            deleteUserStatement.setString(3,secretWord);
            deleteUserStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error(String.format(EXCEPTION_DELETE_USER,e.getMessage()));
        }

        return false;
    }

    public void close() throws SQLException {
        addUserStatement.close();
    }
}
