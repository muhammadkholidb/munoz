package hu.pe.munoz.commondata.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hu.pe.munoz.commondata.entity.UserGroupMenuPermissionEntity;

@Repository
public class UserGroupMenuPermissionDaoImpl extends GenericDaoImpl<UserGroupMenuPermissionEntity> implements UserGroupMenuPermissionDao {

    @SuppressWarnings("unchecked")
	@Override
    public List<UserGroupMenuPermissionEntity> findByUserGroupId(Long userGroupId) {
        Query query = em.createNamedQuery("USER_GROUP_MENU_PERMISSION.FIND_BY_USER_GROUP_ID");
        query.setParameter("userGroupId", userGroupId);
        return query.getResultList();
    }
    
}
