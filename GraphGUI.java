import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;       

/**
 *  Implements a GUI with modes for adding and deleting nodes/edges of a graph.
 *
 *  @author  Maddy Kulke, Nicholas R. Howe
 *  @version CSC 212 final project, 12/14/17
 */
public class GraphGUI {

    /** The graph to be displayed */
    private GraphCanvas canvas;

    /** Label for the input mode instructions */
    private JLabel instr;

    /** The input mode */
    InputMode mode = InputMode.ADD_POINTS;

    /** Remembers point where last mousedown event occurred */
    Point pointUnderMouse;

    /** saves first edge point when drawing an edge */ 
    Point firstEdgePoint; 


    /**
     *  Schedules a job for the event-dispatching thread
     *  creating and showing this application's GUI.
     */

    public static void main(String[] args) {
        final GraphGUI GUI = new GraphGUI();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    GUI.createAndShowGUI();
                }
            });
    }

    /** Sets up the GUI window */
    public void createAndShowGUI() {

        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        JFrame frame = new JFrame("Graph GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add components
        createComponents(frame);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    /** Puts content in the GUI window */
    public void createComponents(JFrame frame) {
        // graph display
        Container pane = frame.getContentPane();
        pane.setLayout(new FlowLayout());
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        canvas = new GraphCanvas();
        PointMouseListener pml = new PointMouseListener();
        canvas.addMouseListener(pml);
        canvas.addMouseMotionListener(pml);
        panel1.add(canvas);
        instr = new JLabel("click to add a new node");
        panel1.add(instr,BorderLayout.NORTH);
        pane.add(panel1);

        // controls
        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(2,1));
        JButton addPointButton = new JButton("Add Nodes");
        panel2.add(addPointButton);
        addPointButton.addActionListener(new AddPointListener());
        JButton rmvPointButton = new JButton("Remove Nodes");
        panel2.add(rmvPointButton);
        rmvPointButton.addActionListener(new RmvPointListener());
        JButton addEdgeButton = new JButton("Add Edges");
        panel2.add(addEdgeButton);
        addEdgeButton.addActionListener(new AddEdgeListener());
        JButton rmvEdgeButton = new JButton("Remove Edges");
        panel2.add(rmvEdgeButton);
        rmvEdgeButton.addActionListener(new RmvEdgeListener());



        pane.add(panel2);
    }

    /** 
     * Returns a point found within the drawing radius of the given location, 
     * or null if none
     *
     *  @param x  the x coordinate of the location
     *  @param y  the y coordinate of the location
     *  @return  a point from the canvas if there is one covering this location, 
     *  or a null reference if not
     */
    public Point findNearbyPoint(int x, int y) {

        for(Point nearbyPoint: canvas.points){
            double distance = nearbyPoint.distance((double) x, (double)y);
            if (distance <= 20){
                return nearbyPoint;
            }
        }
        

        return null; //if no point is returned
		// overlap with the location specified.  
    }

    /** Constaints for recording the input mode */
    enum InputMode {
        ADD_POINTS, RMV_POINTS, ADD_EDGE, RMV_EDGE
    }

    /** Listener for AddPoint button */
    private class AddPointListener implements ActionListener {
        /** Event handler for AddPoint button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.ADD_POINTS;
            instr.setText("Click to add a new node");
        }
    }

    /** Listener for RmvPoint button */
    private class RmvPointListener implements ActionListener {
        /** Event handler for RmvPoint button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.RMV_POINTS;
            instr.setText("Click a node to remove it");
        }
    }

    /** Listener for ADD_EDGE button */
    private class AddEdgeListener implements ActionListener {
        /** Event handler for ADD_EDGE button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.ADD_EDGE;
            instr.setText("Click on two nodes to add an edge between them");
        }
    }
    /** Listener for RMV_EDGE button */
    private class RmvEdgeListener implements ActionListener {
        /** Event handler for ADD_EDGE button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.RMV_EDGE;
            instr.setText("Click on two nodes to remove the edge between them");
        }
    }
    /** Mouse listener for PointCanvas element */
    private class PointMouseListener extends MouseAdapter
        implements MouseMotionListener {

        /** Responds to click event depending on mode */
        public void mouseClicked(MouseEvent e) {
            switch (mode) {
            case ADD_POINTS:
				// If the click is not on top of an existing point, create a new one and add it to the canvas.
                if(findNearbyPoint(e.getX(), e.getY()) == null){
                    Point newPoint = new Point(e.getX(), e.getY());
                    canvas.points.add(newPoint);
                    canvas.graph.addNode(newPoint); //add new point
                    canvas.graph.print(); //print graph 
                } else{

				// Otherwise, emit a beep, as shown below:
				Toolkit.getDefaultToolkit().beep();
            }
                break;

            case RMV_POINTS:
				// If the click is on top of an existing point, remove it from the canvas's list of points.
                pointUnderMouse = findNearbyPoint(e.getX(), e.getY());
                if(pointUnderMouse != null){

                    canvas.points.remove(pointUnderMouse);
                    Graph<Point,Double>.Node toRemove = canvas.getNode(pointUnderMouse); //get node to be removed
                    canvas.graph.removeNode(toRemove); //remove point
                } else{
                Toolkit.getDefaultToolkit().beep(); // Otherwise, emit a beep.
                }

                break;

            case ADD_EDGE:
                //if first click on a node, and second case on a node, add edge
                pointUnderMouse = findNearbyPoint(e.getX(), e.getY());
                if(pointUnderMouse != null){
                    if(firstEdgePoint != null){
                        Graph<Point,Double>.Node head = canvas.getNode(firstEdgePoint);
                        Graph<Point,Double>.Node tail = canvas.getNode(pointUnderMouse);
                        canvas.graph.addEdge((double)e.getX(), head, tail); //change to distance later
                        firstEdgePoint = null; //set back to null 
                    }
                    else{
                        firstEdgePoint = pointUnderMouse; //set first node clicked to first edge point
                    }

                    break; 

                }

                case RMV_EDGE:
                //if first click on a node, and second case on a node, remove edge
                pointUnderMouse = findNearbyPoint(e.getX(), e.getY());
                if(pointUnderMouse != null){
                    if(firstEdgePoint != null){
                        Graph<Point,Double>.Node head = canvas.getNode(firstEdgePoint);
                        Graph<Point,Double>.Node tail = canvas.getNode(pointUnderMouse);
                        canvas.graph.removeEdge(head, tail); 
                        firstEdgePoint = null; //set back to null 
                    }
                    else{
                        firstEdgePoint = pointUnderMouse; //set first node clicked to first edge point
                    }

                    break; 

                }

            }
            canvas.repaint(); //repaint the canvas with new updates
        }

        /** Records point under mousedown event in anticipation of possible drag */
        public void mousePressed(MouseEvent e) {
            pointUnderMouse = new Point(e.getX(), e.getY());

        }

        /** Responds to mouseup event */
        public void mouseReleased(MouseEvent e) {
            // FILL IN:  Clear record of point under mouse, if any
            if(pointUnderMouse != null){
                pointUnderMouse = null; 
            }  
        }

        // Empty but necessary to comply with MouseMotionListener interface.
        public void mouseDragged(MouseEvent e) {
        }

		// Empty but necessary to comply with MouseMotionListener interface.
        public void mouseMoved(MouseEvent e) {}
    }
}