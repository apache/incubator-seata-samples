package io.seata.samples.sca.customer.service;

import io.seata.core.context.RootContext;
import io.seata.samples.sca.common.domain.TbDemo;
import io.seata.samples.sca.common.domain.TbUser;
import io.seata.samples.sca.common.dubbo.api.UserService;
import io.seata.samples.sca.customer.mapper.TbDemoMapper;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class TbDemoService {

    @DubboReference(check = false)
    private UserService userService;

    @Autowired
    private TbDemoMapper demoMapper;

    @GlobalTransactional
    public void save(TbDemo tbDemo) {
        log.info("globalTransactional begin, Xid:{}", RootContext.getXID());
        // local save
        tbDemo.setName("customer");
        demoMapper.insertSelective(tbDemo);

        // call provider save
        TbUser user = new TbUser();
        user.setId(tbDemo.getId());
        user.setAge(user.getAge());
        user.setName(tbDemo.getName());
        userService.add(user);
    }
}
