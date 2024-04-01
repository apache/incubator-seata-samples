package org.apache.seata.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.seata.service.BusinessService;
import org.apache.seata.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jingliu_xiong@foxmail.com
 */
@RestController
public class E2EController {
    @Autowired
    private BusinessService businessService;

    @GetMapping("testCreate")
    public void testCreate(HttpServletResponse response) throws IOException {
        Map<String, String> res = new HashMap<>();
        // 设置响应类型
        response.setContentType("text/yaml");
        response.setCharacterEncoding("UTF-8");

        Yaml yaml = new Yaml();
        try {
            businessService.purchase("U100001", "C00321", 2);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("res", "failed");
            String yamlStr = yaml.dump(res);
            response.getWriter().write(yamlStr);
            return;
        }
        res.put("res", "success");
        String yamlStr = yaml.dump(res);
        response.getWriter().write(yamlStr);
    }
}
