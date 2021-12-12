package classes;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestGraph
{
    private DirectedWeightedGraphAlgorithms Algo;
    public void Start()
    {
        this.Algo=new MyAlgo();
//        Algo.load("src//data//G3.json");
        GraphGenneratur Graph =new GraphGenneratur(10000);
        DirectedWeightedGraph G =Graph.Generate();
        this.Algo.init(G);
        this.Algo.save("src//data//My_10000_Nodes.json");
        System.out.println();
    }

    @Test
    void isConnected()
    {
        Start();
        Assertions.assertTrue(this.Algo.isConnected());
    }

}
