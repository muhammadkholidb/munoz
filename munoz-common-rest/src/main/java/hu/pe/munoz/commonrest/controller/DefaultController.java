package hu.pe.munoz.commonrest.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.common.exception.ExceptionCode;
import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.commondata.ErrorMessageConstants;
import hu.pe.munoz.commondata.bo.UserBo;
import hu.pe.munoz.commondata.entity.UserEntity;
import hu.pe.munoz.commonrest.ResponseWrapper;
import hu.pe.munoz.commonrest.pojo.login.LoginUser;

@RestController
public class DefaultController extends BaseController {
	
	@Autowired
	private UserBo userBo;

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseWrapper<LoginUser> login(
			@RequestParam(value = "username", required =  false) String username,
			@RequestParam(value = "password", required =  false) String password) throws Exception {
		
    	ResponseWrapper<LoginUser> wrapper = new ResponseWrapper<LoginUser>();
    	if (username == null) {
			throw new DataException(ExceptionCode.E0001, ErrorMessageConstants.USER_NOT_FOUND);
		}
		UserEntity userEntity = userBo.getUser(username, username);
		String hash = DigestUtils.sha1Hex(password);
		if (!hash.equals(userEntity.getPassword())) {
			throw new DataException(ExceptionCode.E0001, ErrorMessageConstants.USER_NOT_FOUND);
		}
		
		LoginUser loginUser = mapper.map(userEntity, LoginUser.class);
		
		wrapper.setStatus(CommonConstants.SUCCESS);
		wrapper.setData(loginUser);
		return wrapper;
	}
	
}
