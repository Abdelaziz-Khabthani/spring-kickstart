package com.abdelaziz.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdelaziz.annotations.Loggable;
import com.abdelaziz.consts.ApplicationLayer;
import com.abdelaziz.service.TagService;

@Loggable(layer = ApplicationLayer.RESOURCE_LAYER)
@RestController
public class TagResource extends ApiRootPath {

	@Autowired
	private TagService tagService;

	@GetMapping("/exception")
	public String exception() throws Exception {
		return "Hello World";
	}
}
