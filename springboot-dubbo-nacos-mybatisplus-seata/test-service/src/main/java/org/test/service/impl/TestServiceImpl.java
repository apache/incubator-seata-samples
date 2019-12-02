package org.test.service.impl;




import org.apache.dubbo.config.annotation.Service;
import org.test.entity.Test;
import org.test.mapper.TestMapper;
import org.test.service.ITestService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service(version = "1.0.0",interfaceClass =ITestService.class )
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements ITestService {

}
