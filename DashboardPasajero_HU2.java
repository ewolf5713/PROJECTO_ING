package PROJECTO_ING;
import javax.swing.*;
import java.awt.*;

public class DashboardPasajero_HU2 extends JFrame {
    
    // Constructor de la ventana
    public DashboardPasajero_HU2(String nombreUsuario) {//Recibe el nombre dado en el recuadro de usuario
        // Configuración básica de la nueva ventana
        setTitle("Panel de Pasajero");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(245, 245, 245)); // color del fondo Gris
        add(panel);

        // Mensaje de bienvenida generico
        JLabel lblBienvenida = new JLabel("¡Bienvenido al sistema, " + nombreUsuario + "!");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
        lblBienvenida.setBounds(50, 30, 400, 30);
        panel.add(lblBienvenida);
    }
}
