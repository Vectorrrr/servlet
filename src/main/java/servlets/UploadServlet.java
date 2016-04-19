package servlets;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
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

/**
 * @author Ivan Gladush
 * @since 15.04.16.
 */
public class UploadServlet extends HttpServlet {
    private static final Logger log= Logger.getLogger(UploadServlet.class);
    private static final String EXCEPTION_SAVE_FILE = "Exception when I save file from client %s";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("upload.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (ServletFileUpload.isMultipartContent(request)) {
            downloadFileInServer(request);
        }
        request.getRequestDispatcher("upload.jsp").forward(request, response);
    }

    private void downloadFileInServer(HttpServletRequest request) throws IOException {
        ServletFileUpload upload = getServletFileUpload();
        try {
            saveFiles(upload.parseRequest(request));
        } catch (FileUploadException e) {
          log.error(EXCEPTION_SAVE_FILE);
        }
    }

    private ServletFileUpload getServletFileUpload() {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        return new ServletFileUpload(factory);
    }

    private void saveFiles(List<FileItem> items) throws IOException {
        for (FileItem item : items) {
            try (FileSaver fileSaver = new FileSaver(item.getName(), item.getContentType())) {
                fileSaver.saveInFile(item.get());
            }
        }
    }
}
