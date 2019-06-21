package projects.suffixtree;

public abstract class Node{
    public  Node[] next;
    public int start;
    Node(int start) {
        this.start = start;
        this.next = new Node[128];
    }
}
