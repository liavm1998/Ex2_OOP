package classes;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;

import java.awt.*;
import java.util.*;
import java.util.function.Consumer;

public class MyGraph extends DirectedGraph implements DirectedWeightedGraph,Comparable
{
    //////////properties/////////
    private HashMap<Integer,MyNode> V;
    private HashMap<Point,MyEdge> E;

    private int mc;


    //////////constructors////////
    public MyGraph()
    {
        V=new HashMap<Integer,MyNode>();
        E=new HashMap<Point,MyEdge>();
        mc=0;
    }
    public MyGraph(MyGraph g)
    {
        for (MyNode v:g.getV().values())
        {
            this.addNode(new MyNode(v.getKey(), (location) v.getLocation()));
        }
        for (MyEdge e: g.getE().values())
        {
            this.connect(e.getSrc(),e.getDest(),e.getWeight());
        }
        mc=0;
    }
    //////methods///////
    @Override
    public NodeData getNode(int key)
    {
        for (MyNode n:this.V.values())
        {
            if(n.getKey()==key)
            {
                return n;
            }
        }
        return null;
    }

    @Override
    public EdgeData getEdge(int src, int dest)
    {
        return  V.get(src).getOutEdge(dest);
    }

    @Override
    public void addNode(NodeData n)
    {
        if(this.V.get(n.getKey())!=null)
        {
            this.V.remove(n.getKey());
        }
        this.V.put(n.getKey(),(MyNode) n);
        mc++;
    }

    @Override
    public void connect(int src, int dest, double w)
    {
        MyEdge e=new MyEdge(src,dest,w);
        this.V.get(src).goOut(e);
        this.V.get(dest).goIn(e);
        this.E.put(new Point(src,dest),e);
        mc++;
    }

    @Override
    public Iterator<NodeData> nodeIter()
    {
        ArrayList<NodeData> temp = new ArrayList<>(V.values());
        return temp.iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        ArrayList<EdgeData> temp = new ArrayList<>(E.values());

        return new Iterator<EdgeData>()
        {
            Iterator<EdgeData> i =temp.iterator();
            int cmc=mc;
            @Override
            public boolean hasNext()
            {
                if(mc!=cmc)
                {
                    throw new RuntimeException();
                }
                return i.hasNext();
            }

            @Override
            public EdgeData next()
            {
                if(cmc!=mc)
                {
                    throw new RuntimeException();
                }
                return i.next();
            }

            @Override
            public void remove()
            {
                if(cmc!=mc)
                {
                    throw new RuntimeException();
                }
                cmc++;
                EdgeData e=i.next();
                removeEdge(e.getSrc(),e.getDest());
            }
        };
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id)
    {
        MyNode v=V.get(node_id);
        ArrayList<EdgeData> ans=new ArrayList<>(v.getOutlist());
        return new Iterator<EdgeData>()
        {
            Iterator<EdgeData> i=ans.iterator();
            int cmc=mc;

            @Override
            public boolean hasNext()
            {
                if(mc!=cmc)
                {
                    throw new RuntimeException();
                }
                return i.hasNext();
            }
            @Override
            public EdgeData next()
            {

                if(!hasNext())
                {
                    throw new NoSuchElementException();
                }
                if(mc!=cmc)
                {
                    throw new RuntimeException();
                }
                return i.next();
            }

            @Override
            public void remove()
            {
                if(mc!=cmc)
                {
                    throw new RuntimeException();
                }
                cmc++;
                EdgeData e=i.next();
                removeEdge(e.getSrc(),e.getDest());
            }

            @Override
            public void forEachRemaining(Consumer<? super EdgeData> action)
            {
                while (i.hasNext())
                    action.accept(i.next());
            }
        };
    }

    @Override
    public NodeData removeNode(int key)
    {
        MyNode v=this.V.get(key);
        ArrayList<Point>rp=new ArrayList<>();
        for (Point p:E.keySet())
        {
            if(p.x==key||p.y==key)
            {
                rp.add(p);
            }
        }
        for (Point p:rp)
        {
            E.remove(p);
        }
        V.remove(key);
        return v;
    }

    @Override
    public EdgeData removeEdge(int src, int dest)
    {

        for (Point p:this.E.keySet())
        {
            if (p.x==src&&p.y==dest)
            {
                this.E.remove(p);
                break;
            }
        }
        MyEdge e = (MyEdge) this.V.get(src).getOutEdge(dest);
        this.E.remove(e);
        this.V.get(src).removeEdge(e);
        this.V.get(dest).removeEdge(e);
        return e;
    }

    @Override
    public int nodeSize()
    {
        return this.V.size();
    }

    @Override
    public int edgeSize() {
        return this.E.size();
    }

    @Override
    public int getMC() {
        return this.mc;
    }

    public HashMap<Integer,MyNode> getV()
    {
        return this.V;
    }

    public void clearWeight()
    {
        for (MyNode v:V.values())
        {
          v.setWeight(-1);
        }
    }

    @Override
    public int compareTo(Object o)
    {
        if(o instanceof MyGraph)
        {
            MyGraph ot=(MyGraph) o;
            if(!this.V.values().containsAll(ot.getV().values()))
            {
                return -1;
            }
            else if(!ot.getV().values().containsAll(this.getV().values()))
            {
                return 1;
            }
            else if(!this.getE().values().containsAll(this.getE().values()))
            {
                return -1;
            }
            else if(!ot.getE().values().containsAll(this.getE().values()))
            {
                return -1;
            }
            else
            {
                return 0;
            }
        }
        else
        {
            throw new FormatFlagsConversionMismatchException("Exepted Graph object",'f');
        }

    }

    public HashMap<Point,MyEdge>  getE()
    {
        return this.E;
    }

    public MyGraph reverse()
    {
        MyGraph g=new MyGraph();
        for (MyNode v:this.getV().values())
        {
            g.addNode(new MyNode(v.getKey(), (location) v.getLocation()));
        }
        for (MyEdge e: this.E.values())
        {
            g.connect(e.getDest(),e.getSrc(),e.getWeight());
        }
        return g;
    }
    public void dfs(MyNode v)
    {
        v.visited=true;
        for (MyEdge e:v.getOutlist())
        {
            MyNode w=this.getV().get(e.getDest());
            if(!w.visited)
            {
                dfs(w);
            }
        }
    }


    public void clearTags()
    {
        for (MyNode v:this.V.values())
        {
            v.setTag(0);
        }
    }

    public MyNode getnodeBylocation(Point point)
    {
        for (MyNode v:this.getV().values())
        {
            if(v.getLocation().x()== point.x&&v.getLocation().y()== point.y)
            {
                return v;
            }
        }
        return null;
    }

}
