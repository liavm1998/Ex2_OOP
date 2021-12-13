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
        V=new HashMap<Integer,MyNode>();
        E=new HashMap<Point,MyEdge>();
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
            MyEdge e=E.get(p);
            E.remove(p);
            for (MyNode n:V.values())
            {
                n.removeEdge(e);
            }
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
    public boolean dfs(MyNode v)
    {
        // Initially mark all vertices as not visited
        Vector<Boolean> visited = new Vector<Boolean>(V.size());
        HashMap<Integer,Boolean> VIS=new HashMap<>();
        for (int key:this.V.keySet()) {
            VIS.put(key,false);
        }

        // Create a stack for DFS
        Stack<MyNode> stack = new Stack<>();

        // Push the current source node
        stack.push(v);
        while(!stack.empty())
        {
            // Pop a vertex from stack
            v = stack.peek();
            stack.pop();
            // Stack may contain same vertex twice. So
            // we need mark the popped item only
            // if it is not visited.
            if(VIS.get(v.getKey()) == false)
            {
                VIS.put(v.getKey(),true);
            }
            // Get all adjacent vertices of the popped vertex s
            // If a adjacent has not been visited, then push it
            // to the stack.
            Iterator<EdgeData> itr = this.edgeIter(v.getKey());

            while (itr.hasNext())
            {
                EdgeData e=itr.next();
                MyNode v1=this.V.get(e.getDest());

                if(!VIS.get(v1.getKey()))
                    stack.push(v1);
            }
        }
        for (boolean b: VIS.values())
        {
            if (!b)
            {
                return false;
            }
        }

//        v.visited=true;
//
//        for (MyEdge e:v.getOutlist())
//        {
//            MyNode w=this.getV().get(e.getDest());
//            if(!w.visited)
//            {
//                dfs(w);
//            }
//        }
        return true;
    }
}
