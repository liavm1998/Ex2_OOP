import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.NodeData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MyAlgo implements DirectedWeightedGraphAlgorithms {

    private MyGraph g;
    @Override
    public void init(DirectedWeightedGraph g)
    {
        this.g= (MyGraph) g;
    }

    @Override
    public DirectedWeightedGraph getGraph()
    {
        return this.g;
    }

    @Override
    public DirectedWeightedGraph copy()
    {
        return new MyGraph(this.g);
    }

    @Override
    public boolean isConnected()
    {
        MyNode v=this.g.getV().get(0);
        this.g.dfs(v);
        for (MyNode w:this.g.getV().values())
        {
            if(!w.visited)
            {
                return false;
            }
            w.visited=false;
        }
        MyGraph r=this.g.reverse();
        v=r.getV().get(0);
        r.dfs(v);
        for (MyNode w:r.getV().values())
        {
            if(!w.visited)
            {
                return false;
            }
            w.visited=false;
        }
        return true;
    }




    public void dijkstra(MyNode src)
    {
        src.setWeight(0);
        Set<MyNode> settledNodes = new HashSet<>();
        Set<MyNode> unsettledNodes = new HashSet<>();
        unsettledNodes.add(src);
        while (unsettledNodes.size() != 0)
        {
            MyNode currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            if(currentNode==null)
            {
                continue;
            }
            for (MyEdge e: currentNode.getOutlist())
            {
                MyNode adjacentNode = this.g.getV().get(e.getDest());
                double edgeWeight = e.getWeight();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        double max=0;
        for (MyNode v: this.g.getV().values())
        {
            if(v.getDistance()>max)
            {
                max=v.getDistance();
            }
        }
        src.setMaxDist(max);
    }

    private void calculateMinimumDistance(MyNode evaluationNode, double edgeWeigh, MyNode sourceNode)
    {
        double sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance())
        {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            ArrayList<MyNode> shortestPath = new ArrayList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    private MyNode getLowestDistanceNode(Set<MyNode> unsettledNodes) {
        MyNode lowestDistanceNode = null;
        double lowestDistance = Integer.MAX_VALUE;
        for (MyNode node: unsettledNodes)
        {
            double nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    @Override
    public double shortestPathDist(int src, int dest)
    {
        dijkstra(this.g.getV().get(src));
        return this.g.getV().get(dest).getDistance();
    }
//    @Override
//    public double shortestPathDist(int src, int dest)
//    {
//        Dijkstra p=new Dijkstra(this.g, (MyNode) g.getNode(src));
//        Map<Object,Double> pathes=p.getResultSize();
//        return pathes.get((MyNode) g.getNode(src));
//    }


    @Override
    public List<NodeData> shortestPath(int src, int dest)
    {
        dijkstra(this.g.getV().get(src));
        ArrayList<MyNode> c =this.g.getV().get(dest).getShortestPath();
        c.add(this.g.getV().get(dest));
        return new ArrayList<>(c);
    }

//    @Override
//    public List<NodeData> shortestPath(int src, int dest)
//    {
//        Dijkstra p=new Dijkstra(this.g, (MyNode) g.getNode(src));
//        Map<Object,Double> pathes=p.getResultSize();
////        return pathes.get((MyNode) g.getNode(src));
//        return null;
//    }
    @Override
    public NodeData center()
    {
        HashMap<Double,MyNode>maxDistMap=new HashMap<>();

        for (MyNode v:this.g.getV().values())
        {
            double maxDisd=Double.NEGATIVE_INFINITY;
            Map<MyNode, Double> i = (new Dijkstra(this.g, v)).MyResult;
            for (MyNode o:i.keySet())
            {
                if(i.get(o)>maxDisd)
                {
                    maxDisd=i.get(o);
                }
            }
            maxDistMap.put(maxDisd,v);
        }
        MyNode min= (MyNode) g.getNode(0);
        double minW=Double.POSITIVE_INFINITY;
        for (double dis:maxDistMap.keySet())
        {
            if(dis<minW)
            {
                min=maxDistMap.get(dis);
                minW=dis;
            }
        }
        return min;
    }



    @Override
    public List<NodeData> tsp(List<NodeData> cities)
    {
        ArrayList<MyNode[]> p =new ArrayList<>();
        List<Object[]> temp=permutations(cities.toArray());
        for (Object[] arr:temp)
        {
            p.add(f(arr));
        }
        double cost=Double.MAX_VALUE;
        NodeData[] ans=new NodeData[0];
        for (MyNode[] perm:p) {
            double check=StraightLineDist(perm);
            if(check<cost)
            {
                ans=perm;
                cost=check;
            }
        }

        return List.of(ans);
    }

    private double StraightLineDist(MyNode[] perm)
    {
        double dist=0;
        for (int i = 0; i < perm.length-1; i++)
        {
            double add=perm[i].getOutEdge(perm[i+1].getKey()).getWeight();
            if(add==Double.MAX_VALUE)
            {
                return add;
            }
            else dist+=add;
        }
        return dist;
    }

    private MyNode[] f(Object[] c)
    {
        ArrayList<MyNode> ans= new ArrayList<>();
        for (Object o:c)
        {
               ans.add((MyNode) o);
        }
        return (MyNode[]) ans.toArray();
    }

    @Override
    public boolean save(String file)
    {
        JSONObject ans=new JSONObject();
        JSONArray edges=new JSONArray();
        JSONArray nodes=new JSONArray();
        for (MyEdge e: this.g.getE().values())
        {
            JSONObject edge=new JSONObject();
            edge.put("src",e.getSrc());
            edge.put("w",e.getWeight());
            edge.put("dest",e.getDest());
            edges.add(edge);
        }
        for (MyNode v:this.g.getV().values())
        {
            // {
            //      "pos": "35.19381366747377,32.102419275630254,0.0",
            //      "id": 16
            //    }
            JSONObject vertex=new JSONObject();
            vertex.put("pos",v.getLocation().toString());
            vertex.put("id",v.getKey());
        }
        ans.put("Edges",edges);
        ans.put("Nodes",nodes);
        try (FileWriter writer = new FileWriter(file)) {
            //We can write any JSONArray or JSONObject instance to the file
            writer.write(ans.toJSONString());
            writer.flush();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean load(String file)
    {
        MyGraph ans = new MyGraph();
        try
        {


        // parsing file json_file
        Object obj = new JSONParser().parse(new FileReader(file));

        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;
        //////reading the nodes
        JSONArray nodes = (JSONArray) jo.get("Nodes");
        Iterator i= nodes.iterator();
        while (i.hasNext())
        {
            HashMap<String,Object> map= (HashMap<String, Object>) i.next();
            String pos= (String) map.get("pos");
            int id= (int) ((long)map.get("id"));
            String[]position=pos.split(",");
            location l=new location(Double.parseDouble(position[0])
                    ,Double.parseDouble(position[1])
                    ,Double.parseDouble(position[2]));
            MyNode node=new MyNode(id,l);
            ans.addNode(node);
        }
        ///////reading the edges
        JSONArray edges= (JSONArray) jo.get("Edges");
        i= edges.iterator();
        while (i.hasNext())
        {
            HashMap<String,Object> map= (HashMap<String, Object>) i.next();
            int src= (int)(long) map.get("src");
            int dest=(int)(long) map.get("dest");
            double w=(double) map.get("w");
            ans.connect(src,dest,w);
        }
        this.init(ans);
        return true;
        }
        catch (IOException e1)
        {
            return false;
        }
        catch(ParseException e2)
        {
            return false;
        }
    }


    private static <T> List<T[]> permutations(T[] original)
    {
        List<T[]> permutations = new ArrayList<>();
        allPermutationsHelper(original, permutations, original.length);
        return permutations;
    }
    private static <T> void allPermutationsHelper(T[] permutation, List<T[]> permutations, int n) {
        // Base case - we found a new permutation, add it and return
        if (n <= 0) {
            permutations.add(permutation);
            return;
        }
        // Recursive case - find more permutations by doing swaps
        T[] tempPermutation = Arrays.copyOf(permutation, permutation.length);
        for (int i = 0; i < n; i++) {
            swap(tempPermutation, i, n - 1); // move element at i to the end
            // move everything else around, holding the end constant
            allPermutationsHelper(tempPermutation, permutations, n - 1);
            swap(tempPermutation, i, n - 1); // backtrack
        }
    }
    public static <T> void swap(T[] array, int first, int second) {
        T temp = array[first];
        array[first] = array[second];
        array[second] = temp;
    }

    public static void main(String[] args)
    {
//        MyGraph gr=new MyGraph();
//        MyNode a0 = new MyNode(0, new location(0,0,0));
//        MyNode a1 = new MyNode(1, new location(1,6,0));
//        MyNode a2 = new MyNode(2, new location(4,5,0));
//        MyNode a3 = new MyNode(3, new location(-1,4,0));
//        MyNode a4 = new MyNode(4, new location(3,0,0));
//        MyNode a5 = new MyNode(5, new location(2,-2,0));
//        MyNode a6 = new MyNode(6, new location(-1,-8,0));
//        MyNode a7 = new MyNode(7, new location(-6,-6,0));
//        MyNode a8 = new MyNode(8, new location(-7,-1,0));
//        MyNode a9 = new MyNode(9, new location(-4,-1,0));
//        gr.addNode(a0);
//        gr.addNode(a1);
//        gr.addNode(a2);
//        gr.addNode(a3);
//        gr.addNode(a4);
//        gr.addNode(a5);
//        gr.addNode(a6);
//        gr.addNode(a7);
//        gr.addNode(a8);
//        gr.addNode(a9);
//        gr.connect(0,1,1.5);
//        gr.connect(0,2,20);
//        gr.connect(0,3,8.2);
//        gr.connect(0,4,5.2);
//        gr.connect(0,5,4.9);
//        gr.connect(0,6,2.3);
//        gr.connect(0,7,1.3);
//        gr.connect(0,8,0.4);
//        gr.connect(0,9,1.2);
//        gr.connect(9,1,4.2);
//        gr.connect(1,2,6.3);
//        gr.connect(2,3,1.2);
//        gr.connect(3,4,0.3);
//        gr.connect(4,5,5.3);
//        gr.connect(5,6,6.1);
//        gr.connect(6,7,4.3);
//        gr.connect(7,8,3.9);
//        gr.connect(8,9,9.2);
//        gr.connect(5, 0, 10);
//
//
//        MyAlgo ma=new MyAlgo();
//        ma.init(gr);
//        ma.save("C:\\Users\\Liavm\\Desktop\\Ex2_v2\\data\\yeudit.json");
        MyGraph g=new MyGraph();
        MyNode a,b,c,d;
        location a1,b1,c1,d1;
        a1=new location(0,0,0);
        b1=new location(1,0,0);
        c1=new location(0,1,0);
        d1=new location(0,0,1);
        a=new MyNode(0,a1);
        b=new MyNode(1,b1);
        c=new MyNode(2,c1);
        d=new MyNode(3,d1);
        g.addNode(a);
        g.addNode(b);
        g.addNode(c);
        g.addNode(d);

        g.connect(0,1,1);
        g.connect(0,2,1);
        g.connect(0,3,3);
        g.connect(1,0,1);
        g.connect(1,2,Math.sqrt(2));
        g.connect(1,3,Math.sqrt(2));
        g.connect(2,0,1);

        MyAlgo mg=new MyAlgo();
        mg.init(g);
        ArrayList<NodeData> ans = (ArrayList<NodeData>) mg.shortestPath(0,3);


        double ans2=mg.shortestPathDist(0,3);
//        System.out.println("the path was:");
//        for (NodeData v:ans)
//        {
//            System.out.print(v.getKey()+" ");
//        }
        System.out.println("\n the dist was:"+ans2);
    }

}
