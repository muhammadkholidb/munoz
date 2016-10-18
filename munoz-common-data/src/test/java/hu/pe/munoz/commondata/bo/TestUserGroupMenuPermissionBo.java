package hu.pe.munoz.commondata.bo;

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

import hu.pe.munoz.commondata.AbstractTestDataImport;
import hu.pe.munoz.commondata.bo.UserGroupMenuPermissionBo;
import hu.pe.munoz.commondata.helper.Dto;

@ContextConfiguration(locations = "classpath:munoz-common-data-context-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestUserGroupMenuPermissionBo extends AbstractTestDataImport {

    private static final Logger LOG = LoggerFactory.getLogger(TestUserGroupMenuPermissionBo.class);

    @Autowired
    private UserGroupMenuPermissionBo userGroupMenuPermissionBo;

    @Before
    public void init() throws Exception {
        processDataSets("dataset/test-user-group-menu-permission.dataset.xml");
    }

    @After
    public void finish() throws Exception {
    	LOG.debug("Test done, clearing data ...");
    	clearDataSets();
    }

    @Test
    public void testGetAllUserGroupMenuPermission() {
        LOG.debug("TEST get all user group menu permission ...");
        List<Dto> list = userGroupMenuPermissionBo.getAllUserGroupMenuPermission(null);
        assertEquals(8, list.size());
    }

    @Test
    public void testGetUserGroupMenuPermissionListByUserGroupId() {
        LOG.debug("TEST get user group menu permission by user group id ...");
        Dto dtoInput = new Dto();
        dtoInput.put("userGroupId", 1L);
        try {
            List<Dto> list = userGroupMenuPermissionBo.getUserGroupMenuPermissionListByUserGroupId(dtoInput);
            assertEquals(4, list.size());
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            fail(e.toString());
        }
    }

}
