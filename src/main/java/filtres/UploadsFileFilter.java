package filtres;

import java.io.*;
import javax.servlet.*;

/**
 * @author Ivan Gladush
 * @since 19.04.16.
 */
public class UploadsFileFilter implements Filter {
    private static final String TRUE = "TRUE";
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
            servletRequest.getRequestDispatcher("/").forward(servletRequest,servletResponse);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        filterConfig = null;
        active = false;
    }
}
