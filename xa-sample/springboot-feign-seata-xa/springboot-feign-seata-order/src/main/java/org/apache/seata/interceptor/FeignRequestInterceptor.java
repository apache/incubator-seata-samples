package org.apache.seata.interceptor;

import org.apache.seata.core.context.RootContext;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        // 从全局事务上下文中获取 TX_XID 并添加到 Feign 请求的 Header 中
        String xid = RootContext.getXID();
        if (xid != null) {
            template.header(RootContext.KEY_XID, xid);
        }
    }
}