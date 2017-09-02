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
	private String url = "jdbc:sqlite:C:/Users/leodr/libros.db";

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
			conexion = DriverManager.getConnection(url);

			pStmInsertLibro = conexion.prepareStatement(insertLibro);
			pStmUpdateLibro = conexion.prepareStatement(updateLibro);
			pStmDeleteLibro = conexion.prepareStatement(deleteLibro);
			pStmGetLibro = conexion.prepareStatement(getLibro);
			pStmGetAll = conexion.prepareStatement(getAll);
			pStmSearchLibro = conexion.prepareStatement(searchLibro);

			pStmInsertAutor = conexion.prepareStatement(insertAutor);
			pStmUpdateAutor = conexion.prepareStatement(updateAutor);
			pStmDeleteAutor = conexion.prepareStatement(deleteAutor);
			pStmGetAutores = conexion.prepareStatement(getAutores);
			pStmSearchAutor = conexion.prepareStatement(searchAutor);

			pStmInsertEditorial = conexion.prepareStatement(insertEditorial);
			pStmUpdateEditorial = conexion.prepareStatement(updateEditorial);
			pStmDeleteEditorial = conexion.prepareStatement(deleteEditorial);
			pStmGetEditoriales = conexion.prepareStatement(getEditoriales);
			pStmSearchEditorial = conexion.prepareStatement(searchEditorial);

			pStmInsertUsuario = conexion.prepareStatement(insertUsuario);
			pStmDeleteUsuario = conexion.prepareStatement(deleteUsuario);
			pStmVerificarLogin = conexion.prepareStatement(verificarLogin);

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al establecer la conexión con la base de datos.");
			if (conexion != null) {
				try {
					close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}
	}

	public int insertLibro(String ISBN, String titulo, String autor, String editorial, int edicion, int anioPublicacion)
			throws SQLException {
		pStmInsertLibro.setString(1, ISBN);
		pStmInsertLibro.setString(2, titulo);
		pStmInsertLibro.setString(3, autor);
		pStmInsertLibro.setString(4, editorial);
		pStmInsertLibro.setInt(5, edicion);
		pStmInsertLibro.setInt(6, anioPublicacion);

		return pStmInsertLibro.executeUpdate();
	}

	public int updateLibro(String ISBN, String titulo, String autor, String editorial, int edicion, int anioPublicacion)
			throws SQLException {
		pStmUpdateLibro.setString(1, titulo);
		pStmUpdateLibro.setString(2, autor);
		pStmUpdateLibro.setString(3, editorial);
		pStmUpdateLibro.setInt(4, edicion);
		pStmUpdateLibro.setInt(5, anioPublicacion);
		pStmUpdateLibro.setString(6, ISBN);

		return pStmUpdateLibro.executeUpdate();
	}

	public int deleteLibro(String ISBN) throws SQLException {
		pStmDeleteLibro.setString(1, ISBN);

		return pStmDeleteLibro.executeUpdate();
	}

	public Libro getLibro(String ISBN) throws SQLException {
		pStmGetLibro.setString(1, ISBN);

		ResultSet rs = pStmGetLibro.executeQuery();

		rs.next();

		return new Libro(rs.getString("ISBN"), rs.getString("Titulo"), rs.getString("Autor"), rs.getString("Editorial"),
				rs.getInt("Edicion"), rs.getInt("AnioPublicacion"));

	}

	public List<Libro> getAll(String order) throws SQLException {
		List<Libro> libros = new LinkedList<Libro>();

		ResultSet rs = pStmGetAll.executeQuery();

		while (rs.next()) {
			libros.add(new Libro(rs.getString("ISBN"), rs.getString("Titulo"), rs.getString("Autor"),
					rs.getString("Editorial"), rs.getInt("Edicion"), rs.getInt("AnioPublicacion")));
		}

		ordenarResultado(order, libros);

		return libros;
	}

	public List<Libro> searchLibro(String buscar, String autor, String editorial, String order) throws SQLException {
		buscar = "%" + buscar + "%";

		if (autor.equals("Autor") || autor.equals(""))
			autor = "%";
		if (editorial.equals("Editorial") || editorial.equals(""))
			editorial = "%";

		pStmSearchLibro.setString(1, buscar);
		pStmSearchLibro.setString(2, buscar);
		pStmSearchLibro.setString(3, autor);
		pStmSearchLibro.setString(4, editorial);

		List<Libro> libros = new LinkedList<Libro>();
		ResultSet rs = pStmSearchLibro.executeQuery();

		while (rs.next()) {
			libros.add(new Libro(rs.getString("ISBN"), rs.getString("Titulo"), rs.getString("Autor"),
					rs.getString("Editorial"), rs.getInt("Edicion"), rs.getInt("AnioPublicacion")));
		}

		ordenarResultado(order, libros);

		return libros;
	}

	public int insertAutor(String nombre) throws SQLException {
		pStmInsertAutor.setString(1, nombre);
		return pStmInsertAutor.executeUpdate();
	}

	public int updateAutor(int id, String nombre) throws SQLException {
		pStmUpdateAutor.setString(1, nombre);
		pStmUpdateAutor.setInt(2, id);
		return pStmUpdateAutor.executeUpdate();
	}

	public int deleteAutor(int id) throws SQLException {
		pStmDeleteAutor.setInt(1, id);
		return pStmDeleteAutor.executeUpdate();
	}

	public List<Entidad> getAutores() throws SQLException {
		List<Entidad> autores = new LinkedList<Entidad>();
		ResultSet rs = pStmGetAutores.executeQuery();

		while (rs.next()) {
			autores.add(new Entidad(rs.getInt("ID"), rs.getString("Descripcion")));
		}

		return autores;
	}
	
	public List<Entidad> searchAutor(String buscar) throws SQLException {
		buscar = "%" + buscar + "%";

		pStmSearchAutor.setString(1, buscar);

		List<Entidad> autores = new LinkedList<Entidad>();
		ResultSet rs = pStmSearchAutor.executeQuery();

		while (rs.next()) {
			autores.add(new Entidad(rs.getInt("ID"), rs.getString("Descripcion")));
		}

		return autores;
	}

	public int insertEditorial(String nombre) throws SQLException {
		pStmInsertEditorial.setString(1, nombre);
		return pStmInsertEditorial.executeUpdate();
	}

	public int updateEditorial(int id, String nombre) throws SQLException {
		pStmUpdateEditorial.setString(1, nombre);
		pStmUpdateEditorial.setInt(2, id);
		return pStmUpdateEditorial.executeUpdate();
	}

	public int deleteEditorial(int id) throws SQLException {
		pStmDeleteEditorial.setInt(1, id);
		return pStmDeleteEditorial.executeUpdate();
	}

	public List<Entidad> getEditoriales() throws SQLException {
		List<Entidad> editoriales = new LinkedList<Entidad>();
		ResultSet rs = pStmGetEditoriales.executeQuery();

		while (rs.next()) {
			editoriales.add(new Entidad(rs.getInt("ID"), rs.getString("Descripcion")));
		}

		return editoriales;
	}
	
	public List<Entidad> searchEditorial(String buscar) throws SQLException {
		buscar = "%" + buscar + "%";

		pStmSearchEditorial.setString(1, buscar);

		List<Entidad> editoriales = new LinkedList<Entidad>();
		ResultSet rs = pStmSearchEditorial.executeQuery();

		while (rs.next()) {
			editoriales.add(new Entidad(rs.getInt("ID"), rs.getString("Descripcion")));
		}

		return editoriales;
	}

	public int insertUsuario(String user, String password) throws SQLException {
		pStmInsertUsuario.setString(1, user);
		pStmInsertUsuario.setString(2, password);
		return pStmInsertUsuario.executeUpdate();
	}

	public int deleteUsuario(String user) throws SQLException {
		pStmDeleteUsuario.setString(1, user);
		return pStmDeleteUsuario.executeUpdate();
	}

	public boolean verificarLogin(String user, String password) throws SQLException {
		pStmVerificarLogin.setString(1, user);
		pStmVerificarLogin.setString(2, Encriptar.md5(password));
		
		ResultSet rs;
		
		if ((rs = pStmVerificarLogin.executeQuery()).next()) 
			return true;

		return false;
	}

	public void ordenarResultado(String criterio, List<Libro> libros) {
		if (criterio.equals("ISBN"))
			libros.sort(Comparator.comparing(Libro::getISBN));
		if (criterio.equals("Titulo"))
			libros.sort(Comparator.comparing(Libro::getTitulo));
		if (criterio.equals("Autor"))
			libros.sort(Comparator.comparing(Libro::getAutor));
		if (criterio.equals("Editorial"))
			libros.sort(Comparator.comparing(Libro::getEditorial));
		if (criterio.equals("Edicion"))
			libros.sort(Comparator.comparing(Libro::getEdicion));
		if (criterio.equals("AnioPublicacion"))
			libros.sort(Comparator.comparing(Libro::getAnioPublicacion));
	}

	public void close() throws SQLException {
		pStmInsertLibro.close();
		pStmUpdateLibro.close();
		pStmDeleteLibro.close();
		pStmGetLibro.close();
		pStmGetAll.close();
		conexion.close();
	}

}
