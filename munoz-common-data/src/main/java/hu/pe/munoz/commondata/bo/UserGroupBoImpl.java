package hu.pe.munoz.commondata.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.common.exception.ExceptionCode;
import hu.pe.munoz.commondata.ErrorMessageConstants;
import hu.pe.munoz.commondata.dao.UserDao;
import hu.pe.munoz.commondata.dao.UserGroupDao;
import hu.pe.munoz.commondata.dao.UserGroupMenuPermissionDao;
import hu.pe.munoz.commondata.entity.UserEntity;
import hu.pe.munoz.commondata.entity.UserGroupEntity;
import hu.pe.munoz.commondata.entity.UserGroupMenuPermissionEntity;

@Service
@Transactional
public class UserGroupBoImpl implements UserGroupBo {

    @Autowired
    private UserGroupDao userGroupDao;
    
    @Autowired
    private UserDao userDao;

	@Autowired
	private UserGroupMenuPermissionDao userGroupMenuPermissionDao;

    @Override
    public List<UserGroupEntity> getAllUserGroup() {
        return userGroupDao.findAll();
    }

    @Override
    public UserGroupEntity getOneUserGroup(Long id) throws DataException {
        UserGroupEntity userGroup = userGroupDao.findById(id);
        if (userGroup == null) {
            throw new DataException(ExceptionCode.E0001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }
        return userGroup;
    }

    @Override
    public UserGroupEntity getOneUserGroupWithMenuPermissions(Long id) throws DataException {
    	UserGroupEntity userGroup = userGroupDao.findByIdWithMenuPermissions(id);
        if (userGroup == null) {
            throw new DataException(ExceptionCode.E0001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }
        return userGroup;
    }
    
    @Override
    public UserGroupEntity addUserGroup(UserGroupEntity userGroupEntity, List<UserGroupMenuPermissionEntity> listMenuPermission) throws DataException {
    	
    	// Find other user group with name
        UserGroupEntity findUserGroup = userGroupDao.findByName(userGroupEntity.getName());
        if (findUserGroup != null) {
            throw new DataException(ExceptionCode.E0003, ErrorMessageConstants.USER_GROUP_ALREADY_EXISTS, new Object[] { findUserGroup.getName() });
        }
        
        // Save new user group
        UserGroupEntity inserted = userGroupDao.insert(userGroupEntity);
        
		// Save menus
		List<UserGroupMenuPermissionEntity> listAdded = new ArrayList<UserGroupMenuPermissionEntity>();
		for (UserGroupMenuPermissionEntity entity : listMenuPermission) {
			entity.setUserGroup(inserted);
			listAdded.add(userGroupMenuPermissionDao.insert(entity));
		}
        
        return userGroupDao.insert(userGroupEntity);
    }
 
    @Override
    public UserGroupEntity editUserGroup(UserGroupEntity userGroupEntity, List<UserGroupMenuPermissionEntity> listMenuPermission) throws DataException {
        
        // Find by ID
        UserGroupEntity findUserGroupById = userGroupDao.findById(userGroupEntity.getId());
        if (findUserGroupById == null) {
            throw new DataException(ExceptionCode.E0001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }
        
        // Find another user group having the same name
        UserGroupEntity findUserGroupByName = userGroupDao.findByName(userGroupEntity.getName());
        if ((findUserGroupByName != null) && (findUserGroupByName.getId() != userGroupEntity.getId())) {
            throw new DataException(ExceptionCode.E0003, ErrorMessageConstants.USER_GROUP_ALREADY_EXISTS, new Object[] { findUserGroupByName.getName() });
        }
        
        // Update user group
        UserGroupEntity updated = userGroupDao.update(userGroupEntity);

        // Remove all menus for this user group
		List<UserGroupMenuPermissionEntity> findListUserGroupMenuPermission = userGroupMenuPermissionDao.findByUserGroupId(updated.getId());
		if (findListUserGroupMenuPermission != null) {
			for (UserGroupMenuPermissionEntity entity : findListUserGroupMenuPermission) {
				userGroupMenuPermissionDao.delete(entity);
			}
		}

		// Save menus
		List<UserGroupMenuPermissionEntity> listAdded = new ArrayList<UserGroupMenuPermissionEntity>();
		for (UserGroupMenuPermissionEntity entity : listMenuPermission) {
			entity.setUserGroup(updated);
			listAdded.add(userGroupMenuPermissionDao.insert(entity));
		}

        return updated;
    }
    
    @Override
    public void removeUserGroup(Long id) throws DataException {

        // Find by ID
        UserGroupEntity findUserGroupById = userGroupDao.findById(id);
        if (findUserGroupById == null) {
            throw new DataException(ExceptionCode.E0001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }
        
        // Find users in this group
        List<UserEntity> userList = userDao.findByUserGroupId(id);
        if (userList != null && userList.size() > 0) {
            throw new DataException(ExceptionCode.E0002, ErrorMessageConstants.CANT_REMOVE_USER_GROUP_CAUSE_USER_EXISTS, new Object[] {userList.size()});
        }

        // Remove menus for this user group first to avoid foreign key constraint violation
		List<UserGroupMenuPermissionEntity> findListUserGroupMenuPermission = userGroupMenuPermissionDao.findByUserGroupId(id);
		if (findListUserGroupMenuPermission != null) {
			for (UserGroupMenuPermissionEntity entity : findListUserGroupMenuPermission) {
				userGroupMenuPermissionDao.delete(entity);
			}
		}
		
        // Remove user group
        userGroupDao.deleteById(id);

    }
    
}
