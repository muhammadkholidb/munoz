package hu.pe.munoz.commonrest.controller.test.settings;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.commonrest.controller.settings.SystemController;
import hu.pe.munoz.commonrest.controller.test.AbstractRestClientServerTest;
import hu.pe.munoz.commonrest.helper.ResponseWrapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
        "classpath:munoz-common-rest-context-test.xml",
        "classpath:munoz-common-data-context-test.xml" })
@WebAppConfiguration
public class TestSystemController extends AbstractRestClientServerTest {

    private static final Logger LOG = LoggerFactory.getLogger(TestSystemController.class);
    
    @Autowired
    private SystemController systemController;

    @Test
    public void testGetSystemList() {
        LOG.debug("Test get system list ...");

        try {
            
            ResponseWrapper response = systemController.getAllSystem();
            String responseJson = mapper.writeValueAsString(response);
            
            LOG.debug("Response JSON: " + responseJson);
            
            mockServer.expect(requestTo("/settings/system/list"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

            ResponseWrapper restResponse = restTemplate.getForObject("/settings/system/list", ResponseWrapper.class);
            
            assertEquals(CommonConstants.SUCCESS, restResponse.getStatus());

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

}
