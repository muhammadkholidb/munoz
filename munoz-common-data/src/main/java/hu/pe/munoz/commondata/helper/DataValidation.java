/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.pe.munoz.commondata.helper;

import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.common.exception.ExceptionCode;
import hu.pe.munoz.commondata.ErrorMessageConstants;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author eatonmunoz
 */
public class DataValidation {
    
    public static void containsRequiredData(Map map, String... keys) throws DataException {
        for (String key : keys) {
            if (!map.containsKey(key) || (map.get(key) == null)) {
                throw new DataException(ExceptionCode.E1004, ErrorMessageConstants.REQUIRED_PARAMETER_NOT_FOUND, new Object[] {key});
            }    
        }
    }
    
    public static void validateEmail(String email) throws DataException {
        if (!GenericValidator.isEmail(email)) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_EMAIL_ADDRESS, new Object[] {email});
        }
    }
    
    public static void validateUsername(String username) throws DataException {
        if ((username == null) || username.isEmpty()) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.EMPTY_USERNAME, new Object[] {username});
        }
        if (username.length() < 4) {
            throw new DataException(ExceptionCode.E1006, ErrorMessageConstants.USERNAME_TOO_SHORT, new Object[] {username});
        }    
        
    }
    
    public static void validateNumeric(String string, String name) throws DataException {
        if (!StringUtils.isNumeric(string)) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_NUMERIC, new Object[] {string, name});
        }
    }
    
    public static void validateYesNo(String string, String name) throws DataException {
        if ((string == null) || string.isEmpty() || (!"y".equalsIgnoreCase(string) && !"n".equalsIgnoreCase(string))) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_YES_NO, new Object[] {string, name});
        }
    }
    
    public static void validateJSONArray(String string, String name) throws DataException {
        try {
            if ((string == null) || string.isEmpty() || !(JSONValue.parseWithException(string) instanceof JSONArray)) throw new Exception();
        } catch (Exception e) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_JSON_ARRAY, new Object[] {string, name});
        }
    }
    
    public static void validateJSONObject(String string, String name) throws DataException {
        try {
            if ((string == null) || string.isEmpty() || !(JSONValue.parseWithException(string) instanceof JSONObject)) throw new Exception();
        } catch (Exception e) {
            throw new DataException(ExceptionCode.E1005, ErrorMessageConstants.INVALID_JSON_OBJECT, new Object[] {string, name});
        }
    }
    
    public static void validateEmpty(String string, String name) throws DataException {
        if ((string == null) || string.isEmpty()) {
            throw new DataException(ExceptionCode.E1006, ErrorMessageConstants.EMPTY_VALUE, new Object[] {name});
        }
    }
    
}
