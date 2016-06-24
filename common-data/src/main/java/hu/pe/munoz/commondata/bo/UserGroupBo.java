package hu.pe.munoz.commondata.bo;

import java.util.List;
import java.util.Map;

import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.commondata.entity.UserGroupEntity;

public interface UserGroupBo {

    List<UserGroupEntity> getAllUserGroup();

    void removeUserGroup(Long id) throws DataException;

    UserGroupEntity getOneUserGroup(Long id) throws DataException;

	UserGroupEntity getOneUserGroupWithMenuPermissions(Long id) throws DataException;

	UserGroupEntity editUserGroup(UserGroupEntity userGroupEntity, List<Map<String, String>> listMenuPermission) throws DataException;

	UserGroupEntity addUserGroup(UserGroupEntity userGroupEntity, List<Map<String, String>> listMenuPermission) throws DataException;

}