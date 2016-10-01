package hu.pe.munoz.commondata.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.common.exception.ExceptionCode;
import hu.pe.munoz.commondata.ErrorMessageConstants;
import hu.pe.munoz.commondata.dao.UserDao;
import hu.pe.munoz.commondata.dao.UserGroupDao;
import hu.pe.munoz.commondata.dao.UserGroupMenuPermissionDao;
import hu.pe.munoz.commondata.entity.UserEntity;
import hu.pe.munoz.commondata.entity.UserGroupEntity;
import hu.pe.munoz.commondata.entity.UserGroupMenuPermissionEntity;
import hu.pe.munoz.commondata.helper.DataValidation;
import hu.pe.munoz.commondata.helper.Dto;
import hu.pe.munoz.commondata.helper.DtoUtils;
import java.util.Objects;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserGroupBoImpl implements UserGroupBo {

    @Autowired
    private UserGroupDao userGroupDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserGroupMenuPermissionDao userGroupMenuPermissionDao;

    @Override
    public List<Dto> getAllUserGroup(Dto dtoInput) {
        
        // No validation
        
        List<UserGroupEntity> list = userGroupDao.findAll();
        List<Dto> listDto = new ArrayList<Dto>();
        for (UserGroupEntity userGroup : list) {
            Dto dtoUserGroup = DtoUtils.toDto(userGroup);
            Dto dto = DtoUtils.omit(dtoUserGroup, "lowerName", "createdAt", "modifiedAt");
            listDto.add(dto);
        }
        return listDto;
    }

    @Override
    public Dto getOneUserGroup(Dto dtoInput) throws DataException {
        
        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "id");
        
        String strId = dtoInput.getStringValue("id");
        
        // Validate values
        DataValidation.validateNumeric(strId, "User Group ID");
        
        UserGroupEntity userGroup = userGroupDao.findById(Long.valueOf(strId));
        if (userGroup == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }
        Dto dtoUserGroup = DtoUtils.toDto(userGroup);
        Dto dto = DtoUtils.omit(dtoUserGroup, "lowerName", "createdAt", "modifiedAt");
        return dto;
    }

    @Override
    public Dto getOneUserGroupWithMenuPermissions(Dto dtoInput) throws DataException {
        
        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "id");
        
        String strId = dtoInput.getStringValue("id");
        
        // Validate values
        DataValidation.validateNumeric(strId, "User Group ID");
        
        List<Object[]> list = userGroupDao.findByIdJoinMenuPermissions(Long.valueOf(strId));
        if ((list == null) || list.isEmpty()) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }
        
        UserGroupEntity userGroup = (UserGroupEntity) list.get(0)[0];
        Dto dtoUserGroup = DtoUtils.toDto(userGroup);
        
        List<Dto> listDtoMenuPermission = new ArrayList<Dto>();
        for (Object[] objects : list) {
            UserGroupMenuPermissionEntity menuPermission = (UserGroupMenuPermissionEntity) objects[1];
            if (menuPermission != null) {
                Dto dtoMenuPermission = DtoUtils.toDto(menuPermission);
                listDtoMenuPermission.add(DtoUtils.omit(dtoMenuPermission, "userGroupId", "createdAt", "modifiedAt"));
            }
        }
        
        Dto dto = DtoUtils.omit(dtoUserGroup, "lowerName", "createdAt", "modifiedAt");
        dto.put("menuPermissions", listDtoMenuPermission);
        
        return dto;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Dto addUserGroup(Dto dtoInput) throws DataException {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "userGroup", "menuPermissions");

        String strUserGroup = dtoInput.getStringValue("userGroup");
        String strMenuPermissions = dtoInput.getStringValue("menuPermissions");
        
        // Validate values
        DataValidation.validateJSONObject(strUserGroup, "User Group");
        DataValidation.validateJSONArray(strMenuPermissions, "Menu Permissions");
        
        JSONObject jsonUserGroup = (JSONObject) JSONValue.parse(strUserGroup);
        JSONArray arrMenuPermissions = (JSONArray) JSONValue.parse(strMenuPermissions);
        
        // Validate parameter user group
        DataValidation.containsRequiredData(jsonUserGroup, "name", "active");
        
        String strGroupName = String.valueOf(jsonUserGroup.get("name"));
        String strGroupActive = String.valueOf(jsonUserGroup.get("active"));
        
        // Validate values user group
        DataValidation.validateEmpty(strGroupName, "Name");
        DataValidation.validateYesNo(strGroupActive, "Active");
        
        List<UserGroupMenuPermissionEntity> listMenuPermission = new ArrayList<UserGroupMenuPermissionEntity>();
        
        // Validate parameter menu permissions
        for (Object object : arrMenuPermissions) {
            
            JSONObject jsonMenuPermission = (JSONObject) object;
            
            // Validate dtoInput
            DataValidation.containsRequiredData(jsonMenuPermission, "menuCode", "view", "modify");
            
            String strMenuCode = String.valueOf(jsonMenuPermission.get("menuCode"));
            String strViewPermission = String.valueOf(jsonMenuPermission.get("view"));
            String strModifyPermission = String.valueOf(jsonMenuPermission.get("modify"));
            
            // Validate values
            DataValidation.validateEmpty(strMenuCode, "Menu Code");
            DataValidation.validateYesNo(strViewPermission, "View Permission");
            DataValidation.validateYesNo(strModifyPermission, "Modify Permission");
            
            UserGroupMenuPermissionEntity menuPermission = new UserGroupMenuPermissionEntity();
            menuPermission.setMenuCode(strMenuCode);
            menuPermission.setView(strViewPermission.toLowerCase());
            menuPermission.setModify(strModifyPermission.toLowerCase());
            
            listMenuPermission.add(menuPermission);
        }

        // Find other user group with name
        UserGroupEntity findUserGroup = userGroupDao.findOneByName(strGroupName);
        if (findUserGroup != null) {
            throw new DataException(ExceptionCode.E1003, ErrorMessageConstants.USER_GROUP_ALREADY_EXISTS, new Object[] {strGroupName});
        }

        // Save new user group
        UserGroupEntity userGroup = new UserGroupEntity();
        userGroup.setName(strGroupName);
        userGroup.setActive(strGroupActive.toLowerCase());
        userGroup.setLowerName(strGroupName.toLowerCase());
        UserGroupEntity inserted = userGroupDao.insert(userGroup);
        
        // Save menu permissions
        for (UserGroupMenuPermissionEntity menuPermission : listMenuPermission) {
            menuPermission.setUserGroupId(inserted.getId());
            userGroupMenuPermissionDao.insert(menuPermission);
        }

        Dto dtoUserGroup = DtoUtils.toDto(inserted);
        Dto dto = DtoUtils.omit(dtoUserGroup, "lowerName", "createdAt", "modifiedAt");
        return dto;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Dto editUserGroup(Dto dtoInput) throws DataException {

        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "userGroup", "menuPermissions");

        String strUserGroup = dtoInput.getStringValue("userGroup");
        String strMenuPermissions = dtoInput.getStringValue("menuPermissions");
        
        // Validate values
        DataValidation.validateJSONObject(strUserGroup, "User Group");
        DataValidation.validateJSONArray(strMenuPermissions, "Menu Permissions");
        
        JSONObject jsonUserGroup = (JSONObject) JSONValue.parse(strUserGroup);
        JSONArray arrMenuPermissions = (JSONArray) JSONValue.parse(strMenuPermissions);
        
        // Validate parameter user group
        DataValidation.containsRequiredData(jsonUserGroup, "id", "name", "active");

        String strId = String.valueOf(jsonUserGroup.get("id"));
        String strGroupName = String.valueOf(jsonUserGroup.get("name"));
        String strGroupActive = String.valueOf(jsonUserGroup.get("active"));
        
        // Validate values user group
        DataValidation.validateNumeric(strId, "User Group ID");
        DataValidation.validateEmpty(strId, "Name");
        DataValidation.validateYesNo(strGroupActive, "Active");
        
        List<UserGroupMenuPermissionEntity> listMenuPermission = new ArrayList<UserGroupMenuPermissionEntity>();
        
        // Validate parameter menu permissions
        for (Object object : arrMenuPermissions) {
            
            JSONObject jsonMenuPermission = (JSONObject) object;
            
            // Validate dtoInput
            DataValidation.containsRequiredData(jsonMenuPermission, "menuCode", "view", "modify");
            
            String strMenuCode = String.valueOf(jsonMenuPermission.get("menuCode"));
            String strViewPermission = String.valueOf(jsonMenuPermission.get("view"));
            String strModifyPermission = String.valueOf(jsonMenuPermission.get("modify"));
            
            // Validate values
            DataValidation.validateEmpty(strMenuCode, "Menu Code");
            DataValidation.validateYesNo(strViewPermission, "View Permission");
            DataValidation.validateYesNo(strModifyPermission, "Modify Permission");
            
            UserGroupMenuPermissionEntity menuPermission = new UserGroupMenuPermissionEntity();
            menuPermission.setMenuCode(strMenuCode);
            menuPermission.setView(strViewPermission.toLowerCase());
            menuPermission.setModify(strModifyPermission.toLowerCase());
            
            listMenuPermission.add(menuPermission);
        }

        Long userGroupId = Long.valueOf(strId);
        
        // Find by ID
        UserGroupEntity findUserGroupById = userGroupDao.findById(userGroupId);
        if (findUserGroupById == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }

        // Find another user group having the same name
        UserGroupEntity findUserGroupByName = userGroupDao.findOneByName(strGroupName);
        if ((findUserGroupByName != null) && (!Objects.equals(findUserGroupByName.getId(), userGroupId))) {
            throw new DataException(ExceptionCode.E1003, ErrorMessageConstants.USER_GROUP_ALREADY_EXISTS, new Object[] {strGroupName});
        }

        findUserGroupById.setName(strGroupName);
        findUserGroupById.setActive(strGroupActive.toLowerCase());
        findUserGroupById.setLowerName(strGroupName.toLowerCase());
        
        // Update user group
        UserGroupEntity updated = userGroupDao.update(findUserGroupById);

        // Remove all menus for this user group
        List<UserGroupMenuPermissionEntity> findListUserGroupMenuPermission = userGroupMenuPermissionDao.findByUserGroupId(userGroupId);
        if (findListUserGroupMenuPermission != null) {
            for (UserGroupMenuPermissionEntity entity : findListUserGroupMenuPermission) {
                userGroupMenuPermissionDao.delete(entity);
            }
        }

        // Save menus
        for (UserGroupMenuPermissionEntity menuPermission : listMenuPermission) {
            menuPermission.setUserGroupId(userGroupId);
            userGroupMenuPermissionDao.insert(menuPermission);
        }

        Dto dtoUserGroup = DtoUtils.toDto(updated);
        Dto dto = DtoUtils.omit(dtoUserGroup, "lowerName", "createdAt", "modifiedAt");
        return dto;
    }

    @Override
    public void removeUserGroup(Dto dtoInput) throws DataException {
        
        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "id");
        
        String strId = dtoInput.getStringValue("id");
        
        // Validate values
        DataValidation.validateNumeric(strId, "User Group ID");
        
        Long userGroupId = Long.valueOf(strId);
        
        // Find by ID
        UserGroupEntity findUserGroupById = userGroupDao.findById(userGroupId);
        if (findUserGroupById == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }

        // Find users in this group
        List<UserEntity> userList = userDao.findByUserGroupId(userGroupId);
        if (userList != null && userList.size() > 0) {
            throw new DataException(ExceptionCode.E1002, ErrorMessageConstants.CANT_REMOVE_USER_GROUP_CAUSE_USER_EXISTS, new Object[] {findUserGroupById.getName(), userList.size()});
        }

        // Remove menus for this user group first to avoid foreign key constraint violation
        List<UserGroupMenuPermissionEntity> findListUserGroupMenuPermission = userGroupMenuPermissionDao.findByUserGroupId(userGroupId);
        if (findListUserGroupMenuPermission != null) {
            for (UserGroupMenuPermissionEntity entity : findListUserGroupMenuPermission) {
                userGroupMenuPermissionDao.delete(entity);
            }
        }

        // Remove user group
        userGroupDao.delete(findUserGroupById);

    }

}
