package classes;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
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
        int i=this.g.getV().keySet().iterator().next();
        MyNode v=this.g.getV().get(i);
        MyGraph r=this.g.reverse();
        return r.dfs(v)&&this.g.dfs(v);
    }

    public void dijkstra(MyNode src)
    {
        for (MyNode v: this.g.getV().values())
        {
            v.setDistance(Double.POSITIVE_INFINITY);
            v.setShortestPath(new ArrayList<>());
        }
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

    @Override
    public List<NodeData> shortestPath(int src, int dest)
    {
        dijkstra(this.g.getV().get(src));
        if(this.g.getV().get(dest).getDistance()==Double.POSITIVE_INFINITY)
        {
            return null;
        }
        ArrayList<MyNode> c =this.g.getV().get(dest).getShortestPath();
        c.add(this.g.getV().get(dest));
        return new ArrayList<>(c);
    }

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
        List<NodeData> p =new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        for(int i =0;i<cities.size();i++)
        {
            temp.add(cities.get(i).getKey());
        }

        List<NodeData> shortestpath =new ArrayList<>();
        NodeData cur = cities.get(0);
        p.add(this.g.getNode(temp.get(0)));
        temp.remove(0);
        while (!temp.isEmpty())
        {
            double shortestDist = Integer.MAX_VALUE;
            int idShort = -1;
            int location = -1;
            for(int i = 0 ; i < temp.size();i++)
            {
                int key =temp.get(i);
                Double save=shortestPathDist(cur.getKey(),key);
                if(save < shortestDist && save != 0)
                {
                    shortestDist = save;
                    idShort = key;
                    location = i;
                }
            }
            shortestpath = shortestPath(cur.getKey(),idShort);
            shortestpath.remove(0);
            while (!shortestpath.isEmpty())
            {
                p.add(shortestpath.get(0));
                shortestpath.remove(0);
            }
            int Node_to_get = temp.get(location);
            cur = this.g.getNode(Node_to_get);
            temp.remove(temp.get(location));
        }
        return p;
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
            nodes.add(vertex);
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



//    public void DFS (DirectedWeightedGraph G,NodeData N)
//    {
//        N.setTag(1);
//        Iterator<EdgeData> I = G.edgeIter(N.getKey());
//        while (I.hasNext())
//        {
//            EdgeData cur_edge = I.next();
//            NodeData next_node = G.getNode(cur_edge.getDest());
//            if (next_node.getTag() == 0)
//            {
//                DFS(G, next_node);
//            }
//        }
//    }

}
