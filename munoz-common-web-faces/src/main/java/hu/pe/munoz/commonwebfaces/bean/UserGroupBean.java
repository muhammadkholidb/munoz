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
import hu.pe.munoz.common.helper.CommonUtils;
import hu.pe.munoz.common.helper.HttpClient;
import hu.pe.munoz.common.helper.HttpClientResponse;

@ManagedBean
@ViewScoped
public class UserGroupBean extends DefaultBehaviorBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(UserGroupBean.class);

    private JSONArray userGroups;
    private JSONArray inputMenus;
    private String inputGroupName;
    private String inputGroupActive;

    @ManagedProperty(value = "#{menuBean}")
    protected MenuBean menuBean;

    public void setMenuBean(MenuBean menuBean) {
        this.menuBean = menuBean;
    }

    @Override
    protected void postConstruct() {
        super.postConstruct();
        LOG.debug("Post construct UserGroupBean ...");
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
        try {
            HttpClientResponse response = getHttpClient(hostUrl, "/settings/user-group/list").get();
            if (CommonConstants.SUCCESS.equals(response.getStatus())) {
                userGroups = (JSONArray) response.getData();
            } else {
                LOG.debug(response.getMessage());
                Messages.addGlobalError(response.getMessage());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            Messages.addGlobalError(CommonUtils.getExceptionMessage(e)); 
        }
    }

    @SuppressWarnings("unchecked")
    public void onCheckboxViewPermissionChanged(String menuCode) {
        String selectedParentCode = null;
        boolean viewBoolean = false;
        for(Object object : inputMenus) {
            JSONObject menu = (JSONObject) object;
            if (menuCode.equals(menu.get("code"))) {
                selectedParentCode = (String) menu.get("parentCode");
                viewBoolean = (boolean) menu.get("viewBoolean");
                menu.put("modifyBoolean", false);
                break;
            }
        }
        boolean checkParentView = true;
        boolean checkParentModify = true;
        for (Object object : inputMenus) {
            JSONObject menu = (JSONObject) object;
            String parentCode = (String) menu.get("parentCode");
            if ((parentCode != null) && menuCode.equals(parentCode)) {
                menu.put("viewBoolean", viewBoolean);
                menu.put("modifyBoolean", false);
            }
            if (selectedParentCode != null && selectedParentCode.equals(parentCode)) {
                boolean currentViewBoolean = (boolean) menu.get("viewBoolean");
                boolean currentModifyBoolean = (boolean) menu.get("modifyBoolean");
                checkParentView = checkParentView && currentViewBoolean;
                checkParentModify = checkParentModify && currentModifyBoolean;
            }
        }
        if (selectedParentCode != null) {
            for(Object object : inputMenus) {
                JSONObject menu = (JSONObject) object;
                if (selectedParentCode.equals(menu.get("code"))) {
                    menu.put("viewBoolean", checkParentView);
                    menu.put("modifyBoolean", checkParentModify);
                    break;
                }
            }    
        }
    }
    
    @SuppressWarnings("unchecked")
    public void onCheckboxModifyPermissionChanged(String menuCode) {
        String selectedParentCode = null;
        boolean modifyBoolean = false;
        for(Object object : inputMenus) {
            JSONObject menu = (JSONObject) object;
            if (menuCode.equals(menu.get("code"))) {
                selectedParentCode = (String) menu.get("parentCode");
                modifyBoolean = (boolean) menu.get("modifyBoolean");
                menu.put("viewBoolean", true);
                break;
            }
        }
        boolean checkParentModify = true;
        boolean checkParentView = true;
        for (Object object : inputMenus) {
            JSONObject menu = (JSONObject) object;
            String parentCode = (String) menu.get("parentCode");
            if ((parentCode != null) && menuCode.equals(parentCode)) {
                menu.put("viewBoolean", true);
                menu.put("modifyBoolean", modifyBoolean);
            }
            if (selectedParentCode != null && selectedParentCode.equals(parentCode)) {
                boolean currentModifyBoolean = (boolean) menu.get("modifyBoolean");
                boolean currentViewBoolean = (boolean) menu.get("viewBoolean");
                checkParentModify = checkParentModify && currentModifyBoolean;
                checkParentView = checkParentView && currentViewBoolean;
            }
        }
        if (selectedParentCode != null) {
            for(Object object : inputMenus) {
                JSONObject menu = (JSONObject) object;
                if (selectedParentCode.equals(menu.get("code"))) {
                    menu.put("modifyBoolean", checkParentModify);
                    menu.put("viewBoolean", checkParentView);
                    break;
                }
            }    
        }
    }
    
    @SuppressWarnings("unchecked")
    private void prepareAdd() {
        // Load menus
        JSONArray menus = menuBean.getFlatMenus();
        inputMenus = new JSONArray();
        for (int i = 0; i < menus.size(); i++) {
            JSONObject menu = (JSONObject) menus.get(i);
            menu.put("viewBoolean", false);
            menu.put("modifyBoolean", false);
            inputMenus.add(menu);
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

        try {
            HttpClient httpClient = getHttpClient(hostUrl, "/settings/user-group/add");
            httpClient.setParameters(parameters);
            HttpClientResponse response = httpClient.post();
            if (response != null) {
                if (null != response.getStatus()) {
                    switch (response.getStatus()) {
                        case CommonConstants.SUCCESS:
                            Messages.addFlashGlobalInfo(response.getMessage());
                            break;
                        case CommonConstants.FAIL:
                            Messages.addGlobalError(response.getMessage());
                            return "";
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            Messages.addGlobalError(CommonUtils.getExceptionMessage(e));
            return "";
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
        parameters.put("id", editId);

        try {
            HttpClientResponse response = getHttpClient(hostUrl, "/settings/user-group/find", parameters).get();
            if (response != null) {
                if (CommonConstants.SUCCESS.equals(response.getStatus())) {
                    editUserGroup = (JSONObject) response.getData();
                    editMenuPermissions = (JSONArray) editUserGroup.get("menuPermissions");
                } else if (CommonConstants.FAIL.equals(response.getStatus())) {
                    Messages.addGlobalError(response.getMessage());
                    return;
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            Messages.addGlobalError(CommonUtils.getExceptionMessage(e));
            return;
        }

        inputGroupName = (String) editUserGroup.get("name");
        inputGroupActive = (String) editUserGroup.get("active");
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
        if ((menuPermissions == null) || menuPermissions.isEmpty()) return false;
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
        editUserGroup.put("active", inputGroupActive);

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

        try {
            HttpClientResponse response = getHttpClient(hostUrl, "/settings/user-group/edit", parameters).post();
            if (response != null) {
                if (null != response.getStatus()) {
                    switch (response.getStatus()) {
                        case CommonConstants.SUCCESS:
                            Messages.addFlashGlobalInfo(response.getMessage());
                            JSONObject updated = (JSONObject) response.getData();
                            if (loginBean.getUserGroup().get("id").equals(updated.get("id")) && CommonConstants.NO.equals(updated.get("active"))) {
                                return loginBean.doLogout();
                            }
                            break;
                        case CommonConstants.FAIL:
                            Messages.addGlobalError(response.getMessage());
                            return "";
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            Messages.addGlobalError(CommonUtils.getExceptionMessage(e));
            return "";
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
        parameters.put("id", removeId);
        try {
            HttpClientResponse response = getHttpClient(hostUrl, "/settings/user-group/remove", parameters).post();
            if (response != null) {
                if (null != response.getStatus()) {
                    switch (response.getStatus()) {
                        case CommonConstants.SUCCESS:
                            Messages.addFlashGlobalInfo(response.getMessage());
                            break;
                        case CommonConstants.FAIL:
                            Messages.addFlashGlobalError(response.getMessage());
                            break;
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            Messages.addFlashGlobalError(CommonUtils.getExceptionMessage(e));
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

    public String getInputGroupActive() {
        return inputGroupActive;
    }

    public void setInputGroupActive(String inputGroupActive) {
        this.inputGroupActive = inputGroupActive;
    }

}
