import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class GUITest {
	//NEED TO MODIFY
	ArrayList<RA> circles = new ArrayList<>();
	ArrayList<Arc> arcsPath = new ArrayList<>();
	ArrayList<Line> linesPath = new ArrayList<>();
	//END MODIFY
	
	
	public GUITest() {
		JFrame CurrentMap = new JFrame();
		CurrentMap.setTitle("464 Project");
		CurrentMap.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		CurrentMap.setSize(668, 728);
		
		JPanel graphics = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				//draw all the restricted area
				for(RA circle: circles) {
					g.drawArc((int)circle.x, (int)circle.y, (int)circle.radius, (int)circle.radius, 0, 360);
				}
				
				//draw all the arcs in the path
				for(Arc a: arcsPath) {
					g.drawArc(a.x, a.y, a.radius, a.radius, a.startAngel, a.arcAngel);
				}
				
				//draw all the lines in the path
				for(Line line: linesPath) {
					g.drawLine(line.x1, line.y1, line.x2, line.y2);
				}
				
				
				//test draw; WILL BE DELETE
				g.drawLine(100, 100, 200, 100);
				g.setColor(Color.RED);
				g.drawArc(50, 50, 100, 100, 0, 45);
				g.setColor(Color.CYAN);
				g.drawArc(200, 200, 300, 300, 0, 90);
			}
		};
		
		CurrentMap.getContentPane().add(graphics);
		CurrentMap.setVisible(true);
		
	}
	public static void main(String[] args) {
		new GUITest();
	}
}
