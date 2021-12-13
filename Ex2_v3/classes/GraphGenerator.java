package classes;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.GeoLocation;
import api.NodeData;
import classes.MyGraph;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

public class GraphGenerator
{
    public static void main(String[] args) {
        GraphGenerator G =new GraphGenerator(1000000,"src//data//My1000000Nodes.json");
    }
    int num;
    //this one worckes
    public GraphGenerator(double NumOfNodes,String file)
    {
        JSONObject ans = new JSONObject();
        JSONArray edges = new JSONArray();
        JSONArray nodes = new JSONArray();
        this.num = (int) NumOfNodes;

        Random rand1 =new Random();
        rand1.setSeed(1);
        Random rand2 =new Random();
        rand2.setSeed(2);
        Random rand3 =new Random();
        rand3.setSeed(3);
        for (int i = 0; i < num; i++)
        {
            int x = rand1.nextInt(1000000);
            int y = rand2.nextInt(1000000);
            JSONObject vertex=new JSONObject();
            vertex.put("pos","" + x + "," + y + "," + 0.0 + "");
            vertex.put("id",i);
            nodes.add(vertex);
        }
        for(int i = 0; i < num; i++)
        {
            for (int j=0;j<22;j++)
            {
                int dest = rand1.nextInt(num);
                double weight = rand2.ints(0, 30).findFirst().getAsInt();
                JSONObject edge = new JSONObject();
                edge.put("src", i);
                edge.put("w", weight);
                edge.put("dest", dest);
                edges.add(edge);
            }
        }
        ans.put("Edges",edges);
        ans.put("Nodes",nodes);
        try (FileWriter writer = new FileWriter(file)) {
            //We can write any JSONArray or JSONObject instance to the file
            writer.write(ans.toJSONString());
            writer.flush();
        }
        catch (IOException e) {
        }
    }
    public GraphGenerator(int NumOfNodes)
    {
        this.num=NumOfNodes;
    }
    public DirectedWeightedGraph Generate()
    {

        MyGraph Graph =new MyGraph();
        Random rand =new Random();
        //Node generator
        for (int i =0;i < num;i++)
        {
            int x = rand.ints(0, 10000).findFirst().getAsInt();
            int y = rand.ints(0, 10000).findFirst().getAsInt();
            location loc = new location(x,y, 0);
            NodeData temp = new MyNode(i,loc);
            Graph.addNode(temp);
        }
        Iterator<NodeData> Nodes = Graph.nodeIter();
        while (Nodes.hasNext())
        {
            int weight = rand.ints(0, 30).findFirst().getAsInt();
            MyNode temp = (MyNode) Nodes.next();
            while(temp.getOutlist().size()+temp.getInlist().size() != 20)
            {
                int dest = rand.ints(0, num).findFirst().getAsInt();
                Graph.connect(temp.getKey(),dest,weight);
            }
        }
        return Graph;
    }
}
