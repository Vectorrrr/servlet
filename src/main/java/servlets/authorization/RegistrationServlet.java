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
public class RegistrationServlet extends HttpServlet {
    private static final DbManager DB_MANAGER=new DbManager();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name=request.getParameter("name");
        String password=request.getParameter("password");
        if(name==null || password==null ||!DB_MANAGER.addNewUser(name,password)){
            request.getRequestDispatcher("registration.jsp").forward(request,response);
            return;
        }
        response.addCookie(new Cookie("session",name));
        response.sendRedirect("/");


    }


}
