package gestor;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Gestor extends JFrame {

	// Generales

	private JPanel contentPane;
	
	private BD bd;

	private String ISBN;
	private String titulo;
	private String autor;
	private String editorial;
	private int edicion;
	private int anioPublicacion;

	private String regexISBN = "\\d{13}";
	private String regexTitulo = "\\w+\\s*(\\w+\\s*)*";
	private String regexAutor = "[a-zA-z]+\\s*([a-zA-z]+\\s*)*";
	private String regexEditorial = "[a-zA-z]+\\s*([a-zA-z]+\\s*)*";
	private String regexEdicion = "\\d+";
	private String regexAnio = "\\d+";

	// Inicio

	private JPanel paneInicioVisible;
	private JTable tableLibros;
	private DefaultTableModel tableModelLibros;
	private JTextField textBuscarLibro;
	private JComboBox<String> comboAutores;
	private JComboBox<String> comboEditoriales;
	private JLabel lblOrdenar;
	private JComboBox<?> comboOrdenarPor;
	private JTextField textISBN;
	private JTextField textTitulo;
	private JComboBox<String> comboAutor;
	private JComboBox<String> comboEditorial;
	private JTextField textEdicion;
	private JTextField textAnioPublicacion;
	private JButton btnAgregarLibro;
	private JButton btnModificarLibro;
	private JButton btnEliminarLibro;
	private String[] filaVaciaLibro = { "", "", "", "", "", "" };
	private final int cantidadColumnasLibros = 6;

	// Administracion

	private JPanel paneAdministracionVisible;
	private JTable tableAutores;
	private JTable tableEditoriales;
	private DefaultTableModel tableModelAutores;
	private DefaultTableModel tableModelEditoriales;
	private JTextField textBuscarAutor;
	private JTextField textBuscarEditorial;
	private JTextField textAutor;
	private JTextField textEditorial;
	private JButton btnAgregarAutor;
	private JButton btnAgregarEditorial;
	private JButton btnModificarAutor;
	private JButton btnModificarEditorial;
	private JButton btnEliminarAutor;
	private JButton btnEliminarEditorial;
	private String[] filaVaciaEntidad = { "", "" };
	private final int cantidadColumnasEntidad = 2;

	// Menu

	private JPanel panelInicio;
	private JPanel panelInicioSeleccionado;
	private JPanel panelAdministracion;
	private JPanel panelAdministracionSeleccionado;
	private JLabel lblInicio;
	private JLabel labelInicioSeleccionado;
	private JLabel lblAdministracion;
	private JLabel labelAdministracionSeleccionado;
	private int posXIni = 0;
	private int posYIni = 121;
	private int posXAdmin = 0;
	private int posYAdmin = 170;

	

	public Gestor() {
		
		// Cambiamos el cierre de ventana por defecto
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					bd.close(); // Cerramos las conexiones de la BD
				} catch (SQLException e1) {
					informar("Error al cerrar la conexi�n con la base de datos."); // Informamos si hubo un error en el cierre de las conexiones
					e1.printStackTrace();
				}
				finally {
					// Cerramos la aplicacion
					setVisible(false); 
					dispose();
				}
			}
		});
		
		// Configuracion de la ventana
		setBounds(100, 100, 1340, 740);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		bd = new BD(); // Instanciamos una BD para el almacenamiento y recuperacion de la informacion

		// Cargamos las ventanas y el menu de la aplicacion
		cargarInicio();
		cargarAdministracion();
		cargarMenu();

		// Recuperamos los autores y editoriales de la base de datos
		cargarAutoresYEditoriales();
		
		// Cargamos los listeneres necesarios
		cargarListeners();

		// Mostramos la ventana de inicio y el efecto del menu
		panelInicio.setVisible(false);
		panelInicioSeleccionado.setVisible(true);
		panelAdministracion.setVisible(true);
		panelAdministracionSeleccionado.setVisible(false);
		panelAdministracionSeleccionado.setEnabled(false);

		paneAdministracionVisible.setVisible(false);
		paneInicioVisible.setVisible(true);
	}
	
	

	/*--------------------------------------------------------- Libros ----------------------------------------------------------------*/
	public void agregarFilaLibro(Libro libro) {
		int fila = tableModelLibros.getRowCount(), columna = cantidadColumnasLibros - 1;
		tableModelLibros.addRow(filaVaciaLibro); // Agregamos una fila a la tabla de libros
		// Seteamos los valores del libro en la fila nueva
		tableLibros.setValueAt(libro.getAnioPublicacion(), fila, columna);
		tableLibros.setValueAt(libro.getEdicion(), fila, columna -= 1);
		tableLibros.setValueAt(libro.getEditorial(), fila, columna -= 1);
		tableLibros.setValueAt(libro.getAutor(), fila, columna -= 1);
		tableLibros.setValueAt(libro.getTitulo(), fila, columna -= 1);
		tableLibros.setValueAt(libro.getISBN(), fila, columna -= 1);
	}

	public void llenarCamposLibro(int fila) {
		int columna = cantidadColumnasLibros - 1;
		// Cargamos los valores del libro seleccioando en los campos correspondientes
		textAnioPublicacion.setText(tableModelLibros.getValueAt(fila, columna).toString());
		textEdicion.setText(tableModelLibros.getValueAt(fila, columna -= 1).toString());
		comboEditorial.setSelectedItem(tableModelLibros.getValueAt(fila, columna -= 1).toString());
		comboAutor.setSelectedItem((tableModelLibros.getValueAt(fila, columna -= 1).toString()));
		textTitulo.setText(tableModelLibros.getValueAt(fila, columna -= 1).toString());
		textISBN.setText(tableModelLibros.getValueAt(fila, columna -= 1).toString());
	}
	
	public boolean recuperarInformacionLibro() {
		if (validarCamposLibro()) { // Validamos los campos del libro antes de su insercion
			ISBN = textISBN.getText();
			titulo = textTitulo.getText();
			autor = comboAutor.getSelectedItem().toString();
			editorial = comboEditorial.getSelectedItem().toString();
			edicion = Integer.valueOf(textEdicion.getText());
			anioPublicacion = Integer.valueOf(textAnioPublicacion.getText());
			return true; // Si los campos son correctos podemos insertar el libro
		}

		return false; // Alguno de los campos no es valido
	}
	
	public void actualizarTablaLibros() {
		limpiarTablaLibros(); // Vaciamos la tabla de libros
		buscarLibro("");	// Limpiamos el campo buscar para que se actualice la tabla de libros
	}
	
	public void limpiarTablaLibros() {
		while (tableModelLibros.getRowCount() > 0) { // Removemos fila a fila la tabla de libros
			tableModelLibros.removeRow(0);
		}
		tableLibros.clearSelection(); // Limpiamos la seleccion de un libro en la tabla
	}

	public void limpiarCamposLibro() {
		textAnioPublicacion.setText("");
		textEdicion.setText("");
		comboEditorial.setSelectedItem("Editorial");
		comboAutor.setSelectedItem("Autor");
		textTitulo.setText("");
		textISBN.setText("");
	}

	public void buscarLibro(String buscar) {
		List<Libro> libros = null; // Creamos una lista de libros para almacenar los libros de la base de datos
		limpiarTablaLibros(); // Limpiamos la tabla de libros para llenarla con los libros encontrados
		try {
			if (!buscar.equals("") && !(libros = bd.searchLibro(buscar, "", "", "ISBN")).isEmpty()) { // Si el parametro es "" hay que traer todos los libros de la BD
				for (Libro l : libros) { // Agregamos tantas filas como libros hayamos encontrado
					agregarFilaLibro(l);
				}
				tableLibros.setRowSelectionInterval(0, 0); // Seleccionamos en la tabla el primer libro encontrado 
				llenarCamposLibro(0); // Llenamos las filas previamente cargadas con la informacion de los libros encontrados
				return;
			}
			if(textBuscarLibro.getText() != null && comboAutores.getSelectedItem() != null && comboEditoriales.getSelectedItem() != null ) { // Si el parametro no es "" entonces hay una condicion de busqueda
				libros = bd.searchLibro(textBuscarLibro.getText(), comboAutores.getSelectedItem().toString(), // Realizamos la busqueda de los libros en la BD
						comboEditoriales.getSelectedItem().toString(), comboOrdenarPor.getSelectedItem().toString());
				for (Libro l : libros) { // Agregamos tantas filas como libros hayamos encontrado
					agregarFilaLibro(l);
				}
			}
		} catch (SQLException e) {
			informar("Se ha producido un error al realizar la b�squeda del libro."); // Informamos que hubo un error al recuperar los libros de la BD
			e.printStackTrace();
		}
	}
	
	public void traerTodosLosLibros() {
		List<Libro> libros = null; //  Creamos una lista de libros para almacenar los libros de la base de datos
		try {
			libros = bd.getAll(comboOrdenarPor.getSelectedItem().toString()); // Obtenemos todos los libros de la BD

			for (Libro l : libros) { // Agregamos tantas filas como libros hayamos encontrado
				agregarFilaLibro(l);
			}

		} catch (SQLException e) {
			informar("Se ha producido un error al buscar los libros en la base de datos."); // Informamos que hubo un error al recuperar los libros de la BD
			e.printStackTrace();
		}
	}

	public void insertarLibro() {
		try {
			boolean camposCorrectos = recuperarInformacionLibro(); // Verificamos si los campos son correctos
			if (camposCorrectos && bd.insertLibro(ISBN, titulo, autor, editorial, edicion, anioPublicacion) == 1) { // Si los libros son correctos y la ejecucion de la query es exitosa
				informar("El libro se ha insertado con �xito."); // Informamos al usuario que el libro se inerto con exito
				actualizarTablaLibros(); // Actualizamos la tabla de libros para que se vea reflejada la nueva insercion
			}
		} catch (SQLException e) { // En caso de que la insercion en la BD falle
			informar("Se ha producido un error al agregar el libro."); // Informamos que hubo un error en la insercion
			try {
				bd.close(); // Cerramos las conexiones de la BD
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			bd = new BD(); // Creamos nuevamente las conexiones con la BD
			e.printStackTrace();
		}
	}

	public void modificarLibro() {
		try {
			if(recuperarInformacionLibro()) { // Si los datos del libro ingresados son correctos
				bd.updateLibro(ISBN, titulo, autor, editorial, edicion, anioPublicacion); // Actualizamos el libro en la BD
				informar("Se ha actualizado el libro con �xito."); // Informamos al usuario que se ha actualizado el libro correctamente
				actualizarTablaLibros(); // Actualizamos la tabla para que se vea reflajada la nueva actualizacion
			}
		} catch (SQLException e) {
			informar("Se ha producido un error al actualizar el libro."); // Informamos que la ejecucion de la query fallo
			e.printStackTrace();
		}
	}

	public void eliminarLibro() {
		try {
			bd.deleteLibro(textISBN.getText().toString()); // Borramos el libro de la BD
			informar("Se ha eliminado el libro con �xito."); // Informamos al usuario que el libro se elimino correctamente
			actualizarTablaLibros(); // Actualizamos la tabla para que se vea reflajada la nueva actualizacion
		} catch (SQLException e) {
			informar("Se ha producido un error al eliminar el libro.");
			e.printStackTrace();
		}
	}

	public boolean validarCamposLibro() {
		if (!textISBN.getText().matches(regexISBN)) { // Validamos que el ISBN cumpla con regexISBN
			informar("El ISBN ingresado es invalido.");
			return false;
		}
		if (!textTitulo.getText().matches(regexTitulo)) { // Validamos que el titulo cumpla con regexTitulo
			informar("El t�tulo ingresado es invalido.");
			return false;
		}
		if (!textEdicion.getText().matches(regexEdicion)) {// Validamos que la edicion cumpla con regexEdicion
			informar("La edici�n ingresada es invalida.");
			return false;
		}
		if (!textAnioPublicacion.getText().matches(regexAnio)) { // Validamos que el anio de publicacion cumpla con regexAnio
			informar("El a�o ingresado es invalido.");
			return false;
		}

		return true;
	}
	
	/*--------------------------------------------------------- Autores ----------------------------------------------------------------*/
	
	public void agregarFilaAutor(Entidad autor) {
		int fila = tableModelAutores.getRowCount(), columna = cantidadColumnasEntidad - 1;
		tableModelAutores.addRow(filaVaciaEntidad); // Agregamos una fila a la tabla de autores
		// Llenamos la fila vacia con los datos del autor correspondientes
		tableModelAutores.setValueAt(autor.getNombre(), fila, columna);
		tableModelAutores.setValueAt(autor.getId(), fila, columna -= 1);
	}
	
	public void actualizarTablaAutores() {
		limpiarTablaAutores(); // Vaciamos la tabla de autores
		buscarAutor(""); // Limpiamos el campo buscar para que se actualice la tabla de autores
	}
	
	public void limpiarTablaAutores() {
		while (tableModelAutores.getRowCount() > 0) { // Vaciamos la tabla de autores fila a fila
			tableModelAutores.removeRow(0);
		}
		tableAutores.clearSelection(); // Limpiamos la seleccion del usuario en la tabla
		textAutor.setText(""); // Limpiamos el campo de autor
	}
	
	public void buscarAutor(String buscar) {
		List<Entidad> autores = null; // Creamos una lista de autores vacia para almacenar los autores encontrados en la BD
		limpiarTablaAutores(); // Limpiamos la tabla autores para llenarla con los autores encontrados
		try {
			autores = bd.searchAutor(textBuscarAutor.getText()); // Buscamos los autores en la BD
			for (Entidad a : autores) { // Agregamos tantas filas como autores se hayan encotnrado
				agregarFilaAutor(a);
			}
		} catch (SQLException e) {
			informar("Se ha producido un error al realizar la b�squeda del autor."); // Informamos al usuario que se ha producido un error al recuperar los autores de la BD
			e.printStackTrace();
		}
	}
	
	public void insertarAutor() {
		try {
			if(textAutor.getText().matches(regexAutor)) { // Si el nombre del autor ingresado es valido
				if (bd.insertAutor(textAutor.getText()) == 1) { // Si la insercion en la BD es exitosa
					informar("El autor se ha insertado con �xito."); // Informamos al usuario que se inserto correctamente el autor
					actualizarTablaAutores(); // Actualizamos la tabla de autores para reflejar la nueva insercion
				}
			} else
				informar("El autor ingresado no es v�lido."); // Informamos al usuario que el nombre del autor no es valido
		} catch (SQLException e) {
			informar("Se ha producido un error al agregar el autor."); // Informamos al usuario que la insercion en la BD no fue exitosa
			e.printStackTrace();
			try {
				bd.close(); // Cerramos las conexiones de la BD
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			bd = new BD(); // Abrimos nuevamente las conexiones con la BD
		}
	}

	public void modificarAutor() {
		try {
			if(textAutor.getText().matches(regexAutor)) { // Si el nombre del autor es valido
				bd.updateAutor((int) Integer.valueOf(tableAutores.getValueAt(tableAutores.getSelectedRow(), 0).toString()), textAutor.getText()); // Actualizamos el autor en la BD
				informar("Se ha actualizado el autor con �xito."); // Informamos al usuario que el autor se actualizo con exito en la BD
				actualizarTablaAutores(); // Actualizamos la tabla de autores para reflejar la nueva actualizacion
			} else
				informar("El autor ingresado no es v�lido."); // Informamos al usuario que el nombre del autor no es valido
		} catch (SQLException e) {
			informar("Se ha producido un error al actualizar el autor."); // Informamos al usuario que la actualizacion en la BD no fue exitosa
			e.printStackTrace();
		}
	}

	public void eliminarAutor() {
		try {
			bd.deleteAutor((int) Integer.valueOf(tableAutores.getValueAt(tableAutores.getSelectedRow(), 0).toString())); // Borramos el autor de la BD
			informar("Se ha eliminado el autor con �xito."); // Informamos al usuario que la eliminacion en la BD fue exitosa
			actualizarTablaAutores(); // Actualizamos la tabla para reflejar la eliminacion
		} catch (SQLException e) {
			informar("Se ha producido un error al eliminar el autor."); // Informamos al usuario que la eliminacion en la BD no fue exitosa
			e.printStackTrace();
		}
	}
	
	
	/*--------------------------------------------------------- Editoriales ----------------------------------------------------------------*/
	
	public void agregarFilaEditorial(Entidad editorial) {
		int fila = tableModelEditoriales.getRowCount(), columna = cantidadColumnasEntidad - 1;
		tableModelEditoriales.addRow(filaVaciaEntidad); // Agregamos una fila vacia a la tabla de editoriales
		// Llenamos la fila vacia con los datos correspondientes de la editorial
		tableModelEditoriales.setValueAt(editorial.getNombre(), fila, columna);
		tableModelEditoriales.setValueAt(editorial.getId(), fila, columna -= 1);
	}
	
	public void actualizarTablaEditoriales() {
		limpiarTablaEditoriales(); // Vaciamos la tabla de editoriales
		buscarEditorial(""); // // Limpiamos el campo buscar para que se actualice la tabla de editoriales
	}
	
	public void limpiarTablaEditoriales() {
		while (tableModelEditoriales.getRowCount() > 0) { // Vaciamos la tabla editoriales fila a fila
			tableModelEditoriales.removeRow(0);
		}
		tableEditoriales.clearSelection(); // Limpiamos la seleccion del usuario en la tabla de editoriales
		textEditorial.setText(""); // Limpiamos el campo de editorial
	}

	public void buscarEditorial(String buscar) {
		List<Entidad> editoriales = null; // Creamos una lista vacia de editoriales para almacenar las editoriales recuperadas de la BD
		limpiarTablaEditoriales(); // Limpiamos la tabla de editoriales para llenarla con las editoriales encontradas
		try {
			editoriales = bd.searchEditorial(textBuscarEditorial.getText()); // Buscamos las editoriales en la BD
			for (Entidad e : editoriales) { // Agregamos tantas filas como editoriales hayamos encontrado
				agregarFilaEditorial(e);
			}
		} catch (SQLException e) {
			informar("Se ha producido un error al realizar la b�squeda de la editorial."); // Informamos al usuario que hubo un error al recuperar las editoriales de la BD
			e.printStackTrace();
		}
	}
	
	public void insertarEditorial() {
		try {
			if(textEditorial.getText().matches(regexEditorial)) { // Si el nombre de la editorial es correcto
				if (bd.insertEditorial(textEditorial.getText()) == 1) { // Si la insercion en la BD es exitosa
					informar("La editorial se ha insertado con �xito."); // Informamos al usuario que se inserto correctamente la editorial en la BD
					actualizarTablaEditoriales(); // Actualizamos la tabla de editoriales para para reflejar la nueva insercion
				}
			} else
				informar("La editorial ingresada no es v�lida."); // Inforamos al usuario que el nombre de la editorial no es valido
			
		} catch (SQLException e) {
			informar("Se ha producido un error al agregar la editorial."); // Inforamos al usuario que hubo un error al insertar la editorial
			e.printStackTrace();
			try {
				bd.close(); // Cerrarmos las conexiones de la BD
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			bd = new BD(); // Creamos las conexiones de la BD nuevamente
		}
	}

	public void modificarEditorial() {
		try {
			if(textEditorial.getText().matches(regexEditorial)) { // Si el nombre de la editorial es correcto
				bd.updateEditorial((int) Integer.valueOf(tableEditoriales.getValueAt(tableEditoriales.getSelectedRow(), 0).toString()), textEditorial.getText()); // Actualizamos la editorial en la BD
				informar("Se ha actualizado la editorial con �xito."); // Informamos al usuario que se actualizo correctamente la editorial en la BD
				actualizarTablaEditoriales(); // Actualizamos la tabla de editoriales para reflejar la nueva actualizacion
			} else 
				informar("La editorial ingresada no es v�lida."); // Informamos al usuario que el nombre de la editorial no es correcto
		} catch (SQLException e) {
			informar("Se ha producido un error al actualizar la editorial."); // Informamos al usuario que hubo un error al actualizar la editorial en la BD
			e.printStackTrace();
		}
	}

	public void eliminarEditorial() {
		try {
			bd.deleteEditorial((int) Integer.valueOf(tableEditoriales.getValueAt(tableEditoriales.getSelectedRow(), 0).toString())); // Eliminamos la editorial de la BD
			informar("Se ha eliminado la editorial con �xito."); // Informamos al usuario que se elimino correctamente la editorial en la BD
			actualizarTablaEditoriales(); // Actualizamos la tabla de editoriales para que se refleje la nueva eliminacion
		} catch (SQLException e) {
			informar("Se ha producido un error al eliminar la editorial."); // Informamos al usuario que hubo un error al elimar la editorial de la BD
			e.printStackTrace();
		}
	}
	
	/*--------------------------------------------------------- Inicializacion ----------------------------------------------------------------*/

	public void cargarMenu() {
		// Configuracion de la ventana
		JPanel panelMenu = new JPanel();
		panelMenu.setBackground(new Color(0, 0, 51));
		panelMenu.setBounds(0, 0, 187, 708);
		contentPane.add(panelMenu);
		panelMenu.setLayout(null);

		// Configuracion de panel de inicio
		panelInicio = new JPanel();
		panelInicio.setBackground(new Color(0, 0, 51));
		panelInicio.setBounds(posXIni, posYIni, 187, 47);
		panelMenu.add(panelInicio);

		// Configuracion de la etiqueta inicio
		lblInicio = new JLabel("Inicio");
		lblInicio.setForeground(new Color(255, 255, 255));
		lblInicio.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		panelInicio.add(lblInicio);

		// Configuracion del panel de administracion
		panelAdministracion = new JPanel();
		panelAdministracion.setBackground(new Color(0, 0, 51));
		panelAdministracion.setBounds(posXAdmin, posYAdmin, 187, 47);
		panelMenu.add(panelAdministracion);

		// Configuracion de la etiqueta administracion
		lblAdministracion = new JLabel("Administraci\u00F3n");
		lblAdministracion.setForeground(Color.WHITE);
		lblAdministracion.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		panelAdministracion.add(lblAdministracion);

		// Configuracion del panel de inicio seleccionado
		panelInicioSeleccionado = new JPanel();
		panelInicioSeleccionado.setBackground(new Color(51, 204, 153));
		panelInicioSeleccionado.setBounds(posXIni, posYIni, 187, 47);
		panelMenu.add(panelInicioSeleccionado);

		// Configuracion de la etiqueta de inicio seleccionada
		labelInicioSeleccionado = new JLabel("Inicio");
		labelInicioSeleccionado.setForeground(Color.WHITE);
		labelInicioSeleccionado.setFont(new Font("Segoe UI", Font.BOLD, 18));
		panelInicioSeleccionado.add(labelInicioSeleccionado);

		// Configuracion del panel de administracion seleccionado
		panelAdministracionSeleccionado = new JPanel();
		panelAdministracionSeleccionado.setBackground(new Color(51, 204, 153));
		panelAdministracionSeleccionado.setBounds(posXAdmin, posYAdmin, 187, 47);
		panelMenu.add(panelAdministracionSeleccionado);

		// Configuracion de la etiqueta administracion seleccionada
		labelAdministracionSeleccionado = new JLabel("Administraci\u00F3n");
		labelAdministracionSeleccionado.setForeground(Color.WHITE);
		labelAdministracionSeleccionado.setFont(new Font("Segoe UI", Font.BOLD, 18));
		panelAdministracionSeleccionado.add(labelAdministracionSeleccionado);
	}

	public void cargarInicio() {

		// Configuracion del panel de inicio
		paneInicioVisible = new JPanel();
		paneInicioVisible.setBackground(new Color(255, 255, 255));
		paneInicioVisible.setBounds(187, 0, 1160, 708);
		contentPane.add(paneInicioVisible);
		paneInicioVisible.setLayout(null);

		// Configuracion del tableModel de la tabla de libros
		String columnas[] = { "ISBN", "T�tulo", "Autor", "Editorial", "Edici�n", "A�o de publicaci�n" };
		String datos[][] = {};
		tableModelLibros = new DefaultTableModel(datos, columnas) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Configuracion del scroll de la tabla de libros
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(26, 67, 1073, 344);
		paneInicioVisible.add(scrollPane);
		paneInicioVisible.setLayout(null);

		// Configuracion de la tabla de libros
		tableLibros = new JTable();
		tableLibros.setBackground(new Color(255, 255, 255));
		tableLibros.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		tableLibros.setModel(tableModelLibros);
		tableLibros.setRowHeight(25);
		scrollPane.setViewportView(tableLibros);

		// Configuracion de la etiqueta buscar libros
		JLabel lblBuscar = new JLabel("Buscar:");
		lblBuscar.setBounds(12, 14, 65, 20);
		paneInicioVisible.add(lblBuscar);
		lblBuscar.setForeground(new Color(0, 0, 51));
		lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 20));

		// Configuracion del text buscar libro
		textBuscarLibro = new JTextField();
		textBuscarLibro.setBounds(94, 11, 173, 30);
		paneInicioVisible.add(textBuscarLibro);
		textBuscarLibro.setToolTipText("");
		textBuscarLibro.setForeground(new Color(0, 0, 51));
		textBuscarLibro.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		textBuscarLibro.setColumns(10);

		// Configuracion del combo autores
		comboAutores = new JComboBox();
		comboAutores.setBounds(315, 11, 175, 30);
		paneInicioVisible.add(comboAutores);
		comboAutores.setForeground(new Color(0, 0, 51));
		comboAutores.setFont(new Font("Segoe UI", Font.PLAIN, 16));

		// Configuracion del comobo editoriales
		comboEditoriales = new JComboBox();
		comboEditoriales.setBounds(517, 11, 175, 30);
		paneInicioVisible.add(comboEditoriales);
		comboEditoriales.setForeground(new Color(0, 0, 51));
		comboEditoriales.setFont(new Font("Segoe UI", Font.PLAIN, 16));

		// Configuracion de la etiqueta ordenar libros
		lblOrdenar = new JLabel("Ordenar por:");
		lblOrdenar.setBounds(781, 11, 124, 27);
		paneInicioVisible.add(lblOrdenar);
		lblOrdenar.setForeground(new Color(0, 0, 51));
		lblOrdenar.setFont(new Font("Segoe UI", Font.PLAIN, 20));

		// Configuracion del combo ordenar libros
		comboOrdenarPor = new JComboBox();
		comboOrdenarPor.setBounds(924, 11, 175, 30);
		paneInicioVisible.add(comboOrdenarPor);
		comboOrdenarPor.setModel(new DefaultComboBoxModel(
				new String[] { "ISBN", "Titulo", "Autor", "Editorial", "Edicion", "AnioPublicacion" }));
		comboOrdenarPor.setForeground(new Color(0, 0, 51));
		comboOrdenarPor.setFont(new Font("Segoe UI", Font.PLAIN, 16));

		// Configuracion de la etiqueta ISBN
		JLabel lblISBN = new JLabel("ISBN:");
		lblISBN.setBounds(202, 463, 65, 20);
		paneInicioVisible.add(lblISBN);
		lblISBN.setForeground(new Color(0, 0, 51));
		lblISBN.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		// Configuracion de la etiqueta del titulo del libro
		JLabel lblTitulo = new JLabel("T\u00EDtulo:");
		lblTitulo.setBounds(202, 529, 76, 20);
		paneInicioVisible.add(lblTitulo);
		lblTitulo.setForeground(new Color(0, 0, 51));
		lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		// Configuracion de la etiqueta del autor
		JLabel lblAutor = new JLabel("Autor:");
		lblAutor.setBounds(202, 600, 76, 20);
		paneInicioVisible.add(lblAutor);
		lblAutor.setForeground(new Color(0, 0, 51));
		lblAutor.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		// Configuracion de la etiqueta de la editorial
		JLabel lblEditorial = new JLabel("Editorial:");
		lblEditorial.setBounds(657, 455, 76, 20);
		paneInicioVisible.add(lblEditorial);
		lblEditorial.setForeground(new Color(0, 0, 51));
		lblEditorial.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		// Configuracion de la etiqueta de la edicion
		JLabel lblEdicion = new JLabel("Edici\u00F3n:");
		lblEdicion.setBounds(657, 529, 87, 20);
		paneInicioVisible.add(lblEdicion);
		lblEdicion.setForeground(new Color(0, 0, 51));
		lblEdicion.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		// Configuracion de la etiqueta del anio de publicacion
		JLabel lblAnioDePublicacion = new JLabel("A\u00F1o de publicaci\u00F3n:");
		lblAnioDePublicacion.setBounds(657, 599, 165, 23);
		paneInicioVisible.add(lblAnioDePublicacion);
		lblAnioDePublicacion.setForeground(new Color(0, 0, 51));
		lblAnioDePublicacion.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		// Configuracion del texto ISBN
		textISBN = new JTextField();
		textISBN.setBounds(282, 459, 253, 30);
		paneInicioVisible.add(textISBN);
		textISBN.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		textISBN.setColumns(10);

		// Configuracion del texto titulo
		textTitulo = new JTextField();
		textTitulo.setBounds(283, 526, 253, 30);
		paneInicioVisible.add(textTitulo);
		textTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		textTitulo.setColumns(10);

		// Configuracion del texto autor
		comboAutor = new JComboBox();
		comboAutor.setBounds(287, 599, 253, 30);
		paneInicioVisible.add(comboAutor);
		comboAutor.setForeground(new Color(0, 0, 51));
		comboAutor.setFont(new Font("Segoe UI", Font.PLAIN, 16));

		// Configuracion del editorial
		comboEditorial = new JComboBox();
		comboEditorial.setBounds(774, 451, 253, 30);
		paneInicioVisible.add(comboEditorial);
		comboEditorial.setForeground(new Color(0, 0, 51));
		comboEditorial.setFont(new Font("Segoe UI", Font.PLAIN, 16));

		// Configuracion del texto edicion
		textEdicion = new JTextField();
		textEdicion.setBounds(774, 521, 253, 30);
		paneInicioVisible.add(textEdicion);
		textEdicion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		textEdicion.setColumns(10);

		// Configuracion del texto anio de publicacion
		textAnioPublicacion = new JTextField();
		textAnioPublicacion.setBounds(851, 594, 124, 30);
		paneInicioVisible.add(textAnioPublicacion);
		textAnioPublicacion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		textAnioPublicacion.setColumns(10);

		// Configuracion del boton de agregar libro
		btnAgregarLibro = new JButton("Agregar");
		btnAgregarLibro.setBounds(26, 453, 141, 40);
		paneInicioVisible.add(btnAgregarLibro);
		btnAgregarLibro.setForeground(new Color(0, 0, 51));
		btnAgregarLibro.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		// Configuracion del boton de modificar libro
		btnModificarLibro = new JButton("Modificar");
		btnModificarLibro.setBounds(26, 533, 141, 40);
		paneInicioVisible.add(btnModificarLibro);
		btnModificarLibro.setForeground(new Color(0, 0, 51));
		btnModificarLibro.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		
		// Configuracion del boton de eliminar libro
		btnEliminarLibro = new JButton("Eliminar");
		btnEliminarLibro.setBounds(26, 613, 141, 40);
		paneInicioVisible.add(btnEliminarLibro);
		btnEliminarLibro.setForeground(new Color(0, 0, 51));
		btnEliminarLibro.setFont(new Font("Segoe UI", Font.PLAIN, 18));
	}

	public void cargarAdministracion() {

		// Configuracion del panel de administracion
		paneAdministracionVisible = new JPanel();
		paneAdministracionVisible.setBackground(Color.WHITE);
		paneAdministracionVisible.setBounds(187, 0, 1131, 708);
		contentPane.add(paneAdministracionVisible);

		// Configuracion del tableModel de la tabla de autores
		String[] columnas = { "ID", "Nombre" };
		String datos[][] = {};
		tableModelAutores = new DefaultTableModel(datos, columnas) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Configuracion del tableModel de la tabla de editoriales
		tableModelEditoriales = new DefaultTableModel(datos, columnas) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		// Configuracion del scrollPane de la tabla de autores
		JScrollPane scrollPaneAutores = new JScrollPane();
		scrollPaneAutores.setBounds(26, 67, 650, 250);
		paneAdministracionVisible.add(scrollPaneAutores);
		paneAdministracionVisible.setLayout(null);
		paneAdministracionVisible.setVisible(true);

		// Configuracion de la tabla de autores
		tableAutores = new JTable();
		tableAutores.setBackground(new Color(255, 255, 255));
		tableAutores.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		tableAutores.setModel(tableModelAutores);
		tableAutores.setRowHeight(25);
		scrollPaneAutores.setViewportView(tableAutores);

		// Configuracion del scrollPane de la tabla de editoriales
		JScrollPane scrollPaneEditoriales = new JScrollPane();
		scrollPaneEditoriales.setBounds(26, 400, 650, 250);
		paneAdministracionVisible.add(scrollPaneEditoriales);

		// Configuracion de la tabla de editoriales
		tableEditoriales = new JTable();
		tableEditoriales.setBackground(new Color(255, 255, 255));
		tableEditoriales.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		tableEditoriales.setModel(tableModelEditoriales);
		tableEditoriales.setRowHeight(25);
		scrollPaneEditoriales.setViewportView(tableEditoriales);

		// Configuracion de la etiqueta de buscar autor
		JLabel lblBuscarAutor = new JLabel("Buscar:");
		lblBuscarAutor.setForeground(new Color(0, 0, 51));
		lblBuscarAutor.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblBuscarAutor.setBounds(26, 14, 65, 20);
		paneAdministracionVisible.add(lblBuscarAutor);

		// Configuracion del texto buscar autor
		textBuscarAutor = new JTextField();
		textBuscarAutor.setBounds(108, 9, 173, 35);
		paneAdministracionVisible.add(textBuscarAutor);
		textBuscarAutor.setToolTipText("");
		textBuscarAutor.setForeground(new Color(0, 0, 51));
		textBuscarAutor.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		textBuscarAutor.setColumns(10);
		paneAdministracionVisible.add(textBuscarAutor);

		// Configuracion de la etiqueta de buscar editorial
		JLabel lblBuscarEditorial = new JLabel("Buscar:");
		lblBuscarEditorial.setForeground(new Color(0, 0, 51));
		lblBuscarEditorial.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblBuscarEditorial.setBounds(26, 350, 65, 20);
		paneAdministracionVisible.add(lblBuscarEditorial);

		// Configuracion del texto buscar editorial
		textBuscarEditorial = new JTextField();
		textBuscarEditorial.setBounds(108, 345, 173, 35);
		paneAdministracionVisible.add(textBuscarEditorial);
		textBuscarEditorial.setToolTipText("");
		textBuscarEditorial.setForeground(new Color(0, 0, 51));
		textBuscarEditorial.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		textBuscarEditorial.setColumns(10);
		paneAdministracionVisible.add(textBuscarEditorial);

		// Configuracion de la etiqueta de autor
		JLabel lblAutor = new JLabel("Autor:");
		lblAutor.setBounds(750, 120, 122, 23);
		paneAdministracionVisible.add(lblAutor);
		lblAutor.setForeground(new Color(0, 0, 51));
		lblAutor.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		// Configuracion del texto de autor
		textAutor = new JTextField();
		textAutor.setBounds(850, 115, 200, 35);
		paneAdministracionVisible.add(textAutor);
		textAutor.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		textAutor.setColumns(10);

		// Configuracion del boton de agregar autor
		btnAgregarAutor = new JButton("Agregar");
		btnAgregarAutor.setBounds(680, 190, 130, 40);
		paneAdministracionVisible.add(btnAgregarAutor);
		btnAgregarAutor.setForeground(new Color(0, 0, 51));
		btnAgregarAutor.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		// Configuracion del boton de modificar autor
		btnModificarAutor = new JButton("Modificar");
		btnModificarAutor.setBounds(840, 190, 130, 40);
		paneAdministracionVisible.add(btnModificarAutor);
		btnModificarAutor.setForeground(new Color(0, 0, 51));
		btnModificarAutor.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		// Configuracion del boton de eliminar autor
		btnEliminarAutor = new JButton("Eliminar");
		btnEliminarAutor.setBounds(1000, 190, 130, 40);
		paneAdministracionVisible.add(btnEliminarAutor);
		btnEliminarAutor.setForeground(new Color(0, 0, 51));
		btnEliminarAutor.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		// Configuracion de la etiqueta de la editorial
		JLabel lblEditorial = new JLabel("Editorial:");
		lblEditorial.setBounds(750, 450, 122, 23);
		paneAdministracionVisible.add(lblEditorial);
		lblEditorial.setForeground(new Color(0, 0, 51));
		lblEditorial.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		// Configuracion del texto de editorial
		textEditorial = new JTextField();
		textEditorial.setBounds(850, 445, 200, 35);
		paneAdministracionVisible.add(textEditorial);
		textEditorial.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		textEditorial.setColumns(10);

		// Configuracion del boton de agregar editorial
		btnAgregarEditorial = new JButton("Agregar");
		btnAgregarEditorial.setBounds(680, 520, 130, 40);
		paneAdministracionVisible.add(btnAgregarEditorial);
		btnAgregarEditorial.setForeground(new Color(0, 0, 51));
		btnAgregarEditorial.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		// Configuracion del boton modificar editorial
		btnModificarEditorial = new JButton("Modificar");
		btnModificarEditorial.setBounds(840, 520, 130, 40);
		paneAdministracionVisible.add(btnModificarEditorial);
		btnModificarEditorial.setForeground(new Color(0, 0, 51));
		btnModificarEditorial.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		// Configuracion del boton eliminar editorial
		btnEliminarEditorial = new JButton("Eliminar");
		btnEliminarEditorial.setBounds(1000, 520, 130, 40);
		paneAdministracionVisible.add(btnEliminarEditorial);
		btnEliminarEditorial.setForeground(new Color(0, 0, 51));
		btnEliminarEditorial.setFont(new Font("Segoe UI", Font.PLAIN, 18));
	}

	// Carga de listeners
	
	public void cargarListeners() { // Listener para el combo de ordenar libros
		comboOrdenarPor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buscarLibro(""); // Actualizamos la tabla de libros con el nuevo criterio de busqueda
			}
		});

		comboEditoriales.addActionListener(new ActionListener() { // Listener para el combo editoriales de busqueda
			public void actionPerformed(ActionEvent arg0) {
				buscarLibro(""); // Actualizamos la tabla de libros con el nuevo criterio de busqueda
			}
		});

		comboAutores.addActionListener(new ActionListener() { // Listener para el combo autores de busqueda
			public void actionPerformed(ActionEvent arg0) {
				buscarLibro(""); // Actualizamos la tabla de libros con el nuevo criterio de busqueda
			}
		});

		textBuscarLibro.addKeyListener(new KeyAdapter() { // Listener para el texto libro de busqueda
			@Override
			public void keyReleased(KeyEvent arg0) {
				buscarLibro(""); // Actualizamos la tabla de libros con el nuevo criterio de busqueda
			}
		});

		textBuscarAutor.addKeyListener(new KeyAdapter() { // Listener para el texto autor de busqueda
			@Override
			public void keyReleased(KeyEvent arg0) {
				buscarAutor(""); // Actualizamos la tabla de autores con el nuevo criterio de busqueda
			}
		});

		textBuscarEditorial.addKeyListener(new KeyAdapter() { // Listener para el texto editorial de busqueda
			@Override
			public void keyReleased(KeyEvent arg0) {
				buscarEditorial(""); // Actualizamos la tabla de editoriales con el nuevo criterio de busqueda
			}
		});

		btnEliminarLibro.addMouseListener(new MouseAdapter() { // Listener para el boton eliminar libro
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(tableLibros.getSelectedRow() != -1) // Si existe una fila seleccionada
					eliminarLibro(); // Eliminamos el libro de la BD
			else
				informar("Debe seleccionar un libro de la tabla."); // Informamos al usuario que no hay ninguna fila de la tabla seleccionada
			}
		});

		btnModificarLibro.addMouseListener(new MouseAdapter() { // Listener para el boton modificar libro
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(tableLibros.getSelectedRow() != -1) // Si existe una fila seleccionada
					modificarLibro(); // Actualizamos el libro de la BD
				else
					informar("Debe seleccionar un libro de la tabla."); // Informamos al usuario que no hay ninguna fila de la tabla seleccionada
			}
		});

		btnAgregarLibro.addMouseListener(new MouseAdapter() { // Listener para el boton agregar libro
			@Override
			public void mouseClicked(MouseEvent arg0) {
				insertarLibro(); // Insertamos el libro de la BD
			}
		});

		btnEliminarAutor.addMouseListener(new MouseAdapter() { // Listener para el boton eliminar autor
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(tableAutores.getSelectedRow() != -1) // Si existe una fila seleccionada
					eliminarAutor(); // Eliminamos el autor de la BD
				else
					informar("Debe seleccionar un autor de la tabla."); // Informamos al usuario que no hay ninguna fila de la tabla seleccionada
			}
		});

		btnModificarAutor.addMouseListener(new MouseAdapter() { // Listener para el boton modificar autor
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(tableAutores.getSelectedRow() != -1) // Si existe una fila seleccionada
					modificarAutor();  // Actualizamos el autor de la BD
				else
					informar("Debe seleccionar un autor de la tabla."); // Informamos al usuario que no hay ninguna fila de la tabla seleccionada
			}
		});

		btnAgregarAutor.addMouseListener(new MouseAdapter() { // Listener para el boton agregar autor
			@Override
			public void mouseClicked(MouseEvent arg0) {
				insertarAutor(); // Insertamos el autor de la BD
			}
		});

		btnEliminarEditorial.addMouseListener(new MouseAdapter() { // Listener para el boton eliminar editorial
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(tableEditoriales.getSelectedRow() != -1) // Si existe una fila seleccionada
					eliminarEditorial(); // Eliminamos la editorial de la BD
				else
					informar("Debe seleccionar una editorial de la tabla."); // Informamos al usuario que no hay ninguna fila de la tabla seleccionada
			}
		});

		btnModificarEditorial.addMouseListener(new MouseAdapter() { // Listener para el boton modificar editorial
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(tableEditoriales.getSelectedRow() != -1) // Si existe una fila seleccionada
					modificarEditorial(); // Actualizamos la editorial de la BD
				else
					informar("Debe seleccionar una editorial de la tabla."); // Informamos al usuario que no hay ninguna fila de la tabla seleccionada
			}
		});

		btnAgregarEditorial.addMouseListener(new MouseAdapter() { // Listener para el boton agregar editorial
			@Override
			public void mouseClicked(MouseEvent arg0) {
				insertarEditorial(); // Insertamos la editorial de la BD
			}
		});

		textISBN.addKeyListener(new KeyAdapter() { // Listener para el texto ISBN para comenzar la busqueda del libro
			@Override
			public void keyReleased(KeyEvent arg0) {
				String isbn = textISBN.getText();
				tableLibros.clearSelection(); // Limpiamos la seleccion del usuario en la tabla de libros
				textISBN.setText(isbn);
				if (isbn.matches(regexISBN)) // Si el ISBN ingresado es correcto
					buscarLibro(isbn); // Buscamos el libro en la BD y llenamos el resto de los campos con los datos correspondientes si existe
			}
		});

		tableLibros.addMouseListener(new MouseAdapter() { // Listener para la tabla de libros
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (tableLibros.getSelectedRow() != -1) {
					llenarCamposLibro(tableLibros.getSelectedRow()); // Llenamos los campos con los datos del libro seleccionado
				}
			}
		});

		tableAutores.addMouseListener(new MouseAdapter() { // Listener para la tabla de autores
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (tableAutores.getSelectedRow() != -1) { // Si existe una fila seleccionada
					int columna = cantidadColumnasEntidad - 1;

					textAutor.setText(tableModelAutores.getValueAt(tableAutores.getSelectedRow(), columna).toString()); // Llenamos el campo del autor con el nombre de la fila seleccionada
				}
			}
		});

		tableEditoriales.addMouseListener(new MouseAdapter() { // Listener para la tabla de editoriales
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (tableEditoriales.getSelectedRow() != -1) { // Si existe una fila seleccionada
					int columna = cantidadColumnasEntidad - 1;

					textEditorial.setText(
							tableModelEditoriales.getValueAt(tableEditoriales.getSelectedRow(), columna).toString());  // Llenamos el campo de la editorial con el nombre de la fila seleccionada
				}
			}
		});

		panelInicio.addMouseListener(new MouseAdapter() { // Listener para el panel inicio del menu
			@Override
			public void mouseReleased(MouseEvent e) {
				cargarAutoresYEditoriales(); // Actualizamos los autores y editoriales
				// Cargamos la ventana correspondiente y el efecto del menu
				panelInicio.setVisible(false); 
				panelInicioSeleccionado.setEnabled(true);
				panelInicioSeleccionado.setVisible(true);
				panelAdministracion.setVisible(true);
				panelAdministracionSeleccionado.setVisible(false);
				paneInicioVisible.setVisible(true);
				paneAdministracionVisible.setVisible(false);
			}
		});

		panelAdministracion.addMouseListener(new MouseAdapter() { // Listener para el panel administracion del menu
			@Override
			public void mouseReleased(MouseEvent e) {
				// Cargamos la ventana correspondiente y el efecto del menu
				panelAdministracionSeleccionado.setEnabled(true);
				panelAdministracionSeleccionado.setVisible(true);
				panelAdministracion.setVisible(false);
				panelInicioSeleccionado.setVisible(false);
				panelInicioSeleccionado.setEnabled(false);
				panelInicio.setVisible(true);
				paneInicioVisible.setVisible(false);
				paneAdministracionVisible.setVisible(true);
			}
		});
		
	}

	public void cargarAutoresYEditoriales() { 
		comboAutores.removeAllItems(); // Vaciamos el combo autores de inicio
		comboAutor.removeAllItems(); // Vaciamos el combo autor de administracion
		comboAutores.addItem("Autor"); // Agregamos el item Autor al combo de inicio
		comboAutor.addItem("Autor"); // Agregamos el item Autor al combo de administracion
		comboEditoriales.removeAllItems(); // Vaciamos el combo editoriales de inicio
		comboEditorial.removeAllItems(); // Vaciamos el combo editorial de administracion
		comboEditoriales.addItem("Editorial"); // Agregamos el item Editorial al combo de inicio
		comboEditorial.addItem("Editorial"); // Agregamos el item Editorial al combo de administracion

		try {
			limpiarTablaAutores(); // Vaciamos la tabla de autores
			limpiarTablaEditoriales(); // Vaciamos la tabla de editoriales
			List<Entidad> autores = bd.getAutores(); // Creamos una lista de autores para almacenar los autores recuperados de la BD
			List<Entidad> editoriales = bd.getEditoriales(); // Creamos una lista de editoriales para almacenar los autores recuperados de la BD

			// Agregamos los autores encontrados a los combos de inicio y administracion
			for (Entidad a : autores) {
				comboAutores.addItem(a.getNombre()); 
				comboAutor.addItem(a.getNombre());
				agregarFilaAutor(a);
			}
			// Agregamos las editoriales encontradas a los combos de inicio y administracion
			for (Entidad e : editoriales) {
				comboEditoriales.addItem(e.getNombre());
				comboEditorial.addItem(e.getNombre());
				agregarFilaEditorial(e);
			}

		} catch (SQLException e) {
			informar("Se ha producido un error al cargar los autores y las editoriales."); // Informamos al usuario que se ha producido un error al cargar los autores y editoriales
			e.printStackTrace();
		}

	}
	
	
	/*--------------------------------------------------------- Otras ----------------------------------------------------------------*/

	public void informar(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje); // Creamos una ventana emergente con un mensaje para el usuario
	}
}
