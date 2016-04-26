package file;

import database.DbManager;
import org.apache.log4j.Logger;
import utils.PropertyLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Gladush
 * @since 15.04.16.
 */
public class FileSaver implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(FileSaver.class);
    private static final DbManager DB_MANAGER= DbManager.getDbManager();
    private static final PropertyLoader PROPERTY_LOADER = PropertyLoader.getPropertyLoader("file.configurations.properties");
    private static final String PATH_TO_DEF_DIR = PROPERTY_LOADER.property("path.to.default.directory");

    private static final String EXCEPTION_SAVE_META_INFORMATION_ABOUT_FILE = "Cant save meta information about file in db";
    private static final String EXCEPTION_CREATE_FOLDER = "Could not create a folder for saving files";
    private static final String EXCEPTION_CREATE_FILE = "I don't create a new file for download this document";
    private static final String FAILED_CREATE_NEW_FILE = "Failed to create a new file to save resources";

    private static final String EMPTY_STRING = "";

    private BufferedOutputStream outputStream;

    static {
        File defDir = new File(PATH_TO_DEF_DIR);
        if (!defDir.exists() && !defDir.mkdirs()) {
            logger.warn(EXCEPTION_CREATE_FOLDER);
        }
    }

    public FileSaver(String fileName, String contentType,String UserName) throws IOException {
        saveInformationInDb(fileName, contentType, UserName);
        outputStream = new BufferedOutputStream(new FileOutputStream(createNewFile(fileName)));
    }

    private void saveInformationInDb(String fileName, String contentType, String UserName) {
        if(!DB_MANAGER.addNewFile(fileName,contentType,UserName)){
            logger.warn(EXCEPTION_SAVE_META_INFORMATION_ABOUT_FILE);
            throw new IllegalStateException(EXCEPTION_SAVE_META_INFORMATION_ABOUT_FILE);
        }
    }

    private File createNewFile(String fileName) throws IOException {
        File f = new File(String.format("%s/%s", PATH_TO_DEF_DIR, fileName));
        if (!f.createNewFile()) {
            logger.warn(FAILED_CREATE_NEW_FILE);
            throw new IllegalStateException(EXCEPTION_CREATE_FILE);
        }
        return f;
    }



    public void saveInFile(byte[] content) throws IOException {
        outputStream.write(content);
        outputStream.flush();
    }

    public static List<String> getDownloadsFileName(String needFileName) {
        if (needFileName == null) {
            needFileName = EMPTY_STRING;
        }
        List<String> answer = new ArrayList<>();
        for (String file : DB_MANAGER.getAllUploadFiles()) {
            if (file.contains(needFileName)) {
                answer.add(file);
            }
        }
        return answer;
    }

    @Override
    public void close() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }
    }


}
