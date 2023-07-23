

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import alumnado.DBAccess;
import java.io.BufferedReader;
import java.io.FileReader;


/**
 * Servlet implementation class log2
 */
public class log2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public log2() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// Recoge logs en /home/user
		LocalDateTime fecha = LocalDateTime.now();
		String ip = request.getRemoteAddr();
		String usuario = request.getRemoteUser();
		String metodo = request.getMethod();
		String uri = request.getRequestURI();


		String log = fecha + " Usuario:" + usuario +" Ip:"+ ip + " Metodo: acceso " + metodo + " URI: " + uri + ". LOG DE INDEX";
		
		String rutaLog = getServletContext().getInitParameter("rutaLog");
		

		FileOutputStream ruta = new FileOutputStream(rutaLog,true);
		PrintStream salida = new PrintStream(ruta);
		System.setOut(salida);
		System.out.println(log);
		
		HttpSession sesion = request.getSession();
		String key = (String) sesion.getAttribute("key");
		String[] datos = new String[4];
		
		if (key == null) { //si no hay key guardada  --> Equivalente a if(sesion.isNew())
			if(usuario != null) {
				 
				 datos =  getServletContext().getInitParameter(usuario).split(",");
				 
				 sesion.setAttribute("dni", datos[0]); 
				 sesion.setAttribute("pass",datos[1]);
				 
				 String dni = ""+ sesion.getAttribute("dni");
				 String default_path = getServletContext().getRealPath("");
				 String fotos_path = default_path+"fotos";
				 BufferedReader foto = new BufferedReader(new FileReader(fotos_path+"/"+dni+".pngb64"));
				 String linea = foto.readLine();
				 String url= "data:image/png;base64,"+linea;
				 foto.close();
				 
				 sesion.setAttribute("imagen", url);
				 
				 try {
					DBAccess.bdLogin(sesion);
				 } catch (IOException e) {
					response.sendError(500, "Hubo problemas al recuperar la información. " + e);
				 }
				 
				 key = (String) sesion.getAttribute("key");
				 
				 if (key == null) { // invocando /login por POST
					log = "falloBD";
				} 

				 if(datos[2].equals("rolalu")) {
					 sesion.setAttribute("nombre",DBAccess.getName(sesion, datos[0]));
					 sesion.setAttribute("apellidos",DBAccess.getLastName(sesion, datos[0]));
				 } else {
					 sesion.setAttribute("nombre",DBAccess.getTeacherName(sesion, datos[0]));
					 sesion.setAttribute("apellidos",DBAccess.getTeacherLastName(sesion, datos[0]));
				 }
			} else {log = "falloUSER";}
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
				  + ""
				  + "	.centrado{"
				  + "    display: flex;"
				  + "    justify-content: center;"
				  + "    align-items: center;"
				  + "  }"
				  + "	.card-body {"
				  + "  		display: flex;"
				  + "  		flex-direction: column;"
				  + "  		justify-content: center;"
				  + "  		align-items: center;"
				  + "	}");
		out.println("</style>");
		out.println("");
		out.println("");
		out.println("</head>");
	    out.println("	<body>"
	    		  + " 		<header>");
	    out.println("			<nav class='navbar navbar-expand-lg navbar-dark bg-dark'>"
	    		+ "					<a class='navbar-brand' href='#'>Centro Educativo</a>"
	    		+ "					<form class='ml-auto p-2 bd-highlight' action='#'>"
	    		+ "						<button type='button' id='cerrarSesionBtn' class='btn btn-secondary'>Cerrar Sesi&oacute;n</button>"
	    		+ "					</form>"
	    		+ "				</nav>"
	    		+ "			</header>");
		
		//comprobamos el rol del usuario
		if(request.isUserInRole("rolalu")) {
			
			
			String[] asig = DBAccess.getStudentSubjects(sesion);
			if(sesion.getAttribute("porEvaluar")!=null) {
				sesion.removeAttribute("porEvaluar");
			}
			
			String menu = "<div class='card text-center'>"
					+ "  <div class='card-header'>"
					+ "    <ul class='nav nav-pills card-header-pills'>";
		
			for (String elemento : asig) {
		        menu +="<li class='nav-item centrado'>"
		        	 + "	<form action='asignatura' method='GET'>" 
		             + "		<input type='hidden' name='asig' value='"+elemento+"'>"
		             + "		<button type='submit' class='btn btn-primary mr-2'>"+elemento+"</button>"
		             + "</form></li><br>";
		      }
			menu +=    "<form class='ml-auto p-2 bd-highlight' action='#'>"
                    + "         <a class='btn btn-info' href='/nol2223/certificado'>Ver Certificado</a>"
                    + "         <a class='btn btn-info' href='/nol2223/matricula'>Ver Matricula</a>"
                    + "</form>";
			
			
			menu += "    </ul>"
				  + "  </div>"
				  + "</div>";
		
		    out.println(menu);
		    out.println( "<div id='container' class='container'>"
		    		+ "        <div class='row'>"
		    		+ "            <div class='col-md-4'>"
		    		+ "                <div class='card'>"
		    		+ "                    <div class='card-body'>");
		    out.println("						<img src='"+sesion.getAttribute("imagen")+"'>");
		    out.println("						<h4>" + sesion.getAttribute("nombre") + " " + sesion.getAttribute("apellidos")+ "</h4>");
		    out.println("						<h4>DNI: "+ sesion.getAttribute("dni") +"</h4>");
		    out.println(" 				  </div>"
		    		+ "                </div>"
		    		+ "            </div>");
		    out.println("		   <div class='col-md-4'>");
		    out.println("						<h2>Bienvenid@ a Notas Online!</h2> </br> </br>");
		    out.println("						<p>Pulsando los botones en la parte superior "
		    		  + "							puedes consultar la información de las asignaturas en las que estas matriculado "
		    		  + "							u obtener información sobre el curso."
		    		  + "						</p>");
		    out.println("          </div>");		  
				
			
		} else {

			
			String[] asig = DBAccess.getTeacherSubjects(sesion);
			//String[] asig = {"DCU","DEW","CODIGO"};
			
			String menu = "<div class='card text-center'>"
					+ "  <div class='card-header'>"
					+ "    <ul class='nav nav-pills card-header-pills'>";
		
			for (String elemento : asig) {
		        menu +="<li class='nav-item'>"
		        	 + "	<form action='evaluacion' method='GET'>" 
		             + "		<input type='hidden' name='asig' value='"+elemento+"'>"
		             + "		<button type='submit' class='btn btn-primary mr-2'>"+elemento+"</button>"
		             + "</form></li><br>";
		      }
			
			menu += "    </ul>"
				  + "  </div>"
				  + "</div>";
		
		    out.println(menu);
		    out.println( "<div id='container' class='container'>"
		    		+ "        <div class='row'>"
		    		+ "            <div class='col-md-4'>"
		    		+ "                <div class='card'>"
		    		+ "                    <div class='card-body'>");
		    out.println("						<img src='"+sesion.getAttribute("imagen")+"'>");
		    out.println("						<h4>" + sesion.getAttribute("nombre") + " " + sesion.getAttribute("apellidos")+ "</h4>");
		    out.println("						<h4>DNI: "+ sesion.getAttribute("dni") +"</h4>");
		    out.println(" 				  </div>"
		    		+ "                </div>\n"
		    		+ "            </div>");
		    out.println("		   <div class='col-md-4'>");
		    out.println("						<h2>Bienvenid@ a Notas Online!</h2> </br> </br>");
		    out.println("						<p>Pulsando los botones en la parte superior "
		    		  + "							puedes consultar la información de las asignaturas que imparte."
		    		  + "						</p>");
		    out.println("          </div>");		   
		    
				
		}
		
		out.println( "");
	    out.println("		   <div class='col-md-4'>"
	    		  + "                <div class='card'>"
	    		  + "                    <div class='card-body'>");
	    out.println("						<ul class='list-group list-group-flush'>"
	    		  + "							<h3>Grupo: 3ti12_g01</h3>");
	    out.println("							<li class='list-group-item'>Alberto Ojero Puebla</li>");
	    out.println("							<li class='list-group-item'>Javier Mart&iacute;nez Vargas</li>");
	    out.println("							<li class='list-group-item'>Marcial Carreras Arencibia</li>");
	    out.println("							<li class='list-group-item'>Tatiana Aksenova</li>");
	    out.println("							<li class='list-group-item'>Javier Landete Zamora</li>");
	    out.println("							<li class='list-group-item'>Bautista Galmarini</li>");
	    out.println("						</ul>");
	    out.println(" 				  </div>"
	    		  + "                </div>"
	    		  + "            </div>"
	    		  + "			</div>"
	    		  + "		</div>");
	    out.println("");
	    out.println("  </body>");
	    out.println("</html>");
		
	}
}
