package hu.pe.munoz.commonwebfaces.bean;

import static hu.pe.munoz.commonwebfaces.helper.PageMode.ADD;
import static hu.pe.munoz.commonwebfaces.helper.PageMode.EDIT;
import static hu.pe.munoz.commonwebfaces.helper.PageMode.INDEX;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.common.rest.RESTResponse;

@ManagedBean
@ViewScoped
public class UserGroupBean extends DefaultBehaviorBean implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    private JSONArray userGroups;
    private JSONArray inputMenus;
    private String inputGroupName;

    @ManagedProperty(value = "#{menuBean}")
    protected MenuBean menuBean;

    public void setMenuBean(MenuBean menuBean) {
        this.menuBean = menuBean;
    }

    @Override
    protected void postConstruct() {
    	super.postConstruct();
    	log.debug("Post construct UserGroupBean ...");
        switch (mode) {
		case INDEX:			
			loadUserGroups();
			break;
		case ADD:
			prepareAdd();
			break;
		case EDIT:
			prepareEdit();
			break;
		}
    }

    private void loadUserGroups() {
    	RESTResponse response = restClient.fetchGet(hostUrl, "/settings/user-group/list", null);
        if (CommonConstants.SUCCESS.equals(response.getStatus())) {
        	userGroups = (JSONArray) response.getData();
        } else {
        	log.debug(response.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
	private void prepareAdd() {
    	// Load menus
    	JSONArray menus = menuBean.getMenus(); // Two level menus: parent and children.
    	inputMenus = new JSONArray();
    	for (int i = 0; i < menus.size(); i++) {
    		JSONObject menu = (JSONObject) menus.get(i);
    		menu.put("viewBoolean", false);
    		menu.put("modifyBoolean", false);
    		inputMenus.add(menu);
    		JSONArray submenus = (JSONArray) menu.get("submenus");
    		for (int j = 0; j < submenus.size(); j++) {
    			JSONObject submenu = (JSONObject) submenus.get(j);
    			submenu.put("viewBoolean", false);
    			submenu.put("modifyBoolean", false);
    			inputMenus.add(submenus.get(j));
    		}
    	}
    }
    
    @SuppressWarnings("unchecked")
	public String doSaveAdd() {
    	
    	JSONObject jsonUserGroup = new JSONObject();
    	jsonUserGroup.put("name", inputGroupName.trim());
    	jsonUserGroup.put("active", CommonConstants.YES);
    	
    	JSONArray arrMenuPermission = new JSONArray();
    	for (int i = 0; i < inputMenus.size(); i++) {
    		JSONObject menu = (JSONObject) inputMenus.get(i);
    		JSONObject jsonMenuPermission = new JSONObject();
    		jsonMenuPermission.put("menuCode", menu.get("code"));
    		jsonMenuPermission.put("view", (boolean) menu.get("viewBoolean") ? CommonConstants.YES : CommonConstants.NO);
    		jsonMenuPermission.put("modify", (boolean) menu.get("modifyBoolean") ? CommonConstants.YES : CommonConstants.NO);
    		arrMenuPermission.add(jsonMenuPermission);
    	}
    	
    	JSONObject parameters = new JSONObject();
    	parameters.put("userGroup", jsonUserGroup);
    	parameters.put("menuPermissions", arrMenuPermission);
    	
    	RESTResponse response = restClient.fetchPost(hostUrl, "/settings/user-group/add", parameters);
    	if (response != null) {    		
    		if (CommonConstants.SUCCESS.equals(response.getStatus())) {
    			Messages.addFlashGlobalInfo(response.getMessage());
    		} else if (CommonConstants.FAIL.equals(response.getStatus())) {
    			Messages.addGlobalError(response.getMessage());
    			return "";
    		}
    	}
    	return gotoIndex();
    }
    
    public String gotoEdit(Long editId) {
    	Faces.getFlash().put("editId", editId);
    	return gotoEdit();
    }
    
    private JSONObject editUserGroup;
    private JSONArray editMenuPermissions;
    
    @SuppressWarnings("unchecked")
    private void prepareEdit() {
    	Long editId = (Long) Faces.getFlash().get("editId");
    	JSONObject parameters = new JSONObject();
    	parameters.put("userGroupId", editId);
    	RESTResponse response = restClient.fetchGet(hostUrl, "/settings/user-group/find", parameters);
    	if (response != null) {
    		if (CommonConstants.SUCCESS.equals(response.getStatus())) {
    		    editUserGroup = (JSONObject) response.getData();
    		    editMenuPermissions = (JSONArray) editUserGroup.get("userGroupMenuPermissions");
    		} else if (CommonConstants.FAIL.equals(response.getStatus())) {
    		    Messages.addGlobalError(response.getMessage());
    		    return;
    		}
    	}
    	inputGroupName = (String) editUserGroup.get("name");
    	inputMenus = new JSONArray();
    	JSONArray menus = menuBean.getFlatMenus();
	    for (Object o : menus) {
	        JSONObject menu = (JSONObject) o;
	        String code = (String) menu.get("code");
	        menu.put("viewBoolean", getPermissionBoolean("view", code, editMenuPermissions));
            menu.put("modifyBoolean", getPermissionBoolean("modify", code, editMenuPermissions));
            inputMenus.add(menu);
	    }
    }
    
    private boolean getPermissionBoolean(String key, String code, JSONArray menuPermissions) {
        for (Object o : menuPermissions) {
            JSONObject permission = (JSONObject) o;
            if (code.equals(permission.get("menuCode"))) {
                return CommonConstants.YES.equals(permission.get(key));
            }
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
    public String doSaveEdit() {

        editUserGroup.put("name", inputGroupName.trim());
        
        JSONArray arrMenuPermission = new JSONArray();
        for (int i = 0; i < inputMenus.size(); i++) {
            JSONObject menu = (JSONObject) inputMenus.get(i);
            JSONObject jsonMenuPermission = new JSONObject();
            jsonMenuPermission.put("menuCode", menu.get("code"));
            jsonMenuPermission.put("view", (boolean) menu.get("viewBoolean") ? CommonConstants.YES : CommonConstants.NO);
            jsonMenuPermission.put("modify", (boolean) menu.get("modifyBoolean") ? CommonConstants.YES : CommonConstants.NO);
            arrMenuPermission.add(jsonMenuPermission);
        }
        
        JSONObject parameters = new JSONObject();
        parameters.put("userGroup", editUserGroup);
        parameters.put("menuPermissions", arrMenuPermission);
        
        RESTResponse response = restClient.fetchPost(hostUrl, "/settings/user-group/edit", parameters);
        if (response != null) {         
            if (CommonConstants.SUCCESS.equals(response.getStatus())) {
                Messages.addFlashGlobalInfo(response.getMessage());
                JSONObject updated = (JSONObject) response.getData();
                if (loginBean.getUserGroup().get("id").equals(updated.get("id"))) {
                    return loginBean.doLogout();
                }
            } else if (CommonConstants.FAIL.equals(response.getStatus())) {
                Messages.addGlobalError(response.getMessage());
                return "";
            }
        }
        return gotoIndex();
    }
    
    private Long removeId;
    
    public void prepareRemoveUserGroup(Long removeId) {
    	this.removeId = removeId;
    }
    
    @SuppressWarnings("unchecked")
	public String doRemoveUserGroup() {
    	JSONObject parameters = new JSONObject();
    	parameters.put("userGroupId", removeId);
    	RESTResponse response = restClient.fetchPost(hostUrl, "/settings/user-group/remove", parameters);
    	if (response != null) {
    		if (CommonConstants.SUCCESS.equals(response.getStatus())) {
        		Messages.addFlashGlobalInfo(response.getMessage());	
    		} else if (CommonConstants.FAIL.equals(response.getStatus())) {
        		Messages.addGlobalError(response.getMessage());
        		return "";
    		}
    	}
    	return gotoIndex();
    }
    
	@Override
	protected String getMenuCode() {
		return "menu.settings.usergroup";
	}

	public JSONArray getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(JSONArray userGroups) {
		this.userGroups = userGroups;
	}

	public String getInputGroupName() {
		return inputGroupName;
	}

	public void setInputGroupName(String inputGroupName) {
		this.inputGroupName = inputGroupName;
	}

	public JSONArray getInputMenus() {
		return inputMenus;
	}

	public void setInputMenus(JSONArray inputMenus) {
		this.inputMenus = inputMenus;
	}
    
}
