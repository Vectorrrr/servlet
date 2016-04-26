package database;

import database.manager.FilesTable;
import database.manager.TempUserTable;
import database.manager.UserTable;
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


    private static final String CREATE_TABLE = "create.%s";
    private UserTable userTable;
    private TempUserTable tempUserTable;
    private FilesTable filesTable;

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

    private DbManager() {
        createStatements();
    }

    private void createStatements() {
        try {
            userTable = new UserTable();
            tempUserTable = new TempUserTable();
            filesTable = new FilesTable();
        } catch (SQLException e) {
            logger.error(EXCEPTION_CREATE_STATEMENT);
            throw new IllegalStateException(EXCEPTION_CREATE_STATEMENT + e.getMessage());
        }
    }

    public static DbManager getDbManager() {
        return DB_MANAGER;
    }

    public boolean isValidUser(String session) {
        return userTable.isValidUser(session);

    }

    public boolean confirmUser(String userName, String password, String secretWord) {
        return tempUserTable.confirmUser(userName, password, secretWord);


    }

    public boolean addNewTempUser(String userName, String userPassword, String secretWord) {
        return tempUserTable.addNewUser(userName, userPassword, secretWord);

    }

    public boolean containsUser(String userName, String userPassword) {
        return userTable.containsUser(userName, userPassword);
    }

    public boolean addNewFile(String fileName, String contentType, String userName) {
        return filesTable.addNewFile(fileName, contentType, userName);

    }

    public List<String> getAllUploadFiles() {
        return filesTable.getAllUploadFiles();

    }

    public FileBean getFileBean(String fileName) {
        return filesTable.getFileBean(fileName);

    }

    public boolean isTempUser(String name) {
        return tempUserTable.isTempUser(name);

    }

    public boolean addNewUser(String loginUser, String password) {
        return userTable.addNewUser(loginUser, password);
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
        userTable.close();
        tempUserTable.close();
        filesTable.close();
    }
}
