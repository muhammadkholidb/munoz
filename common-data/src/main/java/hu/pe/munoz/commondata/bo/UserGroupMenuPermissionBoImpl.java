package hu.pe.munoz.commondata.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.pe.munoz.commondata.dao.UserGroupMenuPermissionDao;
import hu.pe.munoz.commondata.entity.UserGroupMenuPermissionEntity;

@Service
@Transactional
public class UserGroupMenuPermissionBoImpl implements UserGroupMenuPermissionBo {

	@Autowired
	private UserGroupMenuPermissionDao userGroupMenuPermissionDao;

	@Override
	public List<UserGroupMenuPermissionEntity> getAllUserGroupMenuPermission() {
		return userGroupMenuPermissionDao.findAll();
	}

	@Override
	public List<UserGroupMenuPermissionEntity> getUserGroupMenuPermissionListByUserGroupId(Long userGroupId) {
		return userGroupMenuPermissionDao.findByUserGroupId(userGroupId);
	}

}
