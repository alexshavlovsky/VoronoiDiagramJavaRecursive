package voronoy;

import gui.Canvas;

import static voronoy.Diagram.mergeDiagrams;


public class Voron {

    public static void main(String[] args) {
        Canvas paper = new Canvas(900, 675, 1);

        Diagram d = new Diagram(145, 475);
        d = mergeDiagrams(d, new Diagram(248, 209));
        d = mergeDiagrams(d, new Diagram(400, 300));
        d = mergeDiagrams(d, new Diagram(401, 320));
        d = mergeDiagrams(d, new Diagram(430, 300));
        d = mergeDiagrams(d, new Diagram(430, 320));
        d = mergeDiagrams(d, new Diagram(440, 100));
        d = mergeDiagrams(d, new Diagram(450, 500));

        Diagram d4 = new Diagram(500, 500);
        d4 = mergeDiagrams(d4, new Diagram(540, 100));
        d4 = mergeDiagrams(d4, new Diagram(543,140));
        d4 = mergeDiagrams(d4, new Diagram(550,170));
        d4 = mergeDiagrams(d4, new Diagram(585,189));
        d4 = mergeDiagrams(d4, new Diagram(590,195));
        d4 = mergeDiagrams(d4, new Diagram(610,200));
        d4 = mergeDiagrams(d4, new Diagram(615,140));
        d4 = mergeDiagrams(d4, new Diagram(630,500));

        d=mergeDiagrams(d,d4);
        System.out.println(d);
        d.draw(paper);
        System.out.println(d.exportSites());
    }

}