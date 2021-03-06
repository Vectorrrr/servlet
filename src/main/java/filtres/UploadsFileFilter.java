package filtres;

import database.DbManager;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * This class is designed to deny access
 * to all authorized users are not on the
 * document download page
 * @author Ivan Gladush
 * @since 19.04.16.
 */
public class UploadsFileFilter implements Filter {
    private static final DbManager DB_MANAGER = DbManager.getDbManager();
    private static final String TRUE = "TRUE";
    private static final String SESSION = "session";
    private boolean active = false;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String act = filterConfig.getInitParameter("active");
        if (act != null)
            active = (act.toUpperCase().equals(TRUE));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (active) {
            for (Cookie cookie : ((HttpServletRequest) servletRequest).getCookies()) {
                if (SESSION.equals(cookie.getName()) && DB_MANAGER.thisLoginExist(cookie.getValue())) {
                    servletRequest.getRequestDispatcher("/upload").forward(servletRequest, servletResponse);
                    return;
                }
            }
            servletRequest.getRequestDispatcher("login.jsp").forward(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        active = false;
    }
}
