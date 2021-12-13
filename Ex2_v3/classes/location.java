package classes;

import api.GeoLocation;

public class location implements GeoLocation
{
    //Props
    private double x,y,z;
    //const
    public location(double _x,double _y,double _z)
    {
        this.x=_x;
        this.y=_y;
        this.z=_z;
    }
    public location(GeoLocation ot)
    {
        this.x = ot.x();
        this.y = ot.y();
        this.z = ot.z();
    }

    public location(String coordinates)
    {
        String[] xyz=coordinates.split(",");
        this.x=Double.parseDouble(xyz[0]);
        this.y=Double.parseDouble(xyz[1]);
        this.z=Double.parseDouble(xyz[2]);
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public double distance(GeoLocation g)
    {
          double xd=this.x-g.x();
          double yd=this.y-g.y();
          double zd=this.z-g.z();
          xd=xd*xd;
          yd*=yd;
          zd*=zd;
          double ans=xd+yd+zd;
          return (Math.sqrt(ans));
    }

    @Override
    public String toString() {
        return x()+","+y()+","+z();
    }
}
