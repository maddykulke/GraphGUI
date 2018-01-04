import java.util.*;
import java.awt.*;
import javax.swing.*;        

/**
 *  Implements a graphical canvas that displays a list of points.
 *
 *  @author  Maddy Kulke, Nicholas R. Howe
 *  @version CSC 112, 18 April 2006
 */
class GraphCanvas extends JComponent {
    /** The points */
    LinkedList<Point> points;

    /** graph */ 
    Graph<Point,Double> graph; 

    /** Constructor */
    public GraphCanvas() {
        points = new LinkedList<Point>();
        graph = new Graph<Point, Double>(3, 4.0);
        setMinimumSize(new Dimension(500,300));
        setPreferredSize(new Dimension(500,300));
    }

    /** 
     * Returns a point in the points list matching the data of the node 
     *
     *  @param node a pre-exisitng node 
     *  @return  a point matching the node's data  
     *  or null if no point matches 
     */

    public Point getPoint(Graph<Point, Double>.Node node){
        Point p = null; 
            for (Point point: points){
                if(node.getData() == point){
                    p = point; 
                }
            }
        return p; 
    }


    /** 
     * Returns a node from graph whose data matches the given point  
     *
     *  @param point a pre-exisitng node 
     *  @return  matching node  
     *  or a null if none 
     */
    public Graph<Point,Double>.Node getNode(Point point){
        Graph<Point,Double>.Node n = null; 
            for (Graph<Point,Double>.Node node: graph.nodes){
                if(node.getData() == point){
                    n = node; 
                }
            }
        return n; 
    }

    /**
     *  Paints a circle ten pixels in diameter at each point
     *  and a line from the head to the tail of each edge.
     *
     *  @param g The graphics object to draw with
     */

    public void paintComponent(Graphics g) {
        for(Point point: points){
            g.fillOval((int)point.getX() -5, (int)point.getY() -5, 10, 10);
        }
        for (Graph<Point,Double>.Edge edge: graph.edges){
                Graph<Point,Double>.Node head = edge.getHead(); 
                Graph<Point,Double>.Node tail = edge.getTail();

            Point headPoint = getPoint(head); //get point matching node from points list
            Point tailPoint = getPoint(tail);

            int x1 = (int)headPoint.getX();
            int x2 = (int)headPoint.getY();
            int y1 = (int)tailPoint.getX();
            int y2 = (int)tailPoint.getY();
            g.drawLine(x1, x2, y1, y2); //draw line beginning at (x1, y1) and ending at (x2,y2)
        }
    }
}
