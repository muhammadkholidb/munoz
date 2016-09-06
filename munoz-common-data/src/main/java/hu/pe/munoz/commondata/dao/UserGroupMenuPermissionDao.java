package hu.pe.munoz.commondata.dao;

import java.util.List;

import hu.pe.munoz.commondata.entity.UserGroupMenuPermissionEntity;

public interface UserGroupMenuPermissionDao extends GenericDao<UserGroupMenuPermissionEntity> {

    List<UserGroupMenuPermissionEntity> findByUserGroupId(Long userGroupId);

}
