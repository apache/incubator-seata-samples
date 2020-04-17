package io.seata.edas.carshop;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * Alibaba Group EDAS. http://www.aliyun.com/product/edas
 */
public class StartListener implements ServletContextListener{

	public static ApplicationContext CONTEXT = null;

	@Override
	public void contextInitialized( ServletContextEvent sce ) {
		CONTEXT = WebApplicationContextUtils.getWebApplicationContext(
			sce.getServletContext()
		);
	}

	@Override
	public void contextDestroyed( ServletContextEvent sce ) {
		// TODO Auto-generated method stub
	}

}
