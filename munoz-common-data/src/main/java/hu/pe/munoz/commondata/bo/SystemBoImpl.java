package hu.pe.munoz.commondata.bo;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.common.exception.ExceptionCode;
import hu.pe.munoz.commondata.ErrorMessageConstants;
import hu.pe.munoz.commondata.dao.SystemDao;
import hu.pe.munoz.commondata.entity.SystemEntity;
import hu.pe.munoz.commondata.helper.DataImporter;
import hu.pe.munoz.commondata.helper.DataValidation;
import hu.pe.munoz.commondata.helper.Dto;
import hu.pe.munoz.commondata.helper.DtoUtils;

@Service
@Transactional(rollbackFor = Exception.class)
public class SystemBoImpl implements SystemBo {

    private static final Logger LOG = LoggerFactory.getLogger(SystemBoImpl.class);
    
    @Autowired
    private SystemDao systemDao;
    
    @Autowired
    protected DataImporter importer;
    
    @Override
    public List<Dto> getAllSystem(Dto dtoInput) throws DataException {
    	LOG.info("Get all system ...");
        
        // No validation
        
        List<SystemEntity> list = systemDao.findAll();
        
//        if ((list == null) || list.isEmpty()) {
//            
//            LOG.debug("System data is empty, load initial data ...");
//            InputStream is = SystemBoImpl.class.getClassLoader().getResourceAsStream("dataset/system.xml");
//            importer.addStreamDataSet(is);
//            importer.process();
//            
//            // Find all again
//            list = systemDao.findAll();
//        }
        
        List<Dto> listDto = new ArrayList<Dto>();
        for (SystemEntity system : list) {
            Dto dtoSystem = DtoUtils.toDto(system);
            Dto dto = DtoUtils.omit(dtoSystem, "createdAt", "modifiedAt");
            listDto.add(dto);
        }
        return listDto;
    }

    @Override
    public Dto getSystemByDataKey(Dto dtoInput) throws DataException {
        
        // Validate parameters
        DataValidation.containsRequiredData(dtoInput, "dataKey");
        
        String strKey = dtoInput.get("dataKey");
        
        SystemEntity systemEntity = systemDao.findOneByDataKey(strKey);
        if (systemEntity == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.SYSTEM_NOT_FOUND, new Object[] {strKey});
        }
        
        Dto dtoSystem = DtoUtils.toDto(systemEntity);
        Dto dto = DtoUtils.omit(dtoSystem, "createdAt", "modifiedAt");
        return dto; 
    }

    @Override
    public Dto getSystemById(Dto dtoInput) throws DataException {
        
        // Validate parameters
        DataValidation.containsRequiredData(dtoInput, "id");
        
        String strId = dtoInput.get("id");
        
        // Validate values
        DataValidation.validateNumeric(strId, "System ID");
        
        SystemEntity systemEntity = systemDao.findById(Long.valueOf(strId));
        if (systemEntity == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.SYSTEM_NOT_FOUND);
        }
        
        Dto dtoSystem = DtoUtils.toDto(systemEntity);
        Dto dto = DtoUtils.omit(dtoSystem, "createdAt", "modifiedAt");
        return dto;
    }

    @Override
    public List<Dto> editSystemList(Dto dtoInput) throws DataException {
        
        // Validate parameters
        DataValidation.containsRequiredData(dtoInput, "systems");
        
        String strSystems = dtoInput.get("systems");
        
        // Validate values
        DataValidation.validateJSONArray(strSystems, "Systems");
        
        JSONArray arrSystems = (JSONArray) JSONValue.parse(strSystems);
        
        List<Dto> updatedList = new ArrayList<Dto>();
        
        for (Object object : arrSystems) {
            
            JSONObject jsonSystem = (JSONObject) object;
            
            // Validate parameters
            DataValidation.containsRequiredData(jsonSystem, "id", "dataValue");
            
            String strId = String.valueOf(jsonSystem.get("id"));
            String strValue = String.valueOf(jsonSystem.get("dataValue"));
            
            // Validate values
            DataValidation.validateNumeric(strId, "System ID");
            DataValidation.validateEmpty(strValue, "Data Value");
            
            SystemEntity findSystem = systemDao.findById(Long.valueOf(strId));
            if (findSystem == null) {
                throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.SYSTEM_NOT_FOUND);
            }
            
            findSystem.setDataValue(strValue);
            
            SystemEntity updated = systemDao.update(findSystem);
            
            Dto dtoSystemUpdated = DtoUtils.toDto(updated);
            Dto dto = DtoUtils.omit(dtoSystemUpdated, "createdAt", "modifiedAt");
            
            updatedList.add(dto);
        }
        return updatedList;
    }
    
}
