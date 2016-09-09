package hu.pe.munoz.commondata.test.bo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.common.exception.ExceptionCode;
import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.commondata.ErrorMessageConstants;
import hu.pe.munoz.commondata.bo.SystemBo;
import hu.pe.munoz.commondata.helper.Dto;
import hu.pe.munoz.commondata.test.AbstractTestDataImport;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@ContextConfiguration(locations = "classpath:munoz-common-data-context-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestSystemBo extends AbstractTestDataImport {

    private static final Logger LOG = LoggerFactory.getLogger(TestSystemBo.class);

    @Autowired
    private SystemBo systemBo;

    @Before
    public void init() throws Exception {
        processDataSets("dataset/test-system.dataset.xml");
    }

    @After
    public void finish() throws Exception {
    	LOG.debug("Test done, clearing data ...");
    	clearDataSets();
    }
    
    @Test
    public void testGetAllSystem() {
        LOG.debug("TEST get all system ...");
        try {
            List<Dto> list = systemBo.getAllSystem(null);
            LOG.debug("Result: " + list);
            assertEquals(4, list.size());
        } catch (Exception ex) {
            LOG.error(ex.toString(), ex);
            fail(ex.toString());
        }
    }

    @Test
    public void testGetSystemByKeySuccess() {
        LOG.debug("TEST SUCCESS get system by key ...");
        Dto dtoInput = new Dto();
        dtoInput.put("dataKey", CommonConstants.SYSTEM_KEY_LANGUAGE_CODE);
        try {
            Dto result = systemBo.getSystemByDataKey(dtoInput);
            assertEquals("en", result.get("dataValue"));
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testGetSystemByKeyFail() {
        LOG.debug("TEST FAIL get system by key ...");
        Dto dtoInput = new Dto();
        dtoInput.put("dataKey", "unknown");
        try {
            systemBo.getSystemByDataKey(dtoInput);
            fail();
        } catch (DataException e) {
            LOG.debug(e.toString());
            assertEquals(ExceptionCode.E1001, e.getCode());
            assertEquals(ErrorMessageConstants.SYSTEM_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEditSystemListSuccess() {
        LOG.debug("TEST SUCCESS edit system list ...");

        JSONObject systemLanguageCode = new JSONObject();
        systemLanguageCode.put("id", 1L);
        systemLanguageCode.put("dataKey", CommonConstants.SYSTEM_KEY_LANGUAGE_CODE);
        systemLanguageCode.put("dataValue", CommonConstants.LANGUAGE_CODE_INDONESIA);

        JSONObject systemOnline = new JSONObject();
        systemOnline.put("id", 4L);
        systemOnline.put("dataKey", CommonConstants.SYSTEM_KEY_ONLINE);
        systemOnline.put("dataValue", CommonConstants.YES);

        JSONArray systemList = new JSONArray();
        systemList.add(systemLanguageCode);
        systemList.add(systemOnline);

        Dto dtoInput = new Dto().put("systems", systemList.toString());

        try {
            List<Dto> result = systemBo.editSystemList(dtoInput);
            LOG.debug("Result: " + result);
            assertEquals(2, result.size());
            for (Dto dto : result) {
                Long id = dto.get("id");
                if (id == 1L) {
                    assertEquals(CommonConstants.SYSTEM_KEY_LANGUAGE_CODE, dto.get("dataKey"));
                    assertEquals(CommonConstants.LANGUAGE_CODE_INDONESIA, dto.get("dataValue"));
                } else if (id == 4L) {
                    assertEquals(CommonConstants.SYSTEM_KEY_ONLINE, dto.get("dataKey"));
                    assertEquals(CommonConstants.YES, dto.get("dataValue"));
                }
            }
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEditSystemListFail() {
        LOG.debug("TEST FAIL edit system list ...");

        JSONObject systemUnknown = new JSONObject();
        systemUnknown.put("id", 10L);
        systemUnknown.put("dataKey", "unknown");
        systemUnknown.put("dataValue", 0);

        JSONArray systemList = new JSONArray();
        systemList.add(systemUnknown);

        Dto dtoInput = new Dto().put("systems", systemList.toString());
        
        try {
            systemBo.editSystemList(dtoInput);
            fail();
        } catch (DataException e) {
            LOG.debug(e.toString());
            assertEquals(ExceptionCode.E1001, e.getCode());
            assertEquals(ErrorMessageConstants.SYSTEM_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }
    }

}
