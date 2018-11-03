package voronoy;

public class DirectedEdge {
    Edge e;
    boolean fwd;
    boolean infinite;
    public DirectedEdge opposite, pre, next;

    DirectedEdge(Edge e, boolean fwd) {
        this.e = e;
        this.fwd = fwd;
    }

    DirectedEdge() {
        infinite = true;
    }


    public static DirectedEdge[] getPairFromEdge(Edge e) {
        DirectedEdge fwd = new DirectedEdge(e, true);
        DirectedEdge bwd = new DirectedEdge(e, false);
        fwd.opposite = bwd;
        bwd.opposite = fwd;
        return new DirectedEdge[]{fwd, bwd};
    }

    @Override
    public String toString() {
        return "DirectedEdge{" +
                "e=" + e +
                ", fwd=" + fwd +
                ", infinite=" + infinite +
                '}';
    }
}
