package cos225.project7.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

import cos225.project7.data.MutableValue;

/**
 * Graphs values that range between 0 and 1
 * 
 * @author Nathan
 *
 */
public class LineGraph extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private List<Double> data;
	private MutableValue<Double> dataValue;
	private int columns;
	private Timer timer;
	private double maxValue;

	/**
	 * Creates a line graph
	 * 
	 * @param watchValue  Value object to watch and record in the graph
	 * @param columnCount  Number of columns for line graph
	 * @param updateRate  Frequency of graph update in milliseconds
	 */
	public LineGraph(MutableValue<Double> watchValue, int columnCount, int updateRate) {
		this.data = new ArrayList<Double>();
		this.dataValue = watchValue;
		this.columns = columnCount;
		this.maxValue = 1.0;
		
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		ActionListener refresh = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double v = dataValue.getValue();
				if (v > LineGraph.this.maxValue)
					LineGraph.this.maxValue = v;
				data.add(v);
				if (data.size() > columns+1) {
					data.remove(0);
				}
				repaint();
			}
		};
		
		timer = new Timer(updateRate, refresh);
	}
	
	/**
	 * Draw the line graph
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
				
		int start = Math.max(0, data.size() - columns - 1);
		int end = data.size();
		double space = getWidth() / (double)(columns);
		double x = getWidth() - (end - start) * space + space;
		
		g.setColor(Color.RED);
		for (int i=start; i<end-1; i++) {
			int x1 = (int) x;
			int x2 = (int) (x + space);
			int y1 = getHeight() - (int) (getHeight() * (data.get(i) / maxValue));
			int y2 = getHeight() - (int) (getHeight() * (data.get(i + 1) / maxValue));
			x += space;
			
			g.drawLine(x1, y1, x2, y2);
		}
		
		g.setColor(Color.BLACK);
		if (data.size() > 0) {
			//String label = String.format("Infected: %1$.2f%%", (data.get(data.size()-1)*100));
			String label = String.format("Population: %1$.0f  Max: %2$.0f", data.get(data.size()-1), maxValue);
			g.drawString(label, 2, 12);
		}
	}
	
	/**
	 * Start the update timer
	 */
	public void start() {
		timer.start();
	}
	
	/**
	 * Stop (pause) the graph update timer
	 */
	public void stop() {
		timer.stop();
	}
	
	/**
	 * Reset the graph
	 */
	public void reset() {
		maxValue = 1.0;
		timer.stop();
		data.clear();
		repaint();
	}
}
