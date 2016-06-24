package hu.pe.munoz.commonrest.controller.settings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.commondata.bo.UserBo;
import hu.pe.munoz.commondata.entity.UserEntity;
import hu.pe.munoz.commonrest.ResponseWrapper;
import hu.pe.munoz.commonrest.controller.BaseController;

@RestController
@RequestMapping("/settings")
public class UserController extends BaseController {
    
    @Autowired
    private UserBo userBo;

    @RequestMapping(value = "/user/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper<List<UserEntity>> getAllUser() {
        List<UserEntity> list = userBo.getAllUser();
        return new ResponseWrapper<List<UserEntity>>(CommonConstants.SUCCESS, list, "");
    }
    
}
