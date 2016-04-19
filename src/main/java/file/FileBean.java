package file;

/**
 * @author Ivan Gladush
 * @since 19.04.16.
 */
public class FileBean {
    private String contentType;
    private String fileName;

    public FileBean(String fileName , String contentType) {
        this.contentType = contentType;
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileName() {
        return fileName;
    }


}
