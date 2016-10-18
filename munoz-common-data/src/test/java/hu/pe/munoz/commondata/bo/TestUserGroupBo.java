package hu.pe.munoz.commondata.bo;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
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
import hu.pe.munoz.commondata.bo.UserGroupBo;
import hu.pe.munoz.commondata.bo.UserGroupMenuPermissionBo;
import hu.pe.munoz.commondata.helper.Dto;
import hu.pe.munoz.commondata.test.AbstractTestDataImport;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:munoz-common-data-context-test.xml")
public class TestUserGroupBo extends AbstractTestDataImport {

    private static final Logger LOG = LoggerFactory.getLogger(TestUserGroupBo.class);

    @Autowired
    private UserGroupBo userGroupBo;

    @Autowired
    private UserGroupMenuPermissionBo userGroupMenuPermissionBo;

    @Before
    public void init() throws Exception {
        processDataSets("dataset/test-user-group.dataset.xml");
    }

    @After
    public void finish() throws Exception {
    	LOG.debug("Test done, clearing data ...");
    	clearDataSets();
    }

    @Test
    public void testGetAllUserGroup() {
        LOG.debug("TEST get all user group ...");
        List<Dto> list = userGroupBo.getAllUserGroup(null);
        LOG.debug("Result: " + list);
        assertEquals(3, list.size());
    }

