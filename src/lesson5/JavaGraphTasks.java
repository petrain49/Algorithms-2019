package lesson5;

import kotlin.NotImplementedError;
import lesson5.impl.GraphBuilder;

import java.util.*;

@SuppressWarnings("unused")
public class JavaGraphTasks {
    /**
     * Эйлеров цикл.
     * Средняя
     *
     * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
     * Если в графе нет Эйлеровых циклов, вернуть пустой список.
     * Соседние дуги в списке-результате должны быть инцидентны друг другу,
     * а первая дуга в списке инцидентна последней.
     * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
     * Веса дуг никак не учитываются.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
     *
     * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
     * связного графа ровно по одному разу
     */
    private static boolean connectedEdges(Graph.Edge f, Graph.Edge s) {
        return f.getBegin().equals(s.getBegin())
                || f.getBegin().equals(s.getEnd())
                || f.getEnd().equals(s.getEnd())
                || f.getEnd().equals(s.getBegin());
    }

    public static List<Graph.Edge> findEulerLoop(Graph graph) {
        Set<Graph.Vertex> vertices = graph.getVertices();
        Set<Graph.Edge> edges = graph.getEdges();

        if (vertices.isEmpty() || edges.isEmpty() || edges.size() < 3)
            return Collections.emptyList();

        if (!coherence(graph)) return Collections.emptyList();

        List<Graph.Edge> result = new ArrayList<>();

        Deque<Graph.Vertex> vst = new ArrayDeque<>();
        vst.add(vertices.iterator().next());
        List<Graph.Vertex> vList = new ArrayList<>();

        Graph.Vertex cur;
        Graph.Edge edge;
        while (!vst.isEmpty()) {
            cur = vst.peekLast();
            for (Graph.Vertex v: vertices) {
                edge = graph.getConnection(cur, v);

                if (edges.contains(edge)) {
                    vst.add(v);
                    edges.remove(edge);
                    break;
                }
            }

            if (cur == vst.peekLast()) {
                vst.removeLast();
                vList.add(cur);
            }
        }

        for (int x = 1; x < vList.size(); x++)
            result.add(graph.getConnection(vList.get(x - 1), vList.get(x)));
        return result;
    }

    /**
     * Минимальное остовное дерево.
     * Средняя
     *
     * Дан граф (получатель). Найти по нему минимальное остовное дерево.
     * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
     * вернуть любое из них. Веса дуг не учитывать.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ:
     *
     *      G    H
     *      |    |
     * A -- B -- C -- D
     * |    |    |
     * E    F    I
     * |
     * J ------------ K
     */
    private static boolean coherence(Graph graph) {
        Set<Graph.Vertex> vertices = graph.getVertices();
        for (Graph.Vertex x: vertices) {
            if (graph.getConnections(x).size() % 2 != 0)
                return false;
        }
        return true;
    }

    public static Graph minimumSpanningTree(Graph graph) {
        Set<Graph.Vertex> vertices = graph.getVertices();
        GraphBuilder newGraph = new GraphBuilder();

        for (Graph.Vertex v: graph.getVertices()) {
            
            newGraph.addVertex(v.getName());
            vertices.remove(v);
            for (Graph.Vertex nb: graph.getNeighbors(v)) {
                if (vertices.contains(nb)) {
                    if ((graph.getConnections(v).size() == 1) && !v.equals(nb)) {
                        newGraph.addVertex(nb.getName());
                        newGraph.addConnection(v, nb, 1);
                        vertices.remove(nb);
                    }
                } else if (newGraph.build().getConnections(v).size() == 0) {
                    newGraph.addConnection(v, nb, 1);
                }
            }
        }
        System.out.println(graph.getEdges());
        System.out.println(newGraph.build().getEdges());
        return newGraph.build();
    }

    /**
     * Максимальное независимое множество вершин в графе без циклов.
     * Сложная
     *
     * Дан граф без циклов (получатель), например
     *
     *      G -- H -- J
     *      |
     * A -- B -- D
     * |         |
     * C -- F    I
     * |
     * E
     *
     * Найти в нём самое большое независимое множество вершин и вернуть его.
     * Никакая пара вершин в независимом множестве не должна быть связана ребром.
     *
     * Если самых больших множеств несколько, приоритет имеет то из них,
     * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
     *
     * В данном случае ответ (A, E, F, D, G, J)
     *
     * Если на входе граф с циклами, бросить IllegalArgumentException
     *
     * Эта задача может быть зачтена за пятый и шестой урок одновременно
     * O(2*(N + M))
     */
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {
        Set<Graph.Vertex> vertices = graph.getVertices();
        List<Graph.Edge> edges = new ArrayList<>(graph.getEdges());

        Set<Graph.Vertex> fRes = new HashSet<>();
        Set<Graph.Vertex> sRes = new HashSet<>();

        int a = minimumSpanningTree(graph).getEdges().size();
        int b = graph.getEdges().size();
        if (a < b) throw new IllegalArgumentException();

        if (vertices.size() == 0) return Collections.emptySet();
        else if (vertices.size() < 3) {
            fRes.add(edges.get(0).getBegin());
            return fRes;
        }

        Map<Graph.Vertex, Integer> map = new HashMap<>();
        Set<Graph.Vertex> nbs;
        int num;
        for (Graph.Vertex x: graph.getVertices()) {
            num = 0;
            if (!map.containsKey(x)) {
                map.put(x, 0);
                fRes.add(x);
            }
            if (map.get(x) == 0) num = 1;

            nbs = graph.getNeighbors(x);
            for (Graph.Vertex nb: nbs) {
                if (!map.containsKey(nb)) {
                    map.put(nb, num);

                    if (num == 0) fRes.add(nb);
                    else sRes.add(nb);
                }
            }
        }
        if (fRes.size() >= sRes.size()) return fRes;
        return sRes;
    }

    /**
     * Наидлиннейший простой путь.
     * Сложная
     *
     * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
     * Простым считается путь, вершины в котором не повторяются.
     * Если таких путей несколько, вернуть любой из них.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ: A, E, J, K, D, C, H, G, B, F, I
     */
    public static Path longestSimplePath(Graph graph) {
        throw new NotImplementedError();
    }
}
