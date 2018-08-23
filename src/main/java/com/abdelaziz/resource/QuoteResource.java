package com.abdelaziz.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.abdelaziz.annotations.Loggable;
import com.abdelaziz.consts.ApplicationLayer;
import com.abdelaziz.service.QuoteService;

@Loggable(layer = ApplicationLayer.RESOURCE_LAYER)
@RestController
public class QuoteResource extends ApiRootPath {

	@Autowired
	private QuoteService quoteService;
}
