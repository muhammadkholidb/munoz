package hu.pe.munoz.commonwebfaces.bean;

import static hu.pe.munoz.common.helper.CommonConstants.SYSTEM_KEY_IMAGE;
import static hu.pe.munoz.common.helper.CommonConstants.SYSTEM_KEY_LANGUAGE_CODE;
import static hu.pe.munoz.common.helper.CommonConstants.SYSTEM_KEY_ONLINE;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
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

    private static final Logger LOG = LoggerFactory.getLogger(SystemBean.class);

    private String languageCode;
    private String image;
    private String online;

    private UploadedFile imageUpload;

    private List<JSONObject> supportedLanguages;

    @Override
    protected void postConstruct() {
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
        String resizedFileName = null;
        String originalFileName = null;

        if (imageUpload != null && imageUpload.getSize() > 0) {

            if (!imageUpload.getContentType().startsWith("image/")) {
                Messages.addGlobalError(MessageHelper.getStringByEL("lang", "error.FileTypeNotSupported"));
                return "";
            }

            String imagesDir = applicationBundle.getString("directory.path.Images");
            String uploadedFileName = imageUpload.getFileName();
            String uploadedFileExtension = uploadedFileName.substring(uploadedFileName.lastIndexOf(".") + 1);
            String baseName = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "-" + RandomStringUtils.randomAlphanumeric(8).toUpperCase();

            resizedFileName = baseName + "." + uploadedFileExtension;
            originalFileName = baseName + "-ORI." + uploadedFileExtension;

            // http://stackoverflow.com/questions/11829958/where-is-the-pfileupload-uploaded-file-saved-and-how-do-i-change-it#answer-11830143
            // InputStream input = null;
            OutputStream output = null;

            try {

                // Can't use InputStream twice, always read from source
                // input = imageUpload.getInputstream();

                // Save original image
                LOG.debug("Copy image ({}) to {}", originalFileName, imagesDir);
                output = new FileOutputStream(new File(imagesDir, originalFileName));
                IOUtils.copy(imageUpload.getInputstream(), output);

                // Save resized image
                LOG.debug("Copy image ({}) to {}", resizedFileName, imagesDir);
                BufferedImage bufferedImage = resizeImage(imageUpload.getInputstream());
                ImageIO.write(bufferedImage, uploadedFileExtension, new File(imagesDir, resizedFileName));

                uploaded = true;

            } catch (Exception e) {

                LOG.error(e.getMessage(), e);
                Messages.addFlashGlobalError(CommonUtils.getExceptionMessage(e));

            } finally {
                // IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(output);
            }
        }

        JSONArray systems = applicationBean.getSystems();

        for (Object object : systems) {
            JSONObject system = (JSONObject) object;
            String key = (String) system.get("dataKey");
            if (SYSTEM_KEY_LANGUAGE_CODE.equals(key)) {
                system.put("dataValue", languageCode);
            } else if (SYSTEM_KEY_ONLINE.equals(key)) {
                system.put("dataValue", online);
            } else if (SYSTEM_KEY_IMAGE.equals(key)) {
                system.put("dataValue", uploaded ? resizedFileName : image);
            }
        }

        JSONObject params = new JSONObject();
        params.put("systems", systems);

        try {
            HttpClient httpClient = getHttpClient().setHost(hostUrl).setPath("/settings/system/edit")
                    .setParameters(params);

            // Update header Accept-Language in restClient if language changed
            if (!languageCode.equals(applicationBean.getLanguageCode())) {
                LOG.debug("Language changed!");
                httpClient.setHeader("Accept-Language", languageCode);
            }

            HttpClientResponse response = httpClient.post();

            if (response != null) {
                String responseStatus = response.getStatus();
                if (null != responseStatus) {
                    if (CommonConstants.SUCCESS.equals(responseStatus)) {
                        applicationBean.setSystems((JSONArray) response.getData());
                        Messages.addFlashGlobalInfo(response.getMessage());
                    } else if (CommonConstants.FAIL.equals(responseStatus)) {
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

    private BufferedImage resizeImage(InputStream is) throws Exception {
        BufferedImage originalImage = ImageIO.read(is);
        int type = (originalImage.getType() == 0) ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

        // http://stackoverflow.com/questions/11959758/java-maintaining-aspect-ratio-of-jpanel-background-image/11959928#11959928
        Image scaledImage = originalImage.getScaledInstance(-1, 30, BufferedImage.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), type);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        return resizedImage;
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
