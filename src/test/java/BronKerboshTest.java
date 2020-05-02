/**
 * Created by Sergey Knyazev on 5/2/2020.
 */

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class BronKerboshTest {
    @Test
    void bronkerbosh_test1() {
        ArrayList<HashSet<Integer>> a = new ArrayList<>();
        for(int i=0; i<5; ++i) a.add(new HashSet<>());                          //0------1-----3
        a.get(0).add(1); a.get(0).add(2);                                       //  \    | \   |
        a.get(1).add(0); a.get(1).add(2); a.get(1).add(3); a.get(1).add(4);     //    \  |   \ |
        a.get(2).add(0); a.get(2).add(1); a.get(2).add(4);                      //       2-----4
        a.get(3).add(1); a.get(3).add(4);
        a.get(4).add(1); a.get(4).add(2); a.get(4).add(3);
        LinkedList<HashSet<Integer>> c = new LinkedList<>();
        assertEquals(c, BronKerbosh.find_maximal_cliques(a));
    }
}