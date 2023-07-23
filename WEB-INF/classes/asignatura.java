

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import alumnado.DBAccess;

/**
 * Servlet implementation class asignatura
 */
public class asignatura extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public asignatura() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		HttpSession sesion = request.getSession();
		
		//String asig = "DEW";
		String asig = request.getParameter("asig");
		
		String[] asigTotales = DBAccess.getSubjects(sesion);
		boolean encontrado= false;
		String nombreasig = "";
		
		for (String elemento : asigTotales) {
		    if (elemento.equals(asig)) {
		        encontrado = true;
		        break;
		    }
		}
		if(encontrado==true) {
			nombreasig = DBAccess.getSubjectByAcronim(sesion, asig);
		}
		
		//int nota = 5;
		String nota = DBAccess.getStudentMark(sesion, (String) sesion.getAttribute("dni"), asig);
		
		
		String relleno = "";
		
		if (asig.equals("DEW")) {
            relleno = "La asignatura "+nombreasig+" permite adquirir una fuerte base en los estándares aplicables, tanto protocolos como formatos de representación, y realizar desarrollos tanto en el lenguaje HTML como en la programación en el servidor de web.";
      } else if (asig.equals("IAP")) {
            relleno = "La asignatura "+nombreasig+" Integración de Aplicaciones pretende introducir a los alumnos los problemas y las necesidades de integración e interoperabilidad de las Organizaciones que usan gran cantidad de tecnologías distintas que necesitan comunicarse e interactuar entre ellas.";
      } else if (asig.equals("DCU")) {
            relleno = " La asignatura "+nombreasig+"presenta el modelo de Diseño Centrado en el Usuario, en el que concibe la interacción persona-ordenador en base a las tareas y la situación (contexto) en el que el usuario usa las interfaces de usuario de los sistemas. Se hará hincapié en la importancia de involucrar a los usuarios desde las primeras etapas de concepción y desarrollo de los sistemas.";
      } else {
          relleno = "La asignatura que has seleccionado no existe";
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
		
		
		
		
		out.println("<div class=\"container\">");
		out.println("<div class=\"row\">");
		out.println("<div class=\"col-md-12 mx-auto\">");
		out.println("<div class=\"card mt-4\">");
		out.println("<div class=\"card-body\">");
		out.println("<h2 class=\"card-title text-center\">"+ asig +" - " + nombreasig+ "</h2>");
		out.println("<p>" + relleno + "</p>");
		out.println("</div>");
		out.println("</div>");
		out.println("</div>");
		out.println("</div>");
		out.println("<div class=\"row\">");
		out.println("<div class=\"mx-auto\">");
				
		if(nota==null && encontrado==false) {
			out.println("<div class=\"card mt-4\" style=\"width: 48rem;\">");
			out.println("<div class=\"card-body\">");
			out.println("<h2 class=\"card-title text-center\"> No existe esta asignatura Ram&oacute;n </h2>");
			out.println("</div>");
			out.println("</div>");
			out.println("</div>");
			out.println("</div>");
			out.println("</div>");
			out.println("</body>");
		}else if(nota==null && encontrado==true) {
			out.println("<div class=\"card mt-4\" style=\"width: 48rem;\">");
			out.println("<div class=\"card-body\">");
			out.println("<h2 class=\"card-title text-center\"> No estas matriculado en la asignatura </h2>");
			out.println("</div>");
			out.println("</div>");
			out.println("</div>");
			out.println("</div>");
			out.println("</div>");
			out.println("</body>");
		}
		else if(nota.length()== 0) {
			out.println("<div class=\"card mt-4\" style=\"width: 48rem;\">");
			out.println("<div class=\"card-body\">");
			out.println("<h2 class=\"card-title text-center\">Aun no tienes nota en esta asignatura </h2>");
			out.println("</div>");
			out.println("</div>");
			out.println("</div>");
			out.println("</div>");
			out.println("</div>");
			out.println("</body>");
		}else {
			out.println("<div class=\"card mt-4\" style=\"width: 24rem;\">");
			out.println("<div class=\"card-body\">");
			out.println("<h2 class=\"card-title text-center\">Nota="+ nota + "</h2>");
			out.println("</div>");
			out.println("</div>");
			out.println("</div>");
			out.println("</div>");
			out.println("</div>");
			out.println("</body>");
		}
		
	}
}