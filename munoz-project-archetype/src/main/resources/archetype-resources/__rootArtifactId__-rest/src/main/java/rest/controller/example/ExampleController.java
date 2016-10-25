#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.rest.controller.example;

import ${package}.data.bo.ExampleBo;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.commondata.helper.Dto;
import hu.pe.munoz.commonrest.controller.BaseController;
import hu.pe.munoz.commonrest.helper.ResponseWrapper;

@RestController
@RequestMapping("/example")
public class ExampleController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleController.class);

    @Autowired
    private ExampleBo exampleBo;

    @RequestMapping(value = "/example/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper getAllUserGroup() {
        LOG.info("Get all example ...");
        List<Dto> list = exampleBo.getAllExamples(null);
        return new ResponseWrapper(CommonConstants.SUCCESS, list);
    }

}
