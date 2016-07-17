package hu.pe.munoz.commonwebfaces.bean;

import static hu.pe.munoz.common.helper.CommonConstants.SYSTEM_KEY_IMAGE;
import static hu.pe.munoz.common.helper.CommonConstants.SYSTEM_KEY_LANGUAGE_CODE;
import static hu.pe.munoz.common.helper.CommonConstants.SYSTEM_KEY_ONLINE;
import static hu.pe.munoz.common.helper.CommonConstants.SYSTEM_KEY_TEMPLATE_CODE;

import java.io.Serializable;
import java.util.Locale;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.common.helper.HttpClient;
import hu.pe.munoz.common.helper.HttpClientResponse;
import hu.pe.munoz.commonwebfaces.helper.WebAppHelper;

@ManagedBean
@ApplicationScoped
public class ApplicationBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private JSONArray systems;
    private Locale locale;
    private String languageCode;
    private String templateCode;
    private String image;
    private String online;

    private Properties applicationProperties;
    private String hostUrl;

    @PostConstruct
    public void postConstruct() {
        log.debug("Post construct ApplicationBean ...");
        applicationProperties = WebAppHelper.getApplicationProperties(Thread.currentThread().getContextClassLoader());
        hostUrl = applicationProperties.getProperty("rest.HostUrl");

        try {
            HttpClientResponse response = new HttpClient()
                    .setHost(hostUrl)
                    .setPath("/settings/system/list")
                    .get();
            if (response != null) {

                if (CommonConstants.SUCCESS.equals(response.getStatus())) {
                    setSystems((JSONArray) response.getData());
                } else {
                    log.error(response.getMessage());
                }
            }
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
    }

    public JSONArray getSystems() {
        return this.systems;
    }

    public void setSystems(JSONArray systems) {
        this.systems = systems;
        for (Object o : systems) {

            JSONObject json = (JSONObject) o;
            String key = (String) json.get("key");
            String value = (String) json.get("value");

            switch (key) {
                case SYSTEM_KEY_LANGUAGE_CODE:
                    languageCode = value;
                    locale = new Locale(languageCode);
                    break;

                case SYSTEM_KEY_TEMPLATE_CODE:
                    templateCode = value;
                    break;

                case SYSTEM_KEY_ONLINE:
                    online = value;
                    break;

                case SYSTEM_KEY_IMAGE:
                    image = value;
                    break;

            }
        }
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
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

}
