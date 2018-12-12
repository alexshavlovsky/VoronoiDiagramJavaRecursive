package voronoi;

class DirectedEdge {
    Edge e;
    boolean fwd;
    int inf = 0;
    DirectedEdge opposite, pre, next;

    // directed edge constructor
    private DirectedEdge(Edge e, boolean fwd) {
        this.e = e;
        this.fwd = fwd;
    }

    // infinity constructor
    private DirectedEdge(int inf) {
        this.inf = inf;
    }

    // empty cell constructor
    // an empty is a self-linked positive infinity
    static DirectedEdge getEmptyCell() {
        DirectedEdge posInf = new DirectedEdge(1);
        posInf.next=posInf;
        posInf.pre=posInf;
        return posInf;
    }

    // negative infinity constructor
    static DirectedEdge getNegInfinity() {
        return new DirectedEdge(-1);
    }

    // joined edges constructor
    // every edge holds a pointer to a joined edge from a neighbour cell
    static DirectedEdge[] getJoinedEdges(Edge e) {
        DirectedEdge fwd = new DirectedEdge(e, true);
        DirectedEdge bwd = new DirectedEdge(e, false);
        fwd.opposite = bwd;
        bwd.opposite = fwd;
        return new DirectedEdge[]{fwd, bwd};
    }

}
