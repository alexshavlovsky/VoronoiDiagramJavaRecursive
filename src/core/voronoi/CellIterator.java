package core.voronoi;

import core.geometry.Line2D;

import static core.geometry.Utils.doLinesIntersect;

class CellIterator {
    private boolean clock;
    private DirectedEdge de;

    CellIterator(DirectedEdge de, boolean clock) {
        this.clock = clock;
        this.de = de;
    }

    DirectedEdge getAnyOpenedEdge() {
        DirectedEdge tmp = de;
        DirectedEdge res = trySearchAnyInfEdge() ? de : tmp;
        de = tmp;
        return res;
    }

    private boolean trySearchAnyInfEdge() {
        if (de.inf != 0) return true;
        DirectedEdge de0 = de;
        int c = 0;
        do {
            if (c++ == 1000) throw new RuntimeException("AnyInf edge search overflow");
            de = clock ? de.next : de.pre;
        } while (de.inf == 0 && de != de0);
        return de.inf != 0;
    }

    private boolean trySearchPosInfEdge() {
        if (de.inf == 1) return true;
        DirectedEdge de0 = de;
        int c = 0;
        do {
            if (c++ == 1000) throw new RuntimeException("PosInf edge search overflow");
            de = clock ? de.next : de.pre;
        } while (de.inf != 1 && de != de0);
        return de.inf == 1;
    }

    CellIterator init(boolean clock) {
        this.clock = clock;
        if (!trySearchPosInfEdge()) throw new RuntimeException("Can't find an opened edge");
        return this;
    }

    CellIterator setDirAndResetToEdge(boolean clock, DirectedEdge e0) {
        this.clock = clock;
        de = e0;
        return this;
    }

    DirectedEdge cropCell(Edge ray, DirectedEdge ins) {
        getNext();
        int c = 0;
        if (ray.getIntersection(de) == null) insert(DirectedEdge.getNegInfinity());
        else
            while (notIntersect(ray) && removeAndGetNext())
                if (c++ == 1000) throw new RuntimeException("Cell crop overflow");
        DirectedEdge res = de;
        insert(ins);
        return res;
    }

    private Line2D toLine() {
        return new Line2D(
                de.inf != 0 ? (de.pre.fwd ? de.pre.e.i[1] : de.pre.e.i[0]) : de.e.o1,
                de.inf != 0 ? (de.next.fwd ? de.next.e.i[0] : de.next.e.i[1]) : de.e.o2);
    }

    private boolean notIntersect(Edge ray) {
        if (de.inf != 0 && (de.next.inf != 0 || de.pre.inf != 0)) return false;
        Line2D e = toLine();
        return !doLinesIntersect(e.p1, e.p2, ray.i[0], ray.i[1]);
    }

    private void getNext() {
        if (clock) de = de.next;
        else de = de.pre;
    }

    private boolean removeAndGetNext() {
        if (de.pre == de && de.next == de) return false;
        DirectedEdge pre = de.pre;
        DirectedEdge next = de.next;
        if (clock) de = next;
        else de = pre;
        pre.next = next;
        next.pre = pre;
        return true;
    }

    void removeLast() {
        DirectedEdge pre = de.pre;
        DirectedEdge next = de.next;
        if (clock) de = pre;
        else de = next;
        pre.next = next;
        next.pre = pre;
    }

    void insert(DirectedEdge ins) {
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

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        DirectedEdge n0 = getAnyOpenedEdge();
        s.append("\n");
        DirectedEdge n = n0;
        int c = 0;
        do {
            if (c++ == 1000) throw new RuntimeException("Cell to string overflow");
            if (n == de) s.append(clock ? ">>>" : "<<<");
            if (n.inf != 0) s.append("\t").append(n.inf == 1 ? "+inf" : "-inf").append("\n");
            else if (n.fwd) s.append("\t").append(n.e.o1).append(" -> ").append(n.e.o2).append("\n");
            else s.append("\t").append(n.e.o2).append(" -> ").append(n.e.o1).append("\n");
            n = n.next;
        } while (n != n0);
        return s.toString();
    }

}
