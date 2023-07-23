

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import alumnado.DBAccess;

/**
 * Servlet implementation class certificado
 */
public class certificado extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public certificado() {
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
		String dni= (String) sesion.getAttribute("dni");
		String nombre= (String) sesion.getAttribute("nombre");
		String apell= (String) sesion.getAttribute("apellidos");
		String [] asigs= DBAccess.getStudentSubjects(sesion);
		LocalDate fechaActual = LocalDate.now();
		String fecha="En Valencia, "+ fechaActual ;
		
		
		out.println("<title>Centro Educativo - Certificado Academico</title> ");
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
				  + " 	#header-fecha{margin-top:20px;}"
				  + "	#impBut{margin-bottom:10px;}"
				  + "	#centro {"
				  + "  		align-self: flex-start;"
				  + "  		border: 1px solid white;"
				  + "  		color: white;"
				  + "  		padding: 5px 10px;"
				  + "	}");
		out.println(".navbar-brand {font-size: 30px;}");
		out.println("h2{font-weight: bold;}");
		out.println("@media print {\n"
				+ "            @page {\n"
				+ "             size: A4;\n"
				+ "                margin: 20px;\n"
				+ "            }\n"
				+ "            body {\n"
				+ "                margin: 1.5cm;\n"
				+ "            }\n"
				+ "				#impBut{display: none;}\n"
				+ "        }  ");
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
	    out.println("<div class=\"container\">\n"
	    		+ "        <div class=\"row\">\n"
	    		+ "            <div class=\"mx-auto\">\n"
	    		+ "                <div class=\"mt-4\" style=\"width: 40rem;\">\n"
	    		+ "                    <h1>Certificado sin validez acad&eacute;mica</h1>\n"
	    		+ "                </div>\n"
	    		+ "            </div>\n"
	    		+ "        </div>");
	    out.println("<div class=\"row mt-5\">\n"
	    		+ "                    <div class=\"col\">\n"
	    		+ "							<div class=\"card\">\n"
	    		+ "								<div class=\"card-body\">\n"
	    		+ "                        			<p style=\"font-size: 20px;\"><strong>Centro Educativo</strong> certifica que D/D&ordf; <strong>" +nombre + " " + apell + "</strong> con DNI: "+ dni +", matriculado/a en el curso 2022/2023, ha obtenido las calificaciones que se muestran en la siguiente tabla.</p>\n"
	    		+ "					   			</div>\n"
	    		+ "					   		</div>\n"
	    		+ "                    </div>\n"
	    		+ "                    <div class=\"col\"> "
	    		+ "								<img src='"+sesion.getAttribute("imagen")+"'>"
	    		+ "					   	</div>\n"
	    		+ "        </div>");
	    
	    out.println("<div class=\"row mt-5 md-6\">"
	    		+ "		<div class=\"col-12\">\n"
	    		+ "			<div class=\"card\">\n"
	    		+ "				<div class=\"card-body\">\n");
	    
	    out.println("            <table class=\"table\">\n"
	    		+ "                <thead>\n"
	    		+ "                <tr>\n"
	    		+ "                    <th scope=\"col\">Acr&oacute;nimo</th>\n"
	    		+ "                    <th scope=\"col\">Asignatura</th>\n"
	    		+ "                    <th scope=\"col\">Nota</th>\n"
	    		+ "                </tr>\n"
	    		+ "                </thead>\n"
	    		+ "                <tbody>");
	    
	    for(String asig : asigs) {
	    	String nbAsig= DBAccess.getSubjectByAcronim(sesion, asig);
	    	String nota = DBAccess.getStudentMark(sesion,dni ,asig);
	    	if(nota.length()==0) {nota="Aun no tienes nota";}
	    	
	    	out.println("                <tr>\n"
	    			+ "                    <td>"+asig +"</td>\n"
	    			+ "                    <td>"+ nbAsig+"</td>\n"
	    			+ "                    <td>"+ nota+"</td>\n"
	    			+ "                </tr>");
	    }
	    
	    
	    out.println(" </tbody>\n"
	    		+ "            		</table>\n"
	    		+ "     		</div>\n"
	    		+ "     	</div>\n"
	    		+ " 	</div>\n"
	    		+ " </div>\n");
	    out.println("<div class=\"row justify-content-end mr-4 mt-6\">\n"
	    		+ "            <h4 id=\"header-fecha\">"+ fecha+ "</h4>\n"
	    		+ "        </div>"
	    		+ "        \n");
	    out.println("<div class=\"row justify-content-end mr-4 mt-6\">\n");
	    out.println("<input type='button' class='btn btn-secondary' id='impBut' value='Imprimir' onclick='javascript:window.print()' />");
	    out.println("</div>"
	    		+ "    </div>");
	    
	    out.println("  </body>");
	    out.println("</html>");
	}

}