package hu.pe.munoz.commondata.dao;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import hu.pe.munoz.commondata.entity.SystemEntity;

@Repository
public class SystemDaoImpl extends GenericDaoImpl<SystemEntity> implements SystemDao {

    @Override
    public SystemEntity findOneByDataKey(String dataKey) {
        Query query = em.createNamedQuery("SYSTEM.FIND_BY_KEY");
        query.setParameter("dataKey", dataKey);
        try {
            return (SystemEntity) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    
}
