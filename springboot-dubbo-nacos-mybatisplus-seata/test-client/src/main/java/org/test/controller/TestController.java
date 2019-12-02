package org.test.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.test.entity.Test;
import org.test.service.DemoService;
import org.test.service.ITestService;
import org.apache.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 文件表 前端控制器
 * </p>
 *
 * @author funkye
 * @since 2019-03-20
 */
@RestController
@RequestMapping("/test")
@Api(tags = "测试接口")
public class TestController {

	private final static Logger logger = LoggerFactory.getLogger(TestController.class);
	@Autowired
	@Lazy
	DemoService demoService;
	@Reference(version = "1.0.0", timeout = 60000)
	private ITestService testService;
	@GetMapping(value = "testSeataOne")
	@ApiOperation(value = "测试手动回滚分布式事务接口")
	public Object testSeataOne() {
		return demoService.One();
	}
	
	@GetMapping(value = "testSeataTwo")
	@ApiOperation(value = "测试异常回滚分布式事务接口")
	public Object testSeataTwo() {
		return demoService.Two();
	}
	@GetMapping(value = "testPage")
	@ApiOperation(value = "测试分布式事务导致mp插件无法使用的bug")
	public Object testPage() {
		return testService.page(new Page<Test>(1, 10));
	}

}
