package servlets;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import file.FileSaver;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;

import static org.apache.commons.fileupload.servlet.ServletFileUpload.*;

/**
 * @author Ivan Gladush
 * @since 15.04.16.
 */
public class UploadServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(UploadServlet.class);
    private static final String EXCEPTION_SAVE_FILE = "Exception when I save file from client %s";
    private static final String SESSION = "session";
    private static final String EMPTY_STRING = "";
    private static final String USER_SAVED_THE_FILE_BUT_IT_DOES_NOT_HAVE_A_NAME = "User saved the file, but it does not have a name";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("upload.jsp").forward(request, response);

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (isMultipartContent(request)) {
            downloadFileInServer(request);
        }
        request.getRequestDispatcher("upload.jsp").forward(request, response);
    }

    private void downloadFileInServer(HttpServletRequest request) throws IOException {
        ServletFileUpload upload = getServletFileUpload();
        String userName= getUserName(request);
        try {
            saveFiles(upload.parseRequest(request),userName);
        } catch (FileUploadException e) {
            logger.error(EXCEPTION_SAVE_FILE);
        }
    }

    private String getUserName(HttpServletRequest request) {
        for(Cookie cookie:request.getCookies()){
            if(SESSION.equals(cookie.getName())){
                return cookie.getValue();
            }
        }
        logger.error(USER_SAVED_THE_FILE_BUT_IT_DOES_NOT_HAVE_A_NAME);
        return EMPTY_STRING;
    }

    private ServletFileUpload getServletFileUpload() {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        return new ServletFileUpload(factory);
    }

    private void saveFiles(List<FileItem> items,String userName) throws IOException {
        for (FileItem item : items) {
            try (FileSaver fileSaver = new FileSaver(item.getName(), item.getContentType(),userName)) {
                fileSaver.saveInFile(item.get());
            }
        }
    }
}
