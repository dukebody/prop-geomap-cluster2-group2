/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Tests;

import Beans.BorderPoint;
import Beans.City;
import Beans.Country;
import Beans.Point;
import Beans.QuadTree;
import java.io.*;
import java.util.*;
//import static org.junit.Assert.assertEquals;
//import org.junit.*;
//import junit.framework.TestCase;
import Beans.Toponym;
import Beans.TypeToponym;
import Controllers.ZonesController;
import Serializers.BordersSerializer;
import Serializers.CitiesSerializer;
import Serializers.ToponymsSerializerIterator;
import Serializers.ToponymsSerializer;

/**
 *
 * @author Guillermo
 */
public class SerializerTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            TypeToponym topoType = new TypeToponym("Whatever", "I don't care", "PPLS");
            
            System.out.println("TEST #1: TOPONYMS SERIALIZER:\n");

            //Manual simulation of Toponym's objects loading and grouping in a list:
            ArrayList<Toponym> spanishTops = new ArrayList<Toponym>();
            spanishTops.add(new Toponym("Barcelona", "Barcelona", 47.23454, 2.72243, topoType));
            spanishTops.add(new Toponym("Madrid", "Madrid", 45.87625, -2.98343, topoType));
            spanishTops.add(new Toponym("Valencia", "Valencia", 45.21845, -0.34534, topoType));
            spanishTops.add(new Toponym("Sevilla", "Sevilla", 42.83765, -3.64307, topoType));
            spanishTops.add(new Toponym("Bilbao", "Bilbao", 47.68428, -1.72348, topoType));
            spanishTops.add(new Toponym("Coruña", "Coruña", 47.37815, -4.98234, topoType));
            spanishTops.add(new Toponym("Zaragoza", "Zaragoza", 46.57208, -0.86201, topoType));
            spanishTops.add(new Toponym("Granada", "Granada", 42.71054, -2.57321, topoType));
            spanishTops.add(new Toponym("Salamanca", "Salamanca", 45.92487, -3.12868, topoType));
            spanishTops.add(new Toponym("Mallorca", "Mallorca", 45.38547 ,2.90345, topoType));
            
            //Ok, here comes the real deal:
            Iterator<Toponym> topoItr = spanishTops.iterator();
            ToponymsSerializer topSerial = new ToponymsSerializer(topoItr);
            Iterator<HashMap<String,String>> myItr = topSerial.getIterator();
            HashMap<String,String> serializedTopo = null;

            while (myItr.hasNext()) {
                serializedTopo = myItr.next();
                System.out.print(serializedTopo.get("utf8_name") + "    ");
                System.out.print(serializedTopo.get("ascii_name") + "    ");
                System.out.print(serializedTopo.get("latitude") + "    ");
                System.out.print(serializedTopo.get("longitude") + "    ");
                System.out.print(serializedTopo.get("type_code") + "    ");
                System.out.println(serializedTopo.get("population"));
            }

            System.out.println("\n...first test completed successfully!\n");

            System.out.println("TEST #2: CITIES SERIALIZER:\n");

            //Manual simulation of Toponym's objects loading and grouping in a list:
            ArrayList<City> spanishCities = new ArrayList<City>();
            spanishCities.add(new City("Barcelona", "Barcelona", 47.23454, 2.72243, topoType,3200000));
            spanishCities.add(new City("Madrid", "Madrid", 45.87625, -2.98343, topoType,6000000));
            spanishCities.add(new City("Valencia", "Valencia", 45.21845, -0.34534, topoType,1500000));
            spanishCities.add(new City("Sevilla", "Sevilla", 42.83765, -3.64307, topoType,1200000));
            spanishCities.add(new City("Bilbao", "Bilbao", 47.68428, -1.72348, topoType,500000));
            spanishCities.add(new City("Coruña", "Coruña", 47.37815, -4.98234, topoType,300000));
            spanishCities.add(new City("Zaragoza", "Zaragoza", 46.57208, -0.86201, topoType,800000));
            spanishCities.add(new City("Granada", "Granada", 42.71054, -2.57321, topoType,200000));
            spanishCities.add(new City("Salamanca", "Salamanca", 45.92487, -3.12868, topoType,100000));
            spanishCities.add(new City("Mallorca", "Mallorca", 45.38547 ,2.90345, topoType,50000));

            //Ok, here comes the real deal again:
            Iterator<City> cityItr = spanishCities.iterator();
            CitiesSerializer citySerial = new CitiesSerializer(cityItr);
            Iterator<HashMap<String,String>> myItr2 = citySerial.getIterator();
            HashMap<String,String> serializedCity = null;

            while (myItr2.hasNext()) {
                serializedCity = myItr2.next();
                System.out.print(serializedCity.get("utf8_name") + "    ");
                System.out.print(serializedCity.get("ascii_name") + "    ");
                System.out.print(serializedCity.get("latitude") + "    ");
                System.out.print(serializedCity.get("longitude") + "    ");
                System.out.print(serializedCity.get("type_code") + "    ");
                System.out.println(serializedCity.get("population"));
            }

            System.out.println("\n...second test completed successfully!\n");

            System.out.println("TEST #3: BORDER POINTS SERIALIZER:\n");

            //Manual simulation of border points's objects loading and grouping in a list:
            ArrayList<BorderPoint> spanishPoints = new ArrayList<BorderPoint>();
            spanishPoints.add(new BorderPoint(47.23454, 2.72243));              //Barcelona
            spanishPoints.add(new BorderPoint(46.57208, -0.86201));             //Zaragoza
            spanishPoints.add(new BorderPoint(47.68428, -1.72348));             //Bilbao
            spanishPoints.add(new BorderPoint(47.37815, -4.98234));             //Coruña
            spanishPoints.add(new BorderPoint(45.92487, -3.12868));             //Salamanca
            spanishPoints.add(new BorderPoint(45.87625, -2.98343));             //Madrid
            spanishPoints.add(new BorderPoint(42.83765, -3.64307));             //Sevilla
            spanishPoints.add(new BorderPoint(42.71054, -2.57321));             //Granada
            spanishPoints.add(new BorderPoint(45.21845, -0.34534));             //Valencia
            spanishPoints.add(new BorderPoint(45.38547 ,2.90345));              //Mallorca

            //Ok, here comes the real deal again:
            Iterator<BorderPoint> borderPointItr = spanishPoints.iterator();
            BordersSerializer borderPointsSerial = new BordersSerializer(borderPointItr);
            Iterator<HashMap<String,String>> myItr3 = borderPointsSerial.getIterator();
            HashMap<String,String> serializedBorderPoint = null;

            //First, iteration with border points itselves:
            while (myItr3.hasNext()) {
                serializedBorderPoint = myItr3.next();
                System.out.print(serializedBorderPoint.get("id_zone") + "    ");
                System.out.print(serializedBorderPoint.get("latitude") + "    ");
                System.out.print(serializedBorderPoint.get("longitude") + "    ");
                System.out.print(serializedBorderPoint.get("id_country") + "    ");
                System.out.println(serializedBorderPoint.get("name_country"));
            }
            
            System.out.println("\n...third test (first part) completed successfully!\n");

            //Simulation of a real country and zone creation to work with:
            Country spain = new Country("Spain");
            QuadTree qTree = new QuadTree();
            ZonesController ctrlZone = new ZonesController(qTree);
            if(!ctrlZone.createZone(spain, spanishPoints)) {
                System.out.println("Third test second part failed, zone was not constructed correctly");
                throw new RuntimeException();
            }

            //Reloading iterator for more action:
            borderPointItr = spanishPoints.iterator();
            borderPointsSerial = new BordersSerializer(borderPointItr);
            myItr3 = borderPointsSerial.getIterator();

            //Second, iteration with border points with zone and country:
            while (myItr3.hasNext()) {
                serializedBorderPoint = myItr3.next();
                System.out.print(serializedBorderPoint.get("id_zone") + "    ");
                System.out.print(serializedBorderPoint.get("latitude") + "    ");
                System.out.print(serializedBorderPoint.get("longitude") + "    ");
                System.out.print(serializedBorderPoint.get("id_country") + "    ");
                System.out.println(serializedBorderPoint.get("name_country"));
            }

            System.out.println("\n...third test (second part) completed successfully!\n");

        } catch (NoSuchElementException e) {
            System.out.println("NoSuchElementException at main(): " + e.toString());
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException at main(): " + e.toString());
        } catch (ClassCastException e) {
            System.out.println("ClassCastException at main(): " + e.toString());
        } catch (RuntimeException e) {
            System.out.println("RuntimeException at main(): " + e.getMessage());
        }
    }

}
