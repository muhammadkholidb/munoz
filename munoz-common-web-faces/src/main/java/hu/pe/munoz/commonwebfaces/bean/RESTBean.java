package hu.pe.munoz.commonwebfaces.bean;

import hu.pe.munoz.common.helper.HttpClient;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;

import hu.pe.munoz.commonwebfaces.helper.WebAppHelper;
import org.json.simple.JSONObject;

public abstract class RESTBean {

    protected Properties applicationProperties;
    protected String hostUrl;

    @ManagedProperty(value = "#{applicationBean}")
    protected ApplicationBean applicationBean;

    public void setApplicationBean(ApplicationBean applicationBean) {
        this.applicationBean = applicationBean;
    }

    @PostConstruct
    protected void postConstruct() {
        applicationProperties = WebAppHelper.getApplicationProperties(Thread.currentThread().getContextClassLoader());
        hostUrl = applicationProperties.getProperty("rest.HostUrl");
    }

    protected HttpClient getHttpClient() {
        return new HttpClient().setHeader("Accept-Language", applicationBean.getLanguageCode());
    }

    protected HttpClient getHttpClient(String host, String path) {
        return new HttpClient()
                .setHost(host)
                .setPath(path)
                .setHeader("Accept-Language", applicationBean.getLanguageCode());
    }

    protected HttpClient getHttpClient(String host, String path, JSONObject parameters) {
        return new HttpClient()
                .setHost(host)
                .setPath(path)
                .setParameters(parameters)
                .setHeader("Accept-Language", applicationBean.getLanguageCode());
    }

}
