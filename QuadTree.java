/*************************************************************************
 *  Compilation:  javac QuadTree.java
 *  Execution:    java QuadTree M N
 *
 *  Quad tree.
 * 
 *************************************************************************/

import java.util.*;


public class QuadTree<Value>  {
    private Node<Value> root;


  /***********************************************************************
    *  Insert (x, y) into appropriate quadrant
    ***********************************************************************/
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
    public void remove(Double x, Double y) {
        root = remove(root, x, y);
    }

    public Node<Value> remove(Node<Value> h, Double x, Double y) {
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

    public ArrayList<Node<Value>> getCloserNodes(Double x, Double y) {
        // return the nodes closer to the specified location, searching
        // in rectangles of doubling width and height (*4 area)
        // Starts with width 1, height 1
        
        return getCloserNodes(x, y, 1.0, 1.0);
    }

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
