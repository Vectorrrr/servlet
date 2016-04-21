package database;

import file.FileBean;
import org.apache.log4j.Logger;
import utils.PropertyLoader;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Ivan Gladush
 * @since 19.04.16.
 */
public class DbManager implements Closeable {
    private static final Logger logger = Logger.getLogger(DbManager.class);
    private static final Connection connection = ConnectionFactory.getConnection();
    private static final PropertyLoader PROPERTY_LOADER = PropertyLoader.getPropertyLoader("database.configuration.properties");
    private static final DbManager DB_MANAGER = new DbManager();

    private static final String EXCEPTION_CLOSE_CONNECTION = "I can't close connection";
    private static final String EXCEPTION_CREATE_TABLES = "I can't create tables";
    private static final String EXCEPTION_CREATE_STATEMENT = "When I create statement I caught exception";
    private static final String EXCEPTION_CHECK_CONTAINS_USER = "Exception when I check user contains in db or not contains %s";
    private static final String EXCEPTION_ADDED_USER = "Exception when I added new user %s";

    private static final String CREATE_TABLE = "create.%s";
    private static final String QUERY_FOR_COUNT_USER_WITH_ID = PROPERTY_LOADER.property("get.count.user.with.id");
    private static final String QUERY_FOR_COUNT_FILES_WITH_ID = PROPERTY_LOADER.property("get.count.file.with.id");
    private static final String QUERY_FOR_GET_ALL_FILES = PROPERTY_LOADER.property("get.all.upload.files");
    private static final String QUERY_CONTAINS_USER = PROPERTY_LOADER.property("contains.user");

    private PreparedStatement addUserStatement;
    private PreparedStatement addFileStatement;

    /**
     * gets a connection to the database and
     * verifies that all required tables, if
     * the table is not present, create it.
     * Upon receipt of the connection or create
     * a database may IllegalArgumentException
     * */
    static {
        try (Statement sql = connection.createStatement()) {
            ResultSet resultSet = sql.executeQuery(PROPERTY_LOADER.property("show.tables.query"));
            List<String> existTables = new ArrayList<>();
            while (resultSet.next()) {
                existTables.add(resultSet.getString(1));
            }
            createAllNeedTables(sql, existTables);

        } catch (SQLException e) {
            throw new IllegalArgumentException(EXCEPTION_CREATE_TABLES);
        }
    }

    private static void createAllNeedTables(Statement sql, List<String> existTables) throws SQLException {
        for (String s : PROPERTY_LOADER.property("need.tables").split(",")) {
            if (!existTables.contains(s)) {
                sql.execute(PROPERTY_LOADER.property(String.format(CREATE_TABLE, s)));
            }
        }
    }

    public DbManager() {
        createStatements();
    }

    public DbManager getDbManager() {
        return DB_MANAGER;
    }

    private void createStatements() {
        try {
            addUserStatement = connection.prepareStatement(PROPERTY_LOADER.property("add.user.query"));
            addFileStatement = connection.prepareStatement(PROPERTY_LOADER.property("add.file.query"));
        } catch (SQLException e) {
            logger.error(EXCEPTION_CREATE_STATEMENT);


            throw new IllegalStateException(EXCEPTION_CREATE_STATEMENT + e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            closeStatement();
            connection.close();
        } catch (SQLException e) {
            throw new IllegalStateException(EXCEPTION_CLOSE_CONNECTION + e.getMessage());
        }
    }


    private void closeStatement() throws SQLException {
        addUserStatement.close();
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

    public boolean containsUser(String userName, String userPassword) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(String.format(QUERY_CONTAINS_USER, userName, userPassword));
            resultSet.next();
            return resultSet.getInt(1) == 1;
        } catch (SQLException e) {
            logger.error(String.format(EXCEPTION_CHACK_CONTAINS_USER, e.getMessage()));
        }
        return false;
    }

    public boolean addNewFile(String fileName, String contentType, String userName) {
        try {
            addFileStatement.setString(1, fileName);
            addFileStatement.setString(2, contentType);
            addFileStatement.setString(3, userName);
            addFileStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error(String.format(CANT_SAVE_INFORMATION_ABOUT_FILE, fileName, e.getMessage()));
        }
        return false;
    }

    public  List<String> getAllUploadFiles(){
        List<String> answer=new ArrayList<>();
        try(Statement statement = connection.createStatement()){
            ResultSet resultSet=statement.executeQuery(QUERY_FOR_GET_ALL_FILES);
            while(resultSet.next()){
                answer.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            logger.error(String.format(EXCEPTION_GET_FILES,e.getMessage()));
            e.printStackTrace();
        }
        return answer;
    }

    public static FileBean getFileBean(String fileName) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(String.format(QUERY_FOR_COUNT_FILES_WITH_ID,fileName));
            if (resultSet.next()) {
                return new FileBean(resultSet.getString(1), resultSet.getString(2));
            }
        } catch (SQLException e) {
            logger.error(String.format("Cant get information about file %s because %s", fileName, e.getMessage()));
        }
        return null;
    }

    private static final String EXCEPTION_CHACK_CONTAINS_USER = "Can't check contains this user or not %s";
    private static final String CANT_SAVE_INFORMATION_ABOUT_FILE = "Cant save information about file %s in db because %s";
    private static final String EXCEPTION_GET_FILES = "Cant get all files from db because %s";
}
