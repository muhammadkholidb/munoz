package hu.pe.munoz.commondata.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hu.pe.munoz.commondata.entity.UserGroupEntity;

@Repository
public class UserGroupDaoImpl extends GenericDaoImpl<UserGroupEntity> implements UserGroupDao {

    @Override
    public UserGroupEntity findByName(String name) {
        Query query = em.createNamedQuery("USER_GROUP.FIND_BY_NAME");
        query.setParameter("name", name);
        try {
            return (UserGroupEntity) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public UserGroupEntity findByIdWithMenuPermissions(Long id) {
    	Query query = em.createQuery("FROM UserGroupEntity a JOIN FETCH a.userGroup b WHERE a.id = :id ").setMaxResults(1);
        query.setParameter("id", id);
        List<UserGroupEntity> list = query.getResultList();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    
}
