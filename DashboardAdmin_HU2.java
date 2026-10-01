package PROJECTO_ING;
import javax.swing.*;
import java.awt.*;

public class DashboardAdmin_HU2 extends JFrame {
    
    // Constructor de la ventana
    public DashboardAdmin_HU2(String nombreUsuario) {//Recibe el nombre dado en el recuadro de usuario
        // Configuración básica de la nueva ventana
        setTitle("Panel de Administrador");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(245, 245, 245)); // Gris claro fondo
        add(panel);

        //Mensaje de bienvenida generico
        JLabel lblBienvenida = new JLabel("¡Bienvenido al sistema, " + nombreUsuario + "!");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
        lblBienvenida.setBounds(50, 30, 400, 30);
        panel.add(lblBienvenida);
    }
}
