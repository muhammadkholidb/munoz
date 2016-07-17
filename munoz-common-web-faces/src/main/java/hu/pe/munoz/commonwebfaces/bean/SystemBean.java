package hu.pe.munoz.commonwebfaces.bean;

import static hu.pe.munoz.common.helper.CommonConstants.SYSTEM_KEY_IMAGE;
import static hu.pe.munoz.common.helper.CommonConstants.SYSTEM_KEY_LANGUAGE_CODE;
import static hu.pe.munoz.common.helper.CommonConstants.SYSTEM_KEY_ONLINE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.common.helper.CommonUtils;
import hu.pe.munoz.common.helper.HttpClient;
import hu.pe.munoz.common.helper.HttpClientResponse;
import hu.pe.munoz.commonwebfaces.helper.MessageHelper;

@ManagedBean
@ViewScoped
public class SystemBean extends DefaultBehaviorBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private String languageCode;
    private String image;
    private String online;

    private UploadedFile imageUpload;

    private List<JSONObject> supportedLanguages;

    @Override
    protected void postConstruct() {
        super.postConstruct();
        log.info("Post construct SystemBean ...");
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

    @SuppressWarnings("unchecked")
    public String saveForm() {

        boolean uploaded = false;
        String newFileName = null;

        if (imageUpload != null && imageUpload.getSize() > 0) {

            if (!imageUpload.getContentType().startsWith("image/")) {
                Messages.addGlobalError(MessageHelper.getStringByEL("lang", "error.FileTypeNotSupported"));
                return "";
            }

            String uploadedFileName = imageUpload.getFileName();
            String imageDir = System.getProperty("user.home") + applicationProperties.getProperty("directory.Images");

            newFileName = UUID.randomUUID().toString() + uploadedFileName.substring(uploadedFileName.lastIndexOf("."));

            log.debug("Upload image ({}) to {}", newFileName, imageDir);
            
            // http://stackoverflow.com/questions/11829958/where-is-the-pfileupload-uploaded-file-saved-and-how-do-i-change-it#answer-11830143
            InputStream input = null;
            OutputStream output = null;
            try {
                input = imageUpload.getInputstream();
                output = new FileOutputStream(new File(imageDir, newFileName));
                IOUtils.copy(input, output);
                uploaded = true;
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                Messages.addFlashGlobalError(CommonUtils.getExceptionMessage(e));
            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(output);
            }
        }

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
                    system.put("value", uploaded ? newFileName : image);
                    break;
                default:
                    break;
            }
        }

        JSONObject params = new JSONObject();
        params.put("systems", systems);

        try {
            HttpClient httpClient = getHttpClient()
                    .setHost(hostUrl)
                    .setPath("/settings/system/edit")
                    .setParameters(params);

            // Update header Accept-Language in restClient if language changed
            if (!languageCode.equals(applicationBean.getLanguageCode())) {
                log.debug("Language changed!");
                httpClient.setHeader("Accept-Language", languageCode);
            }

            HttpClientResponse response = httpClient.post();

            if (response != null) {
                if (null != response.getStatus()) switch (response.getStatus()) {
                    case CommonConstants.SUCCESS:
                        applicationBean.setSystems((JSONArray) response.getData());
                        Messages.addFlashGlobalInfo(response.getMessage());
                        break;
                    case CommonConstants.FAIL:
                        Messages.addGlobalError(response.getMessage());
                        return "";
                }
            }
        } catch (Exception e) { 
            log.error(e.getMessage(), e);
            Messages.addGlobalError(CommonUtils.getExceptionMessage(e));
            return "";
        }
        return gotoIndex();
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

    public UploadedFile getImageUpload() {
        return imageUpload;
    }

    public void setImageUpload(UploadedFile imageUpload) {
        this.imageUpload = imageUpload;
    }

    @Override
    protected String getMenuCode() {
        return "menu.settings.system";
    }

}
