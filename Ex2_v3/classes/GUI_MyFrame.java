package classes;

import api.EdgeData;
import api.NodeData;

import javax.swing.*;
import javax.xml.stream.Location;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GUI_MyFrame extends JFrame implements ActionListener {
    MyAlgo algo;
    private JFrame frame;
    GUI_Panel gp;
    JFrame gui;

    ///////constructors
//    //public classes.GUI_MyFrame()
//    {
//        new classes.GUI_MyFrame(new classes.MyGraph());
//    }
    public GUI_MyFrame(MyGraph g) {


        algo = new MyAlgo();
        algo.init(g);
        gp = new GUI_Panel(algo);
        this.frame = new JFrame();


        //this.setLayout(new BorderLayout());

        this.setBackground(Color.WHITE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        this.setResizable(true);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        //this.setBounds();
        this.setTitle("Graph");
        ImageIcon logo = new ImageIcon("src/Images/logo.png");
        this.setIconImage(logo.getImage());
        //this.setLocationRelativeTo(null);
        this.add(gp, BorderLayout.CENTER);
        this.menu();

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.pack();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }

    // frame.setBounds(30, 30, 300, 300);
    // Set the Frame boundaries to be 300x300 on the screen,
    // and position it 30 pixels from the top and left edges of the monitor.
    public void menu() {
        //////first menu  the save load menu
        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("file");
        JMenuItem load, save;
        load = new JMenuItem("load");
        load.addActionListener(this);
        save = new JMenuItem("save");
        save.addActionListener(this);

        file.add(load);
        file.add(save);

        ///////algo menu
        JMenu algorithms = new JMenu("algorithms");
        JMenuItem addNode, addEdge, center, isConnected, shortestDist, shortestPath, tsp, rmvN = new JMenuItem("remove node"), rmvE;

        addNode = new JMenuItem("add or move Node");
        addEdge = new JMenuItem("add Edge");
        center = new JMenuItem("center");
        isConnected = new JMenuItem("is Connected?");
        shortestDist = new JMenuItem("shortest Dist");
        shortestPath = new JMenuItem("shortest Path");
        tsp = new JMenuItem("TSP");
        rmvE = new JMenuItem("remove edge");

        shortestDist.addActionListener(this);
        shortestPath.addActionListener(this);
        isConnected.addActionListener(this);
        center.addActionListener(this);
        addEdge.addActionListener(this);
        addNode.addActionListener(this);
        addNode.addActionListener(this);
        /////////////////////////////////////////////TSP////////////////////////////////////////
        tsp.addActionListener(this);


        ////////////////////////remove node

        rmvN.addActionListener((ActionListener) this);

        //////////////////////////////remove edge////////////
        rmvE.addActionListener(this);


        ////////////jmenu info
        JMenu info = new JMenu("info");
        JMenuItem getNode, GetEdge, SizeOfNodes, SizeOfEdges;
        getNode = new JMenuItem("get node coordinates");
        GetEdge = new JMenuItem("get edge weight");
        SizeOfEdges = new JMenuItem("amount of edges");
        SizeOfNodes = new JMenuItem("amount of nodes");


        getNode.addActionListener(this);
        GetEdge.addActionListener(this);


        //////////////////////size of nodes
        SizeOfNodes.addActionListener(this);

        //////////////////////size of edges
        SizeOfEdges.addActionListener(this);

        ///////////////name setting//////////


        ////////////////adding;
        algorithms.add(addNode);
        algorithms.add(addEdge);
        algorithms.add(center);
        algorithms.add(isConnected);
        algorithms.add(shortestDist);
        algorithms.add(shortestPath);
        algorithms.add(tsp);
        algorithms.add(rmvN);
        algorithms.add(rmvE);

        info.add(getNode);
        info.add(GetEdge);
        info.add(SizeOfEdges);
        info.add(SizeOfNodes);

        menu.add(file);
        menu.add(algorithms);
        menu.add(info);


        this.setJMenuBar(menu);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "remove node") {
            String id = (JOptionPane.showInputDialog(frame, "which node to remove (by id)", null));
            MyGraph g = (MyGraph) algo.getGraph();
            int NodeId = Integer.parseInt(id);
            if (algo.getGraph().getNode(NodeId) == null) {
                JOptionPane.showMessageDialog(null, "the node doesn't exist");
            } else {
                g.removeNode(NodeId);
                this.algo.init(g);
                this.repaint();
            }
        }
        if (e.getActionCommand() == "save") {
            JFileChooser fileChooser = new JFileChooser("src/data");
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                algo.save(fileChooser.getSelectedFile().getAbsolutePath());
            }
        }
        if (e.getActionCommand() == "load") {
            JFileChooser fileChooser = new JFileChooser("src//data");
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                ///reading
                //System.out.println(fileChooser.getSelectedFile().getAbsolutePath());
                algo.load(fileChooser.getSelectedFile().getAbsolutePath());
//                setVisible(false);
//                new GUI_MyFrame((MyGraph) algo.getGraph());
                this.repaint();
            }
        }
        if (e.getActionCommand() == "get node coordinates") {
            String v = (JOptionPane.showInputDialog(frame, "enter the id of the desired node", null));
            int id = Integer.parseInt(v);
            location l = (location) algo.getGraph().getNode(id).getLocation();
            String loc = l.x() + "," + l.y() + "," + l.y();
            JOptionPane.showMessageDialog(null, "the coordinates of node " + v + " are " + loc);
        }
        if (e.getActionCommand() == "get edge weight") {
            String src = (JOptionPane.showInputDialog(frame, "enter src of the desired edge", null));
            int source = Integer.parseInt(src);
            if (algo.getGraph().getNode(source) == null) {
                JOptionPane.showMessageDialog(null, "the src node doesn't exist");
            } else {
                String dest = (JOptionPane.showInputDialog(frame, "enter the dest of the desired edge", null));
                int d = Integer.parseInt(dest);
                if (algo.getGraph().getNode(d) == null) {
                    JOptionPane.showMessageDialog(null, "the dest node doesn't exist");
                } else {
                    double ans = algo.getGraph().getEdge(source, d).getWeight();
                    JOptionPane.showMessageDialog(null, "the edge weight is: " + ans);

                }
            }
        }
        if (e.getActionCommand() == "amount of edges") {
            JOptionPane.showMessageDialog(null, "we have " + algo.getGraph().edgeSize() + " edges in the graph");
        }
        if (e.getActionCommand() == "amount of nodes") {
            JOptionPane.showMessageDialog(null, "we have " + algo.getGraph().nodeSize() + " nodes in the graph");
        }
        if (e.getActionCommand() == "remove edge") {
            String src = (JOptionPane.showInputDialog(frame, "enter src of the desired edge", null));
            int source = Integer.parseInt(src);
            if (algo.getGraph().getNode(source) == null) {
                JOptionPane.showMessageDialog(null, "the src node doesn't exist");
            } else {
                String dest = (JOptionPane.showInputDialog(frame, "enter the dest of the desired edge", null));
                int d = Integer.parseInt(dest);
                if (algo.getGraph().getNode(d) == null) {
                    JOptionPane.showMessageDialog(null, "the dest node doesn't exist");
                } else {
                    algo.getGraph().removeEdge(source, d);
                    setVisible(false);
                    new GUI_MyFrame((MyGraph) algo.getGraph());
                }

            }
        }
        if (e.getActionCommand() == "TSP") {
            ArrayList<NodeData> cities = new ArrayList<>();
            String[] citiesId = (JOptionPane.showInputDialog(frame, "enter cities ids separated by ',' regex ", null)).split(",");
            for (int i = 0; i < citiesId.length; i++) {
                NodeData v = algo.getGraph().getNode(Integer.parseInt(citiesId[i]));
                cities.add(v);
            }
            List<NodeData> ans = algo.tsp(cities);
            Iterator<NodeData> i = ans.iterator();
            NodeData prev = new MyNode(Integer.MAX_VALUE, new location("1,1,1"));
            boolean first = true;
            String answer = "";
            while (i.hasNext()) {
                if (first) {
                    prev = i.next();
                    first = false;
                    if (cities.contains(prev)) {
                        answer += prev.getKey() + "->";
                        cities.remove(prev);
                    }
                } else {
                    NodeData cur = (NodeData) i.next();
                    if (i.hasNext() && cities.contains(cur)) {
                        answer += cur.getKey() + "->";
                        cities.remove(cur);
                    } else if (cities.contains(cur)) {
                        answer += cur.getKey();
                        cities.remove(cur);
                    }
                }
            }
            JOptionPane.showMessageDialog(null, answer);
        }
        if (e.getActionCommand() == "shortest Path") {

            String src = JOptionPane.showInputDialog(frame, "enter src node id", null);
            int s = Integer.parseInt(src);
            if (algo.getGraph().getNode(s) == null) {
                /////pop up error: node doesn't exist
                JOptionPane.showMessageDialog(null, "error: node doesn't exist");
            } else {
                String dest = JOptionPane.showInputDialog(frame, "enter dest node id", null);
                int d = Integer.parseInt(dest);
                if (algo.getGraph().getNode(d) == null) {
                    JOptionPane.showMessageDialog(null, "node doesn't exist");
                } else {
                    List<NodeData> ans = algo.shortestPath(s, d);
                    if (ans == null)
                    {
                        JOptionPane.showMessageDialog(null, "there is no path");
                    } else {
                        Iterator i = ans.iterator();
                        NodeData prev = new MyNode(Integer.MAX_VALUE, new location("1,1,1"));
                        boolean first = true;
                        String path = "";
                        while (i.hasNext()) {
                            if (first) {
                                prev = (NodeData) i.next();
                                first = false;
                                path += prev.getKey();
                            } else {
                                NodeData cur = (NodeData) i.next();
                                EdgeData edge = algo.getGraph().getEdge(prev.getKey(), cur.getKey());
                                edge.setInfo("red");
                                prev = cur;
                                path += "->" + prev.getKey();
                            }
                        }

                        JOptionPane.showMessageDialog(frame, "the shortest path is " + path);
                    }
                }
            }
        }
        if (e.getActionCommand() == "shortest Dist") {
            String src = JOptionPane.showInputDialog(frame, "enter src node id", null);
            int s = Integer.parseInt(src);
            if (algo.getGraph().getNode(s) == null) {
                /////pop up error: node doesn't exist
                JOptionPane.showMessageDialog(null, "error: src node doesn't exist");
            } else {
                String dest = JOptionPane.showInputDialog(frame, "enter dest node id", null);
                int d = Integer.parseInt(dest);
                if (algo.getGraph().getNode(d) == null) {
                    /////pop up error: node doesn't exist
                    JOptionPane.showMessageDialog(null, "error: dest node doesn't exist");
                } else {
                    double ans = algo.shortestPathDist(s, d);
                    /////////////pop it up
                    JOptionPane.showMessageDialog(null, "the shortest dist between " + s + "-->" + d + " is " + ans);
                }
            }
        }
        if (e.getActionCommand() == "add or move Node") {
            String id = JOptionPane.showInputDialog(frame, "enter node id", null);
            if (algo.getGraph().getNode(Integer.parseInt(id)) == null) {
                String coordinates = JOptionPane.showInputDialog(frame, "enter node coordinates split by regex ','", "x,y,z");
                MyNode a = new MyNode(Integer.parseInt(id), new location(coordinates));
                algo.getGraph().addNode(a);
                MyGraph g = (MyGraph) algo.getGraph();

                JOptionPane.showMessageDialog(null, "node added successfully");
//                setVisible(false);
//                new GUI_MyFrame((MyGraph) algo.getGraph());
                this.repaint();
            } else {
                String coordinates = JOptionPane.showInputDialog(frame, "enter new node coordinates split by regex ','", "x,y,z");
                MyNode a = new MyNode(Integer.parseInt(id), new location(coordinates));
                MyGraph g = (MyGraph) algo.getGraph();
                g.addNode(a);
//                JOptionPane.showMessageDialog(null, "node moved successfully");
//                setVisible(false);
//                new GUI_MyFrame(g);
                this.repaint();
            }
        }
        if (e.getActionCommand() == "add Edge") {
            String src = JOptionPane.showInputDialog(frame, "enter src node id", null);
            int s = Integer.parseInt(src);
            if (algo.getGraph().getNode(s) == null) {
                /////pop up error: node doesn't exist
                JOptionPane.showMessageDialog(null, "error: src node doesn't exist");
            } else {
                String dest = JOptionPane.showInputDialog(frame, "enter dest node id", null);
                int d = Integer.parseInt(dest);
                if (algo.getGraph().getNode(d) == null) {
                    /////pop up error: node doesn't exist
                    JOptionPane.showMessageDialog(null, "error: dest node doesn't exist");
                } else {
                    String w = JOptionPane.showInputDialog(frame, "enter node weight", null);
                    MyGraph g = (MyGraph) algo.getGraph();
                    g.connect(s, d, Double.parseDouble(w));
//                    setVisible(false);
//                    new GUI_MyFrame(g);
                    this.repaint();
                }
            }
        }
        if (e.getActionCommand() == "center") {
            try {
                NodeData n = algo.center();
                /////////popup center
                JOptionPane.showMessageDialog(null, "the center node is: node " + n.getKey());
            } catch (RuntimeException e1) {
                JOptionPane.showMessageDialog(null, "error ");
            }
        }
        if (e.getActionCommand() == "is Connected?") {
            if (algo.isConnected()) {
                ////////pop up this graph is connected
                JOptionPane.showMessageDialog(null, "this graph is connected");
            } else {
                ///////////pop this graph isn't connected
                JOptionPane.showMessageDialog(null, "this graph isn't connected");

            }
            this.repaint();
        }

    }

}