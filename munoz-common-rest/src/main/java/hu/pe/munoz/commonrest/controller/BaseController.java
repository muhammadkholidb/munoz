package hu.pe.munoz.commonrest.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;

import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.commonrest.helper.MessageString;
import hu.pe.munoz.commonrest.helper.ResponseWrapper;

public abstract class BaseController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	protected Mapper mapper;
	
	@Autowired
	protected MessageString messageString;

	@Autowired
	protected HttpServletRequest request;

	protected String getResponseMessage(String code, Object[] arguments) {
		String languageCode = request.getHeader("Accept-Language");
		if (languageCode != null) {			
		    return messageString.get(code, arguments, new Locale(languageCode)).toString();
		}
		return messageString.get(code, arguments).toString();
	}
	
	protected String getResponseMessage(String code) {
		return getResponseMessage(code, null);
	}
	
	@ExceptionHandler(DataException.class)
	public ResponseWrapper<Object> handleDataException(DataException e) {
		log.debug("Data exception caught!");
		String message = getResponseMessage(e.getMessage(), e.getData());
		return new ResponseWrapper<Object>(CommonConstants.FAIL, e.getCode() + ": " + message);	
	}
	
	@ExceptionHandler(Exception.class) 
	public ResponseWrapper<Object> handleOtherException(Exception e) {
		log.debug("Other exception caught!", e);
		return new ResponseWrapper<Object>(CommonConstants.FAIL, null, e.toString());
	}
	
}
