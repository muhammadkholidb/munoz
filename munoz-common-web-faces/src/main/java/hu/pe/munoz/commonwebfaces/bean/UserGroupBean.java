package hu.pe.munoz.commonwebfaces.bean;

import static hu.pe.munoz.commonwebfaces.helper.PageMode.*;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.omnifaces.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.common.rest.RESTResponse;
import hu.pe.munoz.commonwebfaces.helper.MessageHelper;

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

    @PostConstruct
    public void init() {
        log.debug("@PostConstruct UserGroupBean ...");
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
    	RESTResponse response = restClient.doGet(hostUrl, "/settings/user-group/list", null);
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
	public String saveForm() {
    	
    	JSONObject jsonUserGroup = new JSONObject();
    	jsonUserGroup.put("name", inputGroupName);
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
    	
    	RESTResponse response = restClient.doPost(hostUrl, "/settings/user-group/add", parameters);
    	if (CommonConstants.SUCCESS.equals(response.getStatus())) {
    		Messages.addFlashGlobalInfo(MessageHelper.getStringByEL("lang", "success.SuccessfullyAddUserGroup"));
    		log.debug("Data: " + response.getData());
    	} else if (CommonConstants.FAIL.equals(response.getStatus())) {
    		log.debug("Message: " + response.getMessage());
    		Messages.addGlobalError(response.getMessage());
    		return "";
    	}
    	return gotoIndex();
    }
    
    private void prepareEdit() {
    	
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
