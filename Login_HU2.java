package PROJECTO_ING;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login_HU2 {
    public static void main(String[] args) {
        // Crear la ventana principal (JFrame)
        JFrame window = new JFrame("Inicio de Sesion");
        window.setSize(400, 340);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        // Crear el panel que contendra el texto
        JPanel panel = new JPanel();
        panel.setLayout(null);
        window.add(panel);

        //Componentes de la Interfaz --->

        // Título principal
        JLabel lblTitle = new JLabel("Iniciar Sesion", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBounds(50, 20, 300, 30);
        panel.add(lblTitle);

        // Etiqueta y Campo de Usuario
        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setBounds(50, 80, 80, 25);
        panel.add(lblUser);

        JTextField txtUser = new JTextField();
        txtUser.setBounds(140, 80, 200, 25);
        panel.add(txtUser);

        // Etiqueta y Campo de Contraseña (password)
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(50, 120, 80, 25);
        panel.add(lblPassword);

        JPasswordField txtPassword = new JPasswordField();//este tipo se usa para la pass
        txtPassword.setBounds(140, 120, 200, 25);
        panel.add(txtPassword);

        // boton de Ingreso
        JButton buttonLogin = new JButton("Ingresar");
        buttonLogin.setBounds(140, 180, 120, 30);
        buttonLogin.setBackground(new Color(33, 150, 243)); //color boton azul claro**
        buttonLogin.setForeground(Color.white);//color del texto
        buttonLogin.setFocusPainted(false);
        panel.add(buttonLogin);

        // boton de Olvide mi contraseña
        JButton buttonForget = new JButton("Olvide mi contraseña");
        buttonForget.setBounds(100, 230, 200, 25);//size y forma
        //buttonForget.setBackground(new Color(33, 150, 243)); //color boton azul claro**
        buttonForget.setContentAreaFilled(false);//activar color del boton
        buttonForget.setBorderPainted(false);//activar color en el borde
        buttonForget.setForeground(Color.gray);//color del texto
        buttonForget.setFocusPainted(false);
        panel.add(buttonForget);
        //la logica de este boton sera llevarte a un restablecimiento de la password

        // logica del boton (Evento)
        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = txtUser.getText();
                String password = new String(txtPassword.getPassword());

                // Validaciones para Roles posibles**
                //admin (prueba)
                if (user.equals("admin") && password.equals("1234")) {
                    JOptionPane.showMessageDialog(window, "¡Inicio de sesion exitoso!", "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
                    // Crea y hace visible la nueva ventana pasando el nombre del usuario
                    DashboardAdmin_HU2 pantallaAdmin = new DashboardAdmin_HU2(user);
                    pantallaAdmin.setVisible(true);
                    
                    // Destruye y cierra la ventana de login actual
                    window.dispose();
                } else if(user.equals("pasajero") && password.equals("abcd")){
                    JOptionPane.showMessageDialog(window, "¡Inicio de sesión exitoso!", "Bienvenido", JOptionPane.INFORMATION_MESSAGE);

                    // Crear ventana del pasajero
                    DashboardPasajero_HU2 pantallaUser = new DashboardPasajero_HU2(user);
                    pantallaUser.setVisible(true);
                    
                    // Destruir ventana actual
                    window.dispose();
                } else {//Caso por defecto**
                    JOptionPane.showMessageDialog(window, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // hace visible la ventana
        window.setVisible(true);
    }
}
