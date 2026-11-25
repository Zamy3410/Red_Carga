package redcarga;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.traverse.BreadthFirstIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class ZonaInfluencia {

    private final Graph<String, DefaultWeightedEdge> grafo;

    public ZonaInfluencia(Graph<String, DefaultWeightedEdge> grafo) {
        this.grafo = grafo;
    }

    public Map<Integer, List<String>> explorar(String inicio, int maxSaltos) {
        Map<Integer, List<String>> niveles = new TreeMap<>();

        // Iterador BFS de JGraphT, empezando en la ciudad de inicio
        BreadthFirstIterator<String, DefaultWeightedEdge> bfs =
                new BreadthFirstIterator<>(grafo, inicio);

        while (bfs.hasNext()) {
            String ciudad = bfs.next();
            int nivel = bfs.getDepth(ciudad); // saltos desde el inicio

            if (nivel > maxSaltos) {
                break; // BFS visita por niveles, así que ya no habrá más cercanos
            }
            niveles.computeIfAbsent(nivel, k -> new ArrayList<>()).add(ciudad);
        }
        return niveles;
    }

    /** Explora e imprime la zona de influencia por niveles. */
    public void imprimir(String inicio, int maxSaltos) {
        Map<Integer, List<String>> niveles = explorar(inicio, maxSaltos);

        System.out.println();
        String limite = (maxSaltos == Integer.MAX_VALUE) ? "toda la red" : "hasta " + maxSaltos + " salto(s)";
        System.out.println("=== ZONA DE INFLUENCIA (BFS) desde " + inicio + " - " + limite + " ===");

        int total = 0;
        for (Map.Entry<Integer, List<String>> entrada : niveles.entrySet()) {
            int nivel = entrada.getKey();
            List<String> ciudades = entrada.getValue();
            if (nivel == 0) {
                System.out.println("Nivel 0 (origen): " + ciudades);
            } else {
                System.out.println("Nivel " + nivel + " (" + nivel + " salto"
                        + (nivel > 1 ? "s" : "") + "): " + ciudades);
                total += ciudades.size();
            }
        }
        System.out.println("Ciudades alcanzables desde " + inicio + ": " + total
                + " de " + (grafo.vertexSet().size() - 1));
    }
}
