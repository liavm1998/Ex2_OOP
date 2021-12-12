package classes;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.GeoLocation;
import api.NodeData;
import classes.MyGraph;

import java.util.Iterator;
import java.util.Random;

public class GraphGenerator
{
    int num;
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
            while(temp.getOutlist().size()+temp.getInlist().size()!=20)
            {
                int dest = rand.ints(0, num).findFirst().getAsInt();
                Graph.connect(temp.getKey(),dest,weight);
            }
        }
        return Graph;
    }
}
