// helper node data type
public class Node<Value> {
    Double x, y;              // x- and y- coordinates
    Node NW, NE, SE, SW;   // four subtrees
    Value value;           // associated data

    Node(Double x, Double y, Value value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }
}