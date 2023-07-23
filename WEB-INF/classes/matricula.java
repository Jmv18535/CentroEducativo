

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import alumnado.DBAccess;

/**
 * Servlet implementation class matricula
 */
public class matricula extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public matricula() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String preTituloHTML5 = "<!DOCTYPE html>\n<html>\n<head>\n" + "<meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" />";
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		out.println(preTituloHTML5);
		
		HttpSession sesion = request.getSession();
		
		String apellidos = "" + sesion.getAttribute("apellidos");
		String nombre = "" + sesion.getAttribute("nombre");
		String dni = ""+ sesion.getAttribute("dni");
		
		String asignaturas[] = DBAccess.getStudentSubjects(sesion);
		
		
		
		 
		out.println("<title>Centro Educativo - bienvenida de profesores</title> ");
		out.println("<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css'>");
		out.println("<link rel='stylesheet' href='https://getbootstrap.com/docs/4.6/components/list-group/'>"
				  + "<link rel='stylesheet' href='https://getbootstrap.com/docs/4.6/components/card/'> "
				  + "<link rel='stylesheet' href='https://getbootstrap.com/docs/4.6/components/buttons/'> "
				  + "<link rel='stylesheet' href='https://getbootstrap.com/docs/4.6/utilities/flex/'>");

		out.println("<script src='https://code.jquery.com/jquery-3.6.0.min.js'></script>");
		out.println("<script>");
		out.println("function cerrarSesion() {");
		out.println("    $.ajax({");
		out.println("      url: '/nol2223/logout',");
		out.println("      method: 'POST',");
		out.println("      success: function(response) {");
		out.println("        window.location.href = '/nol2223/index';");
		out.println("      }");
		out.println("    });");
		out.println( "}");
		out.println("  $(document).ready(");
		out.println("		function() {");
		out.println("    		$('#cerrarSesionBtn').click(cerrarSesion);");
		out.println("  	});");
		out.println("</script>");
		out.println("<style>");
		out.println("	"
				  + "	body {"
				  + "       background-color: #9EDBEA;"
				  + "   }"
				  + ""
				  + "	.navbar-brand {"
				  + "		font-size: 30px;"
				  + "	}"
				  + "	#container{"
				  + "		  margin-top: 50px;"
				  + "	}"
				  + "	.card-body {"
				  + "  		display: flex;"
				  + "  		flex-direction: column;"
				  + "  		justify-content: center;"
				  + "  		align-items: center;"
				  + "	}"
				  + "	#centro {"
				  + "  		align-self: flex-start;"
				  + "  		border: 1px solid white;"
				  + "  		color: white;"
				  + "  		padding: 5px 10px;"
				  + "	}"
				  + ""
				  + "");
		out.println(".navbar-brand {font-size: 30px;}");
		out.println("h2{font-weight: bold;}");
		out.println("</style>");
		out.println("");
		out.println("");
		out.println("</head>");
	    out.println("	<body>"
	    		  + " 		<header>");
	    out.println("			<nav class='navbar navbar-expand-lg navbar-dark bg-dark'>"
	    		+ "					<a class='navbar-brand btn btn-secondary' href='/nol2223/log2'>Centro Educativo</a>"
	    		+ "					<form class='ml-auto p-2 bd-highlight' action='#'>"
	    		+ "						<button type='button' id='cerrarSesionBtn' class='btn btn-secondary'>Cerrar Sesi&oacute;n</button>"
	    		+ "					</form>"
	    		+ "				</nav>"
	    		+ "			</header>");
	    
	    out.println("<div id = 'container' class = 'container'>");
	    out.println("	<div class='row'>");
	    out.println("		<div class='col-10 bg-white'>");
	    out.println("			<h3>" + apellidos + ", " + nombre + " (" + dni + ")</h3>");
	    out.println("		</div>");
	    out.println("		<div class ='col-md-4 bg-white'>");
	    out.println("			<div class ='card border-0'>");
	    out.println("				<div class ='card-body'>");
	    out.println("					<img src='"+sesion.getAttribute("imagen")+"'>");
	    out.println("				</div>");
	    out.println("			</div>");
	    out.println("		</div>");
	    out.println("		<div class ='col-md-6 bg-white'>");
	    out.println("			<div class ='card border-0'>");
	    out.println("				<div class ='card-body'>");
	    
	    if(asignaturas.length == 0) { //No tiene asignaturas
	    	out.println("				<p>No está matriculad@ en ninguna asignatura</p>");	
	    } else {
	    	out.print("					<p>[Matriculad@ en");
	    	for(int i = 0; i < asignaturas.length; i++) {
	    		out.print(" " + asignaturas[i] + "");
	    	}
	    	out.println("				]</p>");
	    }
	    
	    out.println("					<p>"+ nombre + " " + apellidos + " es un alumno de Centro Educativo. Y esta es la matrícula que le acredita como estudiante del centro.</p>");

	    out.println("				</div>");
	    out.println("			</div>");
	    out.println("		</div>");
	    out.println("	</div>");
	    out.println("</div>");
	    
	    
	    out.println("  </body>");
	    out.println("</html>");
	}

}
