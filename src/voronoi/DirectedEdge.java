package voronoi;

class DirectedEdge {
    Edge e;
    boolean fwd;
    DirectedEdge opposite, pre, next;

    private DirectedEdge(Edge e, boolean fwd) {
        this.e = e;
        this.fwd = fwd;
    }

    // empty cell constructor
    DirectedEdge() {
        this.next = this;
        this.pre = this;
    }

    // half plane constructor
    DirectedEdge(DirectedEdge de) {
        this.pre = de;
        this.next = de;
        de.pre = this;
        de.next = this;
    }

    // joined edges constructor
    static DirectedEdge[] getJoinedEdges(Edge e) {
        DirectedEdge fwd = new DirectedEdge(e, true);
        DirectedEdge bwd = new DirectedEdge(e, false);
        fwd.opposite = bwd;
        bwd.opposite = fwd;
        return new DirectedEdge[]{fwd, bwd};
    }

}
