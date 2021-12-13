package classes;

import api.EdgeData;
import api.GeoLocation;
import api.NodeData;

import java.util.ArrayList;

public class MyNode implements NodeData {
    private double weight;
    private int tag;
    private final int key;
    private location location;
    private ArrayList<MyEdge> in;
    private ArrayList<MyEdge> out;

    /////isConnected components
    //public boolean visited = false;

    /////dikjestra component
    private ArrayList<MyNode> shortestPath = new ArrayList<>();
    private double distance = Integer.MAX_VALUE;
    //////center problem
    private double maxDist;

    public MyNode(int _key, location _location) {
        this.key = _key;
        this.location = _location;
        this.in = new ArrayList<>();
        this.out = new ArrayList<>();
        this.weight = -1;
        this.tag = 0;
    }

    public double getDistance() {
        return this.distance;
    }

    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public GeoLocation getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(GeoLocation p) {
        this.location = (location) p;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double w) {//////////////if this node has weighted(not eq -1) it means that it is already visited
        if (weight == -1) {
            this.weight = w;
            this.distance = w;
            ArrayList<MyNode> ListTillHere = new ArrayList<>();
            ListTillHere.add(this);
        } else {
            ///////set weight
            this.weight = Math.min(this.weight, w);
            this.distance = w;
        }
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void setInfo(String s) {

    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    /////my function
    //go to me ,I am the dest
    // compare the src of the edge
    public void goIn(MyEdge e) {
        if (this.getInEdge(e.getSrc()) != null) {
            //overriding existing edge
            this.in.remove(this.getInEdge(e.getDest()));
        }
        this.in.add(e);
    }

    //go from me, I am the src
    // compare the dest of the edge for existing edge
    public void goOut(MyEdge e) {
        if (this.getInEdge(e.getDest()) != null) {
            //overriding existing edge
            this.out.remove(this.getInEdge(e.getSrc()));
        }
        this.out.add(e);
    }

    public EdgeData getOutEdge(int dest) {
        for (EdgeData e : this.out) {
            if (e.getDest() == dest) {
                return e;
            }
        }
        return null;
    }

    public EdgeData getInEdge(int src) {
        for (EdgeData e : this.in) {
            if (e.getSrc() == src) {
                return e;
            }
        }
        return null;
    }

    public ArrayList<MyEdge> getInlist() {
        return this.in;
    }

    public ArrayList<MyEdge> getOutlist() {
        return this.out;
    }

    public void removeEdge(MyEdge e) {
        this.out.remove(e);
        this.in.remove(e);
    }

    public void setDistance(double v) {
        this.distance = v;
    }

    public ArrayList<MyNode> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(ArrayList<MyNode> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public double getMaxDist() {
        return this.maxDist;
    }

    public void setMaxDist(double maxDist) {
        this.maxDist = maxDist;
    }
}
