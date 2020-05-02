/**
 * Created by Sergey Knyazev on 5/2/2020.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class MatulaBeck {
    public static ArrayList<Integer> get_degeneracy_order(ArrayList<HashSet<Integer>> graph) {
        ArrayList<Integer> L = new ArrayList<>(graph.size());
        ArrayList<Integer> d = new ArrayList<>(graph.size());
        for(HashSet<Integer> es: graph) d.add(es.size());
        int D_len = Collections.max(d)+1;
        ArrayList<HashSet<Integer>> D = new ArrayList<>(D_len);
        for(int i=0; i<D_len; ++i) D.add(new HashSet<>());
        for(int i=0; i<d.size(); ++i) D.get(d.get(i)).add(i);
//        int k=0;
        for(int r=0; r<graph.size(); ++r) {
            int i=0;
            while(true) {
                if(D.get(i).isEmpty()) i++;
                else break;
            }
//            k=Math.max(k,i);
            Integer v = D.get(i).iterator().next();
            D.get(i).remove(v);
            L.add(v);
            for(Integer w: graph.get(v)) {
                int dw = d.get(w);
                if(D.get(dw).contains(w)) {
                    d.set(w, dw-1);
                    D.get(dw).remove(w);
                    D.get(dw-1).add(w);
                }
            }
        }
        return L;
    }
}
