package org.test.service;

import java.time.LocalDateTime;

import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.test.controller.TestController;
import org.test.entity.Test;

import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransactionContext;

@Service
public class DemoService {
	@Reference(version = "1.0.0", timeout = 60000)
	private ITestService testService;
	private final static Logger logger = LoggerFactory.getLogger(DemoService.class);

	/**
	 * 手动回滚示例
	 * 
	 * @return
	 */
	@GlobalTransactional
	public Object One() {
		logger.info("seata分布式事务Id:{}", RootContext.getXID());
		Test t = new Test();
		t.setOne("1");
		t.setTwo("2");
		t.setCreateTime(LocalDateTime.now());
		testService.save(t);
		try {
			int i = 1 / 0;
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			try {
				logger.info("载入事务id进行回滚");
				GlobalTransactionContext.reload(RootContext.getXID()).rollback();
			} catch (TransactionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 抛出异常进行回滚示例
	 * 
	 * @return
	 */
	@GlobalTransactional
	public Object Two() {
		logger.info("seata分布式事务Id:{}", RootContext.getXID());
		Test t = new Test();
		t.setOne("1");
		t.setTwo("2");
		t.setCreateTime(LocalDateTime.now());
		testService.save(t);
		try {
			int i = 1 / 0;
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException();
		}
	}
}
