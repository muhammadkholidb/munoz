package hu.pe.munoz.commonwebfaces.bean;

import static hu.pe.munoz.commonwebfaces.helper.PageMode.ADD;
import static hu.pe.munoz.commonwebfaces.helper.PageMode.EDIT;
import static hu.pe.munoz.commonwebfaces.helper.PageMode.INDEX;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.codec.digest.DigestUtils;
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
import hu.pe.munoz.commonwebfaces.helper.MessageHelper;

@ManagedBean
@ViewScoped
public class UserBean extends DefaultBehaviorBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(UserBean.class);

    private JSONArray users;
    private JSONArray userGroups;

    private String inputFirstName;
    private String inputLastName;
    private String inputUsername;
    private String inputEmail;
    private String inputActive;
    private String inputPassword;
    private Long inputUserGroupId;

    @Override
    protected void postConstruct() {
        super.postConstruct();
        LOG.debug("Post construct UserBean ...");
        switch (mode) {
            case INDEX:
                loadUsers();
                break;
            case ADD:
                prepareAdd();
                break;
            case EDIT:
                prepareEdit();
                break;
        }
    }

    private void loadUsers() {
        try {
            HttpClientResponse response = getHttpClient(hostUrl, "/settings/user/list").get();
            if (CommonConstants.SUCCESS.equals(response.getStatus())) {
                users = (JSONArray) response.getData();
            } else {
                LOG.debug(response.getMessage());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void loadUserGroups() {
        try {
            HttpClientResponse response = getHttpClient(hostUrl, "/settings/user-group/list").get();
            if (CommonConstants.SUCCESS.equals(response.getStatus())) {
                userGroups = (JSONArray) response.getData();
            } else {
                LOG.debug(response.getMessage());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public void generateRandom() {
        inputPassword = CommonUtils.getDefaultRandomPassword();
    }

    private void prepareAdd() {
        loadUserGroups();
        generateRandom();
    }

    @SuppressWarnings("unchecked")
    public String doSaveAdd() {

        if (userGroups == null || userGroups.size() == 0) {
            Messages.addGlobalError(MessageHelper.getStringByEL("lang", "error.UserGroupCannotBeEmpty"));
            return "";
        }
        
        JSONObject jsonUserGroup = new JSONObject();
        jsonUserGroup.put("id", inputUserGroupId);

        JSONObject jsonUser = new JSONObject();
        jsonUser.put("firstName", inputFirstName.trim());
        jsonUser.put("lastName", inputLastName.trim());
        jsonUser.put("username", inputUsername.trim());
        jsonUser.put("email", inputEmail.trim());
        jsonUser.put("password", DigestUtils.sha1Hex(inputPassword));
        jsonUser.put("active", CommonConstants.YES);
        jsonUser.put("userGroup", jsonUserGroup);

        JSONObject parameters = new JSONObject();
        parameters.put("user", jsonUser);

        try {
            HttpClientResponse response = getHttpClient()
                    .setHost(hostUrl)
                    .setPath("/settings/user/add")
                    .setParameters(parameters)
                    .post();
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

    private Long removeId;

    public void prepareRemoveUser(Long removeId) {
        this.removeId = removeId;
    }

    @SuppressWarnings("unchecked")
    public String doRemoveUser() {
        JSONObject parameters = new JSONObject();
        parameters.put("userId", removeId);

        try {
            HttpClient httpClient = getHttpClient(hostUrl, "/settings/user/remove", parameters);
            HttpClientResponse response = httpClient.post();
            if (response != null) {
                if (CommonConstants.SUCCESS.equals(response.getStatus())) {
                    Messages.addFlashGlobalInfo(response.getMessage());
                } else if (CommonConstants.FAIL.equals(response.getStatus())) {
                    Messages.addFlashGlobalInfo(response.getMessage());
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            Messages.addFlashGlobalInfo(CommonUtils.getExceptionMessage(e));
        }
        return gotoIndex();
    }

    public String gotoEdit(Long editId) {
        Faces.getFlash().put("editId", editId);
        return gotoEdit();
    }

    private JSONObject editUser;

    @SuppressWarnings("unchecked")
    public void prepareEdit() {
        Long editId = (Long) Faces.getFlash().get("editId");
        JSONObject parameters = new JSONObject();
        parameters.put("userId", editId);

        try {
            HttpClient httpClient = getHttpClient(hostUrl, "/settings/user/find");
            httpClient.setParameters(parameters);
            HttpClientResponse response = httpClient.get();
            if (response != null) {
                if (CommonConstants.SUCCESS.equals(response.getStatus())) {
                    editUser = (JSONObject) response.getData();
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
        inputFirstName = (String) editUser.get("firstName");
        inputLastName = (String) editUser.get("lastName");
        inputUsername = (String) editUser.get("username");
        inputEmail = (String) editUser.get("email");
        inputActive = (String) editUser.get("active");
        inputUserGroupId = (Long) ((JSONObject) editUser.get("userGroup")).get("id");
        loadUserGroups();
    }

    @SuppressWarnings("unchecked")
    public String doSaveEdit() {
        editUser.put("firstName", inputFirstName.trim());
        editUser.put("lastName", inputLastName.trim());
        editUser.put("username", inputUsername.trim());
        editUser.put("email", inputEmail.trim());
        editUser.put("active", inputActive);
        if ((inputPassword != null) && !CommonConstants.EMPTY_STRING.equals(inputPassword)) {
            editUser.put("password", DigestUtils.sha1Hex(inputPassword));
        }
        JSONObject userGroup = new JSONObject();
        userGroup.put("id", inputUserGroupId);
        editUser.put("userGroup", userGroup);

        JSONObject parameters = new JSONObject();
        parameters.put("user", editUser);

        try {
            HttpClient httpClient = getHttpClient(hostUrl, "/settings/user/edit");
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

    @Override
    protected String getMenuCode() {
        return "menu.settings.user";
    }

    public JSONArray getUsers() {
        return users;
    }

    public void setUsers(JSONArray users) {
        this.users = users;
    }

    public String getInputFirstName() {
        return inputFirstName;
    }

    public void setInputFirstName(String inputFirstName) {
        this.inputFirstName = inputFirstName;
    }

    public String getInputLastName() {
        return inputLastName;
    }

    public void setInputLastName(String inputLastName) {
        this.inputLastName = inputLastName;
    }

    public String getInputUsername() {
        return inputUsername;
    }

    public void setInputUsername(String inputUsername) {
        this.inputUsername = inputUsername;
    }

    public String getInputEmail() {
        return inputEmail;
    }

    public void setInputEmail(String inputEmail) {
        this.inputEmail = inputEmail;
    }

    public String getInputActive() {
        return inputActive;
    }

    public void setInputActive(String inputActive) {
        this.inputActive = inputActive;
    }

    public String getInputPassword() {
        return inputPassword;
    }

    public void setInputPassword(String inputPassword) {
        this.inputPassword = inputPassword;
    }

    public JSONArray getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(JSONArray userGroups) {
        this.userGroups = userGroups;
    }

    public Long getInputUserGroupId() {
        return inputUserGroupId;
    }

    public void setInputUserGroupId(Long inputUserGroupId) {
        this.inputUserGroupId = inputUserGroupId;
    }

}
