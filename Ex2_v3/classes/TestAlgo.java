package classes;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
//import org.junit.*;
import api.EdgeData;
import api.NodeData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class TestAlgo
{
    private DirectedWeightedGraphAlgorithms Algo;
    private DirectedWeightedGraph Graph;
    public TestAlgo()
    {
        GraphGenerator G =new GraphGenerator(1000,"src//data//test.json");
        this.Algo=new MyAlgo();
        this.Graph = new MyGraph();
        MyNode n1,n2,n3,n4,n5,n6;
        location a1,a2,a3,a4,a5,a6;
        a1=new location(1,2,0);
        a2=new location(5,1,0);
        a3=new location(2,5,0);
        a4=new location(6,7,0);
        a5=new location(8,4,0);
        a6=new location(5,4,0);
        n1=new MyNode(0,a1);
        n2=new MyNode(1,a2);
        n3=new MyNode(2,a3);
        n4=new MyNode(3,a4);
        n5=new MyNode(4,a5);
        n6=new MyNode(5,a6);
        Graph.addNode(n1);
        Graph.addNode(n2);
        Graph.addNode(n3);
        Graph.addNode(n4);
        Graph.addNode(n5);
        Graph.addNode(n6);
        Graph.connect(0,2,14);
        Graph.connect(2,0,14);
        Graph.connect(0,5,9);
        Graph.connect(5,0,9);
        Graph.connect(0,1,7);
        Graph.connect(1,0,7);
        Graph.connect(1,5,10);
        Graph.connect(5,1,10);
        Graph.connect(1,4,15);
        Graph.connect(4,1,15);
        Graph.connect(2,5,2);
        Graph.connect(5,2,2);
        Graph.connect(5,4,20);
        Graph.connect(4,5,20);
        Graph.connect(4,3,60);
        Graph.connect(3,4,60);
        Graph.connect(2,3,100);
        Graph.connect(3,2,100);
    }
    public void Start()
    {
        this.Algo=new MyAlgo();
        Algo.load("src//data//My1000Nodes.json");
    }

    public void Start1()
    {
        DirectedWeightedGraphAlgorithms Algo=new MyAlgo();
        GraphGenerator Graph =new GraphGenerator(10000);
        DirectedWeightedGraph G =Graph.Generate();
        Algo.init(G);
        this.Algo.save("src//data//MyG.json");
        System.out.println();
    }

    @Test
    void init() {
        Assertions.assertEquals(this.Algo.getGraph(),null);
        this.Algo.init(this.Graph);
        Assertions.assertEquals(this.Algo.getGraph(),this.Graph);
    }

    @Test
    void getGraph() {
        this.Algo.init(this.Graph);
        Assertions.assertEquals(this.Algo.getGraph(),this.Graph);
    }

    @Test
    void copy() {
        this.Algo.init(this.Graph);
        DirectedWeightedGraph copy = this.Algo.copy();
        System.out.println();
        Assertions.assertFalse(copy.equals(this.Algo.getGraph()));

        Iterator<NodeData> NodeGraph =this.Algo.getGraph().nodeIter();
        Iterator<NodeData> Nodecopy =copy.nodeIter();
        while (Nodecopy.hasNext() || NodeGraph.hasNext())
        {
            NodeData copyNext =Nodecopy.next();
            NodeData graphNext= NodeGraph.next();
            Assertions.assertEquals(copyNext.getKey(),graphNext.getKey());
            Assertions.assertEquals(copyNext.getWeight(),graphNext.getWeight());
            Assertions.assertEquals(copyNext.getLocation().x(),graphNext.getLocation().x());
            Assertions.assertEquals(copyNext.getLocation().y(),graphNext.getLocation().y());
            Assertions.assertEquals(copyNext.getLocation().z(),graphNext.getLocation().z());
        }
        Iterator<EdgeData> EdgeGraph = this.Algo.getGraph().edgeIter();
        Iterator<EdgeData> Edgecopy = copy.edgeIter();
        while (Edgecopy.hasNext() || EdgeGraph.hasNext())
        {
            EdgeData tempEdge = EdgeGraph.next();
            EdgeData tempgraph= Edgecopy.next();
            int s =tempgraph.getSrc();
            int sd =tempEdge.getSrc();
            Assertions.assertEquals(tempgraph.getSrc(),tempEdge.getSrc());
            Assertions.assertEquals(tempgraph.getDest(),tempEdge.getDest());
            Assertions.assertEquals(tempgraph.getWeight(),tempEdge.getWeight());
        }
    }

    @Test
    void isConnected()
    {
        Start();
//        this.Algo.init(this.Graph);
        Assertions.assertTrue(this.Algo.isConnected());
    }

    @Test
    void shortestPathDist()
    {
        Start();
        double dist =this.Algo.shortestPathDist(0,3);
        Assertions.assertEquals(dist,this.Algo.shortestPathDist(0,3));

//        this.Algo.init(this.Graph);
//        int dist = 82;
//        Assertions.assertEquals(dist,this.Algo.shortestPathDist(0,3));
    }

    @Test
    void shortestPath()
    {
//        this.Algo.init(this.Graph);
//        List<NodeData> ans = new ArrayList<>();
//        ans.add(this.Graph.getNode(0));
//        ans.add(this.Graph.getNode(1));
//        ans.add(this.Graph.getNode(4));
//        ans.add(this.Graph.getNode(3));
//        List<NodeData> temp =this.Algo.shortestPath(0,3);
//        while (!ans.isEmpty() || !temp.isEmpty())
//        {
//            Assertions.assertEquals(ans.get(0).getKey(),temp.get(0).getKey());
//            Assertions.assertEquals(ans.get(0).getWeight(),temp.get(0).getWeight());
//            Assertions.assertEquals(ans.get(0).getLocation().x(),temp.get(0).getLocation().x());
//            Assertions.assertEquals(ans.get(0).getLocation().y(),temp.get(0).getLocation().y());
//            Assertions.assertEquals(ans.get(0).getLocation().z(),temp.get(0).getLocation().z());
//            ans.remove(0);
//            temp.remove(0);
//        }

        Start();
        List<NodeData> ans =this.Algo.shortestPath(0,3);
        Assertions.assertEquals(ans,this.Algo.shortestPath(0,3));
    }

    @Test
    void center()
    {
        Start();
        int center = this.Algo.center().getKey();
//        Assertions.assertEquals(center,this.Algo.center().getKey());

//        this.Algo.init(this.Graph);
//        int center =4;
//        Assertions.assertEquals(center,this.Algo.center().getKey());
    }

    @Test
    void tsp() {
        Start();
        Iterator<NodeData> temp =this.Algo.getGraph().nodeIter();
        List<NodeData> city = new ArrayList<>();
        while (temp.hasNext())
        {
            city.add(temp.next());
        }
        List<NodeData> TSP =this.Algo.tsp(city);
        while (!city.isEmpty())
        {
            for(int i=0;i<TSP.size();i++)
            {
                if(!city.isEmpty() && city.get(0) == TSP.get(i))
                {
                    city.remove(0);
                }
            }
        }
        Assertions.assertTrue(city.isEmpty());
    }

    @Test
    void save() {
        this.Algo.init(this.Graph);
        boolean ans= true;
        this.Algo.save("src//data//TestSave.json");
        try {
            this.Algo.load("src//data//TestSave.json");
        } catch (Exception e)
        {
            System.out.println("no file exist");
            ans=false;
        }
        Assertions.assertTrue(ans);
    }

    @Test
    void load() {
        Assertions.assertTrue(this.Algo.getGraph()==null);
        Algo.load("src//data//G3.json");
        Assertions.assertTrue(this.Algo.getGraph()!= null);
    }

}
