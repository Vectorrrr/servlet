package servlets.authorization;

import database.DbManager;
import sender.EmailSender;
import utils.PropertyLoader;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * Class is designed for user registration.
 * @author Ivan Gladush
 * @since 20.04.16.
 */
public class RegistrationServlet extends HttpServlet {
    private static final DbManager DB_MANAGER = DbManager.getDbManager();
    private static final PropertyLoader PROPERTY_LOADER = PropertyLoader.getPropertyLoader("file.configurations.properties");
    private static final String QUERY_SKELETON = PROPERTY_LOADER.property("skeleton.for.url");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        if (name == null || password == null || email == null) {
            request.setAttribute("notCorrect", "false");
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return;
        }
        if (DB_MANAGER.isTempUser(name) || DB_MANAGER.thisLoginExist(name)) {
            request.setAttribute("exist", "false");
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return;
        }

        String secretKey = generateNewSecretKey();
        if (!DB_MANAGER.addNewTempUser(name, password, secretKey)) {
            request.setAttribute("message", "I can't add this user in db, try again and change your login");
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return;
        }

        sendComfirMessage(response, name, password, email, secretKey);
    }

    /**
     * The method sends an email to the user to complete the registration
     */
    private void sendComfirMessage(HttpServletResponse response, String name, String password, String email, String secretKey) throws IOException {
        new EmailSender().addSubject("Please confirm your registration")
                .setRecipient(email)
                .addBody("Please confirm your registration in my site")
                .addBody("\n\n")
                .addBody(String.format(QUERY_SKELETON, name, password, secretKey)).sendMessage();

        response.addCookie(new Cookie("session", name));
        response.sendRedirect("/");
    }

    private String generateNewSecretKey() {
        StringBuilder randString = new StringBuilder();
        for (int i = 0; i < 16; i++)
            randString.append((char) ((Math.random() * 100) % 25 + 97));

        return randString.toString();
    }
}
