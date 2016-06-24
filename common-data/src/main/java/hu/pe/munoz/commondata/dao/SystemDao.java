package hu.pe.munoz.commondata.dao;

import hu.pe.munoz.commondata.entity.SystemEntity;

public interface SystemDao extends GenericDao<SystemEntity> {

    public SystemEntity findByKey(String key);
}
