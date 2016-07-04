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
import hu.pe.munoz.commonrest.ResponseWrapper;
import hu.pe.munoz.commonrest.controller.BaseController;
import hu.pe.munoz.commonrest.pojo.settings.UserGroup;

@RestController
@RequestMapping("/settings")
public class UserGroupController extends BaseController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserGroupBo userGroupBo;

	@RequestMapping(value = "/user-group/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseWrapper<List<UserGroup>> getAllUserGroup() {
		List<UserGroupEntity> listUserGroupEntity = userGroupBo.getAllUserGroup();
		List<UserGroup> list = new ArrayList<UserGroup>();
		for (UserGroupEntity entity : listUserGroupEntity) {
			list.add(mapper.map(entity, UserGroup.class));
		}
		return new ResponseWrapper<List<UserGroup>>(CommonConstants.SUCCESS, list, "");
	}

	@RequestMapping(value = "/user-group/remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseWrapper<UserGroupEntity> removeUserGroup(@RequestParam(value = "userGroupId") Long userGroupId,
			@RequestParam(value = "languageCode") String languageCode) throws Exception {

		ResponseWrapper<UserGroupEntity> wrapper = new ResponseWrapper<UserGroupEntity>();
		userGroupBo.removeUserGroup(userGroupId);
		wrapper.setStatus(CommonConstants.SUCCESS);
		return wrapper;
	}

    @RequestMapping(value = "/user-group/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper<UserGroup> addUserGroup(
    		@RequestParam(value = "userGroup") String strUserGroup, 
    		@RequestParam(value = "menuPermissions") String strMenuPermissions) throws Exception {
    	
    	JSONObject jsonUserGroup = (JSONObject) JSONValue.parse(strUserGroup);
    	log.debug("JSON user group: " + jsonUserGroup);
    	
        JSONArray jsonMenuPermissions = (JSONArray) JSONValue.parse(strMenuPermissions);
        log.debug("JSON menu permissions: " + jsonMenuPermissions);
        
        UserGroupEntity userGroupEntity = mapper.map(jsonUserGroup, UserGroupEntity.class);
        log.debug("User group: " + userGroupEntity.getName());
        
        List<UserGroupMenuPermissionEntity> listMenuPermission = new ArrayList<UserGroupMenuPermissionEntity>();
        for (int i = 0; i < jsonMenuPermissions.size(); i++) {
        	UserGroupMenuPermissionEntity entity = mapper.map(jsonMenuPermissions.get(i), UserGroupMenuPermissionEntity.class);
        	log.debug("Menu: " + entity.getMenuCode() + ", view: " + entity.getView() + ", modify: " + entity.getModify());
        	listMenuPermission.add(entity);
        }
        
        UserGroupEntity added = userGroupBo.addUserGroup(userGroupEntity, listMenuPermission);
        UserGroup userGroup = mapper.map(added, UserGroup.class);
        
        return new ResponseWrapper<UserGroup>(CommonConstants.SUCCESS, userGroup);
    }

}
