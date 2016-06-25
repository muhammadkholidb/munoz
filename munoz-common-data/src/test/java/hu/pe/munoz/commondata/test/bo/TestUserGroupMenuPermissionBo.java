package hu.pe.munoz.commondata.test.bo;

import static org.junit.Assert.assertEquals;

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

import hu.pe.munoz.commondata.bo.UserGroupMenuPermissionBo;
import hu.pe.munoz.commondata.entity.UserGroupMenuPermissionEntity;

@ContextConfiguration(locations = "classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestUserGroupMenuPermissionBo extends AbstractTransactionalJUnit4SpringContextTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestUserGroupMenuPermissionBo.class);

    @Autowired
    private UserGroupMenuPermissionBo userGroupMenuPermissionBo;

    @Before
    public void init() {
        // The SQL file is relative to the application context file
        executeSqlScript("scripts/test-user-group-menu-permission.sql", false);
    }

    @Test
    public void testGetAllUserGroupMenuPermission() {
        LOGGER.debug("TEST get all user group menu permission ...");
        List<UserGroupMenuPermissionEntity> list = userGroupMenuPermissionBo.getAllUserGroupMenuPermission();
        assertEquals(8, list.size());
    }

    @Test
    public void testGetUserGroupMenuPermissionListByUserGroupId() {
    	LOGGER.debug("TEST get user group menu permission by user group id ...");
    	Long userGroupId = 1L;
    	List<UserGroupMenuPermissionEntity> list = userGroupMenuPermissionBo.getUserGroupMenuPermissionListByUserGroupId(userGroupId);
    	assertEquals(4, list.size());
    }
    
}
