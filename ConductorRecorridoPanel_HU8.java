import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//Desde acá el conductor actualiza el estado del recorrido
public class ConductorRecorridoPanel_HU8 extends JPanel implements RecorridoLista_HU8 {

    //establecemos el formato de tiempo y la zona horaria donde estemos trabajando
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    private final Itinerario_HU8 itinerario;
    private final RecorridoServicio_HU8 servicio = RecorridoServicio_HU8.getInstancia();

    //esta logica es para ofreer paradas desde la actual y no las anteriores
    private final Map<EtapasRecorrido_HU8, JRadioButton> radios = new EnumMap<>(EtapasRecorrido_HU8.class);
    private final JSpinner spnEta = new JSpinner(new SpinnerNumberModel(0, 0, 240, 1));
    private final JLabel lblActual = new JLabel();
    private final JLabel lblMensaje = new JLabel(" ");
    private final JButton btnActualizar = Estilo_HU8.botonPrimario("Actualizar Estado");
    private int offsetParada = 0;
    private boolean refrescando = false;

    public ConductorRecorridoPanel_HU8(Itinerario_HU8 itinerario, String nombreConductor) {
        super(new BorderLayout());
        this.itinerario = itinerario;
        setBackground(Estilo_HU8.FONDO);
    }
}