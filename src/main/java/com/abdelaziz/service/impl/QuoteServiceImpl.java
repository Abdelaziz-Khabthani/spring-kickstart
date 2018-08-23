package com.abdelaziz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abdelaziz.annotations.Loggable;
import com.abdelaziz.consts.ApplicationLayer;
import com.abdelaziz.repository.QuoteRepository;
import com.abdelaziz.service.QuoteService;

@Loggable(layer = ApplicationLayer.SERVICE_LAYER)
@Service
@Transactional
public class QuoteServiceImpl implements QuoteService {
	
	@Autowired
	private QuoteRepository quoteRepository;
}
