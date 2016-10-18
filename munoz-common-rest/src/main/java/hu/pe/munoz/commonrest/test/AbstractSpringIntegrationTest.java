package hu.pe.munoz.commonrest.test;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.io.IOException;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractSpringIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSpringIntegrationTest.class);
    
    private static boolean initialized = false;

    protected ObjectMapper mapper = new ObjectMapper();
    
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void before() {
        LOG.debug("Before ...");
        // Initialize test only once in this class.
        // Annotation @BeforeClass needs static method to run which will require the class properties to be static too, so the @Autowired cannot be done.
        // http://stackoverflow.com/questions/32952884/junit-beforeclass-non-static-work-around-for-spring-boot-application#answer-32956539
        if (!initialized) {
            LOG.debug("Initialize Spring MVC infrastructure and controllers ...");
            // https://bitbucket.org/luckyryan/samples/src/82d9c79cd7a6ca24475e61516ac4d8dbc5dbb8f6/SampleMVC-TestSuite/src/test/java/com/luckyryan/sample/webapp/controller/IndexControllerIntegrationTest.java?at=master&fileviewer=file-view-default
            mockMvc = webAppContextSetup(wac).build();
            initialized = true;
        }
    }

    protected <T> T convertJson(String json, Class<T> type) {
        try {
            return (T) mapper.readValue(json, type);
        } catch (IOException e) {
            LOG.warn(e.getMessage());
        }
        return null;
    }
    
}
