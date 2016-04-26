package servlets.authorization;

import database.DbManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ivan Gladush
 * @since 21.04.16.
 */
public class ConfirmServlet extends HttpServlet {
    private static final DbManager DB_MANAGER = DbManager.getDbManager();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String loginUser = request.getParameter("name");
        String secretWord = request.getParameter("secretWord");
        String password = request.getParameter("password");
        if (DB_MANAGER.confirmUser(loginUser, password, secretWord)) {
            DB_MANAGER.addNewUser(loginUser, password);
        }
        response.sendRedirect("/");
    }
}
