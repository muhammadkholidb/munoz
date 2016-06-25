package hu.pe.munoz.commonwebfaces.bean;

import static hu.pe.munoz.common.helper.CommonConstants.SYSTEM_KEY_IMAGE;
import static hu.pe.munoz.common.helper.CommonConstants.SYSTEM_KEY_LANGUAGE_CODE;
import static hu.pe.munoz.common.helper.CommonConstants.SYSTEM_KEY_ONLINE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.omnifaces.util.Faces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.common.rest.RESTResponse;

@ManagedBean
@ViewScoped
public class SystemBean extends RESTBean implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private String languageCode;
    private String image;
    private String online;
    
    private List<JSONObject> supportedLanguages;

    @ManagedProperty(value = "#{applicationBean}")
    protected ApplicationBean applicationBean;

    public void setApplicationBean(ApplicationBean applicationBean) {
        this.applicationBean = applicationBean;
    }

    @PostConstruct
    public void init() {
    	log.info("@PostConstruct SystemBean ...");
    	load();
    }

    @SuppressWarnings("unchecked")
    private void load() {
    	
    	languageCode = applicationBean.getLanguageCode();
    	image = applicationBean.getImage();
    	online = applicationBean.getOnline();
        
    	Locale currentLocale = applicationBean.getLocale();

        supportedLanguages = new ArrayList<JSONObject>();
        List<Locale> locales = Faces.getSupportedLocales();
        for (Locale locale : locales) {
            JSONObject language = new JSONObject();
            language.put("code", locale.getLanguage());
            language.put("name", locale.getDisplayLanguage(currentLocale));
            supportedLanguages.add(language);
        }
    }
    
    public String editSystem() {
        return "edit?faces-redirect=true";
    }
    
    public String cancelForm() {
        return "list?faces-redirect=true";
    }
    
    @SuppressWarnings("unchecked")
	public String saveForm() {

    	JSONArray systems = applicationBean.getSystems();

    	for (Object object : systems) {
    		JSONObject system = (JSONObject) object;
    		String key = (String) system.get("key");
    		switch (key) {
			case SYSTEM_KEY_LANGUAGE_CODE:
				system.put("value", languageCode);
				break;
			case SYSTEM_KEY_ONLINE:
				system.put("value", online);
				break;
			case SYSTEM_KEY_IMAGE:
				system.put("value", image);
				break;
			default:
				break;
			}
    	}

    	JSONObject params = new JSONObject();
    	params.put("systems", systems);
    	
        RESTResponse response = restClient.doPost("localhost:8080/munoz-common-rest", "/settings/system/edit", params);
        log.debug("Response: " + response);
    	if (CommonConstants.SUCCESS.equals(response.getStatus())) {
    		applicationBean.setSystems((JSONArray) response.getData());
    	} else {
    		log.debug(response.getMessage());
    		return "";
    	}
        
        return "list?faces-redirect=true";
    }
    
    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

	public List<JSONObject> getSupportedLanguages() {
		return supportedLanguages;
	}

	public void setSupportedLanguages(List<JSONObject> supportedLanguages) {
		this.supportedLanguages = supportedLanguages;
	}
    
}
