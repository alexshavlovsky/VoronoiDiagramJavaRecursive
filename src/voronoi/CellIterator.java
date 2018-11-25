package voronoi;

import geometry.Point;

import static geometry.Utils.doLinesIntersect;

class CellIterator {
    private boolean clock;
    DirectedEdge de;

    CellIterator() {
        de = new DirectedEdge();
    }

    CellIterator(DirectedEdge e0) {
        de = new DirectedEdge(e0);
    }

    boolean trySearchOpenedEdge() {
        // TODO - determine the most effective cell traverse direction
        if (de.e == null) return true;
        DirectedEdge de0 = de;
        do de = clock ? de.next : de.pre; while (de.e != null && de != de0);
        return de.e == null;
    }

    CellIterator init(boolean clock) {
        this.clock = clock;
        if (!trySearchOpenedEdge()) throw new RuntimeException("Can't find an opened edge");
        return this;
    }

    CellIterator setDirAndResetToEdge(boolean clock, DirectedEdge e0) {
        this.clock = clock;
        de = e0;
        return this;
    }

    DirectedEdge cropCell(Edge e, DirectedEdge ins) {
        DirectedEdge pre = de;
        getNext();
        while (de != pre && notIntersect(e)) removeAndGetNext();
        //if (de==pre) System.out.println("!!!");
        DirectedEdge res = de;
        insert(ins);
        return res;
    }

    private boolean notIntersect(Edge e) {
        if (de.e == null && (de.pre.e == null || de.next.e == null)) return false;
        Point p1 = de.e == null ? (de.pre.fwd ? de.pre.e.i2 : de.pre.e.i1) : de.e.o1;
        Point p2 = de.e == null ? (de.next.fwd ? de.next.e.i1 : de.next.e.i2) : de.e.o2;
        boolean tmp = doLinesIntersect(e.o1, e.o2, p1, p2);
        return !tmp;
    }

    private void getNext() {
        if (clock) de = de.next;
        else de = de.pre;
    }

    private void removeAndGetNext() {
        DirectedEdge pre = de.pre;
        DirectedEdge next = de.next;
        if (clock) de = next;
        else de = pre;
        pre.next = next;
        next.pre = pre;
    }

    private void insert(DirectedEdge ins) {
        DirectedEdge pre = de.pre;
        DirectedEdge next = de.next;
        if (clock) {
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
        de = ins;
    }

    String print() {
        StringBuilder s = new StringBuilder();
        if (trySearchOpenedEdge()) s.append(" inf");
        s.append("\n");
        DirectedEdge n0 = de;
        DirectedEdge n = n0;
        do {
            if (n.e != null)
                if (n.fwd) s.append("\t").append(n.e.o1).append(" -> ").append(n.e.o2).append("\n");
                else s.append("\t").append(n.e.o2).append(" -> ").append(n.e.o1).append("\n");
            n = n.next;
        } while (n != n0);
        return s.toString();
    }

}
