import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/** Tarea 1: pop up de edición (modelo, capacidad y selector de estado operativo). */
public class DialogoEditarUnidad_HU24 extends JDialog {
    private static final Color VERDE = new Color(0x1E8E5A);
    private static final Color BORDE = new Color(0xDADCE2);

    private final JTextField txtPlaca = new JTextField(20);
    private final JTextField txtModelo = new JTextField(20);
    private final JTextField txtCapacidad = new JTextField(20);
    private final JComboBox<EstadoOperativo_HU22> cmbEstado =
            new JComboBox<>(EstadoOperativo_HU22.values());

    public DialogoEditarUnidad_HU24(Frame padre, UnidadServicio_HU24 servicio,
                                    Unidad_HU24 unidad, Runnable alGuardar) {
        super(padre, "Editar Unidad", true);

        txtPlaca.setText(unidad.getPlaca());
        txtPlaca.setEditable(false);
        txtModelo.setText(unidad.getModelo());
        txtCapacidad.setText(String.valueOf(unidad.getCapacidad()));
        cmbEstado.setSelectedItem(unidad.getEstado());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 24, 10, 24));
        agregarFila(form, 0, "Placa", txtPlaca);
        agregarFila(form, 1, "Modelo", txtModelo);
        agregarFila(form, 2, "Capacidad (pasajeros)", txtCapacidad);
        agregarFila(form, 3, "Estado operativo", cmbEstado);

        JButton guardar = new JButton("Guardar cambios");
        guardar.setBackground(VERDE);
        guardar.setForeground(Color.WHITE);
        guardar.setOpaque(true);
        guardar.setFocusPainted(false);
        guardar.setBorder(new EmptyBorder(10, 20, 10, 20));
        JButton cancelar = new JButton("Cancelar");
        cancelar.setFocusPainted(false);
        cancelar.setBackground(Color.WHITE);
        cancelar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDE), new EmptyBorder(9, 20, 9, 20)));

        guardar.addActionListener(e -> {
            int capacidad;
            try {
                capacidad = Integer.parseInt(txtCapacidad.getText().trim());
            } catch (NumberFormatException ex) {
                mostrarError("La capacidad debe ser un número entero mayor a cero.");
                return;
            }
            try {
                servicio.actualizar(unidad.getPlaca(), txtModelo.getText(), capacidad,
                        (EstadoOperativo_HU22) cmbEstado.getSelectedItem());
            } catch (IllegalArgumentException ex) {
                mostrarError(ex.getMessage());
                return;
            }
            alGuardar.run();
            dispose();
        });
        cancelar.addActionListener(e -> dispose());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        botones.setBorder(new EmptyBorder(0, 16, 10, 16));
        botones.add(cancelar);
        botones.add(guardar);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
        pack();
        setResizable(false);
        setLocationRelativeTo(padre);
    }

    private void agregarFila(JPanel panel, int fila, String etiqueta, JComponent campo) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = fila;
        c.insets = new Insets(6, 0, 6, 12);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = new Insets(6, 0, 6, 0);
        panel.add(campo, c);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Datos inválidos", JOptionPane.ERROR_MESSAGE);
    }
}
