package redcarga;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;


public class RutaOptima {

    private final Graph<String, DefaultWeightedEdge> grafo;
    private final DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra;

    public RutaOptima(Graph<String, DefaultWeightedEdge> grafo) {
        this.grafo = grafo;
        // Se crea el objeto Dijkstra una sola vez sobre el grafo.
        this.dijkstra = new DijkstraShortestPath<>(grafo);
    }

    /**
     * Calcula el camino de menor tiempo entre dos ciudades.
     *
     * @return el camino, o null si no existe ninguna ruta de origen a destino
     */
    public GraphPath<String, DefaultWeightedEdge> calcular(String origen, String destino) {
        return dijkstra.getPath(origen, destino);
    }

    /** Calcula e imprime la ruta óptima con el detalle de cada tramo. */
    public void imprimir(String origen, String destino) {
        GraphPath<String, DefaultWeightedEdge> camino = calcular(origen, destino);

        System.out.println();
        System.out.println("=== RUTA ÓPTIMA (Dijkstra): " + origen + " -> " + destino + " ===");

        if (camino == null) {
            System.out.println("No hay conexión disponible entre " + origen + " y " + destino
                    + " (las rutas son de un solo sentido).");
            return;
        }

        // Ruta como texto: A -> B -> C
        List<String> ciudades = camino.getVertexList();
        System.out.println("Ruta: " + String.join(" -> ", ciudades));

        // Detalle de cada tramo con su tiempo
        for (DefaultWeightedEdge tramo : camino.getEdgeList()) {
            String desde = grafo.getEdgeSource(tramo);
            String hasta = grafo.getEdgeTarget(tramo);
            double minutos = grafo.getEdgeWeight(tramo);
            System.out.println("  " + desde + " -> " + hasta + ": " + formato(minutos) + " min");
        }

        System.out.println("Tiempo total: " + formato(camino.getWeight()) + " min");
        System.out.println("Número de tramos: " + camino.getLength());
    }

    /** Muestra 10 en vez de 10.0 cuando el número es entero. */
    private static String formato(double valor) {
        if (valor == Math.floor(valor)) {
            return String.valueOf((long) valor);
        }
        return String.valueOf(valor);
    }
}
