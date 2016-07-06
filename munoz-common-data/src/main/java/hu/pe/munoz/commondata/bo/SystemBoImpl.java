package hu.pe.munoz.commondata.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.common.exception.ExceptionCode;
import hu.pe.munoz.commondata.ErrorMessageConstants;
import hu.pe.munoz.commondata.dao.SystemDao;
import hu.pe.munoz.commondata.entity.SystemEntity;

@Service
@Transactional
public class SystemBoImpl implements SystemBo {

    @Autowired
    private SystemDao systemDao;
    
    @Override
    public List<SystemEntity> getAllSystem() {
        return systemDao.findAll();
    }

    @Override
    public SystemEntity getSystemByKey(String key) throws DataException {
        SystemEntity systemEntity = systemDao.findByKey(key);
        if (systemEntity == null) {
            throw new DataException(ExceptionCode.D0001, ErrorMessageConstants.SYSTEM_NOT_FOUND, new Object[] {key});
        }
        return systemEntity; 
    }

    @Override
    public SystemEntity getOneSystem(Long id) throws DataException {
        SystemEntity systemEntity = systemDao.findById(id);
        if (systemEntity == null) {
            throw new DataException(ExceptionCode.D0001, ErrorMessageConstants.SYSTEM_NOT_FOUND);
        }
        return systemEntity;
    }

    @Override
    public List<SystemEntity> editSystemList(List<SystemEntity> systemList) throws DataException {
        List<SystemEntity> updatedList = new ArrayList<SystemEntity>();
        for (SystemEntity system : systemList) {
            SystemEntity findSystem = systemDao.findById(system.getId());
            if (findSystem == null) {
                throw new DataException(ExceptionCode.D0001, ErrorMessageConstants.SYSTEM_NOT_FOUND, new Object[] { system.getKey() });
            }
            updatedList.add(systemDao.update(system));
        }
        return updatedList;
    }
    
}
