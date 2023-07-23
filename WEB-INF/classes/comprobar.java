

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class comprobar
 */
public class comprobar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public comprobar() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String rol = request.getParameter("rol");
    	String login = request.getParameter("login");
    	String[] datos = new String[3];
    	try {
    		datos = getServletContext().getInitParameter(login).split(",");
    		if (rol.equals(datos[2])) {
                response.getWriter().write("true");
            } else {
            	String compa = rol + " " + datos[2] + " = false";
                response.getWriter().write(compa);
            }
    	} catch (NullPointerException e) {
            response.getWriter().write("false");
    	}
    	
        
	}

}
