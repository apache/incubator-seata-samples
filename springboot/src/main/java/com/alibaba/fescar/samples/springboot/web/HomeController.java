/*
 *  Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.alibaba.fescar.samples.springboot.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fescar.samples.springboot.service.AssetService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The type Home controller.
 */
@Controller
@RequestMapping
public class HomeController {
	@Reference(check = false)
	private AssetService helloService;

	/**
	 * The Port.
	 */
	@Value("${server.port}")
	String port;

	/**
	 * Home string.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/home")
	public String home() {
		System.out.println("redirect to home page!");
		return "index";
	}

}
