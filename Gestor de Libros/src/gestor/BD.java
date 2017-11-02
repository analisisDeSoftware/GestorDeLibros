package gestor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import login.Encriptar;

public class BD {

	private Connection conexion = null;
	private String url = "jdbc:sqlite:libros.db";

	private PreparedStatement pStmInsertLibro;
	private PreparedStatement pStmUpdateLibro;
	private PreparedStatement pStmDeleteLibro;
	private PreparedStatement pStmGetLibro;
	private PreparedStatement pStmGetAll;
	private PreparedStatement pStmSearchLibro;

	private PreparedStatement pStmInsertAutor;
	private PreparedStatement pStmUpdateAutor;
	private PreparedStatement pStmDeleteAutor;
	private PreparedStatement pStmGetAutores;
	private PreparedStatement pStmSearchAutor;

	private PreparedStatement pStmInsertEditorial;
	private PreparedStatement pStmUpdateEditorial;
	private PreparedStatement pStmDeleteEditorial;
	private PreparedStatement pStmGetEditoriales;
	private PreparedStatement pStmSearchEditorial;

	private PreparedStatement pStmInsertUsuario;
	private PreparedStatement pStmDeleteUsuario;
	private PreparedStatement pStmVerificarLogin;

	private String insertLibro = "INSERT INTO LIBRO(ISBN, Titulo, Autor, Editorial, Edicion, AnioPublicacion) VALUES(?, ?, ?, ?, ?, ?) ";
	private String updateLibro = "UPDATE LIBRO SET Titulo = ?, Autor = ?, Editorial = ?, Edicion = ?, AnioPublicacion = ? WHERE ISBN = ?";
	private String deleteLibro = "DELETE FROM LIBRO WHERE ISBN = ?";
	private String getLibro = "SELECT * FROM LIBRO WHERE ISBN = ?";
	private String getAll = "SELECT * FROM LIBRO";
	private String searchLibro = "SELECT * FROM LIBRO WHERE (ISBN LIKE ? OR Titulo LIKE ?) AND Autor Like ? AND Editorial LIKE ?";

	private String insertAutor = "INSERT INTO AUTOR(Descripcion) VALUES(?)";
	private String updateAutor = "UPDATE AUTOR SET Descripcion = ? WHERE ID = ?";
	private String deleteAutor = "DELETE FROM AUTOR WHERE ID = ?";
	private String getAutores = "SELECT * FROM AUTOR";
	private String searchAutor = "SELECT * FROM AUTOR WHERE Descripcion LIKE ?";

	private String insertEditorial = "INSERT INTO EDITORIAL(Descripcion) VALUES(?)";
	private String updateEditorial = "UPDATE EDITORIAL SET Descripcion = ? WHERE ID = ?";
	private String deleteEditorial = "DELETE FROM EDITORIAL WHERE ID = ?";
	private String getEditoriales = "SELECT * FROM EDITORIAL";
	private String searchEditorial = "SELECT * FROM EDITORIAL WHERE Descripcion LIKE ?";

	private String insertUsuario = "INSERT INTO USUARIO(ID, Password) VALUES(?, ?)";
	private String deleteUsuario = "DELETE FROM USUARIO WHERE ID = ?";
	private String verificarLogin = "SELECT * FROM USUARIO WHERE ID = ? AND Password = ?";

