package hu.pe.munoz.commonwebfaces.bean;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;

import hu.pe.munoz.common.rest.RESTClient;
import hu.pe.munoz.commonwebfaces.helper.WebAppHelper;

public abstract class RESTBean {

    protected RESTClient restClient;
    protected Properties applicationProperties;
    protected String hostUrl;

    @ManagedProperty(value = "#{applicationBean}")
    protected ApplicationBean applicationBean;

    public void setApplicationBean(ApplicationBean applicationBean) {
        this.applicationBean = applicationBean;
    }

    @PostConstruct
    protected void postConstruct() {
    	restClient = new RESTClient();
    	restClient.setHeader("Accept-Language", applicationBean.getLanguageCode());
    	applicationProperties = WebAppHelper.getApplicationProperties(Thread.currentThread().getContextClassLoader());
    	hostUrl = applicationProperties.getProperty("rest.HostUrl");
    }
    
}
