package file;


import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import utils.PropertyLoader;

import javax.servlet.ServletOutputStream;
import java.io.*;

/**
 * @author Ivan Gladush
 * @since 18.04.16.
 */
public class FileLoader {
    private static final Logger logger = Logger.getLogger(FileLoader.class);
    private static final PropertyLoader PROPERTY_LOADER = PropertyLoader.getPropertyLoader("file.configurations.properties");
    private static final String PATH_TO_DEF_DIR = PROPERTY_LOADER.property("path.to.default.directory");
    private static final String MANIFEST_FILE_PATH = PATH_TO_DEF_DIR + "/" + PROPERTY_LOADER.property("manifest.file.name");
    private static final String EMPTY_STRING = "";

    public static FileBean getFile(String fileName) {
        FileBean fileBean = containedInManifest(fileName);
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


    private static FileBean containedInManifest(String fileName) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(MANIFEST_FILE_PATH))) {
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                if (s.equals(fileName)) {
                    return new FileBean(fileName, bufferedReader.readLine());
                }
            }
        } catch (IOException e) {
            logger.error(String.format(EXCEPTION_WHEN_CHECK_MANIFEST, fileName, e.getMessage()));
        }
        return null;
    }

    private static File getFileFromBean(FileBean fileBean) {
        return new File(String.format("%s/%s", PATH_TO_DEF_DIR, fileBean.getFileName()));
    }

    private static final String EXCEPTION_WHEN_CHECK_MANIFEST = "When I checked if there is a name in the manifest file " +
            "%s I have exception %s";
    private static final String EXCEPTION_ADD_FILE_CONTENT_IN_STREAM = "When I added file content in stream I have exception %s";

}
