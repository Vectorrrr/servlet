package utils;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Class allows you to download
 * property resources in program
 * @author Ivan Gladush
 * @since 15.04.16.
 */
public class PropertyLoader {
    private static final Logger logger = Logger.getLogger(PropertyLoader.class);
    private static final String EXCEPTION_DOWNLOAD_PROPERTIES = "I can't download properties %s";
    private static final String EXCEPTION_PROPERTY_DONT_FOUND = "This property not exist";
    private static final String CANT_FOUND_RESOURCE_FILE = "I can not find a resource file";
    private Properties properties = new Properties();

    private PropertyLoader(String fileName) throws IOException {
        URL url = PropertyLoader.class.getClassLoader().getResource(fileName);
        if (url != null) {
            InputStream inputStream = url.openStream();
            properties.load(inputStream);
            IOUtils.closeQuietly(inputStream);
        } else {
            throw new FileNotFoundException(CANT_FOUND_RESOURCE_FILE);
        }
    }

    public static PropertyLoader getPropertyLoader(String fileName) {
        try {
            return new PropertyLoader(fileName);
        } catch (IOException e) {
            logger.error(String.format(EXCEPTION_DOWNLOAD_PROPERTIES, e));
            throw new IllegalStateException(String.format(EXCEPTION_DOWNLOAD_PROPERTIES, e));
        }
    }

    public String property(String propertyKey) {
        String property = properties.getProperty(propertyKey);
        if (property == null) {
            logger.error(EXCEPTION_PROPERTY_DONT_FOUND);
            throw new IllegalStateException(EXCEPTION_PROPERTY_DONT_FOUND);
        }
        return property;
    }
}