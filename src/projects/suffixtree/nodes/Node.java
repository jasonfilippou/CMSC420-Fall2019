package projects.suffixtree.nodes;

public abstract class Node{
    public  Node[] next;
    public int start;
    public Node(int start) {
        this.start = start;
        this.next = new Node[128];
    }
}
