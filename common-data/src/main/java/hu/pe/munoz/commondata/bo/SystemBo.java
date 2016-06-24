package hu.pe.munoz.commondata.bo;

import java.util.List;

import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.commondata.entity.SystemEntity;

public interface SystemBo {

    List<SystemEntity> getAllSystem();
    
    SystemEntity getSystemByKey(String key) throws DataException;
    
    List<SystemEntity> editSystemList(List<SystemEntity> systemList) throws DataException;

    SystemEntity getOneSystem(Long id) throws DataException;
    
}
