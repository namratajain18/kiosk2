package com.cusbee.kiosk.controller.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@RequestMapping({"/"})
	public String home() {
		return "forward:/site/index.html";
	}

	@RequestMapping({"/login"})
	public String login() {
		return "test-mail";
	}
}