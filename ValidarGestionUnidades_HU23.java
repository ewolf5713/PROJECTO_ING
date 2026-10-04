import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.io.*;

public class ValidarGestionUnidades_HU23 {

    private GestionUnidades_HU23 gestion;
    private final String ARCHIVO_TEST = "unidades_test.txt";

    @BeforeEach
    public void setUp() {
        // Inicializar la interfaz en el hilo de Swing para evitar problemas de concurrencia
        gestion = new GestionUnidades_HU23();
        // Redirigir la lógica al archivo temporal de pruebas
        gestion.rutaArchivo = ARCHIVO_TEST;
    }

    @AfterEach
    public void tearDown() {
        // Eliminar el archivo temporal de pruebas después de cada caso para no dejar basura
        File archivo = new File(ARCHIVO_TEST);
        if (archivo.exists()) {
            archivo.delete();
        }
    }

    // Auxiliar para escribir rápidamente registros simulados en el archivo de pruebas
    private void escribirLineasEnTest(String... lineas) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_TEST))) {
            for (String linea : lineas) {
                writer.write(linea);
                writer.newLine();
            }
        }
    }

    // === CASO 1: Verificar que el listado devuelva todas las unidades registradas ===
    @Test
    public void testCargarTodasLasUnidades() throws IOException {
        escribirLineasEnTest(
            "ABC-123|Autobús Volvo|45 pasajeros|Activo",
            "XYZ-789|Minivan Iveco|19 pasajeros|En Mantenimiento"
        );

        gestion.cargarDatosDesdeArchivo();

        // Verificar que se leyeron exactamente las 2 filas insertadas
        assertEquals(2, gestion.getExtendedTableModel().getRowCount(), 
            "El listado debería contener exactamente 2 unidades registradas.");
    }

    // === CASO 2: Verificar que el Filtrado por estado funcione correctamente ===
    @Test
    public void testFiltradoPorEstado() throws IOException {
        escribirLineasEnTest(
            "ABC-123|Autobús Volvo|45 pasajeros|Activo",
            "DEF-456|Scania R450|50 pasajeros|Activo",
            "XYZ-789|Minivan Iveco|19 pasajeros|Fuera de Servicio"
        );

        gestion.cargarDatosDesdeArchivo();

        // Simular el comportamiento del JComboBox aplicando el filtro estricto por "Activo"
        RowSorter<? extends javax.swing.table.TableModel> sorter = gestion.getTableSorter();
        var regexFilter = RowFilter.regexFilter("^Activo$", 3);
        ((DefaultRowSorter<?, ?>) sorter).setRowFilter(regexFilter);

        // El getViewRowCount() devuelve cuántas filas son VISIBLES tras el colador del filtro
        assertEquals(2, sorter.getViewRowCount(), 
            "El filtro debería mostrar únicamente las 2 unidades con estado 'Activo'.");
    }

    // === CASO 3: Verificar que la lista está vacía cuando no hay unidades ===
    @Test
    public void testListaVaciaCuandoNoHayUnidades() throws IOException {
        // Crear un archivo de texto totalmente en blanco (0 bytes)
        escribirLineasEnTest(); 

        gestion.cargarDatosDesdeArchivo();

        // Verificar que el modelo de la tabla no tenga ninguna fila
        assertEquals(0, gestion.getExtendedTableModel().getRowCount(), 
            "La tabla debe iniciar con 0 filas si el archivo de texto está vacío.");
    }
}
