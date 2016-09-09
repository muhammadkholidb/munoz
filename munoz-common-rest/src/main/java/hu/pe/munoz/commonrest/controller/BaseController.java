package hu.pe.munoz.commonrest.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;

import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.common.helper.CommonUtils;
import hu.pe.munoz.commonrest.helper.MessageHelper;
import hu.pe.munoz.commonrest.helper.ResponseWrapper;

public abstract class BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    protected MessageHelper messageHelper;

    @Autowired
    protected HttpServletRequest request;

    protected String getResponseMessage(String code, Object[] arguments) {
        String languageCode = request.getHeader("Accept-Language");
        if (languageCode != null) {
            return messageHelper.getMessage(code, arguments, new Locale(languageCode));
        }
        return messageHelper.getMessage(code, arguments);
    }

    protected String getResponseMessage(String code) {
        return getResponseMessage(code, null);
    }

    @ExceptionHandler(DataException.class)
    public ResponseWrapper handleDataException(DataException e) {
        LOG.debug("Data exception caught!");
        String message = getResponseMessage(e.getMessage(), e.getData());
        return new ResponseWrapper(CommonConstants.FAIL, e.getCode() + ": " + message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseWrapper handleOtherException(Exception e) {
        LOG.debug("Other exception caught!", e);
        return new ResponseWrapper(CommonConstants.FAIL, CommonUtils.getExceptionMessage(e));
    }

}
