package login;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField textUser;
	private JPasswordField textPassword;
	
	private BD bd;

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
					Login frame = new Login();
					frame.setLocationRelativeTo(null);
					frame.setTitle("Gestor de Libros - Iniciar Sesión");
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
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 563);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 51));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUser = new JLabel("Usuario");
		lblUser.setForeground(new Color(255, 255, 255));
		lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 22));
		lblUser.setBounds(36, 120, 85, 35);
		contentPane.add(lblUser);
		
		JLabel lblPassword = new JLabel("Contrase\u00F1a");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 22));
		lblPassword.setBounds(36, 212, 109, 35);
		contentPane.add(lblPassword);
		
		textUser = new JTextField();
		textUser.setForeground(new Color(0, 0, 51));
		textUser.setFont(new Font("Segoe UI", Font.PLAIN, 22));
		textUser.setBounds(181, 119, 238, 35);
		contentPane.add(textUser);
		textUser.setColumns(10);
		
		textPassword = new JPasswordField();
		textPassword.setForeground(new Color(0, 0, 51));
		textPassword.setFont(new Font("Segoe UI", Font.PLAIN, 22));
		textPassword.setColumns(10);
		textPassword.setBounds(181, 211, 238, 35);
		contentPane.add(textPassword);
		
		JButton btnIngresar = new JButton("Ingresar");
		btnIngresar.setBackground(new Color(255, 255, 255));
		btnIngresar.setForeground(new Color(0, 0, 51));
		btnIngresar.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		btnIngresar.setBounds(126, 302, 214, 40);
		contentPane.add(btnIngresar);
		
		JPanel panelRegistrarse = new JPanel();
		panelRegistrarse.setBackground(new Color(0, 0, 51));
		panelRegistrarse.setBounds(96, 380, 289, 35);
		contentPane.add(panelRegistrarse);
		panelRegistrarse.setLayout(null);
		
		JLabel lblRegistrarse = new JLabel("\u00BFNo ten\u00E9s una cuenta? \u00A1Reg\u00EDstrate!");
		lblRegistrarse.setBounds(0, 0, 289, 35);
		panelRegistrarse.add(lblRegistrarse);
		lblRegistrarse.setForeground(new Color(255, 255, 255));
		lblRegistrarse.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		
		JLabel lblRegistrarseBold = new JLabel("\u00BFNo ten\u00E9s una cuenta? \u00A1Reg\u00EDstrate!");
		lblRegistrarseBold.setBounds(0, 0, 302, 35);
		panelRegistrarse.add(lblRegistrarseBold);
		lblRegistrarseBold.setForeground(Color.WHITE);
		lblRegistrarseBold.setFont(new Font("Segoe UI", Font.BOLD, 17));
		lblRegistrarseBold.setVisible(false);
		
		
		// Listeners
		
		btnIngresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					login(textUser.getText(), String.valueOf(textPassword.getPassword()));
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Se ha producido un error al iniciar sesion.");
					e.printStackTrace();
				}
			}
		});
		
		
		panelRegistrarse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblRegistrarse.setVisible(false);
				lblRegistrarseBold.setVisible(true);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				setVisible(false);
				dispose();
				new Registro().main(null);
			}
		});
		
		
		
		contentPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblRegistrarse.setVisible(true);
				lblRegistrarseBold.setVisible(false);
			}
		});
		
		
		bd = new BD();
		
	}
	
	public void login(String user, String password) throws SQLException {
		if(bd.verificarLogin(user, password)) {
			setVisible(false);
			dispose();
			new Gestor().main(null);
		} else {
			JOptionPane.showMessageDialog(null, "El usuario y la contraseña no coinciden.");
		}
	}
}
