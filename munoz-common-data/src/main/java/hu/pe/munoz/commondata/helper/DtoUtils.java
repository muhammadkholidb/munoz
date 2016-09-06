/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.pe.munoz.commondata.helper;

import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

/**
 *
 * @author eatonmunoz
 */
public class DtoUtils {

    private static final Mapper MAPPER = new DozerBeanMapper();
    
    public static Dto fromServletRequest(ServletRequest request) {
        Dto dto = new Dto();
        Map<String, String[]> parameters = request.getParameterMap();
        for (String key : parameters.keySet()) {
            String[] values = parameters.get(key);
            if (values.length == 0) {
                dto.put(key, ""); // Put empty string
            } else if (values.length == 1) {
                dto.put(key, values[0]); // Put the first value
            } else {
                dto.put(key, values); // Put all values
            }
        }
        return dto;
    }
    
    public static Dto toDto(Object object) {
        if (object == null) return null;
        return DtoUtils.MAPPER.map(object, Dto.class);
    }
    
    public static <T> T toObject(Dto dto, Class<T> t) {
        if (dto == null) return null;
        return DtoUtils.MAPPER.map(dto, t);
    }
    
    public static Dto omit(Dto dto, String... keys) {
        for (String key : keys) {
            dto.remove(key);
        }
        return dto;
    }
    
}
