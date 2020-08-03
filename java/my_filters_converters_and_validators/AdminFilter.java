/** **********************************************************************************
 * filter to prevent access to "admmin" folder, if user is not with admin privileges.
 * redirects to homepage if user is not with admin privileges.
 *********************************************************************************** */
package my_filters_converters_and_validators;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/faces/user/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        //gets session if exist and checks if user is stored in session as "admin"
        HttpSession session = request.getSession(false);
        boolean admin = (session != null) && (session.getAttribute("admin") != null);
        // if user is admin than continues users request       
        if (admin) {
            // Prevent browser from caching restricted resources.
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
            response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            response.setDateHeader("Expires", 0); // Proxies.
            // continue request.
            chain.doFilter(request, response);
        // if user isnt admin -redirect to home page    
        } else {
            // redirect to home page.
            String homePageUrl = request.getContextPath() + "/faces/user/homePage.xhtml";
            response.sendRedirect(homePageUrl);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
