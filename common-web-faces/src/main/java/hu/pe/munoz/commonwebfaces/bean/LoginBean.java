package hu.pe.munoz.commonwebfaces.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
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
import hu.pe.munoz.common.rest.RESTResponse;

@ManagedBean
@SessionScoped
public class LoginBean extends RESTBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Logger log = LoggerFactory.getLogger(getClass());

    private String inputUsername;

    private String inputPassword;

    private JSONObject user;
    private JSONObject userGroup;
    private JSONArray menuPermissions;

    @PostConstruct
    public void init() {
        log.info("@PostConstruct loginBean ...");
    }

    @SuppressWarnings("unchecked")
	public void doLogin() {
    	
    	JSONObject parameters = new JSONObject();
    	parameters.put("username", inputUsername.trim());
    	parameters.put("password", inputPassword);
    	RESTResponse response = restClient.doPost("localhost:8080/munoz-common-rest", "/login", parameters);
    	String status = response.getStatus();
    	if (CommonConstants.SUCCESS.equals(status)) {
    		user = (JSONObject) response.getData();
    		userGroup = (JSONObject) user.get("userGroup");
    		menuPermissions = (JSONArray) userGroup.get("userGroupMenuPermissions");
    	} else {
    		String message = response.getMessage();
    		// Try servlet login
    		doServletLogin(inputUsername.trim(), inputPassword);
    		if (user == null) {    			
    			Messages.addGlobalError(message, new Object[] {});
        		return;
    		}
    	}
    	
        if (user != null) {
        	Faces.setSessionAttribute(CommonConstants.SESSKEY_IS_LOGGED_IN, true);
        	Faces.setSessionAttribute(CommonConstants.SESSKEY_USER, user);
        	try {
        		String redirect = Faces.getSessionAttribute(CommonConstants.SESSKEY_REDIRECT);
                if (redirect != null) {
                	Faces.removeSessionAttribute(CommonConstants.SESSKEY_REDIRECT);
                	Faces.redirect(redirect, new String[] {});
                	return;
                }
                Faces.redirect("home.xhtml", new String[] {});
                return;
			} catch (Exception e) {
				e.printStackTrace();
    			Messages.addGlobalError(e.getMessage(), new Object[] {});
    			return;
			}
        }
    }

    @SuppressWarnings("unchecked")
	private void doServletLogin(String username, String password) {
    	try {
    		String hash = DigestUtils.sha1Hex(password);
        	Faces.login(username, hash);
			if(Faces.isUserInRole(CommonConstants.ROLE_SUPERADMIN)) {
				userGroup = new JSONObject();
				userGroup.put("id", -1L);
				user = new JSONObject();
				user.put("username", Faces.getRemoteUser());
				user.put("firstName", "Super");
				user.put("lastName", "Admin");
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
    	log.debug("Check permission " + type + ": " + menuCode);
    	if ((Long) userGroup.get("id") == -1) {
    		// User from servlet login (Super Admin)
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
		case "modify":
			for (int i = 0; i < menuPermissions.size(); i++) {
				JSONObject permission = (JSONObject) menuPermissions.get(i);
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
    
    public static void main(String[] args) {
		System.out.println(DigestUtils.sha1Hex("pwd!@#A"));
	}
    
}
