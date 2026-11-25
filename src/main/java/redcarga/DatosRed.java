package redcarga;


public final class DatosRed {

    private DatosRed() {
        // Clase de utilidad: no se instancia.
    }


    public static final class Ruta {
        public final String origen;
        public final String destino;
        public final double minutos;

        public Ruta(String origen, String destino, double minutos) {
            this.origen = origen;
            this.destino = destino;
            this.minutos = minutos;
        }
    }

    /** Los 15 nodos (ciudades) de la red. */
    public static final String[] NODOS = {
            "Bogota", "Medellin", "Cali", "Buenaventura", "Cartagena",
            "Barranquilla", "Santa Marta", "Cucuta", "Bucaramanga", "Ibague",
            "Neiva", "Villavicencio", "Pasto", "Popayan", "Tunja"
    };

    /** Las 20 rutas dirigidas (origen -> destino) con sus minutos. */
    public static final Ruta[] RUTAS = {
            new Ruta("Bogota", "Medellin", 10),
            new Ruta("Medellin", "Cali", 9),
            new Ruta("Cali", "Buenaventura", 10),
            new Ruta("Buenaventura", "Cartagena", 21),
            new Ruta("Cartagena", "Barranquilla", 10),
            new Ruta("Barranquilla", "Santa Marta", 12),
            new Ruta("Santa Marta", "Cucuta", 7),
            new Ruta("Cucuta", "Bucaramanga", 22),
            new Ruta("Bucaramanga", "Ibague", 8),
            new Ruta("Ibague", "Neiva", 11),
            new Ruta("Neiva", "Villavicencio", 23),
            new Ruta("Villavicencio", "Pasto", 21),
            new Ruta("Pasto", "Popayan", 13),
            new Ruta("Popayan", "Tunja", 24),
            new Ruta("Bogota", "Cartagena", 27),
            new Ruta("Buenaventura", "Cucuta", 44),
            new Ruta("Santa Marta", "Neiva", 43),
            new Ruta("Ibague", "Popayan", 23),
            new Ruta("Cali", "Pasto", 29),
            new Ruta("Medellin", "Tunja", 31)
    };
}
