package file;


import database.DbManager;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import utils.PropertyLoader;

import javax.servlet.ServletOutputStream;
import java.io.*;

/**
 * This class is designed to download files.
 * He can write the contents of a particular
 * file to a ServletOutputStream.
 * @author Ivan Gladush
 * @since 18.04.16.
 */
public class FileLoaderServlet {
    private static final Logger logger = Logger.getLogger(FileLoaderServlet.class);
    private static final DbManager DB_MANAGER = DbManager.getDbManager();
    private static final PropertyLoader PROPERTY_LOADER = PropertyLoader.getPropertyLoader("file.configurations.properties");
    private static final String PATH_TO_DEF_DIR = PROPERTY_LOADER.property("path.to.default.directory");
    private static final String EMPTY_STRING = "";

    public static FileBean getFile(String fileName) {
        FileBean fileBean = DB_MANAGER.getFileBean(fileName);
        if (fileBean == null) {
            return new FileBean(EMPTY_STRING, EMPTY_STRING);
        }
        return fileBean;
    }

    public static void addFileContentInStream(FileBean fileBean, ServletOutputStream out) {
        try {
            FileUtils.copyFile(getFileFromBean(fileBean), out);
            out.flush();
        } catch (IOException e) {
            logger.error(String.format(EXCEPTION_ADD_FILE_CONTENT_IN_STREAM, e.getMessage()));
        }
    }


    private static File getFileFromBean(FileBean fileBean) {
        return new File(String.format("%s/%s", PATH_TO_DEF_DIR, fileBean.getFileName()));
    }

    private static final String EXCEPTION_ADD_FILE_CONTENT_IN_STREAM = "When I added file content in stream I have exception %s";
}