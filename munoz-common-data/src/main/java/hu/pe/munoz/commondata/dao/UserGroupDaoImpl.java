package hu.pe.munoz.commondata.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hu.pe.munoz.commondata.entity.UserGroupEntity;

@Repository
public class UserGroupDaoImpl extends GenericDaoImpl<UserGroupEntity> implements UserGroupDao {

    @Override
    public UserGroupEntity findOneByName(String name) {
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
    public List<Object[]> findByIdJoinMenuPermissions(Long id) {
        Query query = em.createQuery("SELECT a, b FROM UserGroupEntity a LEFT JOIN UserGroupMenuPermissionEntity b ON b.userGroupId = a.id WHERE a.id = :id ");
        query.setParameter("id", id);
        List<Object[]> list = query.getResultList();
        return list;
    }

}
