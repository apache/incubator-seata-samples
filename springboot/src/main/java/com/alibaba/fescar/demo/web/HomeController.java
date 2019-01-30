package com.alibaba.fescar.demo.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fescar.demo.dubbo.AssetService;

@Controller
@RequestMapping
public class HomeController {
	@Reference(check = false)
	private AssetService helloService;

	@Value("${server.port}")
	String port;

	@RequestMapping(value = "/home")
	public String home() {
		System.out.println("redirect to home page!");
		return "index";
	}

}
