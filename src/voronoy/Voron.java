package voronoy;

import geometry.Point;
import geometry.Utils;

import static geometry.Utils.paper;


public class Voron {

    public static void main(String[] args) throws Exception {

        Diagram d1 = new Diagram(new Point(0, 0));
        d1 = Diagram.mergeDiagrams(d1, new Diagram(new Point(1, 0)));
        d1 = Diagram.mergeDiagrams(d1,new Diagram(new Point(1.5, 1)));

        d1 = Diagram.mergeDiagrams(d1,new Diagram(new Point(2, -1.2)));
//    Utils.debugFlag = true;
        d1 = Diagram.mergeDiagrams(d1,new Diagram(new Point(2.5, 1.5)));
//        Utils.debugFlag = true;
        d1 = Diagram.mergeDiagrams(d1,new Diagram(new Point(3, 0.8)));
        d1 = Diagram.mergeDiagrams(d1,new Diagram(new Point(3.1, -1.6)));
        d1 = Diagram.mergeDiagrams(d1,new Diagram(new Point(3.5, 0.5)));
       // Utils.debugFlag = true;
        d1 = Diagram.mergeDiagrams(d1,new Diagram(new Point(3.6, -0.8)));
        d1 = Diagram.mergeDiagrams(d1,new Diagram(new Point(3.8, -1.5)));
        d1 = Diagram.mergeDiagrams(d1,new Diagram(new Point(4, 2)));
        d1 = Diagram.mergeDiagrams(d1,new Diagram(new Point(4.2, 0.5)));
        d1 = Diagram.mergeDiagrams(new Diagram(new Point(-0.5, -0.9)),d1);
        Utils.debugFlag = true;
        d1 = Diagram.mergeDiagrams(new Diagram(new Point(-0.8, 1)),d1);

        d1.draw(paper);
    }

}