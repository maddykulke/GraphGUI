import java.util.*; 
import java.lang.*;


/**
 *  Implements a graph of node data V and edge data E
 *
 *  @author  Maddy Kulke
 *  @version CSC 212 final project, 12/14/17
 */

public class Graph <V,E> {


/** fields */ 

/** edges */ 
public ArrayList<Edge> edges; 

/** nodes */ 
public ArrayList<Node> nodes;


/** constructor creates new graph and initalizes empty linkedlists for edges and nodes*/ 
public Graph(Object V, Object E){
edges = new ArrayList<Edge>();
nodes = new ArrayList<Node>();
}


/**
 * Class Node
 * creates a node object with data type v 
 */


public class Node{

/** fields */ 

/** data */
private V data; 

/** list of edges*/ 
private HashSet<Edge> refLst;


/** constructor for class node*/ 
public Node(V data){
this.data = data;
refLst = new HashSet<Edge>();

}

/** methods */ 

/** 
* adds edge reference to node
*  @param edge an edge object 
*/
private void addEdgeRef(Edge edge){
refLst.add(edge);
}

/** 
* returns edge to a specified node, or null if there is none
* @param neighbor  a neighboring node 
* @return edge the edge between this node and neighbor 
*/ 
public Edge edgeTo(Node neighbor){
	Edge e = null; 
	for(Edge edge: refLst){
		if(edge.getHead() == neighbor || edge.getTail() == neighbor){
			e = edge; 
		}
	}
	return e;
}

/** Accessor for data*/ 
public V getData(){
return data; 
}


/** 
* returns a list of node's neighbors
* @return neighbors a linked list of neighbors 
*/ 
public LinkedList<Node> getNeighbors(){
	LinkedList<Node> neighbors = new LinkedList<Node>();
	for(Edge edge: refLst){
		if(edge.getHead() == this){
			neighbors.add(edge.getTail());
		}
		else if(edge.getTail() == this){
			neighbors.add(edge.getHead()); //will only return node once if self edge
		}
	}
	return neighbors;
}
/** 
* returns true if there is an edge to the node in question 
* @param node 
* @return boolean 
*
*/ 
public boolean isNeighbor(Node node){
	boolean b = false;
	if(refLst.size() != 0){
		b = true;
	} 
	return b;
}

/** removes an edge from the edge list*/ 
private void removeEdgeRef(Edge edge){
		refLst.remove(edge);		
}

/** manipulator for data*/ 
public void setData(V data){
	this.data = data; 
}

/** accessor for RefLst */ 
public HashSet<Edge> getRefLst(){
	return refLst;
}

	
} //end of class node 



/**
 * Class Edge
 * creates an edge object with data type E
 */

public class Edge{

/** fields */ 

/** data */ 
private E data;

/** head*/ 
private Node head;

/** tail */ 
private Node tail; 


/** constructor creates a new edge*/ 
public Edge(E data, Node head, Node tail){

	this.data = data;
	this.head = head;
	this.tail = tail;

}

/** methods */ 

/** 
* returns true if two edges are equal regardless of data they are carrying 
* @param object o 
* @return boolean 
*/
public boolean equals(Object o){
	 boolean result = false;
	   if (getClass() == o.getClass()) {
              @SuppressWarnings("unchecked")
                   Edge otherEdge = (Edge)o;
               		if((head == otherEdge.head && tail == otherEdge.tail) || (head == otherEdge.tail && tail == otherEdge.head)){
					result = true; 
					}
        }
            return result;

}


/**  Accessor for data*/
public E getData(){
	return data;
}
/**  Accessor for endpoint #1*/
public Node getHead(){
	return head; 
}

/** Accessor for endpoint #2*/
public Node getTail(){
	return tail;
}
/** Redefined hashcode to match redefined equals*/
public int hashCode(){
	return head.hashCode() * tail.hashCode();
}

/** Accessor for opposite node */
public Node OppositeTo(Node node){
if(head == node){
	return tail; 
}
else{
	return head; 
}

}

/**Manipulator for data */
public void setData(E data){
	this.data = data; 

}

}


/** methods*/ 

/** 
* adds a new edge 
* @param data the data of the edge 
* @param head the head node  
*@param tail the tail node 
* @return edge 
*/
public Edge addEdge(E data, Node head, Node tail) {
	Edge edge = new Edge(data, head, tail);
	edges.add(edge);
	head.addEdgeRef(edge);
	tail.addEdgeRef(edge);
	return edge;
}

/** 
* adds a new edge 
* @param data the data of the node  
* @return node 
*/
public Node addNode(V data) {
	Node node = new Node(data);
	nodes.add(node);
	return node;
}


/** 
* Breadth first traversal of graph 
* @param start the starting node   
* @return Hashset of edges traversed 
*/

public HashSet<Edge> BFT(Node start){
	LinkedList<Node> queue = new LinkedList<Node>();
	LinkedList<Node> visited = new LinkedList<Node>();
	HashSet<Edge> edgeSet = new HashSet<Edge>();
	Node currentNode = start; 

	queue.add(start);
	visited.add(currentNode);//add to visited
	//while empty, take first item out, get neighbors
	while(!queue.isEmpty()){
		currentNode = queue.remove(); //remove first
		LinkedList<Node> neighbors = currentNode.getNeighbors();//get neighbors
		for(Node neighbor: neighbors){ //loop through neighbors
			if(!visited.contains(neighbor)){ 
				queue.add(neighbor);
				visited.add(neighbor);
				if(currentNode.edgeTo(neighbor) != null){ 
					edgeSet.add(currentNode.edgeTo(neighbor)); //add to edgeSet
				}

			}
		}
	}
	return edgeSet;
}


/** 
* checks consistency of graph 
*/
public void check(){

	for (Node node: nodes){
		HashSet<Edge> refLst = node.getRefLst();
		for (Edge edge: refLst){
			Node refHead = edge.getHead();
			Node refTail = edge.getTail();
			if(!edges.contains(edge)){
				System.out.println("edge missing from master list");
			}
			if (!nodes.contains(refHead) || (!nodes.contains(refTail))){
				System.out.println("head or tail not in node list");
			}
		}
	}	
}

/** 
* Depth first traversal of graph (entry interface)
* @param start the starting node   
* @return Hashset of edges traversed 
*/

public HashSet<Edge> DFT(Node start){

	HashSet<Edge> traversedEdges = new HashSet<Edge>(); //to be returned 
	HashSet<Node> traversedNodes = new HashSet<Node>();
	HashSet<Edge> traverse = DFTRecurse(start, traversedEdges, traversedNodes);

	return traverse; 

}

/** 
* recursive method for DFT
* @param start the starting node   
* @param traversedEdges Hashset for edges traversed
* @return traversedNodes Hashset for nodes traversed 
*/

public HashSet<Edge> DFTRecurse(Node start, HashSet<Edge> traversedEdges, HashSet<Node> traversedNodes){


	if(!traversedNodes.contains(start)){
		for(Node neighbor: start.getNeighbors()){
			traversedEdges.add(start.edgeTo(neighbor)); 
			traversedNodes.add(start);
			DFTRecurse(neighbor, traversedEdges, traversedNodes);


		}
	}
	return traversedEdges; 
}

	
/**
*Dijkstra's shortest-path algorithm to compute distances to nodes 
* @param start the starting node   
* @return distances array of disstances from start to each node
*/ 
public Double[] distances(Node start){

	Double[] distances = new Double[nodes.size()];
	HashSet<Node> visited = new HashSet<Node>(); 
	HashMap<Node, Double> distanceTable = new HashMap< Node, Double>();
	ArrayList<Node> unvisited = new ArrayList<Node>(); 
	Node currentNode = start;
	Double minimum = Double.POSITIVE_INFINITY;

	//set start node to 0 and rest to infinity in distanceTable
	for(Node node: nodes){
		unvisited.add(node); // add all nodes to unvisited 
		if (node == start){
			distanceTable.put(node, 0.0);
		} else{
			distanceTable.put(node, Double.POSITIVE_INFINITY);
		}
	}
	//while not all nodes have been visitedd
	while (unvisited.size() > 0){

		visited.add(currentNode); //add to visited 
		unvisited.remove(currentNode); //remove from unvisited 

		for(Node neighbor: currentNode.getNeighbors()){ //get neighbors
			Edge currentEdge = currentNode.edgeTo(neighbor); //get edge between currentNode and neighbor 
			Double currentEdgeCost = (double)currentEdge.getData(); //get cost of edge 
			Double currentNodeCost = distanceTable.get(currentNode); //get cost of current node
			Double neigbhorCost = distanceTable.get(neighbor); //current cost of neighbor 
			Double newCost = currentNodeCost + currentEdgeCost; //new cost
			if(newCost < neigbhorCost){//check if traveling through currentNode cheaper for neigbhor 
				distanceTable.replace(neighbor, newCost); //if so, replace neighbor's value in distance table with new cost 
			}
		}

		for(Node node: unvisited){ //find minimum node in unvisited
			double nodeCost = distanceTable.get(node);
			if(nodeCost < minimum){
				minimum = nodeCost;
				currentNode = node; 
			}
		}

		for(int i = 0; i< distances.length; i++){
			distances[i] = distanceTable.get(nodes.get(i));
		}
		
		return distances;
	}

	return distances;

}


/** 
* returns endpoints of edges    
* @param edges HashSet of edges
* @return hashset of nodes 
*/
public HashSet<Node> endpoints(HashSet<Edge> edges){

	HashSet<Node> endpoints = new HashSet<Node>();
	for(Edge edge: edges){
		endpoints.add(edge.getHead());
		endpoints.add(edge.getTail());

	}
	return endpoints;
}
/**  Accessor for edges*/ 
public Edge getEdge(int i){
	return edges.get(i);
}
/**  Accessor for specific edge*/ 
public Edge getEdgeRef(Node head, Node tail){
	Edge e = null; 
	for(Edge edge: edges){
		if((edge.getHead() == head) && (edge.getTail() == tail)){
			e = edge;
		}
	}
	return e; 
}
/** accessor for nodes */ 
public Node getNode(int i){
	return nodes.get(i); 
}
/** Accessor for number of edges*/ 
public int numEdges(){
	return edges.size();
}
/**Accessor for number of nodes */ 
public int numNodes(){
	return nodes.size();
}
/** Returns nodes not on a given list */ 
public HashSet otherNodes(HashSet<Node> group){
	HashSet<Node> otherNodes = new HashSet<Node>();
	return otherNodes; 
}


/** 
* prints representation of graph 
*/ 
public void print(){

	System.out.println("** Nodes **");
	for(Node node: nodes){
		System.out.println("	Node: "+node.getData());
		for(Edge ref: node.getRefLst()){
			System.out.println("		Edge reference: "+ref.getData()); 
		}	
	}
	System.out.println("** Edges **");
	for(Edge edge: edges){
		System.out.println("	Edge: "+edge.getData());
		System.out.println("		Head: "+edge.getHead().getData());
		System.out.println("		Tail: "+edge.getTail().getData());
	}
}

/** 
*Removes an edge
*@param edge 
*/ 
public void removeEdge(Edge edge){
	edge.getHead().removeEdgeRef(edge);
	edge.getTail().removeEdgeRef(edge);
	for(int i = 0; i < edges.size(); i++){
		if (edges.get(i) == edge){
			edges.remove(i);
			
		}
	}
}

/** 
*Removes an edge
*@param head
*@param tail  
*/ 
public void removeEdge(Node head, Node tail){
	Edge edge = null; 
	for(int i = 0; i < edges.size(); i++){ //finds edge in list of edges
		if (((edges.get(i).getHead() == head )&& (edges.get(i).getTail() == tail)) ||
			(edges.get(i).getTail() == head) && (edges.get(i).getHead() == tail)){
			edge = edges.get(i);
		}
	}
	removeEdge(edge); //calls other method 
}

/** 
*Removes a node
*@param node
*/ 
public void removeNode(Node node){
		
		if (nodes.contains(node)){
			while(!node.getRefLst().isEmpty()){
				Iterator<Edge> hashIterator = node.refLst.iterator();
				removeEdge(hashIterator.next());
		 	
		 }
			nodes.remove(node);
		}
		
		}
	



} //end of class graph 

