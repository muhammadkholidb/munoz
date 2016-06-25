package hu.pe.munoz.commondata.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hu.pe.munoz.commondata.entity.UserEntity;

@Repository
public class UserDaoImpl extends GenericDaoImpl<UserEntity> implements UserDao {

    @SuppressWarnings("unchecked")
    @Override
    public List<UserEntity> findByUserGroupId(Long userGroupId) {
        Query query = em.createQuery("FROM UserEntity e WHERE e.userGroup.id = :userGroupId ");
        query.setParameter("userGroupId", userGroupId);
        List<UserEntity> list = query.getResultList();
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public UserEntity findByEmailOrUsername(String email, String username) {
        Query query = em.createQuery("SELECT a FROM UserEntity a "
        		+ " INNER JOIN FETCH a.userGroup b "
        		+ " INNER JOIN FETCH b.userGroupMenuPermissions c "
        		+ " WHERE a.usernameUpper = UPPER(:username) OR a.emailUpper = UPPER(:email) ").setMaxResults(1);
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
    public List<UserEntity> findAllWithUserGroup() {
        Query query = em.createQuery("FROM UserEntity e INNER JOIN FETCH e.userGroup ");
        List<UserEntity> list = query.getResultList();
        return list;
    }

	@SuppressWarnings("unchecked")
	@Override
	public UserEntity findByUsername(String username) {
		Query query = em.createQuery("FROM UserEntity e WHERE e.usernameUpper = UPPER(:username)").setMaxResults(1);
        query.setParameter("username", username);
        List<UserEntity> list = query.getResultList();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserEntity findByEmail(String email) {
		Query query = em.createQuery("FROM UserEntity e WHERE e.emailUpper = UPPER(:email)").setMaxResults(1);
        query.setParameter("email", email);
        List<UserEntity> list = query.getResultList();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
	}

}
