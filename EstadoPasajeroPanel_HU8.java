import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

//solo muestra si el pasajero tiene reserva y se refresca con el comando del conductor
public class EstadoPasajeroPanel_HU8 extends JPanel implements RecorridoLista_HU8 {
    private static  final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    private final Itinerario_HU8 itinerario;
    private final String pasajeroId;
    private final RecorridoServicio_HU8 servicio = RecorridoServicio_HU8.getInstancia();
    private final JPanel contenido = new JPanel();
 
    public EstadoPasajeroPanel_HU8(Itinerario_HU8 itinerario, String pasajeroId, String nombreUsuario) {
        super(new BorderLayout());
        this.itinerario = itinerario;
        this.pasajeroId = pasajeroId;
        setBackground(Estilo_HU8.FONDO);
 
        // Encabezado: titulo a la izquierda, usuario a la derecha
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Color.WHITE);
        encabezado.setBorder(new EmptyBorder(14, 14, 14, 14));
        encabezado.add(Estilo_HU8.etiqueta("Estado del Recorrido", Estilo_HU8.TITULO_PANTALLA,
                Estilo_HU8.TEXTO), BorderLayout.WEST);
        JPanel usuario = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        usuario.setOpaque(false);
        usuario.add(Estilo_HU8.etiqueta("Usuario:", Estilo_HU8.BASE, Estilo_HU8.TEXTO_SUAVE));
        usuario.add(Estilo_HU8.etiqueta(nombreUsuario, Estilo_HU8.NEGRITA, Estilo_HU8.TEXTO));
        encabezado.add(usuario, BorderLayout.EAST);
        add(encabezado, BorderLayout.NORTH);
 
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(Estilo_HU8.FONDO);
        contenido.setBorder(new EmptyBorder(14, 14, 14, 14));
        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
 
