package hu.pe.munoz.commondata.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hu.pe.munoz.commondata.entity.UserEntity;

@Repository
public class UserDaoImpl extends GenericDaoImpl<UserEntity> implements UserDao {

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findAllJoinUserGroup() {
        Query query = em.createQuery("SELECT a, b FROM UserEntity a, UserGroupEntity b WHERE a.userGroupId = b.id ORDER BY a.id ");
        List<Object[]> list = query.getResultList();
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UserEntity> findByUserGroupId(Long userGroupId) {
        Query query = em.createQuery("SELECT e FROM UserEntity e WHERE e.userGroupId = :userGroupId ");
        query.setParameter("userGroupId", userGroupId);
        List<UserEntity> list = query.getResultList();
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object[] findOneByEmailOrUsernameJoinUserGroup(String email, String username) {
        Query query = em.createQuery("SELECT a, b "
                + " FROM UserEntity a, UserGroupEntity b "
                + " WHERE a.userGroupId = b.id "
                + " AND (a.lowerUsername = LOWER(:username) OR a.lowerEmail = LOWER(:email)) ").setMaxResults(1);
        query.setParameter("username", username);
        query.setParameter("email", email);
        List<Object[]> list = query.getResultList();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public UserEntity findOneByEmailOrUsername(String email, String username) {
        Query query = em.createQuery("SELECT a FROM UserEntity a WHERE a.lowerUsername = LOWER(:username) OR a.lowerEmail = LOWER(:email) ").setMaxResults(1);
        query.setParameter("username", username);
        query.setParameter("email", email);
        List<UserEntity> list = query.getResultList();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public UserEntity findOneByUsername(String username) {
        Query query = em.createQuery("SELECT e FROM UserEntity e WHERE e.lowerUsername = LOWER(:username)").setMaxResults(1);
        query.setParameter("username", username);
        List<UserEntity> list = query.getResultList();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public UserEntity findOneByEmail(String email) {
        Query query = em.createQuery("SELECT e FROM UserEntity e WHERE e.lowerEmail = LOWER(:email)").setMaxResults(1);
        query.setParameter("email", email);
        List<UserEntity> list = query.getResultList();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

}
