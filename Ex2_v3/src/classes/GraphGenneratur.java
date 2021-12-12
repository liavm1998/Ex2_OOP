package classes;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.GeoLocation;
import api.NodeData;

import java.util.Iterator;
import java.util.Random;

public class GraphGenneratur {
    private DirectedWeightedGraph Graph;
    int num;

    public GraphGenneratur(int NumOfNodes)
    {
        this.Graph=new MyGraph();
        this.num=NumOfNodes;
    }
    public DirectedWeightedGraph Generate()
    {

        Graph =new MyGraph();
        Random rand =new Random();
        //Node genneratur
        for (int i =0;i < num;i++)
        {
            int x = rand.ints(0, 10000).findFirst().getAsInt();
            int y = rand.ints(0, 10000).findFirst().getAsInt();
            location loc = new location(x,y, 0);
            NodeData temp = new MyNode(i,loc);
            Graph.addNode(temp);
        }
        Iterator<NodeData> Nodes = this.Graph.nodeIter();
        while (Nodes.hasNext())
        {
            int weight = rand.ints(0, 30).findFirst().getAsInt();
            NodeData temp = Nodes.next();
            for(int i =0;i<30;i++)
            {
                int dest = rand.ints(0, num).findFirst().getAsInt();
                this.Graph.connect(temp.getKey(),dest,weight);
            }
        }
        return this.Graph;
    }
}
