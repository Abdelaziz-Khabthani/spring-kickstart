package com.abdelaziz.resource;

import org.springframework.web.bind.annotation.RestController;

import com.abdelaziz.annotations.Loggable;
import com.abdelaziz.consts.ApplicationLayer;
import com.abdelaziz.service.AuthorService;

@Loggable(layer = ApplicationLayer.RESOURCE_LAYER)
@RestController
public class AuthorResource extends ApiRootPath {

	private AuthorService authorService;
}
