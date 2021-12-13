package classes;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.GeoLocation;
import api.NodeData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestGraph
{
    MyGraph Graph;
    public TestGraph()
    {
        Graph = new MyGraph();
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

    @Test
    void getNode() {
        GeoLocation loc =new location(1,2,0);
        NodeData temp = new MyNode(10,(location) loc);
        this.Graph.addNode(temp);
        Assertions.assertEquals(temp,this.Graph.getNode(temp.getKey()));
    }

    @Test
    void getEdge() {
        this.Graph.connect(1,3,8);
        Assertions.assertEquals(1,this.Graph.getEdge(1,3).getSrc());
        Assertions.assertEquals(3,this.Graph.getEdge(1,3).getDest());
        Assertions.assertEquals(8,this.Graph.getEdge(1,3).getWeight());
    }

    @Test
    void addNode() {
        GeoLocation loc =new location(1,2,0);
        NodeData temp = new MyNode(10,(location) loc);
        this.Graph.addNode(temp);
        Assertions.assertEquals(temp,this.Graph.getNode(temp.getKey()));
    }

    @Test
    void connect() {
        this.Graph.connect(1,3,8);
        EdgeData temp = this.Graph.getEdge(1,3);
        Assertions.assertEquals(temp,this.Graph.getEdge(1,3));
    }

    @Test
    void nodeIter() {

    }

    @Test
    void edgeIter() throws Exception {

    }

    @Test
    void removeNode() {
        Assertions.assertTrue(this.Graph.getNode(0) != null);
        this.Graph.removeNode(0);
        Assertions.assertTrue(this.Graph.getNode(0)==null);
    }

    @Test
    void removeEdge() {
        Assertions.assertTrue(this.Graph.getEdge(0,5)!= null);
        this.Graph.removeEdge(0,5);
        Assertions.assertTrue(this.Graph.getEdge(0,5)==null);
    }

    @Test
    void nodeSize() {
        Assertions.assertEquals(this.Graph.nodeSize(),6);
    }

    @Test
    void edgeSize() {
        Assertions.assertEquals(this.Graph.edgeSize(),18);
    }

    @Test
    void getMC() {
        Assertions.assertEquals(this.Graph.getMC(),24);
    }


}
