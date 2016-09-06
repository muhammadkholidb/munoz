package hu.pe.munoz.commonrest.controller.settings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.commondata.bo.SystemBo;
import hu.pe.munoz.commondata.helper.Dto;
import hu.pe.munoz.commondata.helper.DtoUtils;
import hu.pe.munoz.commonrest.controller.BaseController;
import hu.pe.munoz.commonrest.helper.ResponseWrapper;

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

    @Autowired
    private SystemBo systemBo;

    @RequestMapping(value = "/system/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper getAllSystem() throws Exception {
        
        List<Dto> list = systemBo.getAllSystem(null);
        
        return new ResponseWrapper(CommonConstants.SUCCESS, list);
    }

    @RequestMapping(value = "/system/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper editSystemList() throws Exception {

        List<Dto> list = systemBo.editSystemList(DtoUtils.fromServletRequest(request));

        return new ResponseWrapper(CommonConstants.SUCCESS, list, getResponseMessage("success.SuccessfullyEditSystem"));
    }

}
