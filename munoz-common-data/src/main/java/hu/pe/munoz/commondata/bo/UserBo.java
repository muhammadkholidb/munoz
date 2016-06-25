package hu.pe.munoz.commondata.bo;

import java.util.List;

import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.commondata.entity.UserEntity;

public interface UserBo {

    List<UserEntity> getAllUser();

    List<UserEntity> getUserListByUserGroupId(Long userGroupId);

    UserEntity addUser(UserEntity userEntity) throws DataException;

    UserEntity editUser(UserEntity userEntity) throws DataException;

    void removeUser(Long id) throws DataException;

	UserEntity getUser(Long id) throws DataException;
    
	UserEntity getUser(String email, String username) throws DataException;

	UserEntity getUserByEmail(String email) throws DataException;

	UserEntity getUserByUsername(String username) throws DataException;

}
