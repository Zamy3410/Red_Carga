# Equipo 14 – Red de Carga (Tractomulas) – Guía de exposición

## Cómo abrirlo en IntelliJ
1. `File > Open` y elegir el archivo **pom.xml** → "Open as Project".
2. Esperar a que IntelliJ descargue JGraphT (Maven, requiere internet). Usar JDK 11 o superior.
3. Abrir `src/main/java/redcarga/Main.java` y ejecutar con el botón verde ▶.
4. En el menú, la opción **4 (Demostración automática)** ejecuta todos los ejemplos de una vez.

## Cómo está dividido el código

| Integrante | Archivos | Tema que explica |
|---|---|---|
| **1** | `DatosRed.java`, `RedCarga.java` | Dataset y construcción del grafo |
| **2** | `RutaOptima.java` | Ruta óptima con Dijkstra |
| **3** | `ZonaInfluencia.java`, `Main.java` | Zonas de influencia con BFS y programa principal |

Flujo general: **Parte 1** crea el grafo → **Parte 2** y **Parte 3** lo usan → `Main` los une.

---

## INTEGRANTE 1 – Datos y construcción del grafo

**Qué decir:**
- "Modelamos la red de carga como un **grafo dirigido y ponderado**. Cada **ciudad es un vértice**, cada **ruta es una arista** y el **peso son los minutos de viaje**."
- "Es dirigido porque el dataset define rutas de un solo sentido (origen → destino). Por eso, por ejemplo, de Bogotá se llega a Medellín, pero no hay arista que regrese."
- "Usamos exactamente el JSON del profesor: **15 ciudades y 20 rutas**, sin inventar tiempos."

**`DatosRed.java`**
- `NODOS`: arreglo con las 15 ciudades.
- `RUTAS`: arreglo de objetos `Ruta(origen, destino, minutos)`; son las 20 aristas del JSON.
- La clase interna `Ruta` es solo un contenedor de tres datos.

**`RedCarga.java`** (los 3 pasos son los mismos de la clase "Mi Primer Grafo")
1. `new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class)` crea el grafo dirigido con pesos.
2. `grafo.addVertex(ciudad)` dentro de un `for` agrega las 15 ciudades.
3. `grafo.addEdge(origen, destino)` crea la arista y `grafo.setEdgeWeight(arista, minutos)` le pone su peso.
- `buscarCiudad(texto)` permite escribir "bogotá" o "CUCUTA" sin tildes ni mayúsculas (usa `Normalizer`).

**Preguntas probables:**
- *¿Por qué dirigido y no no-dirigido?* Porque así lo define el dataset: cada ruta tiene origen y destino.
- *¿Por qué ponderado?* Porque el tiempo de viaje no es igual entre todas las ciudades.
- *¿Qué es `DefaultWeightedEdge`?* Es el tipo de arista de JGraphT que guarda un peso.

---

## INTEGRANTE 2 – Ruta óptima (Dijkstra)

**Qué decir:**
- "Dijkstra **depende de los pesos**: suma los minutos de cada tramo y elige el camino de **menor tiempo total**, no el de menos paradas."
- "Ejemplo del dataset: de Bogotá a Cartagena hay una ruta directa de 27 min. El camino largo Bogotá → Medellín → Cali → Buenaventura → Cartagena suma 50 min, así que Dijkstra elige la directa."

**`RutaOptima.java`**
- El constructor recibe el grafo y crea **una sola vez** `new DijkstraShortestPath<>(grafo)`.
- `calcular(origen, destino)` llama `dijkstra.getPath(origen, destino)`. Devuelve un `GraphPath`, o **`null`** si no hay camino.
- `imprimir(...)`:
  - Si es `null`, avisa que no hay conexión (las rutas son de un solo sentido).
  - `camino.getVertexList()` da las ciudades en orden y se muestran con `String.join(" -> ", ...)`.
  - `camino.getEdgeList()` da los tramos; para cada uno se obtiene origen, destino y minutos con `getEdgeSource`, `getEdgeTarget` y `getEdgeWeight`.
  - `camino.getWeight()` es el **tiempo total** y `camino.getLength()` el número de tramos.

**Resultados esperados** (para comprobar al ejecutar):

| Origen → Destino | Ruta óptima | Tiempo |
|---|---|---|
| Bogota → Cartagena | Bogota → Cartagena | 27 min |
| Bogota → Cucuta | Bogota → Cartagena → Barranquilla → Santa Marta → Cucuta | 56 min |
| Cali → Tunja | Cali → Pasto → Popayan → Tunja | 66 min |
| Tunja → Bogota | No hay conexión | — |

**Preguntas probables:**
- *¿Por qué Tunja → Bogotá da "no hay conexión"?* Porque el grafo es dirigido y ninguna ruta llega a Bogotá.
- *¿Por qué no usar BFS para la ruta más rápida?* BFS minimiza saltos, no minutos.
- *¿Dijkstra funciona con pesos negativos?* No; aquí todos los tiempos son positivos.

---

## INTEGRANTE 3 – Zona de influencia (BFS) y programa principal

**Qué decir:**
- "BFS es **ciego a los pesos**: no mira minutos, explora **por niveles**. El nivel 1 son las ciudades a un solo tramo del origen, el nivel 2 a dos tramos, y así."
- "Sirve para definir **zonas de influencia**: qué ciudades puede alcanzar un centro de carga con 1, 2 o 3 transbordos."

**`ZonaInfluencia.java`**
- Usa `new BreadthFirstIterator<>(grafo, inicio)`: recorre el grafo desde la ciudad de inicio.
- `bfs.getDepth(ciudad)` dice a cuántos saltos está cada ciudad del origen.
- Las ciudades se agrupan en un `TreeMap<Integer, List<String>>` (nivel → ciudades), ordenado por nivel.
- `maxSaltos` limita la exploración: como BFS visita por niveles, cuando el nivel supera el límite se hace `break`.

**Resultado esperado desde Bogotá** (toda la red):

| Nivel | Ciudades |
|---|---|
| 0 | Bogota |
| 1 | Medellin, Cartagena |
| 2 | Cali, Tunja, Barranquilla |
| 3 | Buenaventura, Pasto, Santa Marta |
| 4 | Cucuta, Popayan, Neiva |
| 5 | Bucaramanga, Villavicencio |
| 6 | Ibague |

Se alcanzan las 14 ciudades restantes. El orden dentro de un mismo nivel puede variar un poco.
Con `maxSaltos = 1` solo salen Medellín y Cartagena.

**`Main.java`**
- Crea `RedCarga`, `RutaOptima` y `ZonaInfluencia`, y los conecta.
- Menú con `Scanner`: 1) Ruta óptima, 2) Zona de influencia, 3) Ver ciudades, 4) Demostración automática, 0) Salir.
- `pedirCiudad` valida que la ciudad exista; `pedirSaltos` deja vacío = toda la red.
- `demostracion` ejecuta los ejemplos fijos de las tablas de arriba, ideal para la sustentación.

**Preguntas probables:**
- *¿Cuál es la diferencia entre Dijkstra y BFS?* Dijkstra minimiza el peso total (minutos); BFS minimiza la cantidad de saltos e ignora los pesos.
- *¿Cuándo usar BFS?* Cuando importa el alcance en transbordos, no el tiempo.

---

## Para cerrar la sustentación (todos)
1. Ejecutar `Main`, opción 4, y mostrar la salida.
2. Hacer una consulta en vivo con la opción 1 (por ejemplo Cali → Tunja) y otra con la opción 2.
3. Remarcar el contraste: **Dijkstra = minutos (pesos)** vs **BFS = saltos (niveles)**.
