package hu.pe.munoz.commonrest.controller.settings;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.commonrest.controller.AbstractSpringIntegrationTest;
import hu.pe.munoz.commonrest.helper.ResponseWrapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
        "classpath:munoz-common-rest-context-test.xml",
        "classpath:munoz-common-data-context-test.xml" })
@WebAppConfiguration
public class TestSystemController extends AbstractSpringIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(TestSystemController.class);

    @Test
    public void testGetSystemList() {
        LOG.debug("Test get system list ...");
        try {
            MvcResult result = mockMvc.perform(get("/settings/system/list"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andReturn();
            String response = result.getResponse().getContentAsString();
            LOG.debug("Response: {}", response);
            ResponseWrapper wrapper = convertJson(response, ResponseWrapper.class);
            Assert.assertNotEquals(CommonConstants.EMPTY_STRING, response); 
            Assert.assertNotNull(wrapper);
            Assert.assertEquals(CommonConstants.SUCCESS, wrapper.getStatus());
            Assert.assertEquals(CommonConstants.EMPTY_STRING, wrapper.getMessage());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            Assert.fail();
        }
    }

    @Test
    public void testEditSystemList() {
        LOG.debug("Test edit system list ...");
    }
    
}
