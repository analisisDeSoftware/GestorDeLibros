package gestor;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import login.Login;

public class Launcher {
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {//Metodo que se acciona al iniciar el programa
				try {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
					Login frame = new Login();//Crea la ventana de Login
					//Configuracion de la ventana
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
}
