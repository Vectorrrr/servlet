package servlets;


import file.FileSaver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;


import parser.RequestParser;

/**
 * @author root
 * @since 15.04.16.
 */
public class UploadServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("upload.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //todo make this in filter
        InputStreamReader reader = new InputStreamReader(request.getInputStream());
        StringBuilder sb=new StringBuilder();
        int c;
        while ((c = reader.read()) >= 0) {
            sb.append((char)c);
        }
        String requestBody=sb.toString();
        String fileName= RequestParser.getFileName(requestBody);
        String bounder=RequestParser.getBounder(requestBody);
        String content=RequestParser.getFileContent(requestBody,bounder);
        try (FileSaver fileSaver = new FileSaver(fileName)) {
            fileSaver.saveInFile(content);
        }

        request.getRequestDispatcher("upload.jsp").forward(request,response);
    }
}
