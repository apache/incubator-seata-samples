package org.apache.seata;

import org.apache.seata.service.BusinessService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootSeataApplication implements BeanFactoryAware {

	private static BeanFactory BEAN_FACTORY;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringbootSeataApplication.class, args);

		BusinessService businessService = BEAN_FACTORY.getBean(BusinessService.class);

		Thread thread = new Thread(() -> businessService.purchase("U100001", "C00321", 2));
		thread.start();
		thread.join();

		//keep run
		Thread.currentThread().join();
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		BEAN_FACTORY = beanFactory;
	}
}
