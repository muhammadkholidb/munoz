#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.webfaces.bean;

import hu.pe.munoz.common.helper.CommonConstants;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.omnifaces.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.pe.munoz.common.helper.CommonUtils;
import hu.pe.munoz.common.helper.HttpClientResponse;
import hu.pe.munoz.commonwebfaces.bean.DefaultBehaviorBean;
import org.json.simple.JSONArray;

@ManagedBean
@ViewScoped
public class ExampleBean extends DefaultBehaviorBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(ExampleBean.class);

    private JSONArray examples;
    
    @Override
    protected void postConstruct() {
        super.postConstruct();
        LOG.debug("Post construct ExampleBean ...");
        load();
    }

    private void load() {
        try {
            HttpClientResponse response = getHttpClient(hostUrl, "/example/example/list").get();
            if (CommonConstants.SUCCESS.equals(response.getStatus())) {
                examples = (JSONArray) response.getData();
            } else {
                LOG.debug(response.getMessage());
                Messages.addGlobalError(response.getMessage());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            Messages.addGlobalError(CommonUtils.getExceptionMessage(e));
        }
    }

    public JSONArray getExamples() {
        return examples;
    }

    public void setExamples(JSONArray examples) {
        this.examples = examples;
    }

    @Override
    protected String getMenuCode() {
        return "menu.example.example";
    }

}
