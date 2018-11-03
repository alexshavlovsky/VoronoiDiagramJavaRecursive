package voronoy;

import geometry.Point;

import static geometry.Utils.doLinesIntersect;

public class CellIterator {
    private boolean clock;
    public DirectedEdge de;
    private DirectedEdge originEdge;

    public CellIterator() {
        de = new DirectedEdge();
        de.next = de;
        de.pre = de;
    }

    public CellIterator(DirectedEdge e0) {
        de = e0;
        DirectedEdge inf = new DirectedEdge();
        inf.pre = e0;
        inf.next = e0;
        de.pre = inf;
        de.next = inf;
    }

    public boolean tryFindInfiniteEdge() {
        // TODO - determine the most effective cell traverse direction
        if (de.infinite) return true;
        DirectedEdge de0 = de;
        do de = clock ? de.next : de.pre; while (!de.infinite && de != de0);
        return de.infinite;
    }

    public void ResetToInfiniteEdge(boolean clock) {
        this.clock = clock;
        if (!tryFindInfiniteEdge()) throw new AssertionError();
        originEdge = de;
    }

    public boolean notIntersect(Edge e) {
        Point p1 = de.infinite ? (de.pre.fwd ? de.pre.e.i2 : de.pre.e.i1) : de.e.o1;
        Point p2 = de.infinite ? (de.next.fwd ? de.next.e.i1 : de.next.e.i2) : de.e.o2;
        boolean tmp = doLinesIntersect(e.o1, e.o2, p1, p2);
        return !tmp;
    }

    public void reduceCell(Edge e) {
        if (de == originEdge) getNext();
        if (de == originEdge) return;
        while (notIntersect(e))
            removeAndGetNext();
    }

    public void setOriginEdge(DirectedEdge de0) {
        originEdge = de0;
    }

    public void setDirAndResetToEdge(boolean clock, DirectedEdge e0) {
        this.clock = clock;
        de = e0;
        originEdge = e0;
    }

    void getNext() {
        if (clock) de = de.next;
        else de = de.pre;
    }

    void removeAndGetNext() {
        DirectedEdge pre = de.pre;
        DirectedEdge next = de.next;
        if (clock) de = next;
        else de = pre;
        pre.next = next;
        next.pre = pre;
    }

    void insert(DirectedEdge ins, boolean before, boolean setPointer) {
        DirectedEdge pre = de.pre;
        DirectedEdge next = de.next;
        if (before ^ !clock) {
            ins.next = de;
            ins.pre = pre;
            de.pre = ins;
            pre.next = ins;
        } else {
            ins.pre = de;
            ins.next = next;
            de.next = ins;
            next.pre = ins;
        }
        if (setPointer) de = ins;
    }


    String print() {
        StringBuilder s = new StringBuilder();
        if (tryFindInfiniteEdge()) s.append(" inf");
        s.append("\n");
     //   s.append("\n");
        DirectedEdge n0 = de;
        DirectedEdge n = n0;
        do {
            if (!n.infinite)
                if (n.fwd) s.append("\t").append(n.e.o1).append(" -> ").append(n.e.o2).append("\n");
                else s.append("\t").append(n.e.o2).append(" -> ").append(n.e.o1).append("\n");
            n = n.next;
        } while (n != n0);
        return s.toString();
    }
}
