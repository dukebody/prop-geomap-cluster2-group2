import java.util.*;


//TEST FOR LINECONTROLLER AS WELL:
public class LineTest {

	public static void main(String[] arg){
		Country c1 = new Country("c1");
		Country c2 = new Country("c2");
		Country c3 = new Country("c3");
		Country c4 = new Country("c4");

                ArrayList<BorderPoint> testPointsC1 = new ArrayList<BorderPoint>();
                ArrayList<BorderPoint> testPointsC2 = new ArrayList<BorderPoint>();
                ArrayList<BorderPoint> testPointsC3 = new ArrayList<BorderPoint>();
                ArrayList<BorderPoint> testPointsC4 = new ArrayList<BorderPoint>();

		BorderPoint p1 = new BorderPoint(2,1);
                BorderPoint p2 = new BorderPoint(2,2);
                BorderPoint p3 = new BorderPoint(3,3);
                BorderPoint p4 = new BorderPoint(2,4);
                BorderPoint p5 = new BorderPoint(1,3);
                BorderPoint p6 = new BorderPoint(1,2);
                BorderPoint p7 = new BorderPoint(3,4);
                BorderPoint p8 = new BorderPoint(4,3);
                BorderPoint p9 = new BorderPoint(4,1);
                BorderPoint p10 = new BorderPoint(3,2);
                BorderPoint p11 = new BorderPoint(2.5,2.5);
                BorderPoint p12 = new BorderPoint(3.5,2.5);
                
                testPointsC1.add(p1);//country 1
                testPointsC1.add(p11);
                testPointsC1.add(p2);
                testPointsC1.add(p3);
                testPointsC1.add(p4);
                testPointsC1.add(p5);
                testPointsC1.add(p6);

                testPointsC2.add(p3);//country 2
                testPointsC2.add(p4);
                testPointsC2.add(p7);
                testPointsC2.add(p8);

                testPointsC3.add(p3);//country 3
                testPointsC3.add(p12);
                testPointsC3.add(p10);
                testPointsC3.add(p2);
                testPointsC3.add(p11);

                testPointsC4.add(p1);//country 2
                testPointsC4.add(p2);
                testPointsC4.add(p10);
                testPointsC4.add(p9);

                ZonesController ctrlZone = new ZonesController();
                LineController ctrlLine = new LineController();

                boolean b1=ctrlZone.createZone(c1,testPointsC1);
                boolean b2=ctrlZone.createZone(c2,testPointsC2);
                boolean b3=ctrlZone.createZone(c3,testPointsC3);
                boolean b4=ctrlZone.createZone(c2,testPointsC4);

                Zone z1 = c1.getZones().get(c1.getZones().size()-1);
		Zone z2 = c2.getZones().get(c2.getZones().size()-1);
		Zone z3 = c3.getZones().get(c3.getZones().size()-1);
		Zone z4 = c2.getZones().get(c2.getZones().size()-1);

                System.out.println("b1: "+b1);
                System.out.println("b2: "+b2);
                System.out.println("b3: "+b3);
                System.out.println("b4: "+b4+"\n");

                System.out.println("C1:");
                System.out.println("name: "+c1.getName());
                System.out.println("zones #: "+c1.getZones().size());
                System.out.println("points:");
                for (int i=0;i<c1.getZones().get(0).getBorderpoints().size();i++) {
                    System.out.println("-point #"+i+": ("+c1.getZones().get(0).getBorderpoints().get(i).getLatitude()+
                            "-"+c1.getZones().get(0).getBorderpoints().get(i).getLongitude()+")");
                }
                System.out.println("\n");

                System.out.println("C2:");
                System.out.println("name: "+c2.getName());
                System.out.println("zones #: "+c2.getZones().size());
                System.out.println("points zone 2:");
                for (int i=0;i<c2.getZones().get(0).getBorderpoints().size();i++) {
                    System.out.println("-point #"+i+": ("+c2.getZones().get(0).getBorderpoints().get(i).getLatitude()+
                            "-"+c2.getZones().get(0).getBorderpoints().get(i).getLongitude()+")");
                }
                System.out.println("points zone 4:");
                for (int i=0;i<c2.getZones().get(1).getBorderpoints().size();i++) {
                    System.out.println("-point #"+i+": ("+c2.getZones().get(1).getBorderpoints().get(i).getLatitude()+
                            "-"+c2.getZones().get(1).getBorderpoints().get(i).getLongitude()+")");
                }
                System.out.println("\n");

                System.out.println("C3:");
                System.out.println("name: "+c3.getName());
                System.out.println("zones #: "+c3.getZones().size());
                System.out.println("points:");
                for (int i=0;i<c3.getZones().get(0).getBorderpoints().size();i++) {
                    System.out.println("-point #"+i+": ("+c3.getZones().get(0).getBorderpoints().get(i).getLatitude()+
                            "-"+c3.getZones().get(0).getBorderpoints().get(i).getLongitude()+")");
                }
                System.out.println("\n");

                /*System.out.println("C4:");
                System.out.println("name: "+c4.getName());
                System.out.println("zones #: "+c4.getZones().size());
                System.out.println("points:");
                for (int i=0;i<c4.getZones().get(0).getBorderpoints().size();i++) {
                    System.out.println("-point #"+i+": ("+c4.getZones().get(0).getBorderpoints().get(i).getLatitude()+
                            "-"+c4.getZones().get(0).getBorderpoints().get(i).getLongitude()+")");
                }
                System.out.println("\n");*/

                System.out.println("C1 - Z1:");
		metod(c1.getZones().get(0).getBorderpoints());
                System.out.println("C2 - Z2:");
		metod(c2.getZones().get(0).getBorderpoints());
                System.out.println("C2 - Z4:");
                metod(c2.getZones().get(1).getBorderpoints());
                System.out.println("C3 - Z3:");
		metod(c3.getZones().get(0).getBorderpoints());
		
                System.out.println("\nc1 coastal length: "+ctrlLine.getCountryCoastalLength(c1));
                System.out.println("c2 coastal length: "+ctrlLine.getCountryCoastalLength(c2));
                System.out.println("c3 coastal length: "+ctrlLine.getCountryCoastalLength(c3)+"\n");
                //System.out.println("c4 coastal length: "+ctrlLine.getCountryCoastalLength(c4));

                System.out.println("c1 whole border length: "+ctrlLine.getWholeCountryBordersLength(c1));
                System.out.println("c2 whole border length: "+ctrlLine.getWholeCountryBordersLength(c2));
                System.out.println("c3 whole border length: "+ctrlLine.getWholeCountryBordersLength(c3)+"\n");
                //System.out.println("c4 whole border length: "+ctrlLine.getWholeCountryBordersLength(c4));

	}

	public static void metod(List<BorderPoint> list){
		Iterator<BorderPoint> it = list.iterator();
		BorderPoint initPoint = it.next();
		Iterator<BorderPoint> itr1 = list.iterator();
		Iterator<BorderPoint> itr2 = list.iterator();
		itr2.next();
		while(itr2.hasNext()){
			BorderPoint a = itr1.next();
			BorderPoint b = itr2.next();
			Line line = new Line(a, b);
			System.out.println("the line is between (" + a.getLatitude()+ ", " + a.getLongitude() + ") and (" +
					b.getLatitude() + ", " + b.getLongitude() + ") and is: " + line.getType() + " and is long: " + line.getLength() );
		}
		BorderPoint a = itr1.next();
		Line line = new Line(a, initPoint);
		System.out.println("the line is between (" + a.getLatitude()+ ", " + a.getLongitude() + ") and (" + initPoint.getLatitude() +
				", " + initPoint.getLongitude() + ") and is: " + line.getType() + " and is long: "+ line.getLength() );

	}

}
