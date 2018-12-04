import java.awt.Color;
import java.awt.Graphics;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Tester {
	static ArrayList<RA>  RArea = new ArrayList<>();
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
//		ArrayList<RA> RArea = new ArrayList<>();
		System.out.println("Start: ");
		// Restricted Area
		// x, y, r
		RA ra1 = new RA(5, 20, 3);
		RA ra2 = new RA(40, -12, 10);
		RA ra3 = new RA(16, 15, 3);
		RA ra4 = new RA(6, 3, 2);
		RA ra5 = new RA(10, 11, 3);
		RA ra6 = new RA(-12, 13, 5);
		
		// line x1,y1 x2,y2
		//first line should not have intersection with ra1-ra4
		
		// Intersect one area
		Line line1 = new Line(3, 2, 3, 15);
		// Intersect multi-areas
		Line line2 = new Line(20, 15, 3, 2);
		// Non intersect
		Line line3 = new Line (10, 2, 30, 10);
		
		// add
		RArea.add(ra1);
		RArea.add(ra2);
		RArea.add(ra3);
		RArea.add(ra4);
		RArea.add(ra5);
		RArea.add(ra6);
		
		// Test 
		SPCountry sp = new SPCountry(RArea, line1);
		
		System.out.println("Restricted Area in map: ");
		sp.toStringArea();
		System.out.println();

		System.out.println("Input Line: ");
		System.out.println("Line startpoint:(" + sp.line.startX + ", " + sp.line.startY + ")  "
				+ "endpoint: (" + sp.line.endX + ", " + sp.line.endY + ")" );
		System.out.println();
		
		System.out.println("*** Begin finding intersection area ***");
		sp.findAllIntersection();
		System.out.println("Finish finding!");
		System.out.println();
		// System.out.println(sp.tostring());
		sp.tostring();
		System.out.println();
		System.out.println("*** Begin calculating intersection points***");
		sp.calInter();
		System.out.println("*** Finishing calculating ***");
		System.out.println();
		sp.tostringPoint(); ;
		System.out.println();
		System.out.println("*** Begin sorting ***");
		sp.sortIntertsect();
		System.out.println("Finish sorting!");
		System.out.println();
		sp.tostringPoint() ;
		
		long endTime = System.currentTimeMillis();
		System.out.println("Processing time: " + (endTime - startTime) + " milliseconds");
		
//		sp.GUIHelper(sp.inter);
//		country(sp);
		
	}
	
		public static void country(SPCountry sp) {
			ArrayList<RA> circles = sp.map.RArea;
			ArrayList<Arc> arcsPath = sp.arcs;
			ArrayList<Line> linesPath = sp.lines;
			
			JFrame CurrentMap = new JFrame();
			CurrentMap.setTitle("464 Project, country");
			CurrentMap.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			CurrentMap.setSize(668, 728);
			
			JPanel graphics = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					//draw all the restricted area
					for(RA circle: circles) {
						g.drawArc((int)((circle.x - circle.radius/2)*10), (int)((circle.y- circle.radius/2)*10), (int)(circle.radius*10), (int)(circle.radius*10), 0, 360);
					}
					
					g.fillOval(300, 300, 4, 4);
					g.fillOval(304, 300, 4, 4);
					//draw all the arcs in the path
					for(Arc a: arcsPath) {
						g.setColor(Color.RED);
						g.drawArc(a.x*10, a.y*10, a.radius*10, a.radius*10, a.startAngel, a.arcAngel);
					}
					
					for(Intersect in: sp.inter) {
						g.fillOval(in.l1.x*10, in.l1.y*10, 4, 4);
						g.fillOval(in.l2.x*10, in.l2.y*10, 4, 4);
					}
					
					//draw all the lines in the path
//					for(Line line: linesPath) {
//						g.setColor(Color.RED);
//						g.drawLine(line.startX, line.startY, line.endX, line.endY);
//					}
				}
			};
			
			CurrentMap.getContentPane().add(graphics);
			CurrentMap.setVisible(true);
			
		}
		
	}