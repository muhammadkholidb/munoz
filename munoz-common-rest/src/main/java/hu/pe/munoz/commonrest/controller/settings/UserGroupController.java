package hu.pe.munoz.commonrest.controller.settings;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.commondata.bo.UserGroupBo;
import hu.pe.munoz.commondata.entity.UserGroupEntity;
import hu.pe.munoz.commondata.entity.UserGroupMenuPermissionEntity;
import hu.pe.munoz.commonrest.controller.BaseController;
import hu.pe.munoz.commonrest.helper.ResponseWrapper;
import hu.pe.munoz.commonrest.pojo.settings.UserGroup;
import hu.pe.munoz.commonrest.pojo.settings.UserGroupWithMenuPermissions;

@RestController
@RequestMapping("/settings")
public class UserGroupController extends BaseController {

	private Logger log = LoggerFactory.getLogger(UserGroupController.class);
	
	@Autowired
	private UserGroupBo userGroupBo;

	@RequestMapping(value = "/user-group/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseWrapper<List<UserGroup>> getAllUserGroup() {
		List<UserGroupEntity> listUserGroupEntity = userGroupBo.getAllUserGroup();
		List<UserGroup> list = new ArrayList<UserGroup>();
		for (UserGroupEntity entity : listUserGroupEntity) {
			list.add(mapper.map(entity, UserGroup.class));
		}
		return new ResponseWrapper<List<UserGroup>>(CommonConstants.SUCCESS, list);
	}

	@RequestMapping(value = "/user-group/find", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseWrapper<UserGroupWithMenuPermissions> getUserGroup(@RequestParam(value = "userGroupId") Long userGroupId) throws Exception {
		UserGroupEntity entity = userGroupBo.getOneUserGroupWithMenuPermissions(userGroupId);
		log.debug("Entity: " + entity);
		List<UserGroupMenuPermissionEntity> list = entity.getUserGroupMenuPermissions();
		log.debug("List: " + list.size());
		UserGroupWithMenuPermissions userGroup = mapper.map(entity, UserGroupWithMenuPermissions.class);
		return new ResponseWrapper<UserGroupWithMenuPermissions>(CommonConstants.SUCCESS, userGroup);
	}

	@RequestMapping(value = "/user-group/remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseWrapper<Object> removeUserGroup(@RequestParam(value = "userGroupId") Long userGroupId) throws Exception {
		userGroupBo.removeUserGroup(userGroupId);
		return new ResponseWrapper<Object>(CommonConstants.SUCCESS, getResponseMessage("success.SuccessfullyRemoveUserGroup"));
	}

    @RequestMapping(value = "/user-group/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper<UserGroup> addUserGroup(
    		@RequestParam(value = "userGroup") String strUserGroup, 
    		@RequestParam(value = "menuPermissions") String strMenuPermissions) throws Exception {
    	
    	JSONObject jsonUserGroup = (JSONObject) JSONValue.parse(strUserGroup);
        JSONArray jsonMenuPermissions = (JSONArray) JSONValue.parse(strMenuPermissions);
        
        UserGroupEntity userGroupEntity = mapper.map(jsonUserGroup, UserGroupEntity.class);
        
        List<UserGroupMenuPermissionEntity> listMenuPermission = new ArrayList<UserGroupMenuPermissionEntity>();
        for (int i = 0; i < jsonMenuPermissions.size(); i++) {
        	UserGroupMenuPermissionEntity entity = mapper.map(jsonMenuPermissions.get(i), UserGroupMenuPermissionEntity.class);
        	listMenuPermission.add(entity);
        }
        
        UserGroupEntity added = userGroupBo.addUserGroup(userGroupEntity, listMenuPermission);
        UserGroup userGroup = mapper.map(added, UserGroup.class);
        
        return new ResponseWrapper<UserGroup>(CommonConstants.SUCCESS, userGroup, getResponseMessage("success.SuccessfullyAddUserGroup"));
    }

    @RequestMapping(value = "/user-group/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper<UserGroup> editUserGroup(
            @RequestParam(value = "userGroup") String strUserGroup, 
            @RequestParam(value = "menuPermissions") String strMenuPermissions) throws Exception {
        
        JSONObject jsonUserGroup = (JSONObject) JSONValue.parse(strUserGroup);
        JSONArray jsonMenuPermissions = (JSONArray) JSONValue.parse(strMenuPermissions);
        
        UserGroupEntity userGroupEntity = mapper.map(jsonUserGroup, UserGroupEntity.class);
        
        List<UserGroupMenuPermissionEntity> listMenuPermission = new ArrayList<UserGroupMenuPermissionEntity>();
        for (int i = 0; i < jsonMenuPermissions.size(); i++) {
            UserGroupMenuPermissionEntity entity = mapper.map(jsonMenuPermissions.get(i), UserGroupMenuPermissionEntity.class);
            listMenuPermission.add(entity);
        }
        
        UserGroupEntity updated = userGroupBo.editUserGroup(userGroupEntity, listMenuPermission);
        UserGroup userGroup = mapper.map(updated, UserGroup.class);
        
        return new ResponseWrapper<UserGroup>(CommonConstants.SUCCESS, userGroup, getResponseMessage("success.SuccessfullyEditUserGroup"));
    }

}
