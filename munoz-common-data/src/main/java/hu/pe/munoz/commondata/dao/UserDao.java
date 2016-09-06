package hu.pe.munoz.commondata.dao;

import java.util.List;

import hu.pe.munoz.commondata.entity.UserEntity;

public interface UserDao extends GenericDao<UserEntity> {

    List<Object[]> findAllJoinUserGroup();
    
    List<UserEntity> findByUserGroupId(Long userGroupId);

    Object[] findOneByEmailOrUsernameJoinUserGroup(String email, String username);
    
    UserEntity findOneByEmailOrUsername(String email, String username);
    
    UserEntity findOneByUsername(String username);
    
    UserEntity findOneByEmail(String email);
    
}
