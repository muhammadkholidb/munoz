package hu.pe.munoz.commondata.test.bo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
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
import hu.pe.munoz.commondata.bo.UserGroupBo;
import hu.pe.munoz.commondata.dao.UserGroupMenuPermissionDao;
import hu.pe.munoz.commondata.entity.UserGroupEntity;
import hu.pe.munoz.commondata.entity.UserGroupMenuPermissionEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:munoz-common-data-context-test.xml")
public class TestUserGroupBo extends AbstractTransactionalJUnit4SpringContextTests {
    
    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private UserGroupBo userGroupBo;

	@Autowired
	private UserGroupMenuPermissionDao userGroupMenuPermissionDao;

    @Before
    public void init() {
        // The SQL file is relative to the application context file
        executeSqlScript("scripts/test-user-group.sql", false);
    }
    
    @Test
    public void testGetAllUserGroup() {
        log.debug("TEST get all user group ...");
        List<UserGroupEntity> list = userGroupBo.getAllUserGroup();
        assertEquals(3, list.size());
    }
    
    @Test
    public void testFindOneUserGroupFail() {
        log.debug("TEST FAIL find one user group ...");
        Long userGroupId = 123L;
        try {
            userGroupBo.getOneUserGroup(userGroupId);
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.D0001, e.getCode());
            assertEquals(ErrorMessageConstants.USER_GROUP_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testFindOneUserGroupSuccess() {
        log.debug("TEST SUCCESS find one user group ...");
        Long userGroupId = 1L;
        try {
            UserGroupEntity userGroup = userGroupBo.getOneUserGroup(userGroupId);
            assertEquals(1L, userGroup.getId().longValue());
            assertEquals("Administrator", userGroup.getName());
            assertEquals(CommonConstants.YES, userGroup.getActive());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testFindOneUserGroupWithMenuPermissions() {
        log.debug("TEST SUCCESS find one user group with menu permissions ...");
        Long userGroupId = 1L;
        try {
            UserGroupEntity userGroup = userGroupBo.getOneUserGroup(userGroupId);
            assertEquals(1L, userGroup.getId().longValue());
            assertEquals("Administrator", userGroup.getName());
            assertEquals(CommonConstants.YES, userGroup.getActive());
            
            List<UserGroupMenuPermissionEntity> listMenuPermissions = userGroup.getUserGroupMenuPermissions();
            assertEquals(4, listMenuPermissions.size());
            
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    @Test
    public void testAddUserGroupSuccess() {
        log.debug("TEST SUCCESS add user group ...");
        UserGroupEntity userGroupEntity = new UserGroupEntity();
        userGroupEntity.setName("Report Group");
        userGroupEntity.setActive(CommonConstants.YES);

        UserGroupMenuPermissionEntity menu1 = new UserGroupMenuPermissionEntity();
        menu1.setMenuCode("menu.settings");
        menu1.setView(CommonConstants.YES);
        menu1.setModify(CommonConstants.NO);

        UserGroupMenuPermissionEntity menu2 = new UserGroupMenuPermissionEntity();
        menu2.setMenuCode("menu.settings.system");
        menu2.setView(CommonConstants.YES);
        menu2.setModify(CommonConstants.NO);

        UserGroupMenuPermissionEntity menu3 = new UserGroupMenuPermissionEntity();
        menu3.setMenuCode("menu.settings.user");
        menu3.setView(CommonConstants.YES);
        menu3.setModify(CommonConstants.NO);

        UserGroupMenuPermissionEntity menu4 = new UserGroupMenuPermissionEntity();
        menu4.setMenuCode("menu.settings.usergroup");
        menu4.setView(CommonConstants.YES);
        menu4.setModify(CommonConstants.NO);
        
    	List<UserGroupMenuPermissionEntity> listMenuPermission = new ArrayList<UserGroupMenuPermissionEntity>();
    	listMenuPermission.add(menu1);
    	listMenuPermission.add(menu2);
    	listMenuPermission.add(menu3);
    	listMenuPermission.add(menu4);

        try {
            UserGroupEntity created = userGroupBo.addUserGroup(userGroupEntity, listMenuPermission);
            assertEquals("Report Group", created.getName());
            assertEquals(CommonConstants.YES, created.getActive());
            
            // Find total user group now
            List<UserGroupEntity> list = userGroupBo.getAllUserGroup();
            assertEquals(4, list.size());
            
            // Find list menus for this user group
            List<UserGroupMenuPermissionEntity> listMenuPermissionByUserGroupId = userGroupMenuPermissionDao.findByUserGroupId(created.getId());
            assertEquals(listMenuPermission.size(), listMenuPermissionByUserGroupId.size());
            
        } catch (DataException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAddUserGroupFail() {
        log.debug("TEST FAIL add user group ...");
        UserGroupEntity userGroupEntity = new UserGroupEntity();
        userGroupEntity.setName("Administrator");
        userGroupEntity.setActive(CommonConstants.YES);

        try {
            userGroupBo.addUserGroup(userGroupEntity, null);
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.D0003, e.getCode());
            assertEquals(ErrorMessageConstants.USER_GROUP_ALREADY_EXISTS, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
    
    @Test
    public void testEditUserGroupSuccess() {
        log.debug("TEST SUCCESS edit user group ...");
        UserGroupEntity userGroup = new UserGroupEntity();
        userGroup.setId(1L);
        userGroup.setName("Super Administrator");
        userGroup.setActive(CommonConstants.NO);

        UserGroupMenuPermissionEntity menu1 = new UserGroupMenuPermissionEntity();
        menu1.setMenuCode("menu.settings");
        menu1.setView(CommonConstants.YES);
        menu1.setModify(CommonConstants.NO);

        UserGroupMenuPermissionEntity menu2 = new UserGroupMenuPermissionEntity();
        menu2.setMenuCode("menu.settings.system");
        menu2.setView(CommonConstants.YES);
        menu2.setModify(CommonConstants.NO);

        UserGroupMenuPermissionEntity menu3 = new UserGroupMenuPermissionEntity();
        menu3.setMenuCode("menu.settings.user");
        menu3.setView(CommonConstants.YES);
        menu3.setModify(CommonConstants.NO);

        UserGroupMenuPermissionEntity menu4 = new UserGroupMenuPermissionEntity();
        menu4.setMenuCode("menu.settings.usergroup");
        menu4.setView(CommonConstants.YES);
        menu4.setModify(CommonConstants.NO);
        
    	List<UserGroupMenuPermissionEntity> listMenuPermission = new ArrayList<UserGroupMenuPermissionEntity>();
    	listMenuPermission.add(menu1);
    	listMenuPermission.add(menu2);
    	listMenuPermission.add(menu3);
    	listMenuPermission.add(menu4);

        try {
            UserGroupEntity updated = userGroupBo.editUserGroup(userGroup, listMenuPermission);
            Assert.assertEquals(CommonConstants.NO, updated.getActive());
            Assert.assertEquals("Super Administrator", updated.getName());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testEditUserGroupFail1() {
        // Test fail cause ID not foud
        log.debug("TEST FAIL 1 edit user group ...");
        UserGroupEntity userGroup = new UserGroupEntity();
        userGroup.setId(123L);
        userGroup.setName("Administrator");
        userGroup.setActive(CommonConstants.NO);

        try {
            userGroupBo.editUserGroup(userGroup, null);
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.D0001, e.getCode());
            assertEquals(ErrorMessageConstants.USER_GROUP_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testEditUserGroupFail2() {
        // Test fail cause name already exists
        log.debug("TEST FAIL 2 edit user group ...");
        UserGroupEntity userGroup = new UserGroupEntity();
        userGroup.setId(1L);
        userGroup.setName("User");
        userGroup.setActive(CommonConstants.NO);
        
        try {
            userGroupBo.editUserGroup(userGroup, null);
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.D0003, e.getCode());
            assertEquals(ErrorMessageConstants.USER_GROUP_ALREADY_EXISTS, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    @Test
    public void testRemoveUserGroupFail() {
        log.debug("TEST FAIL remove user group ...");
        Long userGroupId = 1L;
        try {
            userGroupBo.removeUserGroup(userGroupId);
            fail();
        } catch (DataException e) {
            log.debug(e.toString());
            assertEquals(ExceptionCode.D0002, e.getCode());
            assertEquals(ErrorMessageConstants.CANT_REMOVE_USER_GROUP_CAUSE_USER_EXISTS, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    @Test
    public void testRemoveUserGroupSuccess() {
        log.debug("TEST SUCCESS remove user group ...");
        Long userGroupId = 3L;
        try {
            userGroupBo.removeUserGroup(userGroupId);
        } catch (Exception e) {
            fail();
        }
        
        // Find removed user group, it should not found
        try {
            userGroupBo.getOneUserGroup(userGroupId);
            fail();
        } catch (DataException e) {
        	log.debug(e.toString());
        	assertEquals(ExceptionCode.D0001, e.getCode());
        	assertEquals(ErrorMessageConstants.USER_GROUP_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        
    }
    
}
