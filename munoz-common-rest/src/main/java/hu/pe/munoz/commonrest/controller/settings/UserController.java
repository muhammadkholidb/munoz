package hu.pe.munoz.commonrest.controller.settings;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.commondata.bo.UserBo;
import hu.pe.munoz.commondata.entity.UserEntity;
import hu.pe.munoz.commonrest.controller.BaseController;
import hu.pe.munoz.commonrest.helper.ResponseWrapper;
import hu.pe.munoz.commonrest.pojo.settings.User;
import hu.pe.munoz.commonrest.pojo.settings.UserWithPassword;

@RestController
@RequestMapping("/settings")
public class UserController extends BaseController {
    
    @Autowired
    private UserBo userBo;

    @RequestMapping(value = "/user/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper<List<User>> getAllUser() throws Exception {
        List<UserEntity> listUserEntity = userBo.getAllUser();
        List<User> list = new ArrayList<User>();
        for (UserEntity entity : listUserEntity) {
            User user = mapper.map(entity, User.class);
            list.add(user);
        }
        return new ResponseWrapper<List<User>>(CommonConstants.SUCCESS, list, "");
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper<User> addUser(@RequestParam(value = "user") String strUser) throws Exception {
        
        JSONObject jsonUser = (JSONObject) JSONValue.parse(strUser);
        
        UserEntity userEntity = mapper.map(jsonUser, UserEntity.class);
        
        UserEntity added = userBo.addUser(userEntity);
        User user = mapper.map(added, User.class);
        
        return new ResponseWrapper<User>(CommonConstants.SUCCESS, user, getResponseMessage("success.SuccessfullyAddUser"));
    }

    @RequestMapping(value = "/user/remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper<Object> removeUserGroup(@RequestParam(value = "userId") Long userId) throws Exception {
        userBo.removeUser(userId);
        return new ResponseWrapper<Object>(CommonConstants.SUCCESS, getResponseMessage("success.SuccessfullyRemoveUser"));
    }

    @RequestMapping(value = "/user/find", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper<UserWithPassword> getUserGroup(@RequestParam(value = "userId") Long userId) throws Exception {
        UserEntity entity = userBo.getUser(userId);
        UserWithPassword user= mapper.map(entity, UserWithPassword.class);
        return new ResponseWrapper<UserWithPassword>(CommonConstants.SUCCESS, user);
    }

    @RequestMapping(value = "/user/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper<User> editUser(@RequestParam(value = "user") String strUser) throws Exception {
        
        JSONObject jsonUser = (JSONObject) JSONValue.parse(strUser);
        
        UserEntity entity = mapper.map(jsonUser, UserEntity.class);
        
        UserEntity updated = userBo.editUser(entity);
        User user = mapper.map(updated, User.class);
        
        return new ResponseWrapper<User>(CommonConstants.SUCCESS, user, getResponseMessage("success.SuccessfullyEditUser"));
    }

}
