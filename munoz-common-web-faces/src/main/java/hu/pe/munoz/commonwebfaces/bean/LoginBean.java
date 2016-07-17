package hu.pe.munoz.commonwebfaces.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.ServletException;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.common.helper.HttpClient;
import hu.pe.munoz.common.helper.HttpClientResponse;
import hu.pe.munoz.common.helper.SuperAdmin;
import java.util.Objects;

@ManagedBean
@SessionScoped
public class LoginBean extends RESTBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Logger log = LoggerFactory.getLogger(LoginBean.class);

    private String inputUsername;

    private String inputPassword;

    private JSONObject user;
    private JSONObject userGroup;
    private JSONArray menuPermissions;

    @Override
    protected void postConstruct() {
        super.postConstruct();
        log.info("Post construct LoginBean ...");
    }

    @SuppressWarnings("unchecked")
    public void doLogin() {

        try {
            HttpClientResponse response = getHttpClient(hostUrl, "/login")
                    .addParameter("username", inputUsername.trim())
                    .addParameter("password", inputPassword)
                    .post();
            if (response != null) {
                String status = response.getStatus();
                String message = response.getMessage();
                if (CommonConstants.SUCCESS.equals(status)) {
                    user = (JSONObject) response.getData();
                    userGroup = (JSONObject) user.get("userGroup");
                    menuPermissions = (JSONArray) userGroup.get("userGroupMenuPermissions");
                } else {
                    // Try servlet login
                    doServletLogin(inputUsername.trim(), inputPassword);
                }
                if (user == null) {
                    Messages.addGlobalError(message);
                    return;
                }
            }
        } catch (Exception e) {
            log.error(e.toString(), e); 
            String message = (e.getMessage() == null) ? e.toString() : e.getMessage();
            Messages.addGlobalError(message);
            return;
        }
        
        // User can't be null here
        Faces.setSessionAttribute(CommonConstants.SESSKEY_IS_LOGGED_IN, true);
        Faces.setSessionAttribute(CommonConstants.SESSKEY_USER, user);
        try {
            String redirect = Faces.getSessionAttribute(CommonConstants.SESSKEY_REDIRECT);
            if (redirect != null) {
                Faces.removeSessionAttribute(CommonConstants.SESSKEY_REDIRECT);
                Faces.redirect(redirect);
                return;
            }
            Faces.redirect("home.xhtml");
        } catch (Exception e) {
            log.error(e.toString(), e);
            Messages.addGlobalError(e.getMessage(), new Object[]{});
        }
    }

    @SuppressWarnings("unchecked")
    private void doServletLogin(String username, String password) {
        try {
            String hash = DigestUtils.sha1Hex(password);
            Faces.login(username, hash);
            if (Faces.isUserInRole(SuperAdmin.USER_GROUP_NAME)) {
                userGroup = new JSONObject();
                userGroup.put("id", SuperAdmin.USER_GROUP_ID);
                userGroup.put("name", SuperAdmin.USER_GROUP_NAME);
                user = new JSONObject();
                user.put("username", Faces.getRemoteUser());
                user.put("firstName", SuperAdmin.FIRST_NAME);
                user.put("lastName", SuperAdmin.LAST_NAME);
                user.put("userGroup", userGroup);
            }
        } catch (ServletException e) {
            log.debug(e.toString());
        }
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public String doLogout() {
        Faces.invalidateSession();
        return "/logout.xhtml?faces-redirect=true";
    }

    private boolean checkPermission(String menuCode, String type) {
        if (Objects.equals((Long) userGroup.get("id"), SuperAdmin.USER_GROUP_ID)) {
            return true;
        }
        switch (type) {
            case "view":
                for (int i = 0; i < menuPermissions.size(); i++) {
                    JSONObject permission = (JSONObject) menuPermissions.get(i);
                    if (permission.get("menuCode").equals(menuCode) && CommonConstants.YES.equals(permission.get("view"))) {
                        return true;
                    }
                }
                break;
            case "modify":
                for (int i = 0; i < menuPermissions.size(); i++) {
                    JSONObject permission = (JSONObject) menuPermissions.get(i);
                    if (permission.get("menuCode").equals(menuCode) && CommonConstants.YES.equals(permission.get("modify"))) {
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    public boolean isViewAllowed(String menuCode) {
        return checkPermission(menuCode, "view");
    }

    public boolean isModifyAllowed(String menuCode) {
        return checkPermission(menuCode, "modify");
    }

    // Getters and setters
    public String getInputUsername() {
        return inputUsername;
    }

    public void setInputUsername(String inputUsername) {
        this.inputUsername = inputUsername;
    }

    public String getInputPassword() {
        return inputPassword;
    }

    public void setInputPassword(String inputPassword) {
        this.inputPassword = inputPassword;
    }

    public JSONObject getUser() {
        return user;
    }

    public JSONObject getUserGroup() {
        return userGroup;
    }

    public JSONArray getMenuPermissions() {
        return menuPermissions;
    }

    public static void main(String[] args) {
        System.out.println(DigestUtils.sha1Hex("pwd!@#A"));
    }

}
