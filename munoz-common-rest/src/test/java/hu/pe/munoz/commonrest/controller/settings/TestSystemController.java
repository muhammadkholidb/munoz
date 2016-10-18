package hu.pe.munoz.commonrest.controller.settings;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import hu.pe.munoz.commonrest.controller.settings.SystemController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
        "classpath:munoz-common-rest-context-test.xml",
        "classpath:munoz-common-data-context-test.xml" })
@WebAppConfiguration
public class TestSystemController {

    private static final Logger LOG = LoggerFactory.getLogger(TestSystemController.class);

    private static boolean initialized = false;
    
    private MockMvc mockMvc;

    @Autowired
    private SystemController systemController;
    
    @Before
    public void init() {
        
        // Initialize test only once in this class.
        // Annotation @BeforeClass needs static method to run which will require the class properties to be static too, so the @Autowired cannot be done.
        // Simple solution from here http://stackoverflow.com/questions/32952884/junit-beforeclass-non-static-work-around-for-spring-boot-application#answer-32956539
        if (!initialized) {            
            
            // http://stackoverflow.com/questions/29053974/how-to-i-write-a-unit-test-for-a-spring-boot-controller-endpoint#answer-29054326
            mockMvc = standaloneSetup(systemController).build();
            
            initialized = true;
        }
    }

    @Test
    public void testGetSystemList() {
        LOG.debug("Test get system list ...");
        try {
            MvcResult result = mockMvc.perform(get("/settings/system/list"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andReturn();
            LOG.debug("Result: " + result.getResponse().getContentAsString());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Test
    public void testEditSystemList() {
        LOG.debug("Test edit system list ...");
    }
    
}
