package io.seata.edas.carshop;

import io.seata.edas.tcc.activity.ActivityServiceImpl;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Alibaba Group EDAS. http://www.aliyun.com/product/edas
 */
public class IndexServlet extends HttpServlet {

	private static final long serialVersionUID = -112210702214857712L;

	@Override
	public void doGet( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();

		final ActivityServiceImpl activityService = ( ActivityServiceImpl )
				StartListener.CONTEXT.getBean( "activityService" );

		String op = req.getParameter("op");
		boolean commit = StringUtils.equalsIgnoreCase("rollback",op) ? false : true;
		writer.write(activityService.doActivity(commit));

		return;
	}
	
	@Override
	protected void doPost( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
		return;
	}

}
