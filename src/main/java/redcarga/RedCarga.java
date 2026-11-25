package redcarga;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.text.Normalizer;
import java.util.Set;


public class RedCarga {

    private final Graph<String, DefaultWeightedEdge> grafo;

    public RedCarga() {
        // 1. Instanciar el grafo (Dirigido y Ponderado)
        grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        // 2. Cargar todas las ciudades como vértices
        for (String ciudad : DatosRed.NODOS) {
            grafo.addVertex(ciudad);
        }

        // 3. Cargar todas las rutas como aristas con su peso (minutos)
        for (DatosRed.Ruta ruta : DatosRed.RUTAS) {
            DefaultWeightedEdge arista = grafo.addEdge(ruta.origen, ruta.destino);
            grafo.setEdgeWeight(arista, ruta.minutos);
        }
    }

    /** Devuelve el grafo para que los algoritmos trabajen sobre él. */
    public Graph<String, DefaultWeightedEdge> getGrafo() {
        return grafo;
    }

    /** Devuelve el conjunto de ciudades (vértices). */
    public Set<String> getCiudades() {
        return grafo.vertexSet();
    }


    public String buscarCiudad(String texto) {
        if (texto == null) {
            return null;
        }
        String buscado = normalizar(texto);
        for (String ciudad : grafo.vertexSet()) {
            if (normalizar(ciudad).equals(buscado)) {
                return ciudad;
            }
        }
        return null;
    }

    /** Quita tildes, espacios sobrantes y pasa a minúsculas. */
    private static String normalizar(String s) {
        String sinTildes = Normalizer.normalize(s.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return sinTildes.toLowerCase();
    }
}
