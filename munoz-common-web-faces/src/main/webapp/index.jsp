<% 
    /**
     * This page is intended to fix the login filter not executed issue.
     * 
     * If we set the <welcome-file> in web.xml with login.xhtml,
     * then when we open localhost:8080/common-webapp/ in a browser, 
     * the site content will be the login page from login.xhtml file,
     * but the url did not change to localhost:8080/common-webapp/login.xhtml 
     * which is causing the login filter did not get executed.
     * 
     * This index.jsp will do a full redirect to login.xhtml page 
     * so the url will change to localhost:8080/common-webapp/login.xhtml
     * and the login filter will get executed.
     */
    response.sendRedirect("login.xhtml"); 
%>