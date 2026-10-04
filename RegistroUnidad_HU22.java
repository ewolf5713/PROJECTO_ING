import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RegistroUnidad_HU22 extends JFrame {

    private GestionUnidades_HU23 ventanaGestion; // Referencia para poder actualizar la tabla

    // Constructor que se usará desde la pantalla de gestión
    public RegistroUnidad_HU22(GestionUnidades_HU23 gestion) {
        this.ventanaGestion = gestion;
        initUI();
    }

    // Constructor vacío por defecto
    public RegistroUnidad_HU22() {
        initUI();
    }

    private void initUI() {
        setTitle("Registro de Unidad");
        setSize(450, 420);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        JLabel lblEncabezado = new JLabel("Formulario de Registro de Unidad de Transporte", SwingConstants.CENTER);
        lblEncabezado.setFont(new Font("Arial", Font.BOLD, 14));
        lblEncabezado.setBounds(20, 20, 400, 25);
        panel.add(lblEncabezado);

        JLabel lblPlaca = new JLabel("Placa:");
        lblPlaca.setBounds(50, 80, 100, 25);
        panel.add(lblPlaca);

        JTextField txtPlaca = new JTextField();
        txtPlaca.setBounds(180, 80, 200, 25);
        panel.add(txtPlaca);

        JLabel lblModelo = new JLabel("Modelo:");
        lblModelo.setBounds(50, 130, 100, 25);
        panel.add(lblModelo);

        JTextField txtModelo = new JTextField();
        txtModelo.setBounds(180, 130, 200, 25);
        panel.add(txtModelo);

        JLabel lblCapacidad = new JLabel("Capacidad Pasajeros:");
        lblCapacidad.setBounds(50, 180, 130, 25);
        panel.add(lblCapacidad);

        JTextField txtCapacidad = new JTextField();
        txtCapacidad.setBounds(180, 180, 200, 25);
        panel.add(txtCapacidad);

        JLabel lblEstado = new JLabel("Estado Operativo:");
        lblEstado.setBounds(50, 230, 120, 25);
        panel.add(lblEstado);

        String[] opcionesEstado = {"Seleccione una opción...", "Activo", "En Mantenimiento", "Fuera de Servicio"};
        JComboBox<String> cmbEstado = new JComboBox<>(opcionesEstado);
        cmbEstado.setBounds(180, 230, 200, 25);
        panel.add(cmbEstado);

        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.setBounds(150, 300, 120, 35);
        btnEnviar.setBackground(new Color(76, 175, 80));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFont(new Font("Arial", Font.BOLD, 12));
        btnEnviar.setFocusPainted(false);
        panel.add(btnEnviar);

        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String placa = txtPlaca.getText().trim();
                String modelo = txtModelo.getText().trim();
                String capacidadTexto = txtCapacidad.getText().trim();
                int estadoSeleccionado = cmbEstado.getSelectedIndex();

                if (placa.isEmpty() || modelo.isEmpty() || capacidadTexto.isEmpty()) {
                    JOptionPane.showMessageDialog(RegistroUnidad_HU22.this, "Error: Todos los campos de texto deben estar llenos.", "Formulario Incompleto", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (estadoSeleccionado == 0) {
                    JOptionPane.showMessageDialog(RegistroUnidad_HU22.this, "Error: Debe seleccionar un Estado Operativo.", "Selección Inválida", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int capacidadNumero = 0;
                try {
                    capacidadNumero = Integer.parseInt(capacidadTexto);
                    if (capacidadNumero <= 0) {
                        JOptionPane.showMessageDialog(RegistroUnidad_HU22.this, "Error: La capacidad debe ser mayor a cero.", "Número Inválido", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(RegistroUnidad_HU22.this, "Error: Ingrese únicamente números enteros en Capacidad.", "Error de Tipo de Dato", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String estadoTexto = (String) cmbEstado.getSelectedItem();

                // === GUARDADO: Guardar en el archivo de texto ===
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("unidades.txt", true))) {
                    // Formato: Placa|Modelo|Capacidad pasajeros|Estado
                    writer.write(placa + "|" + modelo + "|" + capacidadNumero + " pasajeros|" + estadoTexto);
                    writer.newLine(); // Salto de línea para la siguiente unidad
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(RegistroUnidad_HU22.this, "Error al escribir en el archivo de datos.", "Error de Archivo", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(RegistroUnidad_HU22.this, "¡Unidad registrada con éxito en el archivo!", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                
                // Si abrimos esto desde Gestión de Unidades, refrescamos la tabla automáticamente
                if (ventanaGestion != null) {
                    ventanaGestion.cargarDatosDesdeArchivo();
                }

                // Limpiar campos y cerrar formulario
                txtPlaca.setText("");
                txtModelo.setText("");
                txtCapacidad.setText("");
                cmbEstado.setSelectedIndex(0);
                dispose(); 
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistroUnidadTest().setVisible(true));
    }
}
