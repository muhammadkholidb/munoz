package hu.pe.munoz.commondata.bo;


import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.commondata.helper.Dto;
import java.util.List;

public interface UserGroupBo {

    List<Dto> getAllUserGroup(Dto dtoInput);

    void removeUserGroup(Dto dtoInput) throws DataException;

    Dto getOneUserGroup(Dto dtoInput) throws DataException;

    Dto getOneUserGroupWithMenuPermissions(Dto dtoInput) throws DataException;

    Dto editUserGroup(Dto dtoInput) throws DataException;

    Dto addUserGroup(Dto dtoInput) throws DataException;

}
