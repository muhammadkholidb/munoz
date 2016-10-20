package hu.pe.munoz.commonrest.controller.settings;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
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

import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.commonrest.test.AbstractSpringIntegrationTest;

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
            mockMvc.perform(get("/settings/system/list"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(jsonPath("status").value(CommonConstants.SUCCESS))
                    .andExpect(jsonPath("message").value(CommonConstants.EMPTY_STRING))
                    .andDo(print());
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