        refrescar();
    }
 
    // Se suscribe/desuscribe al servicio segun la pantalla este en uso (evita fugas de memoria)
    @Override public void addNotify() {
        super.addNotify();
        servicio.agregarLista(this);
        refrescar(); // por si el estado cambio mientras no estaba visible
    }
 
    @Override public void removeNotify() {
        servicio.quitarLista(this);
        super.removeNotify();
    }
 
    @Override
    public void alCambiarEstado(RecorridoEstado_HU8 nuevoEstado) {
        if (!nuevoEstado.getItinerarioId().equals(itinerario.getId())) return;
        SwingUtilities.invokeLater(this::refrescar);   // siempre actualizar la UI en el EDT
    }
 
    private void refrescar() {
        contenido.removeAll();
        Optional<RecorridoEstado_HU8> estado = servicio.obtenerEstadoParaPasajero(pasajeroId, itinerario);
 
        if (!estado.isPresent()) {
            contenido.add(tarjetaSinReserva());
        } else {
            RecorridoEstado_HU8 e = estado.get();
            contenido.add(bannerInfo());
            contenido.add(Box.createVerticalStrut(12));
            contenido.add(tarjetaViaje());
            contenido.add(Box.createVerticalStrut(12));
            contenido.add(tarjetaEstado(e));
            contenido.add(Box.createVerticalStrut(12));
            contenido.add(tarjetaParadas(e));
        }
        contenido.revalidate();
        contenido.repaint();
    }
 
    // ---------- Piezas de la pantalla ----------
 
    private JComponent bannerInfo() {
        JPanel b = new JPanel(new BorderLayout());
        b.setBackground(Estilo_HU8.AMARILLO);
        b.setBorder(new EmptyBorder(8, 10, 8, 10));
        b.setAlignmentX(LEFT_ALIGNMENT);
        b.add(Estilo_HU8.etiqueta("Información actualizada en tiempo real por el conductor del recorrido.",
                Estilo_HU8.BASE, Estilo_HU8.TEXTO));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, b.getPreferredSize().height));
        return b;
    }
 
    private JComponent tarjetaViaje() {
        Estilo_HU8.Tarjeta t = new Estilo_HU8.Tarjeta("Información del Viaje");
        JPanel fila = new JPanel(new GridLayout(1, 4, 12, 0));
        fila.setOpaque(false);
        fila.add(Estilo_HU8.campo("Ruta:", itinerario.getRuta()));
        fila.add(Estilo_HU8.campo("Unidad:", itinerario.getUnidad()));
        fila.add(Estilo_HU8.campo("Conductor:", itinerario.getConductor()));
        fila.add(Estilo_HU8.campo("Horario:", itinerario.getHorario()));
        t.cuerpo.add(fila);
        return t;
    }
 
    private JComponent tarjetaEstado(RecorridoEstado_HU8 e) {
        Estilo_HU8.Tarjeta t = new Estilo_HU8.Tarjeta("Estado Actual del Recorrido");
        JPanel interior = new JPanel(new BorderLayout(0, 12));
        interior.setOpaque(false);
 
        // Stepper: 3 pasos + barra de progreso
        EtapasRecorrido_HU8[] pasos = EtapasRecorrido_HU8.values();
        JPanel columnas = new JPanel(new GridLayout(1, pasos.length));
        columnas.setOpaque(false);
        for (EtapasRecorrido_HU8 p : pasos) {
            columnas.add(columnaPaso(p, e.getEstado()));
        }
        JPanel stepper = new JPanel(new BorderLayout(0, 6));
        stepper.setOpaque(false);
        stepper.add(columnas, BorderLayout.CENTER);
        stepper.add(new Estilo_HU8.Barra((e.getEstado().paso() - 1) / (double) (pasos.length - 1)),
                BorderLayout.SOUTH);
        interior.add(stepper, BorderLayout.NORTH);
 
        // Recuadro verde con el mensaje del estado
        String detalle = e.getEstado().getDescripcion();
        if (e.getEstado() != EtapasRecorrido_HU8.LLEGANDO_AL_DESTINO || e.getEtaMinutos() > 0) {
            if (e.getEtaMinutos() > 0) detalle += " Tiempo estimado de llegada: " + e.getEtaMinutos() + " minutos.";
        }
        JPanel mensaje = new JPanel(new GridLayout(3, 1, 0, 4));
        mensaje.setBackground(Estilo_HU8.VERDE_CLARO);
        mensaje.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo_HU8.VERDE), new EmptyBorder(10, 10, 10, 10)));
        mensaje.add(Estilo_HU8.etiqueta(e.getEstado().getTitulo(), Estilo_HU8.BASE.deriveFont(Font.BOLD, 14f),
                Estilo_HU8.TEXTO));
        mensaje.add(Estilo_HU8.etiqueta(detalle, Estilo_HU8.BASE, Estilo_HU8.TEXTO_SUAVE));
        mensaje.add(Estilo_HU8.etiqueta("Última actualización: Hoy a las "
                + e.getUltimaActualizacion().format(HORA), Estilo_HU8.PEQUENA, Estilo_HU8.TEXTO_SUAVE));
        interior.add(mensaje, BorderLayout.CENTER);
 
        t.cuerpo.add(interior);
        return t;
    }
 
    private JComponent columnaPaso(EtapasRecorrido_HU8 paso, EtapasRecorrido_HU8 actual) {
        boolean alcanzado = paso.paso() <= actual.paso();
        boolean esActual = paso == actual;
 
        JLabel numero = alcanzado
                ? Estilo_HU8.badge(String.valueOf(paso.paso()), Estilo_HU8.VERDE, Color.WHITE, Estilo_HU8.VERDE, 28)
                : Estilo_HU8.badge(String.valueOf(paso.paso()), Color.WHITE, Estilo_HU8.TEXTO_SUAVE, Estilo_HU8.BORDE, 28);
        numero.setAlignmentX(CENTER_ALIGNMENT);
 
        JLabel texto = Estilo_HU8.etiqueta(paso.getEtiqueta(),
                esActual ? Estilo_HU8.NEGRITA : Estilo_HU8.PEQUENA,
                esActual ? Estilo_HU8.VERDE : Estilo_HU8.TEXTO_SUAVE);
        texto.setAlignmentX(CENTER_ALIGNMENT);
 
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.add(numero);
        col.add(Box.createVerticalStrut(4));
        col.add(texto);
        return col;
    }
 
    private JComponent tarjetaParadas(RecorridoEstado_HU8 e) {
        Estilo_HU8.Tarjeta t = new Estilo_HU8.Tarjeta("Paradas del Recorrido");
        List<Itinerario_HU8.Parada> paradas = itinerario.getParadas();
        JPanel lista = new JPanel(new GridLayout(0, 1, 0, 8));
        lista.setOpaque(false);
        for (int i = 0; i < paradas.size(); i++) {
            lista.add(filaParada(i, paradas.get(i), e.getParadaActual()));
        }
        t.cuerpo.add(lista);
        return t;
    }
 
    private JComponent filaParada(int i, Itinerario_HU8.Parada parada, int paradaActual) {
        boolean completada = i < paradaActual;
        boolean actual = i == paradaActual;
 
        JPanel fila = new JPanel(new BorderLayout(8, 0));
        fila.setBackground(actual ? Estilo_HU8.VERDE_CLARO : new Color(0xF8F9FB));
        fila.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(actual ? Estilo_HU8.VERDE : Estilo_HU8.BORDE),
                new EmptyBorder(6, 8, 6, 8)));
 
        Color fondoBadge = actual ? Estilo_HU8.VERDE : (completada ? Estilo_HU8.GRIS_BADGE : Color.WHITE);
        Color letraBadge = (actual || completada) ? Color.WHITE : Estilo_HU8.TEXTO_SUAVE;
        fila.add(Estilo_HU8.badge(String.valueOf(i + 1), fondoBadge, letraBadge, fondoBadge, 22), BorderLayout.WEST);
 
        Color colorTexto = actual ? Estilo_HU8.TEXTO : Estilo_HU8.TEXTO_SUAVE;
        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        textos.add(Estilo_HU8.etiqueta(parada.getNombre(), Estilo_HU8.BASE, colorTexto));
        textos.add(Estilo_HU8.etiqueta(parada.getHora(), Estilo_HU8.PEQUENA, Estilo_HU8.TEXTO_SUAVE));
        fila.add(textos, BorderLayout.CENTER);
 
        String estadoTxt = completada ? "Completada" : (actual ? "Actual" : "Pendiente");
        JLabel tag = Estilo_HU8.etiqueta(estadoTxt, Estilo_HU8.PEQUENA, actual ? Color.WHITE : Estilo_HU8.TEXTO_SUAVE);
        tag.setOpaque(true);
        tag.setBackground(actual ? Estilo_HU8.VERDE : new Color(0xE5E7EB));
        tag.setBorder(new EmptyBorder(2, 8, 2, 8));
        JPanel tagWrap = new JPanel(new GridBagLayout());   // centra el tag verticalmente
        tagWrap.setOpaque(false);
        tagWrap.add(tag);
        fila.add(tagWrap, BorderLayout.EAST);
        return fila;
    }
 
    private JComponent tarjetaSinReserva() {
        Estilo_HU8.Tarjeta t = new Estilo_HU8.Tarjeta("Estado no disponible");
        t.cuerpo.add(Estilo_HU8.etiqueta(
                "Solo los pasajeros con una reserva para este itinerario pueden ver el estado del recorrido.",
                Estilo_HU8.BASE, Estilo_HU8.TEXTO_SUAVE));
        return t;
    }

}