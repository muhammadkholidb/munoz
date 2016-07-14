package hu.pe.munoz.commondata.test.bo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
import hu.pe.munoz.commondata.bo.UserBo;
import hu.pe.munoz.commondata.entity.UserEntity;
import hu.pe.munoz.commondata.entity.UserGroupEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:munoz-common-data-test-context.xml")
public class TestUserBo extends AbstractTransactionalJUnit4SpringContextTests {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserBo userBo;

    @Before
    public void init() {
        // The SQL file is relative to the application context file
        executeSqlScript("scripts/test-user.sql", false);
    }

    @Test
    public void testGetAllUser() {
        log.debug("TEST get all user ...");
        List<UserEntity> list = userBo.getAllUser();
        assertEquals(2, list.size());
        for (UserEntity user : list) {
            Long id = user.getId();
            if (id.equals(1L)) {
                assertEquals("John", user.getFirstName());
                assertEquals("Doe", user.getLastName());
                assertEquals("johndoe", user.getUsername());
                assertEquals("johndoe@yahoo.com", user.getEmail());
                assertEquals(CommonConstants.YES, user.getActive());
                assertEquals(1, user.getUserGroup().getId().longValue());
                assertEquals("Administrator", user.getUserGroup().getName());
            } else if (id.equals(2L)) {
                assertEquals("Fulan", user.getFirstName());
                assertEquals(CommonConstants.EMPTY_STRING, user.getLastName());
                assertEquals("fulan", user.getUsername());
                assertEquals("fulan@yahoo.com", user.getEmail());
                assertEquals(CommonConstants.YES, user.getActive());
                assertEquals(2L, user.getUserGroup().getId().longValue());
                assertEquals("User", user.getUserGroup().getName());
            }
        }
    }

    @Test
    public void testGetUserListByUserGroupId() {
        log.debug("TEST get user list by user group id ...");
        Long userGroupId = 1L;
        List<UserEntity> list = userBo.getUserListByUserGroupId(userGroupId);
        assertEquals(1, list.size());
        UserEntity user = list.get(0);
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("johndoe", user.getUsername());
        assertEquals("johndoe@yahoo.com", user.getEmail());
        assertEquals(CommonConstants.YES, user.getActive());
        assertEquals(userGroupId, user.getUserGroup().getId());
    }

    @Test
    public void testAddUserSuccess() {
        log.debug("TEST SUCCESS add user ...");
        UserGroupEntity userGroup = new UserGroupEntity();
        userGroup.setId(1L);

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("Brian");
        userEntity.setLastName("Mc Knight");
        userEntity.setUsername("brian");
        userEntity.setEmail("bryan.mckningt@yahoo.com");
        userEntity.setPassword("pojh08ewn");
        userEntity.setActive(CommonConstants.YES);
        userEntity.setUserGroup(userGroup);
        try {
            userEntity = userBo.addUser(userEntity);
            assertEquals(3L, userEntity.getId().longValue());
        } catch (Exception e) {
            e.printStackTrace();
            log.debug(e.toString());
            fail();
        }
    }

    @Test
    public void testAddUserFail() {
        log.debug("TEST FAIL add user ...");
        UserGroupEntity userGroup = new UserGroupEntity();
        userGroup.setId(1L);

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setUsername("johndoe");
        userEntity.setEmail("johndoe@yahoo.com");
        userEntity.setPassword("mbp0jhai");
        userEntity.setActive(CommonConstants.YES);
        userEntity.setUserGroup(userGroup);
        try {
            userBo.addUser(userEntity);
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.D0003, e.getCode());
            assertEquals(ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_USERNAME, e.getMessage());
        } catch (Exception e) {
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testEditUserSuccess() {
        log.debug("TEST SUCCESS edit user ...");
        UserGroupEntity userGroup = new UserGroupEntity();
        userGroup.setId(1L);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(2L);
        userEntity.setFirstName("Fulan");
        userEntity.setLastName("");
        userEntity.setUsername("fulan2");
        userEntity.setEmail("fulan2@yahoo.com");
        userEntity.setPassword("pojh08ewn");
        userEntity.setActive(CommonConstants.NO);
        userEntity.setUserGroup(userGroup);
        try {
            userEntity = userBo.editUser(userEntity);
            assertEquals("fulan2", userEntity.getUsername());
            assertEquals("fulan2@yahoo.com", userEntity.getEmail());
            assertEquals(CommonConstants.NO, userEntity.getActive());
        } catch (Exception e) {
            e.printStackTrace();
            log.debug(e.toString());
            fail();
        }
    }

    @Test
    public void testEditUserFail1() {
        log.debug("TEST FAIL 1 edit user ...");
        UserGroupEntity userGroup = new UserGroupEntity();
        userGroup.setId(1L);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(20L);
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setUsername("johndoe");
        userEntity.setEmail("johndoe@yahoo.com");
        userEntity.setPassword("mbp0jhai");
        userEntity.setActive(CommonConstants.YES);
        userEntity.setUserGroup(userGroup);
        try {
            userBo.editUser(userEntity);
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.D0001, e.getCode());
            assertEquals(ErrorMessageConstants.USER_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.debug(e.toString());
            fail(e.toString());
        }
    }

    @Test
    public void testEditUserFail2() {
        log.debug("TEST FAIL 2 edit user ...");
        UserGroupEntity userGroup = new UserGroupEntity();
        userGroup.setId(1L);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(2L);
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setUsername("johndoe2");
        userEntity.setEmail("johndoe@yahoo.com");
        userEntity.setPassword("mbp0jhai");
        userEntity.setActive(CommonConstants.NO);
        userEntity.setUserGroup(userGroup);
        try {
            userBo.editUser(userEntity);
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.D0003, e.getCode());
            assertEquals(ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_EMAIL, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testRemoveUserSuccess() {
        log.debug("TEST SUCCESS remove user ...");
        Long id = 2L;
        try {
            userBo.removeUser(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug(e.toString());
            fail();
        }
    }

    @Test
    public void testRemoveUserFail() {
        log.debug("TEST FAIL remove user ...");
        Long id = 100L;
        try {
            userBo.removeUser(id);
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.D0001, e.getCode());
            assertEquals(ErrorMessageConstants.USER_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.debug(e.toString());
            fail(e.toString());
        }
    }

}
