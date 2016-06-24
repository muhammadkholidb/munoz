package hu.pe.munoz.commondata.bo;

import java.util.List;

import hu.pe.munoz.commondata.entity.UserGroupMenuPermissionEntity;

public interface UserGroupMenuPermissionBo {

	List<UserGroupMenuPermissionEntity> getAllUserGroupMenuPermission();

	List<UserGroupMenuPermissionEntity> getUserGroupMenuPermissionListByUserGroupId(Long userGroupId);

}
