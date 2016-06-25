package hu.pe.munoz.commonrest.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;

import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.commondata.bo.SystemBo;
import hu.pe.munoz.commondata.entity.SystemEntity;
import hu.pe.munoz.commonrest.ResponseWrapper;
import hu.pe.munoz.commonrest.helper.MessageString;

public abstract class BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);
	
	@Autowired
	protected Mapper mapper;
	
	@Autowired
	protected MessageString messageString;

	@Autowired 
	protected HttpSession session;
	
	@Autowired
	protected HttpServletRequest request;

	@Autowired
	private SystemBo systemBo;

	protected Locale getCurrentLocale() {
		Locale locale = (Locale) session.getAttribute(CommonConstants.SESSKEY_CURRENT_LOCALE);
		if (locale == null) {
			try {
				SystemEntity system = systemBo.getSystemByKey(CommonConstants.SYSTEM_KEY_LANGUAGE_CODE);
				String languageCode = system.getValue();
				locale = new Locale(languageCode);
				session.setAttribute(CommonConstants.SESSKEY_CURRENT_LOCALE, locale);
			} catch (DataException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return locale;
	}
	
	protected void updateCurrentLocale(String languageCode) {
		session.setAttribute(CommonConstants.SESSKEY_CURRENT_LOCALE, new Locale(languageCode));
	}

	@ExceptionHandler(DataException.class)
	public ResponseWrapper<Object> handleDataException(DataException e) {
		LOGGER.debug("Data exception caught!");
		String message = messageString.get(e.getMessage(), e.getData(), getCurrentLocale()).toString();
		return new ResponseWrapper<Object>(CommonConstants.FAIL, null, e.getCode() + ": " + message);	
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseWrapper<Object> handleOtherException(Exception e) {
		LOGGER.debug("Other exception caught!", e);
		return new ResponseWrapper<Object>(CommonConstants.FAIL, null, e.toString());
	}
	
}
