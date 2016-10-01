package hu.pe.munoz.commonwebfaces.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import hu.pe.munoz.common.helper.CommonConstants;

public class LoginFilter implements Filter {

    // private static final Logger LOG = LoggerFactory.getLogger(LoginFilter.class);
    
    private static final List<String> LIST_FILTER_EXCLUDED_PAGES = Arrays.asList(
            "/page-not-found.xhtml",
            "/server-error.xhtml"
    );
    private static final String DASHBOARD_PAGE = "/dashboard.xhtml";
    private static final String LOGIN_PAGE = "/login.xhtml";
    private static final String LOGOUT_PAGE = "/logout.xhtml";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // This method will process all requests to any pages / css / js / images, 
        // so the log information might be printed more than once.

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession httpSession = httpRequest.getSession(true); // With 'true' it creates a session if it does not exist, with 'false' it returns active session if exists otherwise null. 
        String requestUrl = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String loginUrl = contextPath + LOGIN_PAGE;
        String logoutUrl = contextPath + LOGOUT_PAGE;
        String dashboardUrl = contextPath + DASHBOARD_PAGE;

        boolean loggedIn = (httpSession != null) && (Boolean.valueOf(String.valueOf(httpSession.getAttribute(CommonConstants.SESSKEY_IS_LOGGED_IN))));
        boolean loginRequest = requestUrl.equals(loginUrl) || requestUrl.equals(loginUrl.substring(0, loginUrl.indexOf(".xhtml")));
        boolean logoutRequest = requestUrl.equals(logoutUrl) || requestUrl.equals(logoutUrl.substring(0, logoutUrl.indexOf(".xhtml")));
        boolean resourceRequest = requestUrl.startsWith(contextPath + ResourceHandler.RESOURCE_IDENTIFIER + "/");
        
        // Read http://stackoverflow.com/questions/14526574/jsf-page-style-missing-when-using-login-filter
        
        if (!resourceRequest) {
            // Prevent browser from caching restricted resources. See also http://stackoverflow.com/q/4194207/157882
            // Note that no-cache headers should not be set on resource requests, 
            // otherwise you defeat the benefit of the browser cache on CSS/JS/image files.
            httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
            httpResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            httpResponse.setDateHeader("Expires", 0); // Proxies.
        }

        // 1. Check if the page is excluded from login filter
        if (LIST_FILTER_EXCLUDED_PAGES.contains(requestUrl.substring(requestUrl.lastIndexOf("/")))) {
            chain.doFilter(request, response);
        } 

        // 2. Check if the page is resource request
        else if (resourceRequest) {
            chain.doFilter(request, response);
        } 

        // 3. Check if the page is logout request, prevent user to directly open logout page
        else if (logoutRequest) {
            // If the user has logged in, then redirect to home page
            if (loggedIn) {
                httpResponse.sendRedirect(dashboardUrl);
            } 
            // If the user has not logged in, then continue the request
            else {
                chain.doFilter(request, response);
            }
        } 

        // 4. Check if the page is login request
        else if (loginRequest) {
            // If the user has logged in, then redirect to home page
            if (loggedIn) {
                httpResponse.sendRedirect(dashboardUrl);
            } 
            // If the user has not logged in, then continue the request
            else {
                chain.doFilter(request, response);
            }
        } 

        // 5. Check if the page is not login request
        else {
            // If the user has logged in, then continue the request
            if (loggedIn) {
                chain.doFilter(request, response);
            } 
            // If the user has not logged in, then redirect to login page with "redirect" parameter
            else {
                httpSession.setAttribute(CommonConstants.SESSKEY_REDIRECT, requestUrl.substring(contextPath.length() + 1));
                httpResponse.sendRedirect(loginUrl);
            }
        }

    }

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

    @Override
    public void destroy() {

    }

}