	public BD() {
		try {
			conexion = DriverManager.getConnection(url); // Establecemos la conexion con la BD
			pStmInsertLibro = conexion.prepareStatement(insertLibro); // PreparedStatement para insertar un libro
			pStmUpdateLibro = conexion.prepareStatement(updateLibro); // PreparedStatement para actualizar un libro
			pStmDeleteLibro = conexion.prepareStatement(deleteLibro); // PreparedStatement para eliminar un libro
			pStmGetLibro = conexion.prepareStatement(getLibro); // PreparedStatement para recuperar un libro
			pStmGetAll = conexion.prepareStatement(getAll); // PreparedStatement para recuperar todos los libros
			pStmSearchLibro = conexion.prepareStatement(searchLibro); // PreparedStatement para buscar un libro

			pStmInsertAutor = conexion.prepareStatement(insertAutor); // PreparedStatement para insertar un autor
			pStmUpdateAutor = conexion.prepareStatement(updateAutor); // PreparedStatement para actualizar un autor
			pStmDeleteAutor = conexion.prepareStatement(deleteAutor); // PreparedStatement para eliminar un autor
			pStmGetAutores = conexion.prepareStatement(getAutores); // PreparedStatement para recuperar los autores
			pStmSearchAutor = conexion.prepareStatement(searchAutor); // PreparedStatement para buscar un autor

			pStmInsertEditorial = conexion.prepareStatement(insertEditorial); // PreparedStatement para insertar una editorial
			pStmUpdateEditorial = conexion.prepareStatement(updateEditorial); // PreparedStatement para actualizar una editorial
			pStmDeleteEditorial = conexion.prepareStatement(deleteEditorial);// PreparedStatement para eliminar una editorial
			pStmGetEditoriales = conexion.prepareStatement(getEditoriales); // PreparedStatement para recuperar una editorial
			pStmSearchEditorial = conexion.prepareStatement(searchEditorial); // PreparedStatement para buscar una editorial

			pStmInsertUsuario = conexion.prepareStatement(insertUsuario); // PreparedStatement para insertar un usuario
			pStmDeleteUsuario = conexion.prepareStatement(deleteUsuario); // PreparedStatement para eliminar un usuario
			pStmVerificarLogin = conexion.prepareStatement(verificarLogin); // PreparedStatement para verificar un login

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al establecer la conexión con la base de datos.");
			if (conexion != null) { // Si existe una conexion con la BD
				try {
					close(); // Cerramos las conexiones
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}
	}

	public int insertLibro(String ISBN, String titulo, String autor, String editorial, int edicion, int anioPublicacion)
			throws SQLException {
		pStmInsertLibro.setString(1, ISBN); // Seteamos el ISBN
		pStmInsertLibro.setString(2, titulo); // Seteamos el titulo
		pStmInsertLibro.setString(3, autor); // Seteamos el autor
		pStmInsertLibro.setString(4, editorial); // Seteamos la editorial
		pStmInsertLibro.setInt(5, edicion); // Seteamos la edicion
		pStmInsertLibro.setInt(6, anioPublicacion); // Seteamos el anio de publicacion
		
		return pStmInsertLibro.executeUpdate(); // Ejecutamos la query
	}

	public int updateLibro(String ISBN, String titulo, String autor, String editorial, int edicion, int anioPublicacion)
			throws SQLException {
		pStmUpdateLibro.setString(1, titulo); // Seteamos el titulo
		pStmUpdateLibro.setString(2, autor); // Seteamos el autor
		pStmUpdateLibro.setString(3, editorial); // Seteamos la editorial
		pStmUpdateLibro.setInt(4, edicion); // Seteamos la edicion
		pStmUpdateLibro.setInt(5, anioPublicacion); // Seteamos el anio de publicacion
		pStmUpdateLibro.setString(6, ISBN); // Seteamos el ISBN

		return pStmUpdateLibro.executeUpdate(); // Ejecutamos la query
	}

	public int deleteLibro(String ISBN) throws SQLException {
		pStmDeleteLibro.setString(1, ISBN); // Seteamos el ISBN

		return pStmDeleteLibro.executeUpdate(); // Ejecutamos la query
	}

	public Libro getLibro(String ISBN) throws SQLException {
		pStmGetLibro.setString(1, ISBN); // Seteamos el ISBN

		ResultSet rs = pStmGetLibro.executeQuery(); // Ejectuamos la query

		rs.next(); // Nos posicionamos en el primer elemento

		return new Libro(rs.getString("ISBN"), rs.getString("Titulo"), rs.getString("Autor"), rs.getString("Editorial"),
				rs.getInt("Edicion"), rs.getInt("AnioPublicacion")); // Retornamos el libro

	}

	public List<Libro> getAll(String order) throws SQLException {
		List<Libro> libros = new LinkedList<Libro>(); // Creamos una lista para almacenar los libros

		ResultSet rs = pStmGetAll.executeQuery(); // Ejecutamos la query

		while (rs.next()) { // Recorremos los libros y los agregamos a la lista
			libros.add(new Libro(rs.getString("ISBN"), rs.getString("Titulo"), rs.getString("Autor"),
					rs.getString("Editorial"), rs.getInt("Edicion"), rs.getInt("AnioPublicacion")));
		}

		ordenarResultado(order, libros); // Ordenamos los libros

		return libros; // Retornamos la lista
	}

	public List<Libro> searchLibro(String buscar, String autor, String editorial, String order) throws SQLException {
		buscar = "%" + buscar + "%"; // Seteamos los comodines

		if (autor.equals("Autor") || autor.equals("")) // No hay autor
			autor = "%";
		if (editorial.equals("Editorial") || editorial.equals("")) // No hay editorial
			editorial = "%";

		pStmSearchLibro.setString(1, buscar); // Seteamos los comodines
		pStmSearchLibro.setString(2, buscar); // Seteamos los comodines
		pStmSearchLibro.setString(3, autor); // Seteamos el autor
		pStmSearchLibro.setString(4, editorial); // Seteamos la editorial

		List<Libro> libros = new LinkedList<Libro>(); // Creamos una lista para almacenar los libros
		ResultSet rs = pStmSearchLibro.executeQuery(); // Ejecutamos la query

		while (rs.next()) { // Recorremos el resultado y agregamos los libros a la lista
			libros.add(new Libro(rs.getString("ISBN"), rs.getString("Titulo"), rs.getString("Autor"),
					rs.getString("Editorial"), rs.getInt("Edicion"), rs.getInt("AnioPublicacion")));
		}

		ordenarResultado(order, libros); // Ordenamos los libros

		return libros; // Retornamos la lista
	}

	public int insertAutor(String nombre) throws SQLException {
		pStmInsertAutor.setString(1, nombre); // Seteamos el nombre
		return pStmInsertAutor.executeUpdate(); // Ejecutamos la query
	}

	public int updateAutor(int id, String nombre) throws SQLException {
		pStmUpdateAutor.setString(1, nombre); // Seteamos el nombre
		pStmUpdateAutor.setInt(2, id); // Seteamos el id
		return pStmUpdateAutor.executeUpdate(); // Ejecutamos la query
	}

	public int deleteAutor(int id) throws SQLException {
		pStmDeleteAutor.setInt(1, id); // Seteamos el id
		return pStmDeleteAutor.executeUpdate(); // Ejecutamos la query
	}

	public List<Entidad> getAutores() throws SQLException {
		List<Entidad> autores = new LinkedList<Entidad>(); // Creamos una lista para almacenar los autores
		ResultSet rs = pStmGetAutores.executeQuery(); // Ejecutamos la query

		while (rs.next()) { // Recorremos el resultado y agregamos los autores a la lista
			autores.add(new Entidad(rs.getInt("ID"), rs.getString("Descripcion")));
		}

		return autores; // Retornamos la lista
	}
	
	public List<Entidad> searchAutor(String buscar) throws SQLException {
		buscar = "%" + buscar + "%"; // Seteamos los comodines

		pStmSearchAutor.setString(1, buscar); // Seteamos el comodin

		List<Entidad> autores = new LinkedList<Entidad>(); // Creamos una lista para almacenar los autores
		ResultSet rs = pStmSearchAutor.executeQuery(); // Ejecutamos la query

		while (rs.next()) { // Recorremos el resultado y agregamos los autores a la lista
			autores.add(new Entidad(rs.getInt("ID"), rs.getString("Descripcion")));
		}

		return autores; // Retornamos la lista
	}

	public int insertEditorial(String nombre) throws SQLException {
		pStmInsertEditorial.setString(1, nombre); // Seteamos el nombre
		return pStmInsertEditorial.executeUpdate(); // Ejecutamos la query
	}

	public int updateEditorial(int id, String nombre) throws SQLException {
		pStmUpdateEditorial.setString(1, nombre); // Seteamos el nombre
		pStmUpdateEditorial.setInt(2, id); // Seteamos el id
		return pStmUpdateEditorial.executeUpdate(); // Ejecutamos la query
	}

	public int deleteEditorial(int id) throws SQLException {
		pStmDeleteEditorial.setInt(1, id); // Seteamos el id
		return pStmDeleteEditorial.executeUpdate(); // Ejecutamos la query
	}

	public List<Entidad> getEditoriales() throws SQLException {
		List<Entidad> editoriales = new LinkedList<Entidad>(); // Creamos una lista para almacenar las editoriales
		ResultSet rs = pStmGetEditoriales.executeQuery(); // Ejecutamos la query

		while (rs.next()) { // Recorremos el resultado y agregamos las editoriales a la lista
			editoriales.add(new Entidad(rs.getInt("ID"), rs.getString("Descripcion")));
		}

		return editoriales; // Retornamos la lista
	}
	
	public List<Entidad> searchEditorial(String buscar) throws SQLException {
		buscar = "%" + buscar + "%"; // Seteamos los comodines

		pStmSearchEditorial.setString(1, buscar);

		List<Entidad> editoriales = new LinkedList<Entidad>(); // Creamos una lista para almacenar las editoriales
		ResultSet rs = pStmSearchEditorial.executeQuery(); // Ejecutamos la query

		while (rs.next()) { // Recorremos el resultado y agregamos las editoriales a la lista
			editoriales.add(new Entidad(rs.getInt("ID"), rs.getString("Descripcion")));
		}

		return editoriales; // Retornamos la lista
	}

	public int insertUsuario(String user, String password) throws SQLException {
		pStmInsertUsuario.setString(1, user); // Seteamos el user
		pStmInsertUsuario.setString(2, password); // Seteamos el password
		return pStmInsertUsuario.executeUpdate(); // Ejecutamos la query
	}

	public int deleteUsuario(String user) throws SQLException {
		pStmDeleteUsuario.setString(1, user); // Seteamos el usar
		return pStmDeleteUsuario.executeUpdate(); // Ejecutamos la query
	}

	public boolean verificarLogin(String user, String password) throws SQLException {
		pStmVerificarLogin.setString(1, user); // Seteamos el usar
		pStmVerificarLogin.setString(2, Encriptar.md5(password)); // Seteamos el password
		
		ResultSet rs;
		
		if ((rs = pStmVerificarLogin.executeQuery()).next()) // Si los datos ingresados machean con los datos de la BD
			return true;

		return false;
	}

	public void ordenarResultado(String criterio, List<Libro> libros) {
		if (criterio.equals("ISBN")) // Si el criterio de ordenamiento es el ISBN
			libros.sort(Comparator.comparing(Libro::getISBN));
		if (criterio.equals("Titulo")) // Si el criterio de ordenamiento es el titulo
			libros.sort(Comparator.comparing(Libro::getTitulo));
		if (criterio.equals("Autor")) // Si el criterio de ordenamiento es el autor
			libros.sort(Comparator.comparing(Libro::getAutor));
		if (criterio.equals("Editorial")) // Si el criterio de ordenamiento es la editorial
			libros.sort(Comparator.comparing(Libro::getEditorial));
		if (criterio.equals("Edicion")) // Si el criterio de ordenamiento es la edicion
			libros.sort(Comparator.comparing(Libro::getEdicion));
		if (criterio.equals("AnioPublicacion")) // Si el criterio de ordenamiento es el anio de publicacion
			libros.sort(Comparator.comparing(Libro::getAnioPublicacion));
	}

	public void close() throws SQLException {
		pStmInsertLibro.close(); // Cerramos el preparedStatement para insertar un libro
		pStmUpdateLibro.close(); // Cerramos el preparedStatement para actualizar un libro
		pStmDeleteLibro.close(); // Cerramos el preparedStatement para eliminar un libro
		pStmGetLibro.close(); // Cerramos el preparedStatement para recuperar un libro
		pStmGetAll.close(); // Cerramos el preparedStatement para recuperar todos los libros
		pStmSearchLibro.close(); // Cerramos el preparedStatement para buscar un libro
		pStmInsertAutor.close(); // Cerramos el preparedStatement para insertar un autor
		pStmUpdateAutor.close(); // Cerramos el preparedStatement para actualizar un autor
		pStmDeleteAutor.close(); // Cerramos el preparedStatement para eliminar un autor
		pStmGetAutores.close(); // Cerramos el preparedStatement para recuperar los autores
		pStmSearchAutor.close(); // Cerramos el preparedStatement para buscar un autor
		pStmInsertEditorial.close(); // Cerramos el preparedStatement para eliminar una editorial
		pStmUpdateEditorial.close(); // Cerramos el preparedStatement para actualizar una editorial
		pStmDeleteEditorial.close(); // Cerramos el preparedStatement para eliminar una editorial
		pStmGetEditoriales.close(); // Cerramos el preparedStatement para recuperar las editoriales
		pStmSearchEditorial.close(); // Cerramos el preparedStatement para buscar una editorial
		pStmInsertUsuario.close(); // Cerramos el preparedStatement para insertar un user
		pStmDeleteUsuario.close(); // Cerramos el preparedStatement para eliminar un user
		pStmVerificarLogin.close(); // Cerramos el preparedStatement para verificar un login
		conexion.close(); // Cerramos la conexion con la BD
	}

}
