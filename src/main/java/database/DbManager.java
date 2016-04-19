package database;

import utils.PropertyLoader;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Gladush
 * @since 19.04.16.
 */
public class DbManager implements Closeable{
    private static final PropertyLoader PROPERTY_LOADER=PropertyLoader.getPropertyLoader("database.configuration.properties");
    private static final String EXCEPTION_CLOSE_CONNECTION = "I can't close connection";
    private static final String EXCEPTION_CREATE_STATEMENT = "I can't create statement";
    private static final String EXCEPTION_CREATE_TABLES="I can't create tables";
    private static final String QUOTE = "'";
    private static final String EMPTY_STRING = "";
    public static final String CREATE_TABLE = "create.%s";
    private static Connection connection = ConnectionFactory.getConnection();


    private PreparedStatement addUserStatment;

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
                sql.execute(PROPERTY_LOADER.property(String.format(CREATE_TABLE,s)));
            }
        }
    }


    /**
     * method  closes the connection to the database
     * if you call a method on one instance this class,
     * all classes will cease the ability to save
     */
    public DbManager() {
        createStatements();
    }

    /**
     * method creates a state for all queries
     * implemented in this class
     * */
    private void createStatements() {
        try {
            addUserStatment = connection.prepareStatement(PROPERTY_LOADER.property("add.user.query"));

        } catch (SQLException e) {
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



    /**
     * method close a state for all queries
     * implemented in this class
     * */
    private void closeStatement() throws SQLException {

        addUserStatment.close();
    }

}
