package projects.suffixtree.nodes;

public class InnerNode extends Node{
    public int offset;
    public InnerNode(int start, int offset) {
        super(start);
        this.offset = offset;
    }
}
