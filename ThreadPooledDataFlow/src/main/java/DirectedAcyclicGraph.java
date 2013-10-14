/**
 *
 * User: alexthornburg
 * Date: 8/30/13
 * Time: 10:32 AM
 *
 */
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Stack;

public class DirectedAcyclicGraph<N> {

    Map<N,List<N>> neighbors = new HashMap<N,List<N>>();


    public void add (N node) {
        if (neighbors.containsKey(node)) return;
        neighbors.put(node, new ArrayList<N>());
    }


    public boolean contains (N node) {
        return neighbors.containsKey(node);
    }

    public void add(N from, N to) {
        this.add(from); this.add(to);
        neighbors.get(from).add(to);
    }

    public void remove(N from, N to) {
        if (!(this.contains(from) && this.contains(to)))
            throw new IllegalArgumentException("Nonexistent node");
        neighbors.get(from).remove(to);
    }

    public boolean hasCycle () {
        return topologicalSort() != null;
    }

    public int size(){
        return neighbors.size();
    }

    public List<N> topologicalSort () {
        Map<N, Integer> degree = degree();
        Stack<N> verts = new Stack<N>();
        for (N v: degree.keySet()) {
            if (degree.get(v) == 0) verts.push(v);
        }
        List<N> result = new ArrayList<N>();
        while (!verts.isEmpty()) {
            N n = verts.pop();
            result.add(n);
            for (N neighbor: neighbors.get(n)) {
                degree.put(neighbor, degree.get(neighbor) - 1);
                if (degree.get(neighbor) == 0) verts.push(neighbor);
            }
        }
        if (result.size() != neighbors.size()) return null;
        return result;
    }

    public Map<N,Integer> degree () {
        Map<N,Integer> result = new HashMap<N,Integer>();
        for (N n: neighbors.keySet()){
            result.put(n, 0);
        }
            for (N from: neighbors.keySet()) {
                for (N to: neighbors.get(from)) {
                    result.put(to, result.get(to) + 1);
                }
            }
        return result;
    }



}
