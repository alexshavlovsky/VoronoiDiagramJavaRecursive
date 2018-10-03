package voronoy;

import gui.Canvas;

import static voronoy.Diagram.mergeDiagrams;


public class Voron {

    public static void main(String[] args) {
        Canvas paper = new Canvas(900, 675, 1);

        Diagram d1 = new Diagram(145, 475, 248, 209);
        Diagram d2 = new Diagram(289, 397, 442, 468);
        Diagram d = mergeDiagrams(d1, d2);

//        Diagram d3 = new Diagram(490,380);
//        Diagram d = mergeDiagrams(d1, d3);

        System.out.println(d);
        d.draw(paper);
//        System.out.println(d1.exportSites());
    }

}