package hu.pe.munoz.commonrest.controller.settings;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.commondata.bo.UserGroupBo;
import hu.pe.munoz.commondata.entity.UserGroupEntity;
import hu.pe.munoz.commonrest.ResponseWrapper;

/**
 * 
 * 
 * To Read:
 * https://www.genuitec.com/spring-frameworkrestcontroller-vs-controller/
 * 
 */
@RestController
@RequestMapping("/settings")
public class UserGroupController {
    
    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private UserGroupBo userGroupBo;

    @RequestMapping(value = "/user-group/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper<List<UserGroupEntity>> getAllUserGroup() {
        List<UserGroupEntity> list = userGroupBo.getAllUserGroup();
        return new ResponseWrapper<List<UserGroupEntity>>(CommonConstants.SUCCESS, list, "");
    }
    
    @RequestMapping(value = "/user-group/remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper<UserGroupEntity> removeUserGroup(
    		@RequestParam(value = "userGroupId") Long userGroupId, 
    		@RequestParam(value = "languageCode") String languageCode) {
    	
        ResponseWrapper<UserGroupEntity> wrapper = new ResponseWrapper<UserGroupEntity>();
        try {
            userGroupBo.removeUserGroup(userGroupId);
            wrapper.setStatus(CommonConstants.SUCCESS);
            return wrapper;
        } catch (DataException e) {
            e.printStackTrace();
            String message = messageSource.getMessage(e.getMessage(), e.getData(), new Locale(languageCode));
            wrapper.setStatus(CommonConstants.FAIL);
            wrapper.setMessage(e.getCode() + ": " + message);
            return wrapper;
        } catch (Exception e) {
            e.printStackTrace();
            wrapper.setStatus(CommonConstants.FAIL);
            wrapper.setMessage(e.getMessage());
            return wrapper;
        }
    }
    
}
