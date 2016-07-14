package hu.pe.munoz.commondata.test.bo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.common.exception.ExceptionCode;
import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.commondata.ErrorMessageConstants;
import hu.pe.munoz.commondata.bo.SystemBo;
import hu.pe.munoz.commondata.entity.SystemEntity;

@ContextConfiguration(locations = "classpath:munoz-common-data-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestSystemBo extends AbstractTransactionalJUnit4SpringContextTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestSystemBo.class);

    @Autowired
    private SystemBo systemBo;

    @Before
    public void init() {
        // The SQL file is relative to the application context file
        executeSqlScript("scripts/test-system.sql", false);
    }

    @Test
    public void testGetAllSystem() {
        LOGGER.debug("TEST get all system ...");
        List<SystemEntity> list = systemBo.getAllSystem();
        assertEquals(4, list.size());
    }

    @Test
    public void testGetSystemByKeySuccess() {
        LOGGER.debug("TEST SUCCESS get system by key ...");
        String key = CommonConstants.SYSTEM_KEY_LANGUAGE_CODE;
        try {
            SystemEntity system = systemBo.getSystemByKey(key);
            assertEquals("en", system.getValue());
        } catch (DataException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetSystemByKeyFail() {
        LOGGER.debug("TEST FAIL get system by key ...");
        String key = "unknown";
        try {
            systemBo.getSystemByKey(key);
            fail();
        } catch (DataException e) {
            LOGGER.debug(e.toString());
            assertEquals(ExceptionCode.D0001, e.getCode());
            assertEquals(ErrorMessageConstants.SYSTEM_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    @Test
    public void testEditSystemListSuccess() {
        LOGGER.debug("TEST SUCCESS edit system list ...");

        SystemEntity systemLanguageCode = new SystemEntity();
        systemLanguageCode.setId(1L);
        systemLanguageCode.setKey(CommonConstants.SYSTEM_KEY_LANGUAGE_CODE);
        systemLanguageCode.setValue(CommonConstants.LANGUAGE_CODE_INDONESIA);

        SystemEntity systemOnline = new SystemEntity();
        systemOnline.setId(4L);
        systemOnline.setKey(CommonConstants.SYSTEM_KEY_ONLINE);
        systemOnline.setValue(CommonConstants.YES);

        List<SystemEntity> systemList = new ArrayList<SystemEntity>();
        systemList.add(systemLanguageCode);
        systemList.add(systemOnline);

        try {
            List<SystemEntity> updatedList = systemBo.editSystemList(systemList);
            assertEquals(2, updatedList.size());
            for (SystemEntity systemEntity : updatedList) {
                Long id = systemEntity.getId();
                if (id == 1L) {
                    assertEquals(CommonConstants.SYSTEM_KEY_LANGUAGE_CODE, systemEntity.getKey());
                    assertEquals(CommonConstants.LANGUAGE_CODE_INDONESIA, systemEntity.getValue());
                } else if (id == 4L) {
                    assertEquals(CommonConstants.SYSTEM_KEY_ONLINE, systemEntity.getKey());
                    assertEquals(CommonConstants.YES, systemEntity.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    @Test
    public void testEditSystemListFail() {
        LOGGER.debug("TEST FAIL edit system list ...");

        SystemEntity systemUnknown = new SystemEntity();
        systemUnknown.setId(10L);
        systemUnknown.setKey("unknown");
        systemUnknown.setValue("0");

        List<SystemEntity> systemList = new ArrayList<SystemEntity>();
        systemList.add(systemUnknown);

        try {
            systemBo.editSystemList(systemList);
            fail();
        } catch (DataException e) {
            LOGGER.debug(e.toString());
            assertEquals(ExceptionCode.D0001, e.getCode());
            assertEquals(ErrorMessageConstants.SYSTEM_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
