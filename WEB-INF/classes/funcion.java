

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import alumnado.DBAccess;

/**
 * Servlet implementation class funcion
 */
public class funcion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public funcion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession sesion= request.getSession();
		
		String dni = request.getParameter("dni");
		String asig = request.getParameter("asig");
		String nota = request.getParameter("nota");
    		String res = DBAccess.putStudentMark(sesion, dni, nota, asig);
    		if (res.equals("OK")) {
                response.getWriter().write("true");
            } else {
            	String fallo = "false";
                response.getWriter().write(fallo);
            }
    	
	}

}
