/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.pe.munoz.commondata.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

/**
 *
 * @author eatonmunoz
 */
public class DtoUtils {

    private static final Mapper MAPPER = new DozerBeanMapper();
    
    public static Dto fromServletRequest(ServletRequest request) {
        if (request == null) return null;
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
    
    public static List<Dto> toDtoList(List<?> list) {
        if (list == null) return null;
        List<Dto> listDto = new ArrayList<Dto>();
        for (Object o : list) {
            if (o == null) continue;
            listDto.add(MAPPER.map(o, Dto.class));
        }
        return listDto;
    }
    
    public static List<Dto> toDtoList(List<?> list, String... excludeKeys) {
        if (list == null) return null;
        List<Dto> listDto = new ArrayList<Dto>();
        for (Object o : list) {
            if (o == null) continue;
            listDto.add(omit(MAPPER.map(o, Dto.class), excludeKeys));
        }
        return listDto;
    }
    
    public static Dto toDto(Object object) {
        if (object == null) return null;
        return MAPPER.map(object, Dto.class);
    }
    
    public static Dto toDto(Object object, String... excludeKeys) {
        if (object == null) return null;
        return omit(MAPPER.map(object, Dto.class), excludeKeys);
    }
    
    public static <T> T toObject(Dto dto, Class<T> t) {
        if (dto == null) return null;
        return MAPPER.map(dto, t);
    }
    
    public static Dto omit(Dto dto, String... keys) {
        for (String key : keys) {
            dto.remove(key);
        }
        return dto;
    }
    
}
