package classes;

import api.EdgeData;
import api.GeoLocation;
import api.NodeData;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Iterator;

public class GUI_Panel extends JPanel
{
    private double min_x,max_x,min_y,max_y;
    MyGraph graph;

    public GUI_Panel(MyGraph g)
    {
        this.graph =g;
        this.setOpaque(true);
    }

    public void paint(Graphics graphics)
    {
        this.setBackground(Color.WHITE);
        Graphics2D graphic= (Graphics2D) graphics.create();

        ////////////calculate x and y dif

        /*
         * Draws all the Vertices that the user has
         * placed on the canvas
         */
        setMinMax();
        Iterator<NodeData> iterator= graph.nodeIter();
        while(iterator.hasNext())
        {
            drawVertex(iterator.next(),graphic);
        }
        Iterator<EdgeData> i2= graph.edgeIter();

        while (i2.hasNext())
        {
            EdgeData e=i2.next();
            if(e!=null)
            {
                NodeData src=graph.getNode(e.getSrc());
                NodeData dest=graph.getNode(e.getDest());
                Point p1,p2;
                p1=coordinates_Cal(src);
                p2=coordinates_Cal(dest);
                drawArrowLine(graphic,p1.x, p1.y, p2.x, p2.y,30,6);



            }
        }
        this.setVisible(true);

        graphic.dispose();
    }
    private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h) {
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};
        g.setColor(Color.BLACK);
        g.drawLine(x1, y1, x2, y2);

        g.fillPolygon(xpoints, ypoints, 3);
    }

    private void drawVertex(NodeData v,Graphics2D graphics)
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        double ox =v.getLocation().x();
//        ox/=(min_x+max_x);
//       // ox*=(screenSize.width);
//        double oy =v.getLocation().y()+min_y;
//        oy/=(min_y+max_y);
//        oy*=(screenSize.height);
        graphics.setColor(Color.RED);
        Point p=coordinates_Cal(v);
        Shape vertex = new Ellipse2D.Double(p.x-5, p.y-5, 12, 12);
        //Shape vertex = new Ellipse2D.Double(ox, oy, 50, 50);
        graphics.fill(vertex);

    }
    private Point coordinates_Cal(NodeData v)
    {
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        double SWidth=screensize.getWidth();
        double SHeight=screensize.getHeight();

        double xd=max_x-min_x;
        double yd=max_y-min_y;

        GeoLocation l=v.getLocation();
        int x = cal_x(l.x(),xd,SWidth,min_x);
        int y = cal_y(l.y(),yd,SHeight,max_y);

        return new Point(x,y);
    }
    private int cal_y(double y, double yd, double sHeight, double max_y)
    {
        int y_cor=(int)(((y-min_y)/yd)*(sHeight-150));
        y_cor=y_cor+50;
        return y_cor;
    }

    private int cal_x(double x, double xd, double SWidth, double min_x)
    {
        int x_cor=(int)(((x-min_x)/xd)*(SWidth-150));
        x_cor=x_cor+50;
        return x_cor;
    }

    private void setMinMax()
    {
        min_x=Double.MAX_VALUE;
        max_x=Double.MIN_VALUE;
        min_y=Double.MAX_VALUE;
        max_y=Double.MIN_VALUE;
        Iterator<NodeData> i = this.graph.nodeIter();
        while (i.hasNext())
        {
            NodeData v=i.next();
            GeoLocation gl=v.getLocation();
            if(gl.x()<min_x)
            {
                min_x=gl.x();
            }

            if(gl.x()>max_x)
            {
                max_x=gl.x();
            }

            if(gl.y()<min_y)
            {
                min_y=gl.y();
            }

            if(gl.y()>max_y)
            {
                max_y=gl.y();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
}
