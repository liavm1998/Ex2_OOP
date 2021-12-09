import api.EdgeData;

public class MyEdge implements EdgeData {
    private final int src;
    private final int dest;
    private double weight=Double.MAX_VALUE;
    private int tag;

    public MyEdge(int _src,int _dest,double _weight)
    {
        this.src=_src;
        this.dest=_dest;
        this.weight=_weight;
        tag=0;
    }
    @Override
    public int getSrc()
    {
        return this.src;
    }

    @Override
    public int getDest() {
        return this.dest;
    }

    @Override
    public double getWeight() {
        return this.weight;
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
        this.tag=t;
    }

}
