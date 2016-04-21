package servlets;


import file.FileBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static file.FileLoaderServlet.addFileContentInStream;
import static file.FileLoaderServlet.getFile;
import static file.FileSaver.getDownloadsFileName;


/**
 * @author Ivan Gladush
 * @since 18.04.16.
 */
public class FileDownloader extends HttpServlet{
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String ATTACHMENT_FILENAME_FORMAT = "attachment; filename=\"%s\"";
    private static final String FILES = "files";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("hid") == null) {
            getListFiles(request, response);
        } else {
            downloadFile(request, response);
        }
    }

    private void downloadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        FileBean fileBean = getFile(request.getParameter("fileName"));
        response.setContentType(fileBean.getContentType());
        response.setHeader(CONTENT_DISPOSITION,
                String.format(ATTACHMENT_FILENAME_FORMAT, fileBean.getFileName()));

        addFileContentInStream(fileBean, response.getOutputStream());
    }

    private void getListFiles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(FILES, getDownloadsFileName(request.getParameter("name")));
        request.getRequestDispatcher("download.jsp").forward(request, response);
    }


}
