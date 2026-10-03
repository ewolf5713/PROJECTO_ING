import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/** Pop up del botón "Cambiar Estado": solo cambia el estado operativo de la unidad. */
public class DialogoCambiarEstado_HU24 extends JDialog {
    private static final Color VERDE = new Color(0x1E8E5A);
    private static final Color BORDE = new Color(0xDADCE2);

    public DialogoCambiarEstado_HU24(Frame padre, UnidadServicio_HU24 servicio,
                                     Unidad_HU24 unidad, Runnable alGuardar) {
        super(padre, "Cambiar Estado", true);

        JLabel titulo = new JLabel(unidad.getPlaca() + " - " + unidad.getModelo());
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 15f));
        JLabel actual = new JLabel("Estado actual: " + unidad.getEstado().getEtiqueta());
        JComboBox<EstadoOperativo_HU22> cmbEstado = new JComboBox<>(EstadoOperativo_HU22.values());
        cmbEstado.setSelectedItem(unidad.getEstado());

        JPanel centro = new JPanel(new GridLayout(0, 1, 0, 10));
        centro.setBorder(new EmptyBorder(20, 24, 10, 24));
        centro.add(titulo);
        centro.add(actual);
        centro.add(new JLabel("Nuevo estado operativo"));
        centro.add(cmbEstado);

        JButton guardar = new JButton("Guardar");
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
            try {
                servicio.cambiarEstado(unidad.getPlaca(),
                        (EstadoOperativo_HU22) cmbEstado.getSelectedItem());
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        add(centro, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
        pack();
        setMinimumSize(new Dimension(340, 0));
        setResizable(false);
        setLocationRelativeTo(padre);
    }
}
