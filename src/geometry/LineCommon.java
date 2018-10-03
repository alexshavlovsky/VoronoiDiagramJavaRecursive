package geometry;

public class LineCommon {

    protected double A, B, C;

    public LineCommon(double a, double b, double c) {
        this(a, b);
        C = c;
    }

    public LineCommon(double a, double b) {
        A = a;
        B = b;
    }

    @Override
    public String toString() {
        return "{" +
                "A=" + A +
                ", B=" + B +
                ", C=" + C +
                '}';
    }

}
