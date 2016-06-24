package hu.pe.munoz.commondata.dao;

import hu.pe.munoz.commondata.entity.UserGroupEntity;

public interface UserGroupDao extends GenericDao<UserGroupEntity> {

    UserGroupEntity findByName(String name);

	UserGroupEntity findByIdWithMenuPermissions(Long id);

}
