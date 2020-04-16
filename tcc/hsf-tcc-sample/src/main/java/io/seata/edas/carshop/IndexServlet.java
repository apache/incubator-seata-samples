package io.seata.edas.carshop;

import io.seata.edas.tcc.activity.ActivityServiceImpl;

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
		writer.write("OK");
		System.out.println("OK");

		final ActivityServiceImpl activityService = ( ActivityServiceImpl )
				StartListener.CONTEXT.getBean( "activityService" );

		writer.write(activityService.doActivity(999, "seata"));

		return;
	}
	
	@Override
	protected void doPost( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
		return;
	}

}
