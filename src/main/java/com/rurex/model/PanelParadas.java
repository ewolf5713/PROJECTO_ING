package com.rurex.model;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente reutilizable: encabezado "Paradas:" con botón "+ Parada" y una
 * cuadrícula de campos, cada uno con su botón para quitarlo.
 * Se usa tanto en el formulario principal como en la ventana de edición.
 */
public class PanelParadas extends JPanel {

    private final JPanel cuadricula;
    private final List<Estilo.CampoTexto> campos = new ArrayList<>();
    private Runnable alCambiar; // se ejecuta cuando se agrega o quita una parada

    public PanelParadas(int columnas) {
        super(new BorderLayout(0, 8));
        setOpaque(false);

        Estilo.Boton btnAgregar = new Estilo.Boton("+ Parada", Color.WHITE, Estilo.TEXTO, Estilo.BORDE_BOTON);
        btnAgregar.setPreferredSize(new Dimension(100, 30));
        btnAgregar.addActionListener(e -> {
            agregarParada("");
            campos.get(campos.size() - 1).requestFocusInWindow();
        });

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        encabezado.add(Estilo.etiqueta("Paradas:", 13f, false, Estilo.TEXTO), BorderLayout.WEST);
        encabezado.add(btnAgregar, BorderLayout.EAST);

        cuadricula = new JPanel(new GridLayout(0, columnas, Estilo.SEPARACION, 8));
        cuadricula.setOpaque(false);

        // El contenedor evita que las filas se estiren si sobra espacio vertical
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setOpaque(false);
        contenedor.add(cuadricula, BorderLayout.NORTH);

        add(encabezado, BorderLayout.NORTH);
        add(contenedor, BorderLayout.CENTER);
    }

    /** Agrega un campo de parada con el texto indicado (puede ser vacío). */
    public void agregarParada(String texto) {
        Estilo.CampoTexto campo = new Estilo.CampoTexto("");
        campo.setText(texto);

        Estilo.Boton btnQuitar = new Estilo.Boton("×", Color.WHITE, Estilo.ROJO, Estilo.BORDE_BOTON);
        btnQuitar.setToolTipText("Quitar esta parada");
        btnQuitar.setPreferredSize(new Dimension(36, 32));

        JPanel celda = new JPanel(new BorderLayout(6, 0));
        celda.setOpaque(false);
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
        for (Estilo.CampoTexto campo : campos) {
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

    /** Permite avisar a la ventana contenedora (por ejemplo, para que haga pack()). */
    public void setAlCambiar(Runnable alCambiar) {
        this.alCambiar = alCambiar;
    }

    private void refrescar() {
        // Renumera las pistas ("Parada 1", "Parada 2"...) por si se quitó alguna
        for (int i = 0; i < campos.size(); i++) {
            campos.get(i).setPista("Parada " + (i + 1));
        }
        cuadricula.revalidate();
        cuadricula.repaint();
        if (alCambiar != null) {
            alCambiar.run();
        }
    }
}
