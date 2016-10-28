package hu.pe.munoz.commonwebfaces.bean;

import java.io.Serializable;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.common.helper.CommonUtils;
import hu.pe.munoz.common.helper.DefaultUser;
import hu.pe.munoz.common.helper.HttpClientResponse;
import hu.pe.munoz.common.helper.PasswordUtils;

@ManagedBean
@SessionScoped
public class LoginBean extends HttpClientBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(LoginBean.class);

    private ResourceBundle userBundle = ResourceBundle.getBundle("users");

    private String inputUsername;
    private String inputPassword;

    private JSONObject user;
    private JSONObject userGroup;
    private JSONArray menuPermissions;

    @Override
    protected void postConstruct() {
        super.postConstruct();
        LOG.info("Post construct LoginBean ...");
    }

    public void doLogin() {

        String paramUsername = inputUsername.trim();
        String paramPassword = inputPassword.trim();
        
        try {
            HttpClientResponse response = getHttpClient(hostUrl, "/login")
                    .addParameter("username", paramUsername)
                    .addParameter("password", paramPassword)
                    .post();
            if (response != null) {
                String status = response.getStatus();
                String message = response.getMessage();
                if (CommonConstants.SUCCESS.equals(status)) {
                    user = (JSONObject) response.getData();
                    userGroup = (JSONObject) user.get("userGroup");
                    menuPermissions = (JSONArray) userGroup.get("menuPermissions");
                } else {
                    // Check default users
                    checkDefaultUser(paramUsername, paramPassword);
                }
                if (user == null) {
                    Messages.addGlobalError(message);
                    return;
                }
            }
        } catch (Exception e) {
            LOG.error(e.toString(), e); 
            Messages.addGlobalError(CommonUtils.getExceptionMessage(e));
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
            Faces.redirect("dashboard.xhtml");
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            Messages.addGlobalError(e.getMessage(), new Object[]{});
        }
    }

    @SuppressWarnings("unchecked")
    private void checkDefaultUser(String username, String password) {
        String defaultUser = null;
        try {
            defaultUser = userBundle.getString(username);
        } catch (MissingResourceException e) {
            LOG.debug(e.getMessage());
        }
        if ((defaultUser == null) || defaultUser.trim().isEmpty()) {
            return;
        }
        String defaultUserFirstName = CommonConstants.EMPTY_STRING;
        String defaultUserLastName = CommonConstants.EMPTY_STRING;
        String[] dataUserSplit = defaultUser.split(",");
        switch (dataUserSplit.length) {
            case 1 :    // Only contains password
                String[] name = DefaultUser.chooseName();
                defaultUserFirstName = name[0];
                defaultUserLastName = name[1];
                break;
            case 2 :    // Only contains password and first name
                defaultUserFirstName = dataUserSplit[1].trim() ;
                break;
            case 3 :    // Contains password, first name, and last name
                defaultUserFirstName = dataUserSplit[1].trim();
                defaultUserLastName = dataUserSplit[2].trim() ;
                break;
            default:
                
        }
        String defaultUserPassword = dataUserSplit[0].trim();
        String stirredPassword = PasswordUtils.stir(password);
        if ((defaultUserPassword != null) && defaultUserPassword.equals(stirredPassword)) {
            userGroup = new JSONObject();
            userGroup.put("id", DefaultUser.USER_GROUP_ID);
            userGroup.put("name", DefaultUser.USER_GROUP_NAME);
            user = new JSONObject();
            user.put("id", DefaultUser.USER_ID);
            user.put("username", username);
            user.put("firstName", defaultUserFirstName);
            user.put("lastName", defaultUserLastName);
            user.put("userGroup", userGroup);
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
        if (Objects.equals((Long) userGroup.get("id"), DefaultUser.USER_GROUP_ID)) {
            return true;
        }
        if ("view".equals(type)) {
            for (Object menuPermission : menuPermissions) {
                JSONObject permission = (JSONObject) menuPermission;
                if (permission.get("menuCode").equals(menuCode) && CommonConstants.YES.equals(permission.get("view"))) {
                    return true;
                }
            }
        } else if ("modify".equals(type)) {
            for (Object menuPermission : menuPermissions) {
                JSONObject permission = (JSONObject) menuPermission;
                if (permission.get("menuCode").equals(menuCode) && CommonConstants.YES.equals(permission.get("modify"))) {
                    return true;
                }
            }
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

}
