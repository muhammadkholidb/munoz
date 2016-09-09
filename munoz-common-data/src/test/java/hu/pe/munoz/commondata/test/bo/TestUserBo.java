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
import hu.pe.munoz.commondata.bo.UserBo;
import hu.pe.munoz.commondata.helper.Dto;
import hu.pe.munoz.commondata.test.AbstractTestDataImport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:munoz-common-data-context-test.xml")
public class TestUserBo extends AbstractTestDataImport {

    private static final Logger LOG = LoggerFactory.getLogger(TestUserBo.class);

    @Autowired
    private UserBo userBo;

    @Before
    public void init() throws Exception {
        processDataSets("dataset/test-user.dataset.xml");
    }

    @After
    public void finish() throws Exception {
    	LOG.debug("Test done, clearing data ...");
    	clearDataSets();
    }

    @Test
    public void testGetAllUser() {
        LOG.debug("TEST get all user ...");
        List<Dto> list = userBo.getAllUserWithGroup(null);
        LOG.debug("Result: " + list);
        assertEquals(2, list.size());
        for (Dto dto : list) {
            Long id = dto.get("id");
            Dto dtoUserGroup = dto.get("userGroup");
            if (id.equals(1L)) {
                assertEquals("John", dto.get("firstName"));
                assertEquals("Doe", dto.get("lastName"));
                assertEquals("johndoe", dto.get("username"));
                assertEquals("johndoe@yahoo.com", dto.get("email"));
                assertEquals(CommonConstants.YES, dto.get("active"));
                assertEquals(1L, dtoUserGroup.get("id"));
                assertEquals("Administrator", dtoUserGroup.get("name"));
            } else if (id.equals(2L)) {
                assertEquals("Fulan", dto.get("firstName"));
                assertEquals("fulan", dto.get("username"));
                assertEquals("fulan@yahoo.com", dto.get("email"));
                assertEquals(CommonConstants.YES, dto.get("active"));
                assertEquals(2L, dtoUserGroup.get("id"));
                assertEquals("User", dtoUserGroup.get("name"));
            }
        }
    }

    @Test
    public void testGetUserListByUserGroupId() {
        LOG.debug("TEST get user list by user group id ...");
        Dto dtoInput = new Dto();
        dtoInput.put("userGroupId", 1L);
        try {
            List<Dto> list = userBo.getUserListByUserGroupId(dtoInput);
            LOG.debug("Result: " + list);
            assertEquals(1, list.size());
            Dto dtoUser = list.get(0);
            assertEquals("John", dtoUser.get("firstName"));
            assertEquals("Doe", dtoUser.get("lastName"));
            assertEquals("johndoe", dtoUser.get("username"));
            assertEquals("johndoe@yahoo.com", dtoUser.get("email"));
            assertEquals(CommonConstants.YES, dtoUser.get("active"));
            assertEquals(1L, dtoUser.get("userGroupId"));
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testAddUserSuccess() {
        LOG.debug("TEST SUCCESS add user ...");
        
        Dto dtoInput = new Dto();
        dtoInput.put("firstName", "Brian");
        dtoInput.put("lastName", "Mc Knight");
        dtoInput.put("username", "brian");
        dtoInput.put("email", "bryan.mckningt@yahoo.com");
        dtoInput.put("password", "pojh08ewn");
        dtoInput.put("active", CommonConstants.YES);
        dtoInput.put("userGroupId", 1L);
        
        try {
            Dto result = userBo.addUser(dtoInput);
            LOG.debug("Result: " + result);
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testAddUserFail() {
        LOG.debug("TEST FAIL add user ...");
        
        Dto dtoInput = new Dto();
        dtoInput.put("firstName", "John");
        dtoInput.put("lastName", "Doe");
        dtoInput.put("username", "johndoe");
        dtoInput.put("email", "johndoe@yahoo.com");
        dtoInput.put("password", "mbp0jhai");
        dtoInput.put("active", CommonConstants.YES);
        dtoInput.put("userGroupId", 1L);
        
        try {
            userBo.addUser(dtoInput);
            fail();
        } catch (DataException e) {
            LOG.debug(e.toString());
            assertEquals(ExceptionCode.E1003, e.getCode());
            assertEquals(ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_USERNAME, e.getMessage());
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testEditUserSuccess() {
        LOG.debug("TEST SUCCESS edit user ...");
        
        Dto dtoInput = new Dto();
        dtoInput.put("id", 2L);
        dtoInput.put("firstName", "Fulan");
        dtoInput.put("lastName", "");
        dtoInput.put("username", "fulan2");
        dtoInput.put("email", "fulan2@yahoo.com");
        dtoInput.put("password", "pojh08ewn");
        dtoInput.put("active", CommonConstants.NO);
        dtoInput.put("userGroupId", 1L);
        
        try {
            Dto result = userBo.editUser(dtoInput);
            LOG.debug("Result: " + result);
            assertEquals("fulan2", result.get("username"));
            assertEquals("fulan2@yahoo.com", result.get("email"));
            assertEquals(CommonConstants.NO, result.get("active"));
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testEditUserFail1() {
        LOG.debug("TEST FAIL 1 edit user ...");
        
        Dto dtoInput = new Dto();
        dtoInput.put("id", 20L);
        dtoInput.put("firstName", "Fulan");
        dtoInput.put("lastName", "");
        dtoInput.put("username", "fulan2");
        dtoInput.put("email", "fulan2@yahoo.com");
        dtoInput.put("password", "pojh08ewn");
        dtoInput.put("active", CommonConstants.NO);
        dtoInput.put("userGroupId", 1L);
        
        try {
            userBo.editUser(dtoInput);
            fail();
        } catch (DataException e) {
            LOG.debug(e.toString());
            assertEquals(ExceptionCode.E1001, e.getCode());
            assertEquals(ErrorMessageConstants.USER_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testEditUserFail2() {
        LOG.debug("TEST FAIL 2 edit user ...");
        
        Dto dtoInput = new Dto();
        dtoInput.put("id", 2L);
        dtoInput.put("firstName", "John");
        dtoInput.put("lastName", "Doe");
        dtoInput.put("username", "johndoe2");
        dtoInput.put("email", "johndoe@yahoo.com");
        dtoInput.put("password", "pojh08ewn");
        dtoInput.put("active", CommonConstants.NO);
        dtoInput.put("userGroupId", 1L);
        
        try {
            userBo.editUser(dtoInput);
            fail();
        } catch (DataException e) {
            LOG.debug(e.toString());
            assertEquals(ExceptionCode.E1003, e.getCode());
            assertEquals(ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_EMAIL, e.getMessage());
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testRemoveUserSuccess() {
        LOG.debug("TEST SUCCESS remove user ...");
        
        Dto dtoInput = new Dto();
        dtoInput.put("id", 2L);
        
        try {
            userBo.removeUser(dtoInput);
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testRemoveUserFail() {
        LOG.debug("TEST FAIL remove user ...");
        Dto dtoInput = new Dto();
        dtoInput.put("id", 298L);
        try {
            userBo.removeUser(dtoInput);
            fail();
        } catch (DataException e) {
            LOG.debug(e.toString());
            assertEquals(ExceptionCode.E1001, e.getCode());
            assertEquals(ErrorMessageConstants.USER_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }
    }

}