    @Test
    public void testFindOneUserGroupFail() {
        LOG.debug("TEST FAIL find one user group ...");
        Dto dtoInput = new Dto();
        dtoInput.put("id", 123L);
        try {
            userGroupBo.getOneUserGroup(dtoInput);
            fail();
        } catch (DataException e) {
            LOG.debug(e.toString());
            assertEquals(ExceptionCode.E1001, e.getCode());
            assertEquals(ErrorMessageConstants.USER_GROUP_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testFindOneUserGroupSuccess() {
        LOG.debug("TEST SUCCESS find one user group ...");
        Dto dtoInput = new Dto();
        dtoInput.put("id", 1L);
        try {
            Dto result = userGroupBo.getOneUserGroup(dtoInput);
            LOG.debug("Result: " + result);
            assertEquals(1L, result.get("id"));
            assertEquals("Administrator", result.get("name"));
            assertEquals(CommonConstants.YES, result.get("active"));
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testFindOneUserGroupWithMenuPermissions() {
        LOG.debug("TEST SUCCESS find one user group with menu permissions ...");
        Dto dtoInput = new Dto();
        dtoInput.put("id", 1L);
        try {
            Dto result = userGroupBo.getOneUserGroupWithMenuPermissions(dtoInput);
            LOG.debug("Result: " + result);
            assertEquals(1L, result.get("id"));
            assertEquals("Administrator", result.get("name"));
            assertEquals(CommonConstants.YES, result.get("active"));

            List<Dto> listMenuPermissions = result.get("menuPermissions");
            assertEquals(4, listMenuPermissions.size());

        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAddUserGroupSuccess() {
        LOG.debug("TEST SUCCESS add user group ...");
        
        JSONObject userGroup = new JSONObject();
        userGroup.put("name", "Group 1");
        userGroup.put("active", CommonConstants.YES);
        
        JSONObject menu1 = new JSONObject();
        menu1.put("menuCode", "menu.settings");
        menu1.put("view", CommonConstants.YES);
        menu1.put("modify",  CommonConstants.NO);

        JSONObject menu2 = new JSONObject();
        menu2.put("menuCode", "menu.settings.system");
        menu2.put("view", CommonConstants.YES);
        menu2.put("modify", CommonConstants.NO);

        JSONObject menu3 = new JSONObject();
        menu3.put("menuCode", "menu.settings.user");
        menu3.put("view", CommonConstants.YES);
        menu3.put("modify", CommonConstants.NO);

        JSONObject menu4 = new JSONObject();
        menu4.put("menuCode", "menu.settings.usergroup");
        menu4.put("view", CommonConstants.YES);
        menu4.put("modify", CommonConstants.NO);

        JSONArray menuPermissions = new JSONArray();
        menuPermissions.add(menu1);
        menuPermissions.add(menu2);
        menuPermissions.add(menu3);
        menuPermissions.add(menu4);

        Dto dtoInput = new Dto();
        dtoInput.put("userGroup", userGroup);
        dtoInput.put("menuPermissions", menuPermissions);
        
        try {
            Dto created = userGroupBo.addUserGroup(dtoInput);
            LOG.debug("Result: " + created);
            assertEquals("Group 1", created.get("name"));
            assertEquals(CommonConstants.YES, created.get("active"));

            // Find total user group now
            List<Dto> list = userGroupBo.getAllUserGroup(null);
            assertEquals(4, list.size());

            // Find list menus for this user group
            List<Dto> listMenuPermissionByUserGroupId = userGroupMenuPermissionBo.getUserGroupMenuPermissionListByUserGroupId(new Dto().put("userGroupId", created.get("id")));
            assertEquals(menuPermissions.size(), listMenuPermissionByUserGroupId.size());

        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAddUserGroupFail() {
        LOG.debug("TEST FAIL add user group ...");
        
        JSONObject userGroup = new JSONObject();
        userGroup.put("name", "Administrator");
        userGroup.put("active", CommonConstants.YES);
        
        JSONArray menuPermissions = new JSONArray();
        
        Dto dtoInput = new Dto();
        dtoInput.put("userGroup", userGroup);
        dtoInput.put("menuPermissions", menuPermissions);
        
        try {
            userGroupBo.addUserGroup(dtoInput);
            fail();
        } catch (DataException e) {
            LOG.debug(e.toString());
            assertEquals(ExceptionCode.E1003, e.getCode());
            assertEquals(ErrorMessageConstants.USER_GROUP_ALREADY_EXISTS, e.getMessage());
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            Assert.fail(e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEditUserGroupSuccess() {
        LOG.debug("TEST SUCCESS edit user group ...");
        
        JSONObject userGroup = new JSONObject();
        userGroup.put("id", 1L);
        userGroup.put("name", "Super Administrator");
        userGroup.put("active", CommonConstants.NO);
        
        JSONObject menu1 = new JSONObject();
        menu1.put("menuCode", "menu.settings");
        menu1.put("view", CommonConstants.YES);
        menu1.put("modify",  CommonConstants.NO);

        JSONObject menu2 = new JSONObject();
        menu2.put("menuCode", "menu.settings.system");
        menu2.put("view", CommonConstants.YES);
        menu2.put("modify", CommonConstants.NO);

        JSONObject menu3 = new JSONObject();
        menu3.put("menuCode", "menu.settings.user");
        menu3.put("view", CommonConstants.YES);
        menu3.put("modify", CommonConstants.NO);

        JSONObject menu4 = new JSONObject();
        menu4.put("menuCode", "menu.settings.usergroup");
        menu4.put("view", CommonConstants.YES);
        menu4.put("modify", CommonConstants.NO);

        JSONArray menuPermissions = new JSONArray();
        menuPermissions.add(menu1);
        menuPermissions.add(menu2);
        menuPermissions.add(menu3);
        menuPermissions.add(menu4);

        Dto dtoInput = new Dto();
        dtoInput.put("userGroup", userGroup);
        dtoInput.put("menuPermissions", menuPermissions);
        
        try {
            Dto updated = userGroupBo.editUserGroup(dtoInput);
            Assert.assertEquals(CommonConstants.NO, updated.get("active"));
            Assert.assertEquals("Super Administrator", updated.get("name"));
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            Assert.fail(e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEditUserGroupFail1() {
        // Test fail cause ID not foud
        LOG.debug("TEST FAIL 1 edit user group ...");
        
        JSONObject userGroup = new JSONObject();
        userGroup.put("id", 123L);
        userGroup.put("name", "Administrator");
        userGroup.put("active", CommonConstants.YES);
        
        JSONArray menuPermissions = new JSONArray();
        
        Dto dtoInput = new Dto();
        dtoInput.put("userGroup", userGroup);
        dtoInput.put("menuPermissions", menuPermissions);
        
        try {
            userGroupBo.editUserGroup(dtoInput);
            fail();
        } catch (DataException e) {
            LOG.debug(e.toString());
            assertEquals(ExceptionCode.E1001, e.getCode());
            assertEquals(ErrorMessageConstants.USER_GROUP_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEditUserGroupFail2() {
        // Test fail cause name already exists
        LOG.debug("TEST FAIL 2 edit user group ...");
        
        JSONObject userGroup = new JSONObject();
        userGroup.put("id", 1L);
        userGroup.put("name", "User");
        userGroup.put("active", CommonConstants.YES);
        
        JSONArray menuPermissions = new JSONArray();
        
        Dto dtoInput = new Dto();
        dtoInput.put("userGroup", userGroup);
        dtoInput.put("menuPermissions", menuPermissions);
        
        try {
            userGroupBo.editUserGroup(dtoInput);
            fail();
        } catch (DataException e) {
            LOG.debug(e.toString());
            assertEquals(ExceptionCode.E1003, e.getCode());
            assertEquals(ErrorMessageConstants.USER_GROUP_ALREADY_EXISTS, e.getMessage());
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testRemoveUserGroupFail() {
        LOG.debug("TEST FAIL remove user group ...");
        Long userGroupId = 1L;
        try {
            userGroupBo.removeUserGroup(new Dto().put("id", userGroupId));
            fail();
        } catch (DataException e) {
            LOG.debug(e.toString());
            assertEquals(ExceptionCode.E1002, e.getCode());
            assertEquals(ErrorMessageConstants.CANT_REMOVE_USER_GROUP_CAUSE_USER_EXISTS, e.getMessage());
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }
    }

    @Test
    public void testRemoveUserGroupSuccess() {
        LOG.debug("TEST SUCCESS remove user group ...");
        Dto dtoInput = new Dto().put("id", 3L);
        try {
            userGroupBo.removeUserGroup(dtoInput);
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }

        // Find removed user group, it should not found
        try {
            userGroupBo.getOneUserGroup(dtoInput);
            fail();
        } catch (DataException e) {
            LOG.debug(e.toString());
            assertEquals(ExceptionCode.E1001, e.getCode());
            assertEquals(ErrorMessageConstants.USER_GROUP_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }

    }

}
