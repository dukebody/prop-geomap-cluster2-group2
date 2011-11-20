/*************************************************************************
 *  Compilation:  javac QuadTree.java
 *  Execution:    java QuadTree M N
 *
 *  Quad tree.
 * 
 *************************************************************************/

import java.util.*;


public class QuadTree<Key extends Comparable<Key>, Value>  {
    private Node root;

    // helper node data type
    public class Node {
        Key x, y;              // x- and y- coordinates
        Node NW, NE, SE, SW;   // four subtrees
        Value value;           // associated data

        Node(Key x, Key y, Value value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }
    }


  /***********************************************************************
    *  Insert (x, y) into appropriate quadrant
    ***********************************************************************/
    public void insert(Key x, Key y, Value value) {
        root = insert(root, x, y, value);
    }

    private Node insert(Node h, Key x, Key y, Value value) {
        if (h == null) return new Node(x, y, value);
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
    public void remove(Key x, Key y) {
        root = remove(root, x, y);
    }

    public Node remove(Node h, Key x, Key y) {
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

    public Node query(Key x, Key y) {
        return query(root, x, y);
    }

    private Node query(Node h, Key x, Key y) {
        if (h == null) 
            return null;  // the node doesn't exist
        else if (eq(x, h.x) && eq(y, h.y)) {
            return h;  // node found!
        }
        else if ( less(x, h.x) &&  less(y, h.y)) h.SW = query(h.SW, x, y);
        else if ( less(x, h.x) && !less(y, h.y)) h.NW = query(h.NW, x, y);
        else if (!less(x, h.x) &&  less(y, h.y)) h.SE = query(h.SE, x, y);
        else if (!less(x, h.x) && !less(y, h.y)) h.NE = query(h.NE, x, y);
        return h;
    }

  /***********************************************************************
    *  Range search.
    ***********************************************************************/

    public ArrayList<Node> query2D(Interval2D<Key> rect) {
        ArrayList<Node> foundNodes = new ArrayList<Node>();
        return query2D(root, rect, foundNodes);
    }

    private ArrayList<Node> query2D(Node h, Interval2D<Key> rect, ArrayList<Node> foundNodes) {
        if (h == null) return foundNodes;
        Key xmin = rect.intervalX.low;
        Key ymin = rect.intervalY.low;
        Key xmax = rect.intervalX.high;
        Key ymax = rect.intervalY.high;
        if (rect.contains(h.x, h.y))
            foundNodes.add(h);
            //System.out.println("    (" + h.x + ", " + h.y + ") " + h.value);
        if ( less(xmin, h.x) &&  less(ymin, h.y)) query2D(h.SW, rect, foundNodes);
        if ( less(xmin, h.x) && !less(ymax, h.y)) query2D(h.NW, rect, foundNodes);
        if (!less(xmax, h.x) &&  less(ymin, h.y)) query2D(h.SE, rect, foundNodes);
        if (!less(xmax, h.x) && !less(ymax, h.y)) query2D(h.NE, rect, foundNodes);

        return foundNodes;
    }


   /*************************************************************************
    *  helper comparison functions
    *************************************************************************/

    private boolean less(Key k1, Key k2) { return k1.compareTo(k2) <  0; }
    private boolean eq  (Key k1, Key k2) { return k1.compareTo(k2) == 0; }


   /*************************************************************************
    *  test client
    *************************************************************************/
    public static void main(String[] args) {
        int M = Integer.parseInt(args[0]);   // queries
        int N = Integer.parseInt(args[1]);   // points

        QuadTree<Integer, String> st = new QuadTree<Integer, String>();

        // insert N random points in the unit square
        for (int i = 0; i < N; i++) {
            Integer x = (int) (100 * Math.random());
            Integer y = (int) (100 * Math.random());
            // System.out.println("(" + x + ", " + y + ")");
            st.insert(x, y, "P" + i);
        }
        System.out.println("Done preprocessing " + N + " points");

        // do some range searches
        for (int i = 0; i < M; i++) {
            Integer xmin = (int) (100 * Math.random());
            Integer ymin = (int) (100 * Math.random());
            Integer xmax = xmin + (int) (10 * Math.random());
            Integer ymax = ymin + (int) (20 * Math.random());
            Interval<Integer> intX = new Interval<Integer>(xmin, xmax);
            Interval<Integer> intY = new Interval<Integer>(ymin, ymax);
            Interval2D<Integer> rect = new Interval2D<Integer>(intX, intY);
            System.out.println(rect + " : ");
            st.query2D(rect);
        }
    }

}
