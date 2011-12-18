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

    private Graphics g2;

    private Double scale = 3.0;
    private Double xoffset = 0.0;
    private Double yoffset = 0.0;

    private Double gap = 100.0;

    private String countryName;

    @Override
    public void paint(Graphics g) {

        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setBackground(Color.WHITE);

        paintCountry(countryName, g2);

    }

    public void setCountry(String countryName) {
        this.countryName = countryName;
    }

    private void paintCountry(String countryName, Graphics2D g2) {
        CountryController cc = Application.getCountryController();
        setOffsetsAndScale(cc.getCountryExtremeValues(countryName));

        // paint the rest of the countries in black
        g2.setColor(Color.BLACK);
        for (HashMap<String,String> map: cc.getNeighborCountries(countryName)) {
            paintPoints(cc.getCountryBorderPointsForDrawing(map.get("name")), g2);
        }

        // paint the selected country in blue
        //g2.drawString(countryName, 20, 20) ;
        g2.setColor(Color.BLUE);
        paintPoints(cc.getCountryBorderPointsForDrawing(countryName), g2);
    }

    private void paintPoints(ArrayList<ArrayList<Double[]>> points, Graphics2D g2) {
        for (ArrayList<Double[]> zonePoints: points) {
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
    }

    public void setOffsetsAndScale(ArrayList<Double> countryExtremeValues) {
        Double maxLong = countryExtremeValues.get(0);
        Double minLong= countryExtremeValues.get(1);
        Double maxLat = countryExtremeValues.get(2);
        Double minLat = countryExtremeValues.get(3);
        
        Double sizex = maxLong - minLong;
        Double sizey = maxLat - minLat;
        scale = Math.min((800.0-gap)/sizex, (800.0-gap)/sizey);

        xoffset = -minLong*scale + gap;
        yoffset = maxLat*scale + gap;
    }

    @Override
    public void update(Graphics g2) {
        super.update(g2);
        g2.clearRect(0, 0, this.getWidth(), this.getHeight());
        paint(g2);
    }
}
