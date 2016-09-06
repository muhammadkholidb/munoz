package hu.pe.munoz.commonrest.controller.settings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.commondata.bo.UserBo;
import hu.pe.munoz.commondata.helper.Dto;
import hu.pe.munoz.commondata.helper.DtoUtils;
import hu.pe.munoz.commonrest.controller.BaseController;
import hu.pe.munoz.commonrest.helper.ResponseWrapper;

@RestController
@RequestMapping("/settings")
public class UserController extends BaseController {
    
    @Autowired
    private UserBo userBo;

    @RequestMapping(value = "/user/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper getAllUser() throws Exception {
        
        List<Dto> list = userBo.getAllUserWithGroup(null);
        
        return new ResponseWrapper(CommonConstants.SUCCESS, list);
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper addUser() throws Exception {
        
        Dto added = userBo.addUser(DtoUtils.fromServletRequest(request));
        
        return new ResponseWrapper(CommonConstants.SUCCESS, added, getResponseMessage("success.SuccessfullyAddUser"));
    }

    @RequestMapping(value = "/user/remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper removeUser() throws Exception {
        
        userBo.removeUser(DtoUtils.fromServletRequest(request));
        
        return new ResponseWrapper(CommonConstants.SUCCESS, getResponseMessage("success.SuccessfullyRemoveUser"));
    }

    @RequestMapping(value = "/user/find", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper getUser() throws Exception {
        
        Dto user = userBo.getUserById(DtoUtils.fromServletRequest(request));
        
        return new ResponseWrapper(CommonConstants.SUCCESS, user);
    }

    @RequestMapping(value = "/user/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper editUser() throws Exception {
        
        Dto updated = userBo.editUser(DtoUtils.fromServletRequest(request));
        
        return new ResponseWrapper(CommonConstants.SUCCESS, updated, getResponseMessage("success.SuccessfullyEditUser"));
    }

}
