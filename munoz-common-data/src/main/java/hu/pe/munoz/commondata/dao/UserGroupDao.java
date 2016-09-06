package hu.pe.munoz.commondata.dao;

import hu.pe.munoz.commondata.entity.UserGroupEntity;
import java.util.List;

public interface UserGroupDao extends GenericDao<UserGroupEntity> {

    UserGroupEntity findOneByName(String name);

    List<Object[]> findByIdJoinMenuPermissions(Long id);

}
