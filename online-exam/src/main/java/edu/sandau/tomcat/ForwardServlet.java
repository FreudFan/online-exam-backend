package edu.sandau.tomcat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
		name = "MyServlet",
		urlPatterns = {"*.json"}
		)
public class ForwardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	
    	String path=req.getRequestURI();
    	path=path.substring(0, path.length()-"*.json".length()+1);
    	
    	
    	String ctpath=this.getServletContext().getContextPath();
    	path= "/rest" +path.substring(ctpath.length(),path.length());
    	
    	System.out.println("path="+path);
    	
    	req.getRequestDispatcher(path).forward(req, resp);
    	
    }
    
}
