package com.pa.demo.angular;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AngularController {

	// @GetMapping(value = "/**/{[path:[^\\.]*}")
	@GetMapping(value = { "/customers/**", "/authx/**", "/callback/**", "/error/**", "/page-not-found/**" })
	public String index() {
		return "forward:/index.html";
	}
}
