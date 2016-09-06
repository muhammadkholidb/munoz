package hu.pe.munoz.commondata.bo;


import hu.pe.munoz.common.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.pe.munoz.commondata.dao.UserGroupMenuPermissionDao;
import hu.pe.munoz.commondata.entity.UserGroupMenuPermissionEntity;
import hu.pe.munoz.commondata.helper.DataValidation;
import hu.pe.munoz.commondata.helper.Dto;
import hu.pe.munoz.commondata.helper.DtoUtils;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserGroupMenuPermissionBoImpl implements UserGroupMenuPermissionBo {

    @Autowired
    private UserGroupMenuPermissionDao userGroupMenuPermissionDao;

    @Override
    public List<Dto> getAllUserGroupMenuPermission(Dto dtoInput) {
        
        // No validation
        
        List<UserGroupMenuPermissionEntity> list = userGroupMenuPermissionDao.findAll();
        List<Dto> listDto = new ArrayList<Dto>();
        for (UserGroupMenuPermissionEntity menuPermission : list) {
            Dto dtoMenuPermission = DtoUtils.toDto(menuPermission);
            Dto dto = DtoUtils.omit(dtoMenuPermission, "createdAt", "modifiedAt");
            listDto.add(dto);
        }
        
        return listDto;
    }

    @Override
    public List<Dto> getUserGroupMenuPermissionListByUserGroupId(Dto dtoInput) throws DataException {
        
        // Validate parameters
        DataValidation.containsRequiredData(dtoInput, "userGroupId");
        
        String strUserGroupId = dtoInput.getStringValue("userGroupId");
        
        // Validate values
        DataValidation.validateNumeric(strUserGroupId, "User Group ID");
        
        List<UserGroupMenuPermissionEntity> list = userGroupMenuPermissionDao.findByUserGroupId(Long.valueOf(strUserGroupId));
        List<Dto> listDto = new ArrayList<Dto>();
        for (UserGroupMenuPermissionEntity menuPermission : list) {
            Dto dtoMenuPermission = DtoUtils.toDto(menuPermission);
            Dto dto = DtoUtils.omit(dtoMenuPermission, "createdAt", "modifiedAt");
            listDto.add(dto);
        }
        
        return listDto;
    }

}
