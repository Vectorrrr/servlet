package database.manager;

import database.ConnectionFactory;
import file.FileBean;
import org.apache.log4j.Logger;
import utils.PropertyLoader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Gladush
 * @since 21.04.16.
 */
public class FilesTable {
    private static final Logger logger = Logger.getLogger(FilesTable.class);
    private static final PropertyLoader PROPERTY_LOADER = PropertyLoader.getPropertyLoader("database.configuration.properties");//todo rewrite this property

    private static final String QUERY_FOR_COUNT_FILES_WITH_ID = PROPERTY_LOADER.property("get.count.file.with.id");
    private static final String QUERY_FOR_GET_ALL_FILES = PROPERTY_LOADER.property("get.all.upload.files");


    private static final String EXCEPTION_GET_FILES = "Cant get all files from db because %s";
    private static final String EXCEPTION_SAVE_NEW_FILE = "Cant save information about file %s in db because %s";

    private static Connection connection = ConnectionFactory.getConnection();

    private PreparedStatement addFileStatement;

    public FilesTable() throws SQLException {
        addFileStatement = connection.prepareStatement(PROPERTY_LOADER.property("add.file.query"));
    }

    public boolean addNewFile(String fileName, String contentType, String userName) {
        try {
            addFileStatement.setString(1, fileName);
            addFileStatement.setString(2, contentType);
            addFileStatement.setString(3, userName);
            addFileStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error(String.format(EXCEPTION_SAVE_NEW_FILE, fileName, e.getMessage()));
        }
        return false;
    }

    public FileBean getFileBean(String fileName) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(String.format(QUERY_FOR_COUNT_FILES_WITH_ID, fileName));
            if (resultSet.next()) {
                return new FileBean(resultSet.getString(1), resultSet.getString(2));
            }
        } catch (SQLException e) {
            logger.error(String.format("Cant get information about file %s because %s", fileName, e.getMessage()));
        }
        return null;
    }

    public List<String> getAllUploadFiles() {
        List<String> answer = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(QUERY_FOR_GET_ALL_FILES);
            while (resultSet.next()) {
                answer.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            logger.error(String.format(EXCEPTION_GET_FILES, e.getMessage()));
            e.printStackTrace();
        }
        return answer;
    }

    public void close() throws SQLException {
        connection.close();
    }
}
