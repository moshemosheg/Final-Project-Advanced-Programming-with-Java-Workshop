/** **********************************************************************************
 * filter to prevent access if user not logged In.
 * prevents access to all pages in 'user' folder.
 * redirects to log in page
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

@WebFilter("/faces/user/*")
public class RegisteredFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        //gets session if exist
        HttpSession session = request.getSession(false);
        // checks if "user" was stored in session
        boolean loggedIn = (session != null) && (session.getAttribute("user") != null);
        if (loggedIn) {
            // Prevent browser from caching restricted resources. 
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
            response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            response.setDateHeader("Expires", 0); // Proxies.
            // continue request.
            chain.doFilter(request, response);
        } else {
            // redirect to login page.
            String loginURL = request.getContextPath() + "/faces/logIn.xhtml";
            response.sendRedirect(loginURL);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
