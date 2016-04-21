package servlets.authorization;

import database.DbManager;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ivan Gladush
 * @since 20.04.16.
 */
public class LoginServlet extends HttpServlet {
    private static final DbManager DB_MANAGER = new DbManager();
    private static final String EMPTY_STRING = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String session = doAuthorizedUser(request);
        if (EMPTY_STRING.equals(session)) {
            response.sendRedirect("registration.jsp");
            return;
        }
        addCookie(response, session);
        response.sendRedirect("/upload");
    }

    private void addCookie(HttpServletResponse response, String session) {
        Cookie cookie= new Cookie("session", session);
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }

    private String doAuthorizedUser(HttpServletRequest request) {
        String userName = request.getParameter("name");
        String userPassword = request.getParameter("password");
        if (userName != null && userPassword != null) {
            if( DB_MANAGER.containsUser(userName, userPassword)){
                return userName;
            }
        }
        return EMPTY_STRING;
    }
}
