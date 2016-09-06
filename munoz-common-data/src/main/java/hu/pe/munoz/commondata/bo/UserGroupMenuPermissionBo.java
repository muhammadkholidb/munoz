package hu.pe.munoz.commondata.bo;

import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.commondata.helper.Dto;
import java.util.List;

public interface UserGroupMenuPermissionBo {

    List<Dto> getAllUserGroupMenuPermission(Dto dtoInput);

    List<Dto> getUserGroupMenuPermissionListByUserGroupId(Dto dtoInput) throws DataException ;

}
