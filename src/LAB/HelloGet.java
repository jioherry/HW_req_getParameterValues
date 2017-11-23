package LAB;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HelloGet
 */
@WebServlet("/HelloGet")

public class HelloGet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public HelloGet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String name = request.getParameter("user_status");
		String name2 = new String(name.getBytes("ISO-8859-1"), "UTF-8");
		// 畫出 html 表
		out.println("<HTML>");
		out.println("<HTML><title>Hello,"+ name2 +"</title></HTML>");
		out.println("<BODY>");
		out.println("Hello, 你好: "+ name2);
		out.println();
		
		out.println("<br><img scr=\""+ request.getContextPath() +"/images/tomcat.gif\">");
		out.println("<br>");
		
		// 練習 checkbox
		String user_status[] = request.getParameterValues("user_status");
		String str = "";
		if(user_status != null) {
			for(int i = 0; i < user_status.length; i++)
				str += " " + user_status[i];
			
			out.println(new String(str.getBytes("ISO-8859-1"), "UTF-8"));
		} else {
			out.println("未勾選");
			out.println("</BODY></HTML>");
		}	
	}
	
	public String getServletInfo() {
		return "A servlet that knows the name of the person to whom it's" + "saying hello";
	}

}
