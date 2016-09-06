package hu.pe.munoz.commondata.bo;


import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.commondata.helper.Dto;
import java.util.List;

public interface UserBo {

    List<Dto> getAllUserWithGroup(Dto dtoInput);

    List<Dto> getUserListByUserGroupId(Dto dtoInput) throws DataException;

    Dto addUser(Dto dtoInput) throws DataException;

    Dto editUser(Dto dtoInput) throws DataException;

    void removeUser(Dto dtoInput) throws DataException;

    Dto getUserById(Dto dtoInput) throws DataException;

    Dto login(Dto dtoInput) throws DataException;

    Dto getUserByEmail(Dto dtoInput) throws DataException;

    Dto getUserByUsername(Dto dtoInput) throws DataException;

}
