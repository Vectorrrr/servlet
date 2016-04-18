package file;

import org.apache.log4j.Logger;
import utils.PropertyLoader;

import java.io.*;

/**
 * @author root
 * @since 15.04.16.
 */
public class FileSaver implements AutoCloseable {
    private static final Logger logger= Logger.getLogger(FileSaver.class);
    private static final PropertyLoader PROPERTY_LOADER = PropertyLoader.getPropertyLoader("file.configurations.properties");
    private static final String PATH_TO_DEF_DIR="files";
    private static final byte[] NEW_STRING="\n".getBytes();
    private BufferedOutputStream outputStream;
    private static final String COULD_NOT_CREATE_DEF_FOLDER = "Could not create a folder for saving files";


    static {
        File defDir=new File(PATH_TO_DEF_DIR);
        if(!defDir.exists() && !defDir.mkdirs()){
//            logger.warn(COULD_NOT_CREATE_DEF_FOLDER);
        }
    }
    public FileSaver(String fileName) throws IOException {
        if(fileName!=null){
//        logger.info(String.format(CREATE_NEW_FILE_SAVER,fileName));
        }
        File f=new File(String.format("%s/%s",PATH_TO_DEF_DIR,fileName));
        if(!f.createNewFile()){
//            logger.warn("Failed to create a new file to save resources");
        }
        outputStream=new BufferedOutputStream(new FileOutputStream(f));
    }
    public void saveInFile(String content) throws IOException {
        outputStream.write(content.getBytes());
        outputStream.flush();
    }
    public void saveInFile(int c) throws IOException {
        outputStream.write((byte)c);
        outputStream.write(NEW_STRING);
    }

    @Override
    public void close() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }
    }

    private  static final String CREATE_NEW_FILE_SAVER = "CREATE NEW FILE SAVER %";
}
