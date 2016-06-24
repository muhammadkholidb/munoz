package hu.pe.munoz.commonwebfaces.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.common.helper.CommonUtils;
import hu.pe.munoz.commonwebfaces.helper.PageMode;

@ManagedBean
@ViewScoped
public class UserBean extends AbstractBreadCrumbSupport implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserBean.class);
    
    private List<HashMap> userList;
    
    private String inputFirstName;
    private String inputLastName;
    private String inputUsername;
    private String inputEmail;
    private String inputActive;
    private String inputPassword;
    
    private int mode = PageMode.LIST;
    
    @PostConstruct
    public void init() {
        userList = loadUsers();
        inputPassword = CommonUtils.getDefaultRandomPassword();
    }

    private List<HashMap> loadUsers() {
        List<HashMap> list = new ArrayList<HashMap>();
        for (int i = 0; i < 5; i++) {
            HashMap map = new HashMap();
            map.put("firstName", "First" + i);
            map.put("lastName", "Last" + i);
            map.put("username", "Username" + i);
            map.put("email", "Email" + i);
            map.put("status", "Status" + i);
            list.add(map);
        }
        return  list;
    }
    
    private List<HashMap> loadUserGroups() {
        return null;
    }
    
    public void generateRandom() {
        inputPassword = CommonUtils.getDefaultRandomPassword();
    }
    
    public String addUser() {
        return "add?faces-redirect=true";
    }

    public String cancelForm() {
        return "list?faces-redirect=true";
    }
    
    public String saveForm() {
        // Call rest to save data
        return "list?faces-redirect=true";
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected void addCrumbs(List<JSONObject> crumbs, String languageCode) {
        if (CommonConstants.LANGUAGE_CODE_ENGLISH.equals(languageCode)) {
            JSONObject crumb = new JSONObject();
            crumb.put("label", "User");
            crumb.put("page", "/settings/user/list.xhtml");
            crumbs.add(crumb);
        } else if (CommonConstants.LANGUAGE_CODE_INDONESIA.equals(languageCode)) {
            JSONObject crumb = new JSONObject();
            crumb.put("label", "Pengguna");
            crumb.put("page", "/settings/user/list.xhtml");
            crumbs.add(crumb);
        }
    }

    public List<HashMap> getUserList() {
        return userList;
    }

    public void setUserList(List<HashMap> userList) {
        this.userList = userList;
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
    
}
