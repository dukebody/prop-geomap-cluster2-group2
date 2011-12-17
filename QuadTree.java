/*
Author: Israel Saeta Pérez
*/

import java.util.*;


/**
This is a geometric high-efficiency data structure for storing objects
indexed by 2D-position. Check https://en.wikipedia.org/wiki/Quadtree for
more info.

The implementation is based on
http://algs4.cs.princeton.edu/92search/QuadTree.java.html
but has been adapted, enlarged and tested to serve our specific proposals.
*/
public class QuadTree<Value>  {
    private Node<Value> root;

    /**
    Check if the QuadTree is empty.

    @return True if it's empty, false otherwise.
    */
    public boolean isEmpty() {
        return (root == null);
    }

  /***********************************************************************
    *  Insert (x, y) into appropriate quadrant
    ***********************************************************************/

    /**
    Insert a node at the specified position.

    If there was already a node there, don't insert anything and fail silently.

    @param value Object to be attached to the node at that position.
    */
    public void insert(Double x, Double y, Value value) {
        root = insert(root, x, y, value);
    }

    private Node<Value> insert(Node<Value> h, Double x, Double y, Value value) {
        if (h == null) return new Node<Value>(x, y, value);
        //// if (eq(x, h.x) && eq(y, h.y)) h.value = value;  // duplicate
        else if ( less(x, h.x) &&  less(y, h.y)) h.SW = insert(h.SW, x, y, value);
        else if ( less(x, h.x) && !less(y, h.y)) h.NW = insert(h.NW, x, y, value);
        else if (!less(x, h.x) &&  less(y, h.y)) h.SE = insert(h.SE, x, y, value);
        else if (!less(x, h.x) && !less(y, h.y)) h.NE = insert(h.NE, x, y, value);
        return h;
    }

  /***********************************************************************
    *  Remove (x, y) from appropriate quadrant
    ***********************************************************************/

    /**
    Remove the node present at (x,y) if present.

    If there isn't any node at that position, do nothing.
    */
    public void remove(Double x, Double y) {
        root = remove(root, x, y);
    }

    private Node<Value> remove(Node<Value> h, Double x, Double y) {
        if (h == null) 
            return null;  // the node doesn't exist, we don't do anything
        else if (eq(x, h.x) && eq(y, h.y)) {
            return null;  // delete found node
        }
        else if ( less(x, h.x) &&  less(y, h.y)) h.SW = remove(h.SW, x, y);
        else if ( less(x, h.x) && !less(y, h.y)) h.NW = remove(h.NW, x, y);
        else if (!less(x, h.x) &&  less(y, h.y)) h.SE = remove(h.SE, x, y);
        else if (!less(x, h.x) && !less(y, h.y)) h.NE = remove(h.NE, x, y);
        return h;
    }

  /***********************************************************************
    *  Exact point search
    ***********************************************************************/

    /**
    Get the node at the specified (x,y) coordinates.

    @return The node at the specified position, or null if it can't be found.
    */
    public Node query(Double x, Double y) {
        Interval<Double> intX = new Interval<Double>(x, x);
        Interval<Double> intY = new Interval<Double>(y, y);
        Interval2D<Double> rect = new Interval2D<Double>(intX, intY);
        ArrayList<Node<Value>> nodes = query2D(rect);
        if (!nodes.isEmpty()) {
            return nodes.get(0);
        }
        return null;
    }


  /***********************************************************************
    *  Range search.
    ***********************************************************************/

    /**
    Get the nodes inside (or at the border of) the specified rectangle.

    @param rect Rectangle to be searched inside.

    @return List of nodes found into the specified rectangle.
    */
    public ArrayList<Node<Value>> query2D(Interval2D<Double> rect) {
        ArrayList<Node<Value>> foundNodes = new ArrayList<Node<Value>>();
        return query2D(root, rect, foundNodes);
    }

    private ArrayList<Node<Value>> query2D(Node<Value> h, Interval2D<Double> rect, ArrayList<Node<Value>> foundNodes) {
        if (h == null) return foundNodes;
        Double xmin = rect.intervalX.low;
        Double ymin = rect.intervalY.low;
        Double xmax = rect.intervalX.high;
        Double ymax = rect.intervalY.high;
        if (rect.contains(h.x, h.y))
            foundNodes.add(h);
            //System.out.println("    (" + h.x + ", " + h.y + ") " + h.value);
        if ( less(xmin, h.x) &&  less(ymin, h.y)) query2D(h.SW, rect, foundNodes);
        if ( less(xmin, h.x) && !less(ymax, h.y)) query2D(h.NW, rect, foundNodes);
        if (!less(xmax, h.x) &&  less(ymin, h.y)) query2D(h.SE, rect, foundNodes);
        if (!less(xmax, h.x) && !less(ymax, h.y)) query2D(h.NE, rect, foundNodes);

        return foundNodes;
    }


  /***********************************************************************
    *  Closer nodes search.
    ***********************************************************************/

    /**
    Get the nodes closer to the specified location, searching
    in rectangles of doubling width and height (*4 area).
    
    Start with width 1, height 1.
    */
    public ArrayList<Node<Value>> getCloserNodes(Double x, Double y) {

        
        return getCloserNodes(x, y, 1.0, 1.0);
    }

    /**
    Get the nodes closer to the specified location, searching
    in rectangles of doubling width and height (*4 area).
    
    Start with specified width and height.
    */
    public ArrayList<Node<Value>> getCloserNodes(Double x, Double y, Double width, Double height) {
        ArrayList<Node<Value>> closerPoints;
        Interval<Double> intX = new Interval<Double>(x-width, x+width);
        Interval<Double> intY = new Interval<Double>(y-height, y+height);
        Interval2D<Double> rect = new Interval2D<Double>(intX, intY);
        closerPoints = query2D(rect);
        if (!closerPoints.isEmpty()) {
            return closerPoints;
        }
        return getCloserNodes(x, y, width*2, height*2);
    }

   /*************************************************************************
    *  helper comparison functions
    *************************************************************************/

    private boolean less(Double k1, Double k2) { return k1.compareTo(k2) <  0; }
    private boolean eq  (Double k1, Double k2) { return k1.compareTo(k2) == 0; }

}
