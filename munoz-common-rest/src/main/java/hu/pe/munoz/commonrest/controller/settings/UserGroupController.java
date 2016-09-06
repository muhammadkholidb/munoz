package hu.pe.munoz.commonrest.controller.settings;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.commondata.bo.UserGroupBo;
import hu.pe.munoz.commondata.helper.Dto;
import hu.pe.munoz.commondata.helper.DtoUtils;
import hu.pe.munoz.commonrest.controller.BaseController;
import hu.pe.munoz.commonrest.helper.ResponseWrapper;

@RestController
@RequestMapping("/settings")
public class UserGroupController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(UserGroupController.class);

    @Autowired
    private UserGroupBo userGroupBo;

    @RequestMapping(value = "/user-group/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper getAllUserGroup() {
        
        List<Dto> list = userGroupBo.getAllUserGroup(null);
        
        return new ResponseWrapper(CommonConstants.SUCCESS, list);
    }

    @RequestMapping(value = "/user-group/find", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper getUserGroup() throws Exception {
        
        Dto dto = userGroupBo.getOneUserGroupWithMenuPermissions(DtoUtils.fromServletRequest(request));
        
        return new ResponseWrapper(CommonConstants.SUCCESS, dto);
    }

    @RequestMapping(value = "/user-group/remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper removeUserGroup() throws Exception {
        
        userGroupBo.removeUserGroup(DtoUtils.fromServletRequest(request));
        
        return new ResponseWrapper(CommonConstants.SUCCESS, getResponseMessage("success.SuccessfullyRemoveUserGroup"));
    }

    @RequestMapping(value = "/user-group/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper addUserGroup() throws Exception {

        Dto added = userGroupBo.addUserGroup(DtoUtils.fromServletRequest(request));
        
        return new ResponseWrapper(CommonConstants.SUCCESS, added, getResponseMessage("success.SuccessfullyAddUserGroup"));
    }

    @RequestMapping(value = "/user-group/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper editUserGroup() throws Exception {

        Dto updated = userGroupBo.editUserGroup(DtoUtils.fromServletRequest(request));
        
        return new ResponseWrapper(CommonConstants.SUCCESS, updated, getResponseMessage("success.SuccessfullyEditUserGroup"));
    }

}
