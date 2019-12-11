package io.seata.samples.sca.common.dubbo.api;

import io.seata.samples.sca.common.domain.TbUser;

/**
 * Description:
 * author: yu.hb
 * Date: 2019-11-01
 */
public interface UserService {
    void add(TbUser user);
}
