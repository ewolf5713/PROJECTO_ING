/*
La idea es pasar 
EN_PARADA -> EN_TRAYECTO
EN_TRAYECTO -> EN_PARADA O LLEGANDO
LLEGANDO DESTINO -> FINAL
*/

//tenemos las etapas de un viaje con su logica para las transciones con sus constructores
public enum EtapasRecorrido_HU8 {
    EN_PARADA("En Parada",
            "El bus está en parada",
            "La unidad se encuentra detenida en una parada."),
    EN_TRAYECTO("En Trayecto",
            "El bus está en trayecto",
            "La unidad se encuentra circulando hacia su destino."),
    LLEGANDO_AL_DESTINO("Llegando al Destino",
            "El bus está llegando al destino",
            "La unidad está próxima a llegar a su destino final.");

            private final String etiqueta;
            private final String titulo;
            private final String descripcion;

            EtapasRecorrido_HU8(String etiqueta, String titulo, String descripcion) {
                this.etiqueta = etiqueta;
                this.titulo = titulo;
                this.descripcion = descripcion;
            }

            public String getEtiqueta() { return etiqueta; }
            public String getTitulo() { return titulo; }
            public String getDescripcion() { return descripcion; }

            public int paso() { return ordinal()+1; }

            public boolean puedeCambiarA(EtapasRecorrido_HU8 nuevo) {
                if (nuevo == this) return true;
                switch (this) {
                    case EN_PARADA: return nuevo == EN_TRAYECTO;
                    case EN_TRAYECTO: return true;
                
                    default:
                        return  false;
                }
            }
}