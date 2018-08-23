package com.abdelaziz.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.abdelaziz.annotations.Loggable;
import com.abdelaziz.consts.ApplicationLayer;

@Loggable(layer = ApplicationLayer.UTILITY_LAYER)
@Component
public class LocalsUtil {

	@Autowired
	private MessageSource messageSource;

	public String getSimpleLocalizedMessage(String code) {
		return this.messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
	}
	
	public Locale getCurrentLocal() {
		return LocaleContextHolder.getLocale();
	}
}
