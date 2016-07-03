package hu.pe.munoz.commonwebfaces.bean;

import java.util.Properties;

import hu.pe.munoz.common.rest.RESTClient;
import hu.pe.munoz.commonwebfaces.helper.WebAppHelper;

public abstract class RESTBean {

    protected RESTClient restClient;
    protected Properties applicationProperties;
    protected String hostUrl;
    
    public RESTBean() {
    	restClient = new RESTClient();
    	applicationProperties = WebAppHelper.getApplicationProperties(Thread.currentThread().getContextClassLoader());
    	hostUrl = applicationProperties.getProperty("rest.HostUrl");
    }
    
}
