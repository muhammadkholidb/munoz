package hu.pe.munoz.commonrest.controller.settings;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.commondata.bo.SystemBo;
import hu.pe.munoz.commondata.entity.SystemEntity;
import hu.pe.munoz.commonrest.ResponseWrapper;
import hu.pe.munoz.commonrest.controller.BaseController;
import hu.pe.munoz.commonrest.pojo.settings.System;

/**
 * 
 * 
 * To Read:
 * https://www.genuitec.com/spring-frameworkrestcontroller-vs-controller/
 * 
 */
@RestController
@RequestMapping("/settings")
public class SystemController extends BaseController {
    
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    private SystemBo systemBo;
    
    @RequestMapping(value = "/system/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper<List<System>> getAllSystem() {
    	List<System> list = new ArrayList<System>();
        List<SystemEntity> listSystemEntity = systemBo.getAllSystem();
        for (SystemEntity entity : listSystemEntity) {
        	list.add(mapper.map(entity, System.class));
        }
        return new ResponseWrapper<List<System>>(CommonConstants.SUCCESS, list);
    }
    
    @RequestMapping(value = "/system/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper<List<System>> editSystemList(@RequestParam(value = "systems") String strSystems) throws Exception {
        JSONArray systems = (JSONArray) JSONValue.parse(strSystems);
        log.debug("Systems: " + systems);
        
        List<SystemEntity> listSystemEntity = new ArrayList<SystemEntity>();
        for (Object object : systems) {
        	JSONObject system = (JSONObject) object;
        	SystemEntity entity = new SystemEntity();
        	entity.setId((Long) system.get("id"));
        	entity.setKey((String) system.get("key"));
        	entity.setValue((String) system.get("value"));
        	listSystemEntity.add(entity);
        }

        listSystemEntity = systemBo.editSystemList(listSystemEntity);

        List<System> list = new ArrayList<System>();
        for (SystemEntity entity : listSystemEntity) {
        	list.add(mapper.map(entity, System.class));
        }
         
        return new ResponseWrapper<List<System>>(CommonConstants.SUCCESS, list);
    }
    
}
