package redcarga;

import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        RedCarga red = new RedCarga();
        RutaOptima rutaOptima = new RutaOptima(red.getGrafo());
        ZonaInfluencia zona = new ZonaInfluencia(red.getGrafo());

        System.out.println("=== RED DE CARGA - TRACTOMULAS (Equipo 14) ===");
        System.out.println("Ciudades: " + red.getCiudades().size()
                + " | Rutas: " + red.getGrafo().edgeSet().size());

        Scanner teclado = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println();
            System.out.println("----------- MENÚ -----------");
            System.out.println("1. Ruta óptima (Dijkstra)");
            System.out.println("2. Zona de influencia (BFS)");
            System.out.println("3. Ver ciudades");
            System.out.println("4. Demostración automática");
            System.out.println("0. Salir");
            System.out.print("Opción: ");

            String opcion = teclado.nextLine().trim();

            switch (opcion) {
                case "1":
                    String origen = pedirCiudad(teclado, red, "Ciudad de origen: ");
                    String destino = pedirCiudad(teclado, red, "Ciudad de destino: ");
                    if (origen != null && destino != null) {
                        rutaOptima.imprimir(origen, destino);
                    }
                    break;

                case "2":
                    String inicio = pedirCiudad(teclado, red, "Ciudad de inicio: ");
                    if (inicio != null) {
                        int saltos = pedirSaltos(teclado);
                        zona.imprimir(inicio, saltos);
                    }
                    break;

                case "3":
                    System.out.println("Ciudades: " + red.getCiudades());
                    break;

                case "4":
                    demostracion(rutaOptima, zona);
                    break;

                case "0":
                    salir = true;
                    System.out.println("Hasta luego.");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        }
        teclado.close();
    }

    /** Pide una ciudad al usuario y valida que exista en la red. */
    private static String pedirCiudad(Scanner teclado, RedCarga red, String mensaje) {
        System.out.print(mensaje);
        String ciudad = red.buscarCiudad(teclado.nextLine());
        if (ciudad == null) {
            System.out.println("Esa ciudad no existe. Ciudades válidas: " + red.getCiudades());
        }
        return ciudad;
    }

    /** Pide el máximo de saltos; si el usuario deja vacío, explora toda la red. */
    private static int pedirSaltos(Scanner teclado) {
        System.out.print("Máximo de saltos (Enter = toda la red): ");
        String texto = teclado.nextLine().trim();
        if (texto.isEmpty()) {
            return Integer.MAX_VALUE;
        }
        try {
            int saltos = Integer.parseInt(texto);
            return saltos < 0 ? Integer.MAX_VALUE : saltos;
        } catch (NumberFormatException e) {
            System.out.println("Número no válido, se explora toda la red.");
            return Integer.MAX_VALUE;
        }
    }

    /** Ejecuta ejemplos fijos, útiles para mostrar en la sustentación. */
    private static void demostracion(RutaOptima rutaOptima, ZonaInfluencia zona) {
        rutaOptima.imprimir("Bogota", "Cartagena");
        rutaOptima.imprimir("Bogota", "Cucuta");
        rutaOptima.imprimir("Cali", "Tunja");
        rutaOptima.imprimir("Tunja", "Bogota"); // no hay ruta de vuelta
        zona.imprimir("Bogota", 1);
        zona.imprimir("Bogota", Integer.MAX_VALUE);
    }
}
