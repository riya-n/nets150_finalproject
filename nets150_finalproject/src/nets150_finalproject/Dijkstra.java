package nets150_finalproject;

import java.util.*;

public class Dijkstra {
    
    private List<Node> nodes;
    
    public Dijkstra() {
        nodes = new ArrayList<Node>();
    }
    
    class Node {
        Integer p; // index of parent in graph list
        double d;
        
        Node() {
            this.d = Double.MAX_VALUE;
            this.p = null;
        }

        void setParent(Integer p) {
            this.p = p;
        }
        
        void setDistance(double d) {
            this.d = d;
        }
    }
    
    class NodeComparator implements Comparator<Node>{ 
        
        @Override
        public int compare(Node o1, Node o2) {
            if (o1.d < o2.d) {
                return -1;
            } else if (o1.d > o2.d) {
                return 1;
            }
            return 0;
        } 
    }
    
    // src is always index 0 and tgt is always last index (nodes.size - 1)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ArrayList<Integer> getShortestRoute(Graph g) {
        initializeSS(g);
        Set<Node> s = new HashSet<Node>();
        PriorityQueue pq = new PriorityQueue(nodes.size(), new NodeComparator());
        for (int i = 0; i < g.getSize(); i++) {
            pq.add(nodes.get(i));
        }
        while (!pq.isEmpty()) {
            Node u = (Node) pq.poll();
            s.add(u);
            int i = nodes.indexOf(u);
            for (Integer j : g.getOutNeighbors(i)) {
                Node v = nodes.get(j);
                relax(u, v, g.getEdge(i, j).getPrice());
            }
        }
        
        ArrayList<Integer> path = new ArrayList<Integer>();
        Integer curr = nodes.size() - 1; // tgt
        path.add(curr);
        while (curr != 0) { // while not src
            Node u = nodes.get(curr);
            path.add(u.p);
            curr = u.p;
        }
        Collections.reverse(path);

        return path;
    }
    
    // src is always index 0
    private void initializeSS(Graph g) {
        for (int i = 0; i < g.getSize(); i++) {
            nodes.add(new Node());
        }
        nodes.get(0).setDistance(0);
    }
    
    private void relax(Node u, Node v, double w) {
        if (v.d > u.d + w) {
            v.setDistance(u.d + w);
            v.setParent(nodes.indexOf(u));
        }
    }
    
    
}
