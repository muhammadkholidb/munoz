package hu.pe.munoz.commondata.bo;


import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.commondata.helper.Dto;
import java.util.List;

public interface SystemBo {

    List<Dto> getAllSystem(Dto dtoInput) throws DataException;
    
    Dto getSystemByDataKey(Dto dtoInput) throws DataException;
    
    List<Dto> editSystemList(Dto dtoInput) throws DataException;

    Dto getSystemById(Dto dtoInput) throws DataException;
    
}
