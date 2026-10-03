package PROJECTO_ING;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistroUnidad_HU22 extends JFrame {

    public RegistroUnidad_HU22() {
        //Crea la ventana inicial
        setTitle("Transporte UCV - Registrar Nueva Unidad");
        setSize(1546, 890);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla

        // Panel contenedor
        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        // Encabezado
        JLabel lblEncabezado = new JLabel("Formulario de Registro de Unidad de Transporte", SwingConstants.CENTER);
        lblEncabezado.setFont(new Font("Arial", Font.BOLD, 16));
        lblEncabezado.setBounds(20, 20, 400, 25);
        panel.add(lblEncabezado);

        //boton de Identificador
        JLabel lblIdent = new JLabel("Identificador:");
        lblIdent.setBounds(50, 80, 100, 25);
        panel.add(lblIdent);

        JTextField txtIdent = new JTextField();
        txtIdent.setBounds(180, 80, 200, 25);
        panel.add(txtIdent);

        //boton que recibe PLACA
        JLabel lblPlaca = new JLabel("Placa:");
        lblPlaca.setBounds(50, 130, 100, 25);
        panel.add(lblPlaca);

        JTextField txtPlaca = new JTextField();
        txtPlaca.setBounds(180, 130, 200, 25);
        panel.add(txtPlaca);

        // MODELO
        JLabel lblModelo = new JLabel("Modelo:");
        lblModelo.setBounds(50, 180, 100, 25);
        panel.add(lblModelo);

        JTextField txtModelo = new JTextField();
        txtModelo.setBounds(180, 180, 200, 25);
        panel.add(txtModelo);

        // CAPACIDAD
        JLabel lblCapacidad = new JLabel("Capacidad:");
        lblCapacidad.setBounds(50, 230, 130, 25);
        panel.add(lblCapacidad);

        JTextField txtCapacidad = new JTextField();
        txtCapacidad.setBounds(180, 230, 200, 25);
        panel.add(txtCapacidad);

        // Año del vehiculo
        JLabel lblYear = new JLabel("Año:");
        lblYear.setBounds(50, 280, 130, 25);
        panel.add(lblYear);

        JTextField txtYear = new JTextField();
        txtYear.setBounds(180, 280, 200, 25);
        panel.add(txtYear);


        // ESTADO OPERATIVO lista de opciones
        /** esta opcion debe estar disponible en otra ventana 
        JLabel lblEstado = new JLabel("Estado Operativo:");
        lblEstado.setBounds(50, 230, 120, 25);
        panel.add(lblEstado);

        String[] opcionesEstado = {"Seleccione una opción...", "Operativo", "Requiere Revision", "En Mantenimiento"};
        JComboBox<String> cmbEstado = new JComboBox<>(opcionesEstado);
        cmbEstado.setBounds(180, 230, 200, 25);
        panel.add(cmbEstado);
*/
        // boton de enviar
        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.setBounds(150, 400, 120, 35);
        btnEnviar.setBackground(new Color(76, 175, 80)); // Verde
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFont(new Font("Arial", Font.BOLD, 12));
        btnEnviar.setFocusPainted(false);
        panel.add(btnEnviar);

        // Validaciones que pueden ser necesarias
        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //elimina espacios extras
                //en este punto de la clase se guarda la informacion de interes
                String identificador = txtIdent.getText().trim();
                String placa = txtPlaca.getText().trim();
                String modelo = txtModelo.getText().trim();
                String capacidadTexto = txtCapacidad.getText().trim();
                String yearTexto = txtYear.getText().trim();
                //int estadoSeleccionado = cmbEstado.getSelectedIndex();

                //Validaciones, excepciones, errores
                String errorBody; 
                String errorTitle;

                // Verificar que ningun campo de texto este vacío
                if (identificador.isEmpty() || placa.isEmpty() || modelo.isEmpty() || capacidadTexto.isEmpty() || yearTexto.isEmpty()) {
                    errorBody ="Error: Todos los campos de texto deben estar llenos."; 
                    errorTitle = "Formulario Incompleto";
                    JOptionPane.showMessageDialog(RegistroUnidad_HU22.this, errorBody, errorTitle, JOptionPane.WARNING_MESSAGE);
                    return; // Detener la ejecución
                }

                // Verificar que se haya seleccionado un estado valido
                /** 
                if (estadoSeleccionado == 0) {
                    errorBody = "Error: Debe seleccionar un Estado Operativo de la lista.";
                    errorTitle = "Selección Invalida";
                    JOptionPane.showMessageDialog(RegistroUnidad.this, errorBody, errorTitle, JOptionPane.WARNING_MESSAGE);
                    return;
                }
*/
                // Validación 3: Verificar que la capacidad sea estrictamente un número entero (int)
                int capacidadNumero = 0;
                int yearNumero = 0;
                try {
                    capacidadNumero = Integer.parseInt(capacidadTexto);
                    // OPCIONAL que no sea un numero negativo

                    if (capacidadNumero <= 0) {
                        JOptionPane.showMessageDialog(RegistroUnidad_HU22.this, 
                            "Error: La capacidad debe ser un número mayor a cero.", 
                            "Número Inválido", JOptionPane.ERROR_MESSAGE);
                        return;

                    }
                } catch (NumberFormatException ex) {
                    // Entra aqui si contiene letras, decimales o caracteres especiales
                    errorBody = "Error en el campo Capacidad: Debe ingresar unicamente numeros enteros (sin letras ni simbolos).";
                    errorTitle = "Error de Tipo de Dato";
                    JOptionPane.showMessageDialog(RegistroUnidad_HU22.this, errorBody, errorTitle, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    yearNumero = Integer.parseInt(yearTexto);
                    // OPCIONAL que no sea un numero negativo
                    
                    if (yearNumero <= 0) {
                        JOptionPane.showMessageDialog(RegistroUnidad_HU22.this, 
                            "Error: El año debe ser un número mayor a cero.", 
                            "Número Inválido", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                } catch (NumberFormatException ex) {
                    // Entra aqui si contiene letras, decimales o caracteres especiales
                    errorBody = "Error en el campo Año: Debe ingresar unicamente numeros enteros (sin letras ni simbolos).";
                    errorTitle = "Error de Tipo de Dato";
                    JOptionPane.showMessageDialog(RegistroUnidad_HU22.this, errorBody, errorTitle, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Paso todas las validaciones=>
                String mensajeExito = "¡Unidad registrada con exito!\n\n" +
                                      "Identificador: " + identificador + "\n" +
                                      "Placa: " + placa + "\n" +
                                      "Modelo: " + modelo + "\n" +
                                      "Capacidad: " + capacidadNumero + " pasajeros\n" +
                                      "Año: " + yearNumero + "\n";
                String tituloExito= "Registro Exitoso";
                JOptionPane.showMessageDialog(RegistroUnidad_HU22.this, mensajeExito, tituloExito, JOptionPane.INFORMATION_MESSAGE);
                
                // Opcional: Limpiar los campos después del éxito
                /** 
                txtPlaca.setText("");
                txtModelo.setText("");
                txtCapacidad.setText("");
                cmbEstado.setSelectedIndex(0);
                */
            }
        });
    }

    // test
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RegistroUnidad_HU22().setVisible(true);
        });
    }
}
