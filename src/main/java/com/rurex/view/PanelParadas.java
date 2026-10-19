package com.rurex.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente reutilizable: encabezado "Paradas:" con botón "+ Parada" y una
 * cuadrícula de campos, cada uno con su botón para quitarlo.
 * Se usa en el formulario principal y en la ventana de edición.
 */
public class PanelParadas extends JPanel {

    private final JPanel cuadricula;
    private final List<JTextField> campos = new ArrayList<>();
    private Runnable alCambiar; // se ejecuta cuando se agrega o quita una parada

    public PanelParadas(int columnas) {
        super(new BorderLayout(0, 8));
        setBackground(Color.WHITE);
        setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnAgregar = Estilos.botonOutline("+ Parada");
        btnAgregar.addActionListener(e -> {
            agregarParada("");
            campos.get(campos.size() - 1).requestFocusInWindow();
        });

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Color.WHITE);
        encabezado.add(Estilos.etiqueta("Paradas:", Font.PLAIN, 12, Estilos.NAVY), BorderLayout.WEST);
        encabezado.add(btnAgregar, BorderLayout.EAST);

        cuadricula = new JPanel(new GridLayout(0, columnas, 12, 8));
        cuadricula.setBackground(Color.WHITE);

        // El contenedor evita que las filas se estiren si sobra espacio vertical
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(Color.WHITE);
        contenedor.add(cuadricula, BorderLayout.NORTH);

        add(encabezado, BorderLayout.NORTH);
        add(contenedor, BorderLayout.CENTER);
    }

    /** Agrega un campo de parada con el texto indicado (puede ser vacío). */
    public void agregarParada(String texto) {
        JTextField campo = Estilos.campo("Nombre de la parada");
        campo.setText(texto);

        JButton btnQuitar = Estilos.botonOutline("×");
        btnQuitar.setToolTipText("Quitar esta parada");
        btnQuitar.setPreferredSize(new Dimension(44, 34));

        JPanel celda = new JPanel(new BorderLayout(6, 0));
        celda.setBackground(Color.WHITE);
        celda.add(campo, BorderLayout.CENTER);
        celda.add(btnQuitar, BorderLayout.EAST);

        btnQuitar.addActionListener(e -> {
            campos.remove(campo);
            cuadricula.remove(celda);
            refrescar();
        });

        campos.add(campo);
        cuadricula.add(celda);
        refrescar();
    }

    /** Devuelve el texto de todos los campos (incluidos los vacíos; Ruta los ignora). */
    public List<String> getParadas() {
        List<String> paradas = new ArrayList<>();
        for (JTextField campo : campos) {
            paradas.add(campo.getText());
        }
        return paradas;
    }

    /** Quita todos los campos. */
    public void limpiar() {
        campos.clear();
        cuadricula.removeAll();
        refrescar();
    }

    /** Permite avisar a la ventana contenedora (por ejemplo, para que se reajuste el diálogo). */
    public void setAlCambiar(Runnable alCambiar) {
        this.alCambiar = alCambiar;
    }

    private void refrescar() {
        cuadricula.revalidate();
        cuadricula.repaint();
        if (alCambiar != null) {
            alCambiar.run();
        }
    }

    // Altura máxima dinámica: así el BoxLayout de los formularios crece junto con las paradas
    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }
}
