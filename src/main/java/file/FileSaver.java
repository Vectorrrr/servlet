package file;

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
    private static final PropertyLoader PROPERTY_LOADER = PropertyLoader.getPropertyLoader("file.configurations.properties");
    private static final String PATH_TO_DEF_DIR = PROPERTY_LOADER.property("path.to.default.directory");
    private static final String MANIFEST_FILE_NAME = PATH_TO_DEF_DIR + "/" + PROPERTY_LOADER.property("manifest.file.name");
    private static final String COULD_NOT_CREATE_DEF_FOLDER = "Could not create a folder for saving files";
    private static final String NEW_LINE = "\n";
    private static final String EMPTY_STRING = "";

    private BufferedOutputStream outputStream;

    static {
        File defDir = new File(PATH_TO_DEF_DIR);
        if (!defDir.exists() && !defDir.mkdirs()) {
            logger.warn(COULD_NOT_CREATE_DEF_FOLDER);
        }
    }

    public FileSaver(String fileName, String contentType) throws IOException {
        File f = new File(String.format("%s/%s", PATH_TO_DEF_DIR, fileName));
        if (!f.createNewFile()) {
            logger.warn(FAILED_CREATE_NEW_FILE);
            throw new IllegalStateException(DONT_CREATE_FILE);
        }

        try (FileWriter fw = new FileWriter(MANIFEST_FILE_NAME, true)) {
            writeLine(fileName, fw);
            writeLine(contentType, fw);
        }

        outputStream = new BufferedOutputStream(new FileOutputStream(f));
    }

    private void writeLine(String contentType, FileWriter fw) throws IOException {
        fw.write(contentType);
        fw.write(NEW_LINE);
        fw.flush();
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
        try (BufferedReader br = new BufferedReader(new FileReader(new File(MANIFEST_FILE_NAME)))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(needFileName)) {
                    answer.add(line);
                }
                br.readLine();
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return answer;
    }

    @Override
    public void close() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }
    }

    private static final String DONT_CREATE_FILE = "I don't create a new file for download this document";
    private static final String FAILED_CREATE_NEW_FILE = "Failed to create a new file to save resources";

}
