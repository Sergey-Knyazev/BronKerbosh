/**
 * Created by Sergey Knyazev on 5/1/2020.
 */
import picocli.CommandLine;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvParser;
import io.vavr.Tuple2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

@CommandLine.Command(name = "nn", mixinStandardHelpOptions = true, version = "0.0")
public class Main implements Runnable {
    @CommandLine.Option(names={"-i", "--inFile"}, description="input file with edges in CSV format",
            paramLabel = "FILE", required=true)
    private File inputFile;
    @CommandLine.Option(names={"-o", "--outFile"},
            description="output file with maximal cliques",
            paramLabel = "FILE", required=true)
    private File outputFile;

    public void run() {
        try {
            List<String[]> edges = load_edges(inputFile);
            Tuple2<HashMap<String, Integer>, HashMap<Integer, String>> nodes = get_node_indices(edges);
            HashMap<String, Integer> node_indices = nodes._1;
            HashMap<Integer, String> node_names = nodes._2;
            ArrayList<HashSet<Integer>> graph = get_adjacency_list(edges, node_indices);
            LinkedList<HashSet<Integer>> cliques = BronKerbosh.find_maximal_cliques(graph);
            export_graph(cliques, node_names, outputFile);
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<HashSet<Integer>> get_adjacency_list(List<String[]> edges,
                                                                  HashMap<String, Integer> node_indices) {
        ArrayList<HashSet<Integer>> adjacency_list = new ArrayList<>();
        for(int i=0; i<node_indices.size(); ++i) {
            adjacency_list.add(new HashSet<>());
        }
        for(String[] a: edges) {
            int u = node_indices.get(a[0]);
            int v = node_indices.get(a[1]);
            adjacency_list.get(u).add(v);
            adjacency_list.get(v).add(u);
        }
        return adjacency_list;
    }

    public static void main(String[] args) {
        CommandLine.run(new Main(), System.out, args);
    }

    private static List<String[]> load_edges(File file_name) throws FileNotFoundException{
        CsvParserSettings settings= new CsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        settings.setHeaderExtractionEnabled(true);
        CsvParser parser = new CsvParser(settings);
        return parser.parseAll(new FileInputStream(file_name));
    }

    private static Tuple2<HashMap<String, Integer>, HashMap<Integer,String>> get_node_indices(List<String[]> edges) {
        HashMap<String, Integer> node_indices = new HashMap<>();
        HashMap<Integer, String> node_names = new HashMap<>();
        for(String[] a: edges) {
            for (int i: new int[]{0, 1}) {
                if(node_indices.containsKey(a[i])) continue;
                int idx = node_indices.size();
                node_indices.put(a[i], idx);
                node_names.put(idx, a[i]);
            }
        }
        return new Tuple2<>(node_indices, node_names);
    }

    private static void export_graph(LinkedList<HashSet<Integer>> cliques,
                                     HashMap<Integer, String> node_names,
                                     File file_name) throws FileNotFoundException {
        PrintWriter f = new PrintWriter(file_name);
        f.println("Clique,Node");
        for(int i=0; i<cliques.size(); ++i)
            for (int j : cliques.get(i))
                if (i < j) f.println(String.format("%s,%s", i, node_names.get(j)));
        f.close();
    }
}
