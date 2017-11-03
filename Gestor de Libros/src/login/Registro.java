package login;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

import gestor.BD;
import gestor.Gestor;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class Registro extends JFrame {

	private JPanel contentPane;
	private JTextField textUser;
	private JPasswordField textPassword;
	private JPasswordField textPassword2;
	
	private BD bd;
	private String regexUser = "[a-zA-Z]{1,15}";
	private String regexPass = ".{6}.*";
	

	/**
	 * Launch the application.
	 */
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
					Registro frame = new Registro();
					frame.setLocationRelativeTo(null);
					frame.setTitle("Gestor de Libros - Registro");
					frame.setResizable(false);
					frame.setVisible(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Registro() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 560, 563);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 51));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Configuracion de la etiqueta de Usuario
		JLabel lblUser = new JLabel("Usuario");
		lblUser.setForeground(new Color(255, 255, 255));
		lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 22));
		lblUser.setBounds(36, 120, 85, 35);
		contentPane.add(lblUser);
		
		// Configuracion de la etiqueta de la Contrasenia
		JLabel lblPassword = new JLabel("Contrase\u00F1a");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 22));
		lblPassword.setBounds(36, 212, 109, 35);
		contentPane.add(lblPassword);
		
		// Configuracion del texto de Usuario
		textUser = new JTextField();
		textUser.setForeground(new Color(0, 0, 51));
		textUser.setFont(new Font("Segoe UI", Font.PLAIN, 22));
		textUser.setBounds(259, 120, 238, 35);
		contentPane.add(textUser);
		textUser.setColumns(10);
		
		// Configuracion del texto de Contrasenia
		textPassword = new JPasswordField();
		textPassword.setForeground(new Color(0, 0, 51));
		textPassword.setFont(new Font("Segoe UI", Font.PLAIN, 22));
		textPassword.setColumns(10);
		textPassword.setBounds(259, 212, 238, 35);
		contentPane.add(textPassword);
		
		// Configuracion del boton Registrarse
		JButton btnRegistrarse = new JButton("Registrarse");
		btnRegistrarse.setBackground(new Color(255, 255, 255));
		btnRegistrarse.setForeground(new Color(0, 0, 51));
		btnRegistrarse.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		btnRegistrarse.setBounds(173, 378, 214, 40);
		contentPane.add(btnRegistrarse);
		
		// Configuracion del panel Iniciar sesion
		JPanel panelIniciarSesion = new JPanel();
		panelIniciarSesion.setBackground(new Color(0, 0, 51));
		panelIniciarSesion.setBounds(135, 456, 289, 35);
		contentPane.add(panelIniciarSesion);
		panelIniciarSesion.setLayout(null);
		
		// Configuracion de la etiqueta de Iniciar sesion
		JLabel lblIniciarSesion = new JLabel("¿Ya tenés una cuenta? ¡Inicia sesión!");
		lblIniciarSesion.setBounds(0, 0, 289, 35);
		panelIniciarSesion.add(lblIniciarSesion);
		lblIniciarSesion.setForeground(new Color(255, 255, 255));
		lblIniciarSesion.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		
		// Configuracion de la etiqueta de Iniciar sesion (en negrita)
		JLabel lblIniciarSesionBold = new JLabel("¿Ya tenés una cuenta? ¡Inicia sesión!");
		lblIniciarSesionBold.setBounds(0, 0, 302, 35);
		panelIniciarSesion.add(lblIniciarSesionBold);
		lblIniciarSesionBold.setForeground(Color.WHITE);
		lblIniciarSesionBold.setFont(new Font("Segoe UI", Font.BOLD, 17));
		
		// Configuracion de la etiqueta de Constrasenia2
		JLabel labelPassword2 = new JLabel("Verifiar Contrase\u00F1a");
		labelPassword2.setForeground(Color.WHITE);
		labelPassword2.setFont(new Font("Segoe UI", Font.PLAIN, 22));
		labelPassword2.setBounds(36, 304, 185, 35);
		contentPane.add(labelPassword2);
		
		// Configuracion del texto de Contrasenia2
		textPassword2 = new JPasswordField();
		textPassword2.setForeground(new Color(0, 0, 51));
		textPassword2.setFont(new Font("Segoe UI", Font.PLAIN, 22));
		textPassword2.setColumns(10);
		textPassword2.setBounds(259, 304, 238, 35);
		contentPane.add(textPassword2);
		lblIniciarSesionBold.setVisible(false);
		
		
		// Listeners
		
		btnRegistrarse.addActionListener(new ActionListener() {//Listener para el boton registrarse
			public void actionPerformed(ActionEvent arg0) {
				try {
					registrarse();//Registra al Usuario
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "El usuario ingresado ya existe.");//Error, ya existe el Usuario
					e.printStackTrace();
				}
			}
		});
		
		
		
		panelIniciarSesion.addMouseListener(new MouseAdapter() {//Listener del panel Iniciar sesion
			@Override
			public void mouseEntered(MouseEvent arg0) {//Pone el panel en negrita
				lblIniciarSesion.setVisible(false);
				lblIniciarSesionBold.setVisible(true);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {//Abre la ventana de Iniciar Sesion
				setVisible(false);
				dispose();
				Login frame = new Login();
				frame.setLocationRelativeTo(null);
				frame.setTitle("Gestor de Libros - Iniciar Sesión");
				frame.setResizable(false);
				frame.setVisible(true);
			}
		});
		
		
		
		contentPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {//Quita la negrita del panel Iniciar sesion
				lblIniciarSesion.setVisible(true);
				lblIniciarSesionBold.setVisible(false);
			}
		});
		
		
		bd = new BD();
	}
	
	public void registrarse() throws SQLException {
		String user = textUser.getText();
		String pass = textPassword.getText();
		String verificacionPass = textPassword2.getText();
		
		if(!user.matches(regexUser)) { // Verificamos que el nombre de usuario sea correcto
			JOptionPane.showMessageDialog(null, "El nombre de usuario debe estar compuesto por letras minúsculas y/o mayúsculas con un máximo de 15 caracteres.");
			return;
		}
		
		if(!pass.matches(regexPass)) { // Verificamos que el password sea correcto
			JOptionPane.showMessageDialog(null, "La contraseña debe estar formada por al menos 6 caracteres.");
			return;
		}
		
		if(!pass.equals(verificacionPass)) { // Verificamos que ambos passwords coincidan
			JOptionPane.showMessageDialog(null, "La contraseña ingresada y su verificación no coinciden.");
			return;
		}
		
		registrarEnDB(textUser.getText(), String.valueOf(textPassword.getPassword())); // Registramos el nuevo usuario en la BD
		setVisible(false); // Cerramos la ventana
		dispose();
		Gestor frame = new Gestor(); // Creamos un gestor
		frame.setLocationRelativeTo(null); // Posicionamos la ventana en el centro
		frame.setTitle("Gestor de Libros - Registrarse"); // Seteamos el nombre de la ventana
		frame.setResizable(false); // Deshabilitamos el cambio de tamanio
		frame.setVisible(true); // Hacemos visible el gestor
	}
	
	public void registrarEnDB(String user, String password) throws SQLException {
		bd.insertUsuario(user, Encriptar.md5(password)); // Insertamos el nuevo usuario en la BD
	}
}
