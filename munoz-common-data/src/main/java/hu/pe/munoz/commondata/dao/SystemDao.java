package hu.pe.munoz.commondata.dao;

import hu.pe.munoz.commondata.entity.SystemEntity;

public interface SystemDao extends GenericDao<SystemEntity> {

    SystemEntity findOneByDataKey(String dataKey);
}
