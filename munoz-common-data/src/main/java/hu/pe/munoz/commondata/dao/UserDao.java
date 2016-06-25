package hu.pe.munoz.commondata.dao;

import java.util.List;

import hu.pe.munoz.commondata.entity.UserEntity;

public interface UserDao extends GenericDao<UserEntity> {

    List<UserEntity> findAllWithUserGroup();
    
    List<UserEntity> findByUserGroupId(Long userGroupId);

    UserEntity findByEmailOrUsername(String email, String username);
    
    UserEntity findByUsername(String username);
    
    UserEntity findByEmail(String email);
    
}
