package filtres;

import database.DbManager;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Ivan Gladush
 * @since 19.04.16.
 */
public class UploadsFileFilter implements Filter {
    private static final DbManager DB_MANAGER = new DbManager();
    private static final String TRUE = "TRUE";
    private static final String SESSION = "session";
    private FilterConfig filterConfig = null;
    private boolean active = false;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        String act = filterConfig.getInitParameter("active");
        if (act != null)
            active = (act.toUpperCase().equals(TRUE));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (active) {
            for (Cookie cookie : ((HttpServletRequest) servletRequest).getCookies()) {
                if (SESSION.equals(cookie.getName()) && DB_MANAGER.isValidUser(cookie.getValue())) {
                    servletRequest.getRequestDispatcher("/upload").forward(servletRequest, servletResponse);
                    return;
                }
            }
            servletRequest.getRequestDispatcher("login.jsp").forward(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        filterConfig = null;
        active = false;
    }
}
