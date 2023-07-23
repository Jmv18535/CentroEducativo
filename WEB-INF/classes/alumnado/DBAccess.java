package alumnado;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DBAccess {

	private static String maquina = "jlanzam";
	
	@SuppressWarnings("unchecked")
	private static StringBuilder conexionGET(String direccion, HttpSession sesion) throws IOException {
		URL url = new URL(direccion + "?key=" + sesion.getAttribute("key"));
		HttpURLConnection conexion = (HttpURLConnection) url.openConnection();

		for (String cookie : (List<String>) sesion.getAttribute("cookie")) {
			conexion.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
		}

		conexion.setDoOutput(true);
		conexion.setRequestMethod("GET");
		conexion.setRequestProperty("accept", "*/*");
		conexion.setRequestProperty("Content-Type", "application/json");

		BufferedReader buffer = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
		StringBuilder json = new StringBuilder();
		String respuesta;
		while ((respuesta = buffer.readLine()) != null) {
			json.append(respuesta);
		}
		buffer.close();
		conexion.disconnect();
		
		return json;
		
	}
	

	/*
	 * Given a HttpSession with the student variables bdLogin will login into the
	 * data base and introduce the attributes key and cookie into the sesion object
	 */
	public static void bdLogin(HttpSession sesion) throws IOException {
		String direccion = "http://dew-" + maquina +"-2223.dsicv.upv.es:9090/CentroEducativo/login";
		String bdkey = "";

		URL url = new URL(direccion);
		HttpURLConnection conexion = (HttpURLConnection) url.openConnection();

		conexion.setDoOutput(true);
		conexion.setRequestMethod("POST");
		conexion.setRequestProperty("accept", "*/*");
		conexion.setRequestProperty("Content-Type", "application/json");

		// construir JSON
		Gson gson = new Gson();
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("dni", (String) sesion.getAttribute("dni"));
		jsonObject.addProperty("password", (String) sesion.getAttribute("pass"));

		// Enviamos JSON
		OutputStream os = conexion.getOutputStream();
		os.write(gson.toJson(jsonObject).getBytes());
		os.flush();
		os.close();

		InputStreamReader perico = new InputStreamReader(conexion.getInputStream(), "utf-8");
		BufferedReader buffer = new BufferedReader(perico);

		try (buffer) {
			String lineaRespuesta = "";

			while ((lineaRespuesta = buffer.readLine()) != null) {
				// key
				bdkey += lineaRespuesta.trim();
			}
		}
		// cookie
		List<String> cookies = conexion.getHeaderFields().get("Set-Cookie");

		sesion.setAttribute("key", bdkey);
		sesion.setAttribute("cookie", cookies);
	}

	/*
	 * Return an array of the subjects the loged stundent is registered on
	 */
	public static String[] getStudentSubjects(HttpSession sesion) throws IOException {
		String direccion = "http://dew-" + maquina +"-2223.dsicv.upv.es:9090/CentroEducativo/alumnos/"
				+ sesion.getAttribute("dni") + "/asignaturas";

		StringBuilder json = conexionGET(direccion, sesion);

		Gson g = new Gson();
		JsonArray fila = g.fromJson(json.toString(), JsonArray.class);
		String[] res = new String[fila.size()];
		int i = 0;
		for (JsonElement elemento : fila) {
			res[i] = elemento.getAsJsonObject().get("asignatura").getAsString();
			i++;
		}

		return res;
	}

	/*
	 * Return an array of the subjects the loged stundent is registered on
	 */
	public static String[] getTeacherSubjects(HttpSession sesion) throws IOException {
		String direccion = "http://dew-" + maquina +"-2223.dsicv.upv.es:9090/CentroEducativo/profesores/"
				+ sesion.getAttribute("dni") + "/asignaturas";

		StringBuilder json = conexionGET(direccion, sesion);

		Gson g = new Gson();
		JsonArray fila = g.fromJson(json.toString(), JsonArray.class);
		String[] res = new String[fila.size()];
		int i = 0;
		for (JsonElement elemento : fila) {
			res[i] = elemento.getAsJsonObject().get("acronimo").getAsString();
			i++;
		}

		return res;
	}
	/*
	 * Return the mark of the given student by dni in the given subject given by
	 * acronim
	 */
	public static String getStudentMark(HttpSession sesion, String dni, String acronim) throws IOException {
		String direccion = "http://dew-" + maquina +"-2223.dsicv.upv.es:9090/CentroEducativo/alumnos/" + dni
				+ "/asignaturas";

		StringBuilder json = conexionGET(direccion, sesion);
		

		Gson g = new Gson();
		JsonArray fila = g.fromJson(json.toString(), JsonArray.class);

		for (JsonElement elemento : fila) {
			if (elemento.getAsJsonObject().get("asignatura").getAsString().equals(acronim)) {
				return elemento.getAsJsonObject().get("nota").getAsString();
			}

		}

		return null;
	}

	/*
	 * Returns the last name of the given student by dni
	 */
	public static String getLastName(HttpSession sesion, String dni) throws IOException {
		String direccion = "http://dew-" + maquina +"-2223.dsicv.upv.es:9090/CentroEducativo/alumnos/" + dni;

		StringBuilder json = conexionGET(direccion, sesion);

		Gson g = new Gson();
		JsonElement fila = g.fromJson(json.toString(), JsonElement.class);

		return fila.getAsJsonObject().get("apellidos").getAsString();
	}
	/*
	 * Returns the last name of the given student by dni
	 */
	public static String getTeacherLastName(HttpSession sesion, String dni) throws IOException {
		String direccion = "http://dew-" + maquina +"-2223.dsicv.upv.es:9090/CentroEducativo/profesores/" + dni;

		StringBuilder json = conexionGET(direccion, sesion);

		Gson g = new Gson();
		JsonElement fila = g.fromJson(json.toString(), JsonElement.class);

		return fila.getAsJsonObject().get("apellidos").getAsString();
	}

	/*
	 * Returns the name of the student given by dni
	 */
	public static String getName(HttpSession sesion, String dni) throws IOException {
		String direccion = "http://dew-" + maquina +"-2223.dsicv.upv.es:9090/CentroEducativo/alumnos/" + dni;

		StringBuilder json = conexionGET(direccion, sesion);

		Gson g = new Gson();
		JsonElement fila = g.fromJson(json.toString(), JsonElement.class);

		return fila.getAsJsonObject().get("nombre").getAsString();
	}
	/*
	 * Returns the name of the teacher given by dni
	 */
	public static String getTeacherName(HttpSession sesion, String dni) throws IOException {
		String direccion = "http://dew-" + maquina +"-2223.dsicv.upv.es:9090/CentroEducativo/profesores/" + dni;

		StringBuilder json = conexionGET(direccion, sesion);

		Gson g = new Gson();
		JsonElement fila = g.fromJson(json.toString(), JsonElement.class);

		return fila.getAsJsonObject().get("nombre").getAsString();
	}

	/*
	 * Returns an array of all subjects in the center
	 */
	public static String[] getSubjects(HttpSession sesion) throws IOException {
		String direccion = "http://dew-" + maquina +"-2223.dsicv.upv.es:9090/CentroEducativo/asignaturas";

		StringBuilder json = conexionGET(direccion, sesion);

		Gson g = new Gson();
		JsonArray fila = g.fromJson(json.toString(), JsonArray.class);
		String[] res = new String[fila.size()];
		int i = 0;
		for (JsonElement elemento : fila) {
			res[i] = elemento.getAsJsonObject().get("acronimo").getAsString();
			i++;
		}

		return res;
	}

	/*
	 * Return an array of the dni of the students registered in a subject
	 */
	public static String[] getSubjectStudents(HttpSession sesion, String asignatura) throws IOException {
		String direccion = "http://dew-" + maquina +"-2223.dsicv.upv.es:9090/CentroEducativo/asignaturas/" + asignatura
				+ "/alumnos";

		StringBuilder json = conexionGET(direccion, sesion);

		Gson g = new Gson();
		JsonArray fila = g.fromJson(json.toString(), JsonArray.class);
		String[] res = new String[fila.size()];
		int i = 0;
		for (JsonElement elemento : fila) {
			res[i] = elemento.getAsJsonObject().get("alumno").getAsString();
			i++;
		}

		return res;
	}

	
	/*
	 * Return the full name of a subject given his acronim
	 */
	public static String getSubjectByAcronim(HttpSession sesion, String acronim) throws IOException {
		String direccion = "http://dew-" + maquina +"-2223.dsicv.upv.es:9090/CentroEducativo/asignaturas/" + acronim;

		StringBuilder json = conexionGET(direccion, sesion);

		Gson g = new Gson();
		JsonElement fila = g.fromJson(json.toString(), JsonElement.class);

		return fila.getAsJsonObject().get("nombre").getAsString();
	}


	/*
	 *	Sets the given student mark on the given subject
	 *	Return "OK" if the mark is set properly 
	 */
	public static String putStudentMark(HttpSession sesion, String studentDNI, String nota, String asignatura)
	        throws IOException {
	    String direccion = "http://dew-" + maquina +"-2223.dsicv.upv.es:9090/CentroEducativo/alumnos/" + studentDNI+ "/asignaturas/" + asignatura + "?key=" + sesion.getAttribute("key");

	        URL url = new URL(direccion);
	        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();

	        for (String cookie : (List<String>) sesion.getAttribute("cookie")) {
	            conexion.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
	        }

	        conexion.setDoOutput(true);
	        conexion.setRequestMethod("PUT");
	        conexion.setRequestProperty("accept", "*/*");
	        conexion.setRequestProperty("Content-Type", "application/json");

	        // Enviamos JSON
	        OutputStream os = conexion.getOutputStream();
	        os.write(nota.getBytes());
	        os.flush();
	        os.close();

	        InputStreamReader perico = new InputStreamReader(conexion.getInputStream());
	        BufferedReader buffer = new BufferedReader(perico);

	        String res = "";
	        try (buffer) {
	            String lineaRespuesta = "";

	            while ((lineaRespuesta = buffer.readLine()) != null) {
	                res += lineaRespuesta.trim();
	            }
	        }

	        return res;
	    }

}