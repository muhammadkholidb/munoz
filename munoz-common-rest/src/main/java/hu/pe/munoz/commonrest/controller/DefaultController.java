package hu.pe.munoz.commonrest.controller;

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
import hu.pe.munoz.commondata.helper.Dto;
import hu.pe.munoz.commondata.helper.DtoUtils;
import hu.pe.munoz.commonrest.helper.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class DefaultController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultController.class);
    
    @Autowired
    private UserBo userBo;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper login() throws Exception {

        Dto dtoUser = userBo.login(DtoUtils.fromServletRequest(request));

        return new ResponseWrapper(CommonConstants.SUCCESS, dtoUser);
    }

}
