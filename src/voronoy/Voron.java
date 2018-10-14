package voronoy;

import gui.Canvas;

import static voronoy.Diagram.mergeDiagrams;


public class Voron {

    public static void main(String[] args) {
        Canvas paper = new Canvas(900, 675, 1);

        Diagram d = new Diagram(145, 475);
        Diagram d2 = new Diagram(248, 209);
        Diagram d3 = new Diagram(400, 300);

        Diagram d4 = new Diagram(500, 500);
        Diagram d5 = new Diagram(540, 100);
        Diagram d6 = new Diagram(543,140);
        Diagram d7 = new Diagram(550,170);
        Diagram d8 = new Diagram(585,189);
        Diagram d9 = new Diagram(636,395);

        d = mergeDiagrams(d, d2, null);
        d = mergeDiagrams(d, d3, null);

       d4 = mergeDiagrams(d4, d5, null);
        d4 = mergeDiagrams(d4, d6, null);
        d4 = mergeDiagrams(d4, d7, null);
        d4 = mergeDiagrams(d4, d8, null);
        d4 = mergeDiagrams(d4, d9, null);

        d=mergeDiagrams(d,d4,null);
        System.out.println(d);
        d.draw(paper);
        System.out.println(d.exportSites());
    }

}