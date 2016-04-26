package servlets;


import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ivan Gladush
 * @since 15.04.16.
 */
public class RootServlet extends HttpServlet {
    private static final String EMPTY_STRING = "";
    private static final String SESSION = "session";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("name", getUserName(request));
        request.getRequestDispatcher("root.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("name", "");
        deleteSessionInformation(request, response);
        response.sendRedirect("/");
    }

    private void deleteSessionInformation(HttpServletRequest request, HttpServletResponse response) {
        for (Cookie c : request.getCookies()) {
            if (SESSION.equals(c.getName())) {
                c.setValue("");
                c.setMaxAge(0);
                response.addCookie(c);
            }
        }
    }

    private String getUserName(HttpServletRequest request) {
        for (Cookie c : request.getCookies()) {
            if (SESSION.equals(c.getName())) {
                return c.getValue();
            }
        }
        return EMPTY_STRING;
    }
}
