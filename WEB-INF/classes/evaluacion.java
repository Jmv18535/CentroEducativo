
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import alumnado.DBAccess;

/**
 * Servlet implementation class evaluacion
 */
public class evaluacion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public evaluacion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession sesion= request.getSession();
		
		String asig = request.getParameter("asig");
		
		String[] asigsPropias = DBAccess.getTeacherSubjects(sesion);
		boolean encontrado=false;
		
		for(String asigPropia:asigsPropias) {
			if(asig.equals(asigPropia)) {
				encontrado=true;
				break;
			}
		}
		
		if(!encontrado) {response.sendRedirect("/nol2223/log2");}
		
		
		String[] dniAlumnos = DBAccess.getSubjectStudents(sesion,asig);
		sesion.setAttribute("asignaturaEvaluada", asig);
		
		if(sesion.getAttribute("porEvaluar")!=null) {
			sesion.removeAttribute("porEvaluar");
		}
		
		 String tabla =  "<div class='row'>"
		    		+ "	<div class='col-12'>"
		    		+ "			<div class='card'>		"
		    		+ "				<div class='card-body'>"
		    		+ "					<table class='table'>"
		    		+ "  					<thead>"
		    		+ "    						<tr>"
		    		+ "      						<th scope='col'>#</th>"
		    		+ "      						<th scope='col'>Apellidos</th>"
		    		+ "      						<th scope='col'>Nombre</th>"
		    		+ "								<th scope='col'>DNI</th>"
		    		+ "      						<th scope='col'>Nota</th>"
		    		+ "								<th scope='col'>Evaluar</th>"
		    		+ "    						</tr>"
		    		+ "  					</thead>"
		    		+ "  					<tbody>";
		    		
		
		
		int count = 1;
		float media = 0;
		for(String dni : dniAlumnos) {
			String apellidos = DBAccess.getLastName(sesion, dni);
			String nombre = DBAccess.getName(sesion, dni);
			String nota = DBAccess.getStudentMark(sesion, dni, asig);
			if(nota.length()==0) {nota="Aun no tiene nota";}
		    tabla += "    						<tr>"
		    		+ "     					    <th scope='row'>"+count+"</th>"
		    		+ "      							<td>"+apellidos+"</td>"
		    		+ "      							<td>"+nombre+"</td>"
		    		+ "									<td>"+dni+"</td>"
		    		+ "      							<td>"+nota+"</td>"
		    		+ "									<td> "
		    		+ "										<form action='alumno' method='GET'>"
		    		+ "											<input type='hidden' name='alumno' value='"+dni+"'>"
		    		+ "											<input type='submit' class='btn btn-success' id='impBut' value='Evaluar'>	"
		    		+ "										</form>"
		    		+ "									</td>"
		    		+ "   						</tr>";
		    
		    if(nota.equals("Aun no tiene nota")) {
		    	media += 0;
		    } else {
		    	media += Float.parseFloat(nota);
		    }
		    
		    
		    count++;
		}
		float valor;
		if(dniAlumnos.length != 0) {
			valor = (media/dniAlumnos.length);
		} else {
			valor = 0;
		}
		
		
		String preTituloHTML5 = "<!DOCTYPE html>\n<html>\n<head>\n" + "<meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" />";
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		out.println(preTituloHTML5);
		
		
		
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
		out.println("function mostrar() {");
		out.println("	document.getElementById('nMedia').innerHTML = '"+valor+"'");
		out.println( "}");
		out.println("  $(document).ready(");
		out.println("		function() {");
		out.println("    		$('#btnMedia').click(mostrar);");
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
				  + "	}");
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
	    out.println("		<div class='container'>"
	    		+ "				<div class='row'>"
	    		+ "					<h1 class='m-4'>Se van a evaluar a los alumnos de la asignatura "+ asig +"</h1>"
	    		+ "				</div>");
	    
	   
	    		//muevo el for de la tabla arriba para calcular la media
	    		
	    		
	    tabla += "  					</tbody>"
	    		+ "					</table>"
	    		+ "					<input id='btnMedia' type='button' class='btn btn-primary mb-2' value='Calcular nota media'><p id='nMedia'>Sin calcular</p>"
	    		+ "				</div>"
	    		+ "			</div>"
	    		+ "		  </div>"
	    		+ "		</div>"
	    		+ "	</div>";
	    out.println(tabla);
	    out.println("  </body>");
	    out.println("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}