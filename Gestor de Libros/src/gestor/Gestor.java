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

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
					Gestor frame = new Gestor();
					frame.setLocationRelativeTo(null);
					frame.setTitle("Gestor de Libros");
					frame.setResizable(false);
					frame.setVisible(true);

					frame.traerTodosLosLibros();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Gestor() {
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					bd.close();
				} catch (SQLException e1) {
					informar("Error al cerrar la conexi�n con la base de datos.");
					e1.printStackTrace();
				}
				finally {
					setVisible(false);
					dispose();
				}
			}
		});
		setBounds(100, 100, 1340, 740);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		bd = new BD();

		cargarInicio();
		cargarAdministracion();
		cargarMenu();

		cargarAutoresYEditoriales();
		cargarListeners();

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
		tableModelLibros.addRow(filaVaciaLibro);
		tableLibros.setValueAt(libro.getAnioPublicacion(), fila, columna);
		tableLibros.setValueAt(libro.getEdicion(), fila, columna -= 1);
		tableLibros.setValueAt(libro.getEditorial(), fila, columna -= 1);
		tableLibros.setValueAt(libro.getAutor(), fila, columna -= 1);
		tableLibros.setValueAt(libro.getTitulo(), fila, columna -= 1);
		tableLibros.setValueAt(libro.getISBN(), fila, columna -= 1);
	}

	public void llenarCamposLibro(int fila) {
		int columna = cantidadColumnasLibros - 1;

		textAnioPublicacion.setText(tableModelLibros.getValueAt(fila, columna).toString());
		textEdicion.setText(tableModelLibros.getValueAt(fila, columna -= 1).toString());
		comboEditorial.setSelectedItem(tableModelLibros.getValueAt(fila, columna -= 1).toString());
		comboAutor.setSelectedItem((tableModelLibros.getValueAt(fila, columna -= 1).toString()));
		textTitulo.setText(tableModelLibros.getValueAt(fila, columna -= 1).toString());
		textISBN.setText(tableModelLibros.getValueAt(fila, columna -= 1).toString());
	}
	
	public boolean recuperarInformacionLibro() {
		if (validarCamposLibro()) {
			ISBN = textISBN.getText();
			titulo = textTitulo.getText();
			autor = comboAutor.getSelectedItem().toString();
			editorial = comboEditorial.getSelectedItem().toString();
			edicion = Integer.valueOf(textEdicion.getText());
			anioPublicacion = Integer.valueOf(textAnioPublicacion.getText());
			return true;
		}

		return false;
	}
	
	public void actualizarTablaLibros() {
		limpiarTablaLibros();
		buscarLibro("");
	}
	
	public void limpiarTablaLibros() {
		while (tableModelLibros.getRowCount() > 0) {
			tableModelLibros.removeRow(0);
		}
		tableLibros.clearSelection();
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
		List<Libro> libros = null;
		limpiarTablaLibros();
		try {
			if (!buscar.equals("") && !(libros = bd.searchLibro(buscar, "", "", "ISBN")).isEmpty()) {
				for (Libro l : libros) {
					agregarFilaLibro(l);
				}
				tableLibros.setRowSelectionInterval(0, 0);
				llenarCamposLibro(0);
				return;
			}
			if(textBuscarLibro.getText() != null && comboAutores.getSelectedItem() != null && comboEditoriales.getSelectedItem() != null ) {
				libros = bd.searchLibro(textBuscarLibro.getText(), comboAutores.getSelectedItem().toString(),
						comboEditoriales.getSelectedItem().toString(), comboOrdenarPor.getSelectedItem().toString());
				for (Libro l : libros) {
					agregarFilaLibro(l);
				}
			}
		} catch (SQLException e) {
			informar("Se ha producido un error al realizar la b�squeda del libro.");
			e.printStackTrace();
		}
	}
	
	public void traerTodosLosLibros() {
		List<Libro> libros = null;
		try {
			libros = bd.getAll(comboOrdenarPor.getSelectedItem().toString());

			for (Libro l : libros) {
				agregarFilaLibro(l);
			}

		} catch (SQLException e) {
			informar("Se ha producido un error al buscar los libros en la base de datos.");
			e.printStackTrace();
		}
	}

	public void insertarLibro() {
		try {
			boolean camposCorrectos = recuperarInformacionLibro();
			if (camposCorrectos && bd.insertLibro(ISBN, titulo, autor, editorial, edicion, anioPublicacion) == 1) {
				informar("El libro se ha insertado con �xito.");
				actualizarTablaLibros();
			}
		} catch (SQLException e) {
			informar("Se ha producido un error al agregar el libro.");
			try {
				bd.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			bd = new BD();
			e.printStackTrace();
		}
	}

	public void modificarLibro() {
		try {
			if(recuperarInformacionLibro()) {
				bd.updateLibro(ISBN, titulo, autor, editorial, edicion, anioPublicacion);
				informar("Se ha actualizado el libro con �xito.");
				actualizarTablaLibros();
			}
		} catch (SQLException e) {
			informar("Se ha producido un error al actualizar el libro.");
			e.printStackTrace();
		}
	}

	public void eliminarLibro() {
		try {
			bd.deleteLibro(textISBN.getText().toString());
			informar("Se ha eliminado el libro con �xito.");
			actualizarTablaLibros();
		} catch (SQLException e) {
			informar("Se ha producido un error al eliminar el libro.");
			e.printStackTrace();
		}
	}

	public boolean validarCamposLibro() {
		if (!textISBN.getText().matches(regexISBN)) {
			informar("El ISBN ingresado es invalido.");
			return false;
		}
		if (!textTitulo.getText().matches(regexTitulo)) {
			informar("El t�tulo ingresado es invalido.");
			return false;
		}
		if (!textEdicion.getText().matches(regexEdicion)) {
			informar("La edici�n ingresada es invalida.");
			return false;
		}
		if (!textAnioPublicacion.getText().matches(regexAnio)) {
			informar("El a�o ingresado es invalido.");
			return false;
		}

		return true;
	}
	
	/*--------------------------------------------------------- Autores ----------------------------------------------------------------*/
	
	public void agregarFilaAutor(Entidad autor) {
		int fila = tableModelAutores.getRowCount(), columna = cantidadColumnasEntidad - 1;
		tableModelAutores.addRow(filaVaciaEntidad);
		tableModelAutores.setValueAt(autor.getNombre(), fila, columna);
		tableModelAutores.setValueAt(autor.getId(), fila, columna -= 1);
	}
	
	public void actualizarTablaAutores() {
		limpiarTablaAutores();
		buscarAutor("");
	}
	
	public void limpiarTablaAutores() {
		while (tableModelAutores.getRowCount() > 0) {
			tableModelAutores.removeRow(0);
		}
		tableAutores.clearSelection();
		textAutor.setText("");
	}
	
	public void buscarAutor(String buscar) {
		List<Entidad> autores = null;
		limpiarTablaAutores();
		try {
			autores = bd.searchAutor(textBuscarAutor.getText());
			for (Entidad a : autores) {
				agregarFilaAutor(a);
			}
		} catch (SQLException e) {
			informar("Se ha producido un error al realizar la b�squeda del autor.");
			e.printStackTrace();
		}
	}
	
	public void insertarAutor() {
		try {
			if(textAutor.getText().matches(regexAutor)) {
				if (bd.insertAutor(textAutor.getText()) == 1) {
					informar("El autor se ha insertado con �xito.");
					actualizarTablaAutores();
				}
			} else
				informar("El autor ingresado no es v�lido.");
		} catch (SQLException e) {
			informar("Se ha producido un error al agregar el autor.");
			e.printStackTrace();
			try {
				bd.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			bd = new BD();
		}
	}

	public void modificarAutor() {
		try {
			if(textAutor.getText().matches(regexAutor)) { 
				bd.updateAutor((int) Integer.valueOf(tableAutores.getValueAt(tableAutores.getSelectedRow(), 0).toString()), textAutor.getText());
				informar("Se ha actualizado el autor con �xito.");
				actualizarTablaAutores();
			} else
				informar("El autor ingresado no es v�lido.");
		} catch (SQLException e) {
			informar("Se ha producido un error al actualizar el autor.");
			e.printStackTrace();
		}
	}

	public void eliminarAutor() {
		try {
			bd.deleteAutor((int) Integer.valueOf(tableAutores.getValueAt(tableAutores.getSelectedRow(), 0).toString()));
			informar("Se ha eliminado el autor con �xito.");
			actualizarTablaAutores();
		} catch (SQLException e) {
			informar("Se ha producido un error al eliminar el autor.");
			e.printStackTrace();
		}
	}
	
	
	/*--------------------------------------------------------- Editoriales ----------------------------------------------------------------*/
	
	public void agregarFilaEditorial(Entidad editorial) {
		int fila = tableModelEditoriales.getRowCount(), columna = cantidadColumnasEntidad - 1;
		tableModelEditoriales.addRow(filaVaciaEntidad);
		tableModelEditoriales.setValueAt(editorial.getNombre(), fila, columna);
		tableModelEditoriales.setValueAt(editorial.getId(), fila, columna -= 1);
	}
	
	public void actualizarTablaEditoriales() {
		limpiarTablaEditoriales();
		buscarEditorial("");
	}
	
	public void limpiarTablaEditoriales() {
		while (tableModelEditoriales.getRowCount() > 0) {
			tableModelEditoriales.removeRow(0);
		}
		tableEditoriales.clearSelection();
		textEditorial.setText("");
	}

	public void buscarEditorial(String buscar) {
		List<Entidad> editoriales = null;
		limpiarTablaEditoriales();
		try {

			editoriales = bd.searchEditorial(textBuscarEditorial.getText());
			for (Entidad e : editoriales) {
				agregarFilaEditorial(e);
			}
		} catch (SQLException e) {
			informar("Se ha producido un error al realizar la b�squeda de la editorial.");
			e.printStackTrace();
		}
	}
	
	public void insertarEditorial() {
		try {
			if(textEditorial.getText().matches(regexEditorial)) {
				if (bd.insertEditorial(textEditorial.getText()) == 1) {
					informar("La editorial se ha insertado con �xito.");
					actualizarTablaEditoriales();
				}
			} else
				informar("La editorial ingresada no es v�lida.");
			
		} catch (SQLException e) {
			informar("Se ha producido un error al agregar la editorial.");
			e.printStackTrace();
			try {
				bd.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			bd = new BD();
		}
	}

	public void modificarEditorial() {
		try {
			if(textEditorial.getText().matches(regexEditorial)) {
				bd.updateEditorial((int) Integer.valueOf(tableEditoriales.getValueAt(tableEditoriales.getSelectedRow(), 0).toString()), textEditorial.getText());
				informar("Se ha actualizado la editorial con �xito.");
				actualizarTablaEditoriales();
			} else 
				informar("La editorial ingresada no es v�lida.");
		} catch (SQLException e) {
			informar("Se ha producido un error al actualizar la editorial.");
			e.printStackTrace();
		}
	}

	public void eliminarEditorial() {
		try {
			bd.deleteEditorial((int) Integer.valueOf(tableEditoriales.getValueAt(tableEditoriales.getSelectedRow(), 0).toString()));
			informar("Se ha eliminado la editorial con �xito.");
			actualizarTablaEditoriales();
		} catch (SQLException e) {
			informar("Se ha producido un error al eliminar la editorial.");
			e.printStackTrace();
		}
	}
	
	/*--------------------------------------------------------- Inicializacion ----------------------------------------------------------------*/

	public void cargarMenu() {
		JPanel panelMenu = new JPanel();
		panelMenu.setBackground(new Color(0, 0, 51));
		panelMenu.setBounds(0, 0, 187, 708);
		contentPane.add(panelMenu);
		panelMenu.setLayout(null);

		panelInicio = new JPanel();
		panelInicio.setBackground(new Color(0, 0, 51));
		panelInicio.setBounds(posXIni, posYIni, 187, 47);
		panelMenu.add(panelInicio);

		lblInicio = new JLabel("Inicio");
		lblInicio.setForeground(new Color(255, 255, 255));
		lblInicio.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		panelInicio.add(lblInicio);

		panelAdministracion = new JPanel();
		panelAdministracion.setBackground(new Color(0, 0, 51));
		panelAdministracion.setBounds(posXAdmin, posYAdmin, 187, 47);
		panelMenu.add(panelAdministracion);

		lblAdministracion = new JLabel("Administraci\u00F3n");
		lblAdministracion.setForeground(Color.WHITE);
		lblAdministracion.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		panelAdministracion.add(lblAdministracion);

		panelInicioSeleccionado = new JPanel();
		panelInicioSeleccionado.setBackground(new Color(51, 204, 153));
		panelInicioSeleccionado.setBounds(posXIni, posYIni, 187, 47);
		panelMenu.add(panelInicioSeleccionado);

		labelInicioSeleccionado = new JLabel("Inicio");
		labelInicioSeleccionado.setForeground(Color.WHITE);
		labelInicioSeleccionado.setFont(new Font("Segoe UI", Font.BOLD, 18));
		panelInicioSeleccionado.add(labelInicioSeleccionado);

		panelAdministracionSeleccionado = new JPanel();
		panelAdministracionSeleccionado.setBackground(new Color(51, 204, 153));
		panelAdministracionSeleccionado.setBounds(posXAdmin, posYAdmin, 187, 47);
		panelMenu.add(panelAdministracionSeleccionado);

		labelAdministracionSeleccionado = new JLabel("Administraci\u00F3n");
		labelAdministracionSeleccionado.setForeground(Color.WHITE);
		labelAdministracionSeleccionado.setFont(new Font("Segoe UI", Font.BOLD, 18));
		panelAdministracionSeleccionado.add(labelAdministracionSeleccionado);
	}

	public void cargarInicio() {

		paneInicioVisible = new JPanel();
		paneInicioVisible.setBackground(new Color(255, 255, 255));
		paneInicioVisible.setBounds(187, 0, 1160, 708);
		contentPane.add(paneInicioVisible);
		paneInicioVisible.setLayout(null);

		String columnas[] = { "ISBN", "T�tulo", "Autor", "Editorial", "Edici�n", "A�o de publicaci�n" };
		String datos[][] = {};
		tableModelLibros = new DefaultTableModel(datos, columnas) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(26, 67, 1073, 344);
		paneInicioVisible.add(scrollPane);
		paneInicioVisible.setLayout(null);

		tableLibros = new JTable();
		tableLibros.setBackground(new Color(255, 255, 255));
		tableLibros.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		tableLibros.setModel(tableModelLibros);
		tableLibros.setRowHeight(25);
		scrollPane.setViewportView(tableLibros);

		JLabel lblBuscar = new JLabel("Buscar:");
		lblBuscar.setBounds(12, 14, 65, 20);
		paneInicioVisible.add(lblBuscar);
		lblBuscar.setForeground(new Color(0, 0, 51));
		lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 20));

		textBuscarLibro = new JTextField();
		textBuscarLibro.setBounds(94, 11, 173, 30);
		paneInicioVisible.add(textBuscarLibro);
		textBuscarLibro.setToolTipText("");
		textBuscarLibro.setForeground(new Color(0, 0, 51));
		textBuscarLibro.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		textBuscarLibro.setColumns(10);

		comboAutores = new JComboBox();
		comboAutores.setBounds(315, 11, 175, 30);
		paneInicioVisible.add(comboAutores);
		comboAutores.setForeground(new Color(0, 0, 51));
		comboAutores.setFont(new Font("Segoe UI", Font.PLAIN, 16));

		comboEditoriales = new JComboBox();
		comboEditoriales.setBounds(517, 11, 175, 30);
		paneInicioVisible.add(comboEditoriales);
		comboEditoriales.setForeground(new Color(0, 0, 51));
		comboEditoriales.setFont(new Font("Segoe UI", Font.PLAIN, 16));

		lblOrdenar = new JLabel("Ordenar por:");
		lblOrdenar.setBounds(781, 11, 124, 27);
		paneInicioVisible.add(lblOrdenar);
		lblOrdenar.setForeground(new Color(0, 0, 51));
		lblOrdenar.setFont(new Font("Segoe UI", Font.PLAIN, 20));

		comboOrdenarPor = new JComboBox();
		comboOrdenarPor.setBounds(924, 11, 175, 30);
		paneInicioVisible.add(comboOrdenarPor);
		comboOrdenarPor.setModel(new DefaultComboBoxModel(
				new String[] { "ISBN", "Titulo", "Autor", "Editorial", "Edicion", "AnioPublicacion" }));
		comboOrdenarPor.setForeground(new Color(0, 0, 51));
		comboOrdenarPor.setFont(new Font("Segoe UI", Font.PLAIN, 16));

		JLabel lblISBN = new JLabel("ISBN:");
		lblISBN.setBounds(202, 463, 65, 20);
		paneInicioVisible.add(lblISBN);
		lblISBN.setForeground(new Color(0, 0, 51));
		lblISBN.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		JLabel lblTitulo = new JLabel("T\u00EDtulo:");
		lblTitulo.setBounds(202, 529, 76, 20);
		paneInicioVisible.add(lblTitulo);
		lblTitulo.setForeground(new Color(0, 0, 51));
		lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		JLabel lblAutor = new JLabel("Autor:");
		lblAutor.setBounds(202, 600, 76, 20);
		paneInicioVisible.add(lblAutor);
		lblAutor.setForeground(new Color(0, 0, 51));
		lblAutor.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		JLabel lblEditorial = new JLabel("Editorial:");
		lblEditorial.setBounds(657, 455, 76, 20);
		paneInicioVisible.add(lblEditorial);
		lblEditorial.setForeground(new Color(0, 0, 51));
		lblEditorial.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		JLabel lblEdicion = new JLabel("Edici\u00F3n:");
		lblEdicion.setBounds(657, 529, 87, 20);
		paneInicioVisible.add(lblEdicion);
		lblEdicion.setForeground(new Color(0, 0, 51));
		lblEdicion.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		JLabel lblAnioDePublicacion = new JLabel("A\u00F1o de publicaci\u00F3n:");
		lblAnioDePublicacion.setBounds(657, 599, 165, 23);
		paneInicioVisible.add(lblAnioDePublicacion);
		lblAnioDePublicacion.setForeground(new Color(0, 0, 51));
		lblAnioDePublicacion.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		textISBN = new JTextField();
		textISBN.setBounds(282, 459, 253, 30);
		paneInicioVisible.add(textISBN);
		textISBN.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		textISBN.setColumns(10);

		textTitulo = new JTextField();
		textTitulo.setBounds(283, 526, 253, 30);
		paneInicioVisible.add(textTitulo);
		textTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		textTitulo.setColumns(10);

		comboAutor = new JComboBox();
		comboAutor.setBounds(287, 599, 253, 30);
		paneInicioVisible.add(comboAutor);
		comboAutor.setForeground(new Color(0, 0, 51));
		comboAutor.setFont(new Font("Segoe UI", Font.PLAIN, 16));

		comboEditorial = new JComboBox();
		comboEditorial.setBounds(774, 451, 253, 30);
		paneInicioVisible.add(comboEditorial);
		comboEditorial.setForeground(new Color(0, 0, 51));
		comboEditorial.setFont(new Font("Segoe UI", Font.PLAIN, 16));

		textEdicion = new JTextField();
		textEdicion.setBounds(774, 521, 253, 30);
		paneInicioVisible.add(textEdicion);
		textEdicion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		textEdicion.setColumns(10);

		textAnioPublicacion = new JTextField();
		textAnioPublicacion.setBounds(851, 594, 124, 30);
		paneInicioVisible.add(textAnioPublicacion);
		textAnioPublicacion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		textAnioPublicacion.setColumns(10);

		btnAgregarLibro = new JButton("Agregar");
		btnAgregarLibro.setBounds(26, 453, 141, 40);
		paneInicioVisible.add(btnAgregarLibro);
		btnAgregarLibro.setForeground(new Color(0, 0, 51));
		btnAgregarLibro.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		btnModificarLibro = new JButton("Modificar");
		btnModificarLibro.setBounds(26, 533, 141, 40);
		paneInicioVisible.add(btnModificarLibro);
		btnModificarLibro.setForeground(new Color(0, 0, 51));
		btnModificarLibro.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		btnEliminarLibro = new JButton("Eliminar");
		btnEliminarLibro.setBounds(26, 613, 141, 40);
		paneInicioVisible.add(btnEliminarLibro);
		btnEliminarLibro.setForeground(new Color(0, 0, 51));
		btnEliminarLibro.setFont(new Font("Segoe UI", Font.PLAIN, 18));
	}

	public void cargarAdministracion() {

		paneAdministracionVisible = new JPanel();
		paneAdministracionVisible.setBackground(Color.WHITE);
		paneAdministracionVisible.setBounds(187, 0, 1131, 708);
		contentPane.add(paneAdministracionVisible);

		String[] columnas = { "ID", "Nombre" };
		String datos[][] = {};
		tableModelAutores = new DefaultTableModel(datos, columnas) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableModelEditoriales = new DefaultTableModel(datos, columnas) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		JScrollPane scrollPaneAutores = new JScrollPane();
		scrollPaneAutores.setBounds(26, 67, 650, 250);
		paneAdministracionVisible.add(scrollPaneAutores);
		paneAdministracionVisible.setLayout(null);
		paneAdministracionVisible.setVisible(true);

		tableAutores = new JTable();
		tableAutores.setBackground(new Color(255, 255, 255));
		tableAutores.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		tableAutores.setModel(tableModelAutores);
		tableAutores.setRowHeight(25);
		scrollPaneAutores.setViewportView(tableAutores);

		JScrollPane scrollPaneEditoriales = new JScrollPane();
		scrollPaneEditoriales.setBounds(26, 400, 650, 250);
		paneAdministracionVisible.add(scrollPaneEditoriales);

		tableEditoriales = new JTable();
		tableEditoriales.setBackground(new Color(255, 255, 255));
		tableEditoriales.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		tableEditoriales.setModel(tableModelEditoriales);
		tableEditoriales.setRowHeight(25);
		scrollPaneEditoriales.setViewportView(tableEditoriales);

		JLabel lblBuscarAutor = new JLabel("Buscar:");
		lblBuscarAutor.setForeground(new Color(0, 0, 51));
		lblBuscarAutor.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblBuscarAutor.setBounds(26, 14, 65, 20);
		paneAdministracionVisible.add(lblBuscarAutor);

		textBuscarAutor = new JTextField();
		textBuscarAutor.setBounds(108, 9, 173, 35);
		paneAdministracionVisible.add(textBuscarAutor);
		textBuscarAutor.setToolTipText("");
		textBuscarAutor.setForeground(new Color(0, 0, 51));
		textBuscarAutor.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		textBuscarAutor.setColumns(10);
		paneAdministracionVisible.add(textBuscarAutor);

		JLabel lblBuscarEditorial = new JLabel("Buscar:");
		lblBuscarEditorial.setForeground(new Color(0, 0, 51));
		lblBuscarEditorial.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblBuscarEditorial.setBounds(26, 350, 65, 20);
		paneAdministracionVisible.add(lblBuscarEditorial);

		textBuscarEditorial = new JTextField();
		textBuscarEditorial.setBounds(108, 345, 173, 35);
		paneAdministracionVisible.add(textBuscarEditorial);
		textBuscarEditorial.setToolTipText("");
		textBuscarEditorial.setForeground(new Color(0, 0, 51));
		textBuscarEditorial.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		textBuscarEditorial.setColumns(10);
		paneAdministracionVisible.add(textBuscarEditorial);

		JLabel lblAutor = new JLabel("Autor:");
		lblAutor.setBounds(750, 120, 122, 23);
		paneAdministracionVisible.add(lblAutor);
		lblAutor.setForeground(new Color(0, 0, 51));
		lblAutor.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		textAutor = new JTextField();
		textAutor.setBounds(850, 115, 200, 35);
		paneAdministracionVisible.add(textAutor);
		textAutor.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		textAutor.setColumns(10);

		btnAgregarAutor = new JButton("Agregar");
		btnAgregarAutor.setBounds(680, 190, 130, 40);
		paneAdministracionVisible.add(btnAgregarAutor);
		btnAgregarAutor.setForeground(new Color(0, 0, 51));
		btnAgregarAutor.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		btnModificarAutor = new JButton("Modificar");
		btnModificarAutor.setBounds(840, 190, 130, 40);
		paneAdministracionVisible.add(btnModificarAutor);
		btnModificarAutor.setForeground(new Color(0, 0, 51));
		btnModificarAutor.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		btnEliminarAutor = new JButton("Eliminar");
		btnEliminarAutor.setBounds(1000, 190, 130, 40);
		paneAdministracionVisible.add(btnEliminarAutor);
		btnEliminarAutor.setForeground(new Color(0, 0, 51));
		btnEliminarAutor.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		JLabel lblEditorial = new JLabel("Editorial:");
		lblEditorial.setBounds(750, 450, 122, 23);
		paneAdministracionVisible.add(lblEditorial);
		lblEditorial.setForeground(new Color(0, 0, 51));
		lblEditorial.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		textEditorial = new JTextField();
		textEditorial.setBounds(850, 445, 200, 35);
		paneAdministracionVisible.add(textEditorial);
		textEditorial.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		textEditorial.setColumns(10);

		btnAgregarEditorial = new JButton("Agregar");
		btnAgregarEditorial.setBounds(680, 520, 130, 40);
		paneAdministracionVisible.add(btnAgregarEditorial);
		btnAgregarEditorial.setForeground(new Color(0, 0, 51));
		btnAgregarEditorial.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		btnModificarEditorial = new JButton("Modificar");
		btnModificarEditorial.setBounds(840, 520, 130, 40);
		paneAdministracionVisible.add(btnModificarEditorial);
		btnModificarEditorial.setForeground(new Color(0, 0, 51));
		btnModificarEditorial.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		btnEliminarEditorial = new JButton("Eliminar");
		btnEliminarEditorial.setBounds(1000, 520, 130, 40);
		paneAdministracionVisible.add(btnEliminarEditorial);
		btnEliminarEditorial.setForeground(new Color(0, 0, 51));
		btnEliminarEditorial.setFont(new Font("Segoe UI", Font.PLAIN, 18));
	}

	public void cargarListeners() {
		comboOrdenarPor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buscarLibro("");
			}
		});

		comboEditoriales.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buscarLibro("");
			}
		});

		comboAutores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buscarLibro("");
			}
		});

		textBuscarLibro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				buscarLibro("");
			}
		});

		textBuscarAutor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				buscarAutor("");
			}
		});

		textBuscarEditorial.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				buscarEditorial("");
			}
		});

		btnEliminarLibro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(tableLibros.getSelectedRow() != -1)
					eliminarLibro();
			else
				informar("Debe seleccionar un libro de la tabla.");			}
		});

		btnModificarLibro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(tableLibros.getSelectedRow() != -1)
					modificarLibro();
				else
					informar("Debe seleccionar un libro de la tabla.");
			}
		});

		btnAgregarLibro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				insertarLibro();
			}
		});

		btnEliminarAutor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(tableAutores.getSelectedRow() != -1)
					eliminarAutor();
				else
					informar("Debe seleccionar un autor de la tabla.");
			}
		});

		btnModificarAutor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(tableAutores.getSelectedRow() != -1)
					modificarAutor();
				else
					informar("Debe seleccionar un autor de la tabla.");
			}
		});

		btnAgregarAutor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				insertarAutor();
			}
		});

		btnEliminarEditorial.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(tableEditoriales.getSelectedRow() != -1)
					eliminarEditorial();
				else
					informar("Debe seleccionar una editorial de la tabla.");
			}
		});

		btnModificarEditorial.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(tableEditoriales.getSelectedRow() != -1)
					modificarEditorial();
				else
					informar("Debe seleccionar una editorial de la tabla.");
			}
		});

		btnAgregarEditorial.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				insertarEditorial();
			}
		});

		textISBN.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				String isbn = textISBN.getText();
				tableLibros.clearSelection();
				textISBN.setText(isbn);
				if (isbn.matches(regexISBN))
					buscarLibro(isbn);
			}
		});

		tableLibros.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (tableLibros.getSelectedRow() != -1) {
					llenarCamposLibro(tableLibros.getSelectedRow());
				}
			}
		});

		tableAutores.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (tableAutores.getSelectedRow() != -1) {
					int columna = cantidadColumnasEntidad - 1;

					textAutor.setText(tableModelAutores.getValueAt(tableAutores.getSelectedRow(), columna).toString());
				}
			}
		});

		tableEditoriales.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (tableEditoriales.getSelectedRow() != -1) {
					int columna = cantidadColumnasEntidad - 1;

					textEditorial.setText(
							tableModelEditoriales.getValueAt(tableEditoriales.getSelectedRow(), columna).toString());
				}
			}
		});

		panelInicio.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				cargarAutoresYEditoriales();
				panelInicio.setVisible(false);
				panelInicioSeleccionado.setEnabled(true);
				panelInicioSeleccionado.setVisible(true);
				panelAdministracion.setVisible(true);
				panelAdministracionSeleccionado.setVisible(false);
				paneInicioVisible.setVisible(true);
				paneAdministracionVisible.setVisible(false);
			}
		});

		panelAdministracion.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
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
		comboAutores.removeAllItems();
		comboAutor.removeAllItems();
		comboAutores.addItem("Autor");
		comboAutor.addItem("Autor");
		comboEditoriales.removeAllItems();
		comboEditorial.removeAllItems();
		comboEditoriales.addItem("Editorial");
		comboEditorial.addItem("Editorial");

		try {
			limpiarTablaAutores();
			limpiarTablaEditoriales();
			List<Entidad> autores = bd.getAutores();
			List<Entidad> editoriales = bd.getEditoriales();

			for (Entidad a : autores) {
				comboAutores.addItem(a.getNombre());
				comboAutor.addItem(a.getNombre());
				agregarFilaAutor(a);
			}

			for (Entidad e : editoriales) {
				comboEditoriales.addItem(e.getNombre());
				comboEditorial.addItem(e.getNombre());
				agregarFilaEditorial(e);
			}

		} catch (SQLException e) {
			informar("Se ha producido un error al cargar los autores y las editoriales.");
			e.printStackTrace();
		}

	}
	
	
	
	/*--------------------------------------------------------- Otras ----------------------------------------------------------------*/

	public void informar(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje);
	}

	public JTable getTableLibros() {
		return tableLibros;
	}

	public void setTableLibros(JTable tableLibros) {
		this.tableLibros = tableLibros;
	}

	public DefaultTableModel getTableModel() {
		return tableModelLibros;
	}

	public void setTableModel(DefaultTableModel tableModel) {
		this.tableModelLibros = tableModel;
	}

	public String[] getDatos() {
		return filaVaciaLibro;
	}

	public BD getBd() {
		return bd;
	}
}
