package classes;

import api.EdgeData;
import api.NodeData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GUI_MyFrame extends JFrame
{
    MyAlgo algo;
    private JFrame frame;
    GUI_Panel gp;
    ///////constructors
//    //public classes.GUI_MyFrame()
//    {
//        new classes.GUI_MyFrame(new classes.MyGraph());
//    }
    public GUI_MyFrame(MyGraph g)
    {

        gp=new GUI_Panel(g);
        algo=new MyAlgo();
        algo.init(g);
        this.frame =new JFrame();


        //this.setLayout(new BorderLayout());

        this.setBackground(Color.WHITE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        this.setResizable(true);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        //this.setBounds();
        this.setTitle("Graph");
        ImageIcon logo=new ImageIcon("src/Images/logo.png");
        this.setIconImage(logo.getImage());
        //this.setLocationRelativeTo(null);
        this.add(gp,BorderLayout.CENTER);
        this.menu();

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.pack();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }



    // frame.setBounds(30, 30, 300, 300);
    // Set the Frame boundaries to be 300x300 on the screen,
    // and position it 30 pixels from the top and left edges of the monitor.

    public void setBounds() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (dimension.getWidth()/2);
        int Height = (int) (dimension.getHeight()/2);
        int x = (int) (dimension.getWidth()/2-dimension.getWidth()/4);
        int y = (int) (dimension.getHeight()/2 - dimension.getHeight()/ 4);
        this.setBounds(x, y,width,Height);
    }

    public void menu()
    {
        //////first menu  the save load menu
        JMenuBar menu=new JMenuBar();
        JMenu file=new JMenu("file");
        JMenuItem load, save;
        load=new JMenuItem("load");

        /////load
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser fileChooser = new JFileChooser("data");
                if (fileChooser.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION)
                {
                   ///reading
                    //System.out.println(fileChooser.getSelectedFile().getAbsolutePath());
                    algo.load(fileChooser.getSelectedFile().getAbsolutePath());
                    setVisible(false);
                    new GUI_MyFrame((MyGraph) algo.getGraph());
                }
            }
        });
        /////////save
        save=new JMenuItem("save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser("data");
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    algo.save(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        file.add(load);
        file.add(save);

        ///////algo menu
        JMenu algorithms=new JMenu("algorithms");
        JMenuItem addNode,addEdge,center, isConnected,shortestDist,shortestPath,tsp;


        ////////////////////////////////////////////////////////addNode///////////////////////////////////////////////////////////////
        addNode=new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String id = JOptionPane.showInputDialog(frame, "enter node id", null);
                if(algo.getGraph().getNode(Integer.parseInt(id))==null)
                {
                    String coordinates=JOptionPane.showInputDialog(frame, "enter node coordinates split by regex ','", "x,y,z");
                    MyNode a=new MyNode(Integer.parseInt(id),new location(coordinates));
                    algo.getGraph().addNode(a);
                    MyGraph g = (MyGraph) algo.getGraph();
                    JOptionPane.showInputDialog("node added successfully");
                    setVisible(false);
                    new GUI_MyFrame((MyGraph) algo.getGraph());
                }
                else
                {
                    String coordinates=JOptionPane.showInputDialog(frame, "enter new node coordinates split by regex ','", "x,y,z");
                    MyNode a=new MyNode(Integer.parseInt(id),new location(coordinates));
                    MyGraph g = (MyGraph) algo.getGraph();
                    g.addNode(a);
                    JOptionPane.showInputDialog("node moved successfully");
                    setVisible(false);
                    new GUI_MyFrame(g);
                }
            }
        });
        /////////////////////////////////////////////////////////add edge///////////////////////////////////////////////////////////
        addEdge=new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String src = JOptionPane.showInputDialog(frame, "enter src node id", null);
                int s=Integer.parseInt(src);
                if(algo.getGraph().getNode(s)==null){
                    /////pop up error: node doesn't exist
                    JOptionPane.showMessageDialog(null,"error: src node doesn't exist");
                }
                else
                {
                    String dest= JOptionPane.showInputDialog(frame, "enter dest node id", null);
                    int d=Integer.parseInt(dest);
                    if(algo.getGraph().getNode(d)==null)
                    {
                        /////pop up error: node doesn't exist
                        JOptionPane.showMessageDialog(null,"error: dest node doesn't exist");
                    }
                    else
                    {
                        String w=    JOptionPane.showInputDialog(frame, "enter node weight", null);
                        MyGraph g= (MyGraph) algo.getGraph();
                        g.connect(s,d,Double.parseDouble(w));
                        setVisible(false);
                        new GUI_MyFrame(g);
                    }
                }
            }
        });
        /////////////////////////////////////////////////////////center/////////////////////////////////////////////////////////
        center=new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                NodeData n=algo.center();
                /////////popup center

                JOptionPane.showMessageDialog(null,"the center node is: node "+n.getKey());
            }
        });

        isConnected=new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(algo.isConnected())
                {
                    ////////pop up this graph is connected
                    JOptionPane.showMessageDialog(null,"this graph is connected");
                }
                else
                {
                    ///////////pop this graph isn't connected
                    JOptionPane.showMessageDialog(null,"this graph isn't connected");

                }
            }
        });
        ////////////////////////////////////shortest path////////////////////////////////////////////
        shortestDist=new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String src = JOptionPane.showInputDialog(frame, "enter src node id", null);
                int s=Integer.parseInt(src);
                if(algo.getGraph().getNode(s)==null){
                    /////pop up error: node doesn't exist
                    JOptionPane.showMessageDialog(null,"error: src node doesn't exist");
                }
                else
                {
                    String dest= JOptionPane.showInputDialog(frame, "enter dest node id", null);
                    int d=Integer.parseInt(dest);
                    if(algo.getGraph().getNode(d)==null)
                    {
                        /////pop up error: node doesn't exist
                        JOptionPane.showMessageDialog(null,"error: dest node doesn't exist");
                    }
                    else
                    {
                        double ans=algo.shortestPathDist(s,d);
                        /////////////pop it up
                        JOptionPane.showMessageDialog(null,"the shortest dist between "+s+"-->"+d +" is "+ans);
                    }
                }
            }
        });

        //////////////////////shortest path/////////////////////////////////
        shortestPath=new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String src = JOptionPane.showInputDialog(frame, "enter src node id", null);
                int s=Integer.parseInt(src);
                if(algo.getGraph().getNode(s)==null){
                    /////pop up error: node doesn't exist
                    JOptionPane.showMessageDialog(null, "error: node doesn't exist");
                }
                else
                {
                    String dest= JOptionPane.showInputDialog(frame, "enter dest node id", null);
                    int d=Integer.parseInt(dest);
                    if(algo.getGraph().getNode(d)==null)
                    {
                        /////pop up error: node doesn't exist
                    }
                    else
                    {
                        List<NodeData> ans=algo.shortestPath(s,d);
                        Iterator i =ans.iterator();
                        NodeData prev=new MyNode(Integer.MAX_VALUE,new location("1,1,1"));
                        boolean first=true;
                        String path="";
                        while (i.hasNext())
                        {
                            if(first)
                            {
                                prev= (NodeData) i.next();
                                first=false;
                                path+=prev.getKey();
                            }
                            else
                            {
                                NodeData cur= (NodeData) i.next();
                                EdgeData edge=algo.getGraph().getEdge(prev.getKey(),cur.getKey() );
                                edge.setInfo("red");
                                prev=cur;
                                path+="->"+prev.getKey();
                            }
                        }

                        JOptionPane.showMessageDialog(frame, "the shortest path is "+path);
                    }
                }
            }
        });
        /////////////////////////////////////////////TSP////////////////////////////////////////
        tsp=new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<NodeData> cities=new ArrayList<>();
                String[] citiesId = (JOptionPane.showInputDialog(frame, "enter cities ids separated by ',' regex ", null)).split(",");
                for (int i = 0; i < citiesId.length; i++)
                {
                    NodeData v = algo.getGraph().getNode(Integer.parseInt(citiesId[i]));
                    cities.add(v);
                }
                List<NodeData> ans=algo.tsp(cities);
                Iterator i =ans.iterator();
                NodeData prev=new MyNode(Integer.MAX_VALUE,new location("1,1,1"));
                boolean first=true;
                while (i.hasNext())
                {
                    if(first)
                    {
                        prev= (NodeData) i.next();
                        first=false;

                    }
                    else
                    {
                        NodeData cur= (NodeData) i.next();
                        EdgeData edge=algo.getGraph().getEdge(prev.getKey(),cur.getKey() );
                        edge.setInfo("green");
                    }
                }

            }
        });
        ///////////////name setting//////////
        addNode.setText("add or move Node");
        addEdge.setText("add Edge");
        center.setText("center");
        isConnected.setText("isConnected");
        shortestDist.setText("shortest Dist");
        shortestPath.setText("shortest Path");
        tsp.setText("TSP");


        ////////////////adding;
        algorithms.add(addNode);
        algorithms.add(addEdge);
        algorithms.add(center);
        algorithms.add(isConnected);
        algorithms.add(shortestDist);
        algorithms.add(shortestPath);
        algorithms.add(tsp);






        menu.add(file);
        menu.add(algorithms);


        this.setJMenuBar(menu);
    }






}
