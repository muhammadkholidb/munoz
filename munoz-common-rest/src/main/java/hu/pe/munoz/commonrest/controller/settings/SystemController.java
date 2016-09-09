package hu.pe.munoz.commonrest.controller.settings;

import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.commondata.bo.SystemBo;
import hu.pe.munoz.commondata.bo.SystemBoImpl;
import hu.pe.munoz.commondata.helper.DataImporter;
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

	private static final Logger LOG = LoggerFactory.getLogger(SystemController.class);
	
    @Autowired
    private SystemBo systemBo;

    @Autowired
    protected DataImporter importer;
    
    @RequestMapping(value = "/system/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper getAllSystem() throws Exception {
        
        List<Dto> list = systemBo.getAllSystem(null);

        // Import default system data when no data returned
        if ((list == null) || list.isEmpty()) {

            LOG.debug("System data is empty, load initial data ...");
            InputStream is = SystemBoImpl.class.getClassLoader().getResourceAsStream("dataset/system.xml");
            importer.addStreamDataSet(is);
            importer.importAll();
            
            // Find again
            list = systemBo.getAllSystem(null);
        }
        
        return new ResponseWrapper(CommonConstants.SUCCESS, list);
    }

    @RequestMapping(value = "/system/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper editSystemList() throws Exception {

        List<Dto> list = systemBo.editSystemList(DtoUtils.fromServletRequest(request));

        return new ResponseWrapper(CommonConstants.SUCCESS, list, getResponseMessage("success.SuccessfullyEditSystem"));
    }

}
