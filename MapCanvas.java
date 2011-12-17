import java.awt.*;
import java.awt.geom.Line2D;

import java.util.*;

/**
 *
 * @author javier
 */

public class MapCanvas extends Canvas {

    /**
     * 
     */
    private static final long serialVersionUID = 1390605443082598731L;
    private static int gapX = 30;
    private static int gapY = 30;
    private static int boardSize = 200;
    private int sep;
    private boolean init = false;
    ArrayList<ArrayList<Double[]>> allPoints;
    private Graphics g2;


    @Override
    public void paint(Graphics g) {

        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        Double scale = 3.0;
        Double xoffset = 600.0;
        Double yoffset = 300.0;

        ArrayList<ArrayList<Double[]>> allPoints = getPoints();
        for (ArrayList<Double[]> zonePoints: allPoints) {
            for (int i = 0; i < zonePoints.size() - 1; i++) {
                Double[] p1 = zonePoints.get(i);
                Double[] p2 = zonePoints.get(i+1);
                g2.draw(new Line2D.Double(xoffset+p1[0]*scale, yoffset-p1[1]*scale, 
                    xoffset+p2[0]*scale, yoffset-p2[1]*scale));

            }
            int s = zonePoints.size();
            g2.draw(new Line2D.Double(xoffset+zonePoints.get(s-1)[0]*scale, yoffset-zonePoints.get(s-1)[1]*scale, 
                    xoffset+zonePoints.get(0)[0]*scale, yoffset-zonePoints.get(0)[1]*scale));

        }

        //g2.draw(new Line2D.Double(1.0,1.0, 300.0, 300.0));



    }

    public void setPoints(ArrayList<ArrayList<Double[]>> allPoints) {
        this.allPoints = allPoints;
        init = true;
    }

    public ArrayList<ArrayList<Double[]>> getPoints() {
        return this.allPoints;
    }


    @Override
    public void update(Graphics g2) {
        super.update(g2);
        g2.clearRect(0, 0, this.getWidth(), this.getHeight());
        paint(g2);
    }
}
