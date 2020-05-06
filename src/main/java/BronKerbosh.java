/**
 * Created by Sergey Knyazev on 5/2/2020.
 */
import com.google.common.collect.Sets;
import java.util.*;

public class BronKerbosh {
    public static LinkedList<HashSet<Integer>> find_maximal_cliques(ArrayList<HashSet<Integer>> graph) {
        LinkedList<HashSet<Integer>> cliques = new LinkedList<>();
        Set<Integer> P = new HashSet<>();
        Set<Integer> X = new HashSet<>();
        for(int i=0; i<graph.size();++i) {
            P.add(i);
        }
        for(Integer v : MatulaBeck.get_degeneracy_order(graph)) {
            bronkerbosh(cliques, graph, new HashSet<>(Collections.singletonList(v)),
                        Sets.intersection(P, graph.get(v)), Sets.intersection(X, graph.get(v)));
            P.remove(v);
            X.add(v);
        }
        return cliques;
    }
    private static void bronkerbosh(LinkedList<HashSet<Integer>> cliques, ArrayList<HashSet<Integer>> graph,
                                    Set<Integer> R, Set<Integer> P, Set<Integer> X) {
        if(P.isEmpty() && X.isEmpty()) {
            cliques.add(new HashSet<>(R));
        }
        if (P.isEmpty()) {
            return;
        }
        Integer pivot = P.iterator().next();
        for(Integer v: Sets.difference(P, graph.get(pivot))) {
            bronkerbosh(cliques, graph, Sets.union(R, new HashSet<>(Collections.singletonList(v))),
                    Sets.intersection(P, graph.get(v)), Sets.intersection(X, graph.get(v)));
            P=Sets.difference(P,new HashSet<>(Collections.singletonList(v)));
            X=Sets.union(X,new HashSet<>(Collections.singletonList(v)));
        }
    }
}
