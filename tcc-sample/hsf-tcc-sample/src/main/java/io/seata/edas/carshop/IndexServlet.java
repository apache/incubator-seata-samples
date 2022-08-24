/*
 *  Copyright 1999-2021 Seata.io Group.
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
package io.seata.edas.carshop;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.seata.edas.tcc.activity.ActivityServiceImpl;
import org.apache.commons.lang3.StringUtils;

/**
 * Alibaba Group EDAS. http://www.aliyun.com/product/edas
 */
public class IndexServlet extends HttpServlet {

    private static final long serialVersionUID = -112210702214857712L;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        final ActivityServiceImpl activityService = (ActivityServiceImpl)StartListener.CONTEXT.getBean(
            "activityService");

        String op = req.getParameter("op");
        boolean commit = StringUtils.equalsIgnoreCase("rollback", op) ? false : true;
        writer.write(activityService.doActivity(commit));

        return;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        return;
    }

}
