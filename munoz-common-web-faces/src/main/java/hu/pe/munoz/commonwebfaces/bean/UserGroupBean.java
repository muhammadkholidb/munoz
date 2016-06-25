package hu.pe.munoz.commonwebfaces.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.json.simple.JSONObject;

import hu.pe.munoz.common.helper.CommonConstants;

@ManagedBean
@ViewScoped
public class UserGroupBean extends AbstractBreadCrumbSupport implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @PostConstruct
    public void init() {
        
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addCrumbs(List<JSONObject> crumbs, String languageCode) {
        if (CommonConstants.LANGUAGE_CODE_ENGLISH.equals(languageCode)) {
            JSONObject crumb = new JSONObject();
            crumb.put("label", "User Group");
            crumb.put("page", "/settings/user-group/list.xhtml");
            crumbs.add(crumb);
        } else if (CommonConstants.LANGUAGE_CODE_INDONESIA.equals(languageCode)) {
            JSONObject crumb = new JSONObject();
            crumb.put("label", "Kelompok Pengguna");
            crumb.put("page", "/settings/user-group/list.xhtml");
            crumbs.add(crumb);
        }
    }
    
}
