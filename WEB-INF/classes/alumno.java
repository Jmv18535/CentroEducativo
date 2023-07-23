

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import alumnado.DBAccess;

/**
 * Servlet implementation class alumno
 */
public class alumno extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public alumno() {
        super();
        // TODO Auto-generated constructor stub
    }

    public class Alumno { // Estructura de los datos de un alumno
    	public String url;
    	public String dni;
    	public String nombreCom;
    	public String nota;
    }
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession sesion= request.getSession();
		
		String asig = (String) sesion.getAttribute("asignaturaEvaluada");
		
		Alumno actual = new Alumno();
		
		String[] alumnosComp = DBAccess.getSubjectStudents(sesion, asig);
		boolean encontrado=false;
		String x = request.getParameter("alumno");
		for(String al:alumnosComp) {
			if(x.equals(al)) {
				encontrado=true;
				break;
			}
		}
		
		if(!encontrado) {response.sendRedirect("/nol2223/log2");}
		else {
			actual.dni=request.getParameter("alumno");
			
			String default_path = getServletContext().getRealPath("");
			String fotos_path = default_path+"fotos";
			BufferedReader foto = new BufferedReader(new FileReader(fotos_path+"/"+ actual.dni +".pngb64"));
			String linea = foto.readLine();
			actual.url= "data:image/png;base64,"+linea;
			foto.close();
		    
			actual.nombreCom = DBAccess.getLastName(sesion, actual.dni) +", "+ DBAccess.getName(sesion, actual.dni);
			if(DBAccess.getStudentMark(sesion, actual.dni, asig).length() == 0) {
				actual.nota= "Sin evaluar";
			}else actual.nota = DBAccess.getStudentMark(sesion, actual.dni, asig);
			
			String[] dniAlumnos = DBAccess.getSubjectStudents(sesion,asig);
			
			Alumno[] alumnos = new Alumno[dniAlumnos.length];
			
			for (int i = 0; i < dniAlumnos.length; i++) {
				
			    if (dniAlumnos[i].equals(actual.dni)) {
			    	alumnos[i] = actual;
			    } else {
			        alumnos[i] = new Alumno();
			        
			        alumnos[i].dni = dniAlumnos[i];
			        alumnos[i].nombreCom = DBAccess.getLastName(sesion, alumnos[i].dni) +", "+ DBAccess.getName(sesion, alumnos[i].dni);
			        
			        
			        if(DBAccess.getStudentMark(sesion, alumnos[i].dni, asig).length() == 0) {
			        	alumnos[i].nota = "Sin evaluar";
					}else alumnos[i].nota = DBAccess.getStudentMark(sesion, alumnos[i].dni, asig);
			        
					BufferedReader f = new BufferedReader(new FileReader(fotos_path+"/"+ alumnos[i].dni +".pngb64"));
					String l = f.readLine();
					
					alumnos[i].url= "data:image/png;base64,"+l;
					
					f.close();
			    }
			}
			//ahora tenemos todos los alumnos de la asignatura en el array alumno que contiene alumnos
			
			
			
			String preTituloHTML5 = "<!DOCTYPE html>\n<html>\n<head>\n" + "<meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" />";
			response.setContentType("text/html");
			
			PrintWriter out = response.getWriter();
			out.println(preTituloHTML5);
			
			out.println("<title>Centro Educativo - evaluacion de un alumno</title> ");
			out.println("<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css'>");
			out.println("<link rel='stylesheet' href='https://getbootstrap.com/docs/4.6/components/list-group/'>"
					  + "<link rel='stylesheet' href='https://getbootstrap.com/docs/4.6/components/card/'> "
					  + "<link rel='stylesheet' href='https://getbootstrap.com/docs/4.6/components/buttons/'> "
					  + "<link rel='stylesheet' href='https://getbootstrap.com/docs/4.6/utilities/flex/'>");
	
			out.println("<script src='https://code.jquery.com/jquery-3.6.0.min.js'></script>");
			out.println("<script src='https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js'></script>");
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
			out.println("function evaluar(dni) {");
			out.println("      var idInput = 'nota' + dni;");
			out.println("      var valor = document.getElementById(idInput).value");
			out.println("	   if(valor == '' || isNaN(valor) || valor > 10 || valor < 0){");
			out.println("                    			document.getElementById(idInput).value = '';");
			out.println("								var div = document.createElement('div');");
			out.println("                    	  		div.setAttribute('id', 'warning');");
			out.println("                    	  		div.setAttribute('class', 'container');");
			out.println("                    	  		var divAlert = document.createElement('div');");
			out.println("                    	  		divAlert.setAttribute('class', 'alert alert-danger');");
			out.println("                    	  		divAlert.setAttribute('role', 'alert');");
			out.println("								divAlert.innerHTML ='No se ha actualizado la nota';");
			out.println("								div.appendChild(divAlert);"); 
			out.println("								document.getElementById('h').appendChild(div);");
			out.println("								setTimeout(function() {");
			out.println("                    		  		div.remove();");
			out.println("                    			}, 2000);");
			out.println("		}else{");
			out.println("		   $.ajax({");
			out.println("	      	  url: '/nol2223/funcion',");
			out.println("   	  	  method: 'GET',");
			out.println("      	  	  data:{'dni': dni, 'asig': '"+asig+"', 'nota': valor},");
			out.println("        	  success: function(response) {");
			out.println("                    		if(response == 'true'){");
			out.println("								document.getElementById(idInput).placeholder = document.getElementById(idInput).value;");
			out.println("                    			document.getElementById(idInput).value = '';");
			out.println("								var button = document.getElementById('sig');");
			out.println("  								button.click();");
			out.println("                    		} else {");
			out.println("                    			document.getElementById(idInput).value = '';");
			out.println("								var div = document.createElement('div');");
			out.println("                    	  		div.setAttribute('id', 'warning');");
			out.println("                    	  		div.setAttribute('class', 'container');");
			out.println("                    	  		var divAlert = document.createElement('div');");
			out.println("                    	  		divAlert.setAttribute('class', 'alert alert-danger');");
			out.println("                    	  		divAlert.setAttribute('role', 'alert');");
			out.println("								divAlert.innerHTML ='No se ha actualizado la nota';");
			out.println("								div.appendChild(divAlert);"); 
			out.println("								document.getElementById('h').appendChild(div);");
			out.println("								setTimeout(function() {");
			out.println("                    		  		div.remove();");
			out.println("                    			}, 2000);");
			out.println("							}");
			out.println("						}");
			out.println("    		});");
			out.println("	   }");
			out.println("}");
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
			out.println("#carouselExampleIndicators {"
					+ "        height: calc(100vh - 60px); /* Altura de la ventana menos la altura del encabezado */"
					+ "        padding-top: 60px; /* Espacio de relleno superior igual a la altura del encabezado */"
					+ "    }");
			out.println("</style>");
			out.println("");
			out.println("");
			out.println("</head>");
		    out.println("	<body>"
		    		  + " 		<header id='h'>");
		    out.println("			<nav class='navbar navbar-expand-lg navbar-dark bg-dark mb-2'>"
		    		+ "					<a class='navbar-brand btn btn-secondary' href='/nol2223/evaluacion?asig="+asig+"'>Centro Educativo</a>"
		    		+ "					<form class='ml-auto p-2 bd-highlight' action='#'>"
		    		+ "						<button type='button' id='cerrarSesionBtn' class='btn btn-secondary'>Cerrar Sesi&oacute;n</button>"
		    		+ "					</form>"
		    		+ "				</nav>"
		    		+ "			</header>");
		    out.println("<div id='carouselExampleIndicators' class='carousel slide' data-interval='false'>");
			out.println("<ol class='carousel-indicators'>");
			int count = 0;	
			
			boolean active = true;
			String act = "";
			
			for(Alumno a: alumnos) {
				if(active && a.dni.equals(actual.dni)) { 
					act = " class='active'"; active = false;
				} else { 
					act = ""; 
				}
				out.println("<li data-target='#carouselExampleIndicators' data-slide-to="+count+ " " + act + " ></li>");
				count++;
			}
			
			out.println("</ol>");
			out.println("<div class='carousel-inner'>");
			active = true;
			act = "";
			//revisar que salga bien el formato
			for(Alumno a: alumnos) {
				if(active && a.dni.equals(actual.dni)) { act = " active"; active = false;} else { act = ""; }
				out.println("<div class='carousel-item"+ act +"'>");
				out.println("	<div class='row mt-5 justify-content-center'>"
			    		+ "				<div>"
			    		+ "                <div class='card'>"
			    		+ "                    <div class='card-body'>");
			    out.println("						<img src='"+a.url+"'/>");
			    out.println("						<h4>" + a.nombreCom+ "</br> DNI: "+ a.dni +"</h4>");
			    out.println("						<form action='#' method='#'>"
					    + "                            <div class='row mb-2'>"
				 		+ "                            		<input id='nota"+a.dni+"' type='number' class='w-100' step='0.01' min='0' max='10' placeholder='" + a.nota + "'>"
				   		+ "                            </div>"
			    		+ "							   <div class='row'>"
			    		+ "                            		<button id='btnEval' type='button' class='btn btn-success col-sm h-75' onclick='evaluar(\""+a.dni+"\")'>Evaluar</button>"
				   		+ "                        	   </div>"
			    		+ "						   </form>"
			    		+ " 				  </div>"
			    		+ "                </div>"
			    		+ "				</div>"
			    		+ "			 </div>"
			    		+ "		</div>");
			}
			
			out.println("</div>");
			
			out.println("<a class='carousel-control-prev' href='#carouselExampleIndicators' role='button' data-slide='prev'>");
			out.println("  <span class='carousel-control-prev-icon' aria-hidden='true'></span>");
			out.println("  <span class='sr-only'>Previous</span>");
			out.println("</a>");
			
			out.println("<a id='sig' class='carousel-control-next' href='#carouselExampleIndicators' role='button' data-slide='next'>");
			out.println("  <span class='carousel-control-next-icon' aria-hidden='true'></span>");
			out.println("  <span class='sr-only'>Next</span>");
			out.println("</a>");
			
			out.println("</div>");
			
		    out.println("  </body>");
		    out.println("</html>");
	    }
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
