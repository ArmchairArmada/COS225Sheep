package cos225.project7.controller;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import cos225.project7.data.MutableValue;
import cos225.project7.gui.LineGraph;
import cos225.project7.math.Rand;
import cos225.project7.sheep.PlanterEntity;
import cos225.project7.sheep.SheepTurtle;
import cos225.project7.simview.SwingWorld;
import cos225.project7.turtle.Turtle;

/**
 * The main window of the Infected Turtle application 
 * 
 * @author Nathan
 *
 */
public class SwingSheepController extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private boolean running = false;
	private boolean justReset = true;
	//private InfectionData infectionData;
	private MutableValue<Double> populationSize;
	private JToggleButton btnStartStop;
	private JFormattedTextField txtPopulation;
	private JFormattedTextField txtStartEnergy;
	private JFormattedTextField txtMoveEnergy;
	private JFormattedTextField txtSpawnEnergy;
	private JFormattedTextField txtRandTurn;
	private JFormattedTextField txtStepSize;
	private LineGraph graph;
	private SwingWorld world;

	private JFormattedTextField txtGrassEnergy;

	/**
	 * A while big pile of mess for controls and stuff.
	 */
	SwingSheepController() {
		//infectionData = new InfectionData();
		populationSize = new MutableValue<Double>(0.0);
		
		setTitle("Bonus Project - Sheep (or Rabbits) Eating Grass");
		
		// Outer top and bottom
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		// Top Space
		add(Box.createRigidArea(new Dimension(0,5)));
		
		// Top part
		JPanel pnlTop = new JPanel();
		pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.X_AXIS));
		add(pnlTop);
		
		// Space between top and bottom
		add(Box.createRigidArea(new Dimension(0,5)));
		
		// Top Left Space
		pnlTop.add(Box.createRigidArea(new Dimension(5,0)));
		
		// The Turtle world simulation
		world = new SwingWorld(51, 10, 16);
		pnlTop.add(world);
		
		// Space between World and Controls
		pnlTop.add(Box.createRigidArea(new Dimension(5,0)));
		
		// User input controls
		JPanel pnlControls = new JPanel();
		pnlControls.setPreferredSize(new Dimension(200,510));
		pnlControls.setLayout(new GridLayout(16, 1, 5, 5));
		
		// Start / Running / Paused button
		btnStartStop = new JToggleButton("Start");
		btnStartStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				running = !running;
				if (running) {
					// Just Started
					btnStartStop.setText("Running");
					setControlsEnabled(false);
					
					if (justReset)
						configureSimulation();
					startSimulation();
				} else {
					// Paused
					btnStartStop.setText("Paused");
					stopSimulation();
				}
			}
		});
		pnlControls.add(btnStartStop);
		
		// Reset button
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				running = false;
				justReset = true;
				btnStartStop.setSelected(false);
				btnStartStop.setText("Start");
				setControlsEnabled(true);
				graph.reset();
				world.reset();
			}
		});
		pnlControls.add(btnReset);
		
		// Population size
		JLabel lblPopulation = new JLabel("Population Size");
		lblPopulation.setVerticalAlignment(JLabel.BOTTOM);
		pnlControls.add(lblPopulation);
		
		txtPopulation = new JFormattedTextField();
		txtPopulation.setValue(new Integer(5));
		pnlControls.add(txtPopulation);
		
		// Start infected chance
		JLabel lblStartEnergy = new JLabel("Start Energy");
		lblStartEnergy.setVerticalAlignment(JLabel.BOTTOM);
		pnlControls.add(lblStartEnergy);
		
		txtStartEnergy = new JFormattedTextField();
		txtStartEnergy.setValue(new Double(50.0));
		pnlControls.add(txtStartEnergy);
		
		// Infection contagious chance
		JLabel lblGrassEnergy = new JLabel("Grass Energy");
		lblGrassEnergy.setVerticalAlignment(JLabel.BOTTOM);
		pnlControls.add(lblGrassEnergy);
		
		txtGrassEnergy = new JFormattedTextField();
		txtGrassEnergy.setValue(new Double(20.0));
		pnlControls.add(txtGrassEnergy);
		
		// Amount of time to heal
		JLabel lblMoveEnergy = new JLabel("Move Energy (energy/sec)");
		lblMoveEnergy.setVerticalAlignment(JLabel.BOTTOM);
		pnlControls.add(lblMoveEnergy);
		
		txtMoveEnergy = new JFormattedTextField();
		txtMoveEnergy.setValue(new Double(10));
		pnlControls.add(txtMoveEnergy);
		
		// Additional random time for healing
		JLabel lblSpawnEnergy = new JLabel("Spawn Energy");
		lblSpawnEnergy.setVerticalAlignment(JLabel.BOTTOM);
		pnlControls.add(lblSpawnEnergy);
		
		txtSpawnEnergy = new JFormattedTextField();
		txtSpawnEnergy.setValue(new Double(150));
		pnlControls.add(txtSpawnEnergy);
		
		// Turtle's movement speed
		JLabel lblStepSize = new JLabel("Turtle Speed (pix/sec)");
		lblStepSize.setVerticalAlignment(JLabel.BOTTOM);
		pnlControls.add(lblStepSize);
		
		txtStepSize = new JFormattedTextField();
		txtStepSize.setValue(new Double(25.0));
		pnlControls.add(txtStepSize);
		
		// Turtle's random rotation amount
		JLabel lblRandTurn = new JLabel("Turtle Turn (deg/sec)");
		lblRandTurn.setVerticalAlignment(JLabel.BOTTOM);
		pnlControls.add(lblRandTurn);
		
		txtRandTurn = new JFormattedTextField();
		txtRandTurn.setValue(new Double(500));
		pnlControls.add(txtRandTurn);
		
		pnlTop.add(pnlControls);
		
		// Top Right Space
		pnlTop.add(Box.createRigidArea(new Dimension(5,0)));
		
		// Graph
		JPanel pnlBottom = new JPanel();
		pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.X_AXIS));
		add(pnlBottom);
		
		// Bottom Left Space
		pnlBottom.add(Box.createRigidArea(new Dimension(5,0)));
		
		// Infected percentage line graph
		graph = new LineGraph(populationSize, 200, 100);
		graph.setPreferredSize(new Dimension(1, 150));
		pnlBottom.add(graph);
		pnlBottom.add(Box.createRigidArea(new Dimension(5,0)));
		
		// Bottom Right Space
		add(Box.createRigidArea(new Dimension(0,5)));
		
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				graph.stop();
				world.stop();
				System.exit(0);
			}
		});
	}
	
	/**
	 * Enable or disable controls on the gui 
	 * 
	 * @param value  If they should be enabled or disabled
	 */
	private void setControlsEnabled(boolean value) {
		txtPopulation.setEnabled(value);
		txtStartEnergy.setEnabled(value);
		txtMoveEnergy.setEnabled(value);
		txtSpawnEnergy.setEnabled(value);
		txtRandTurn.setEnabled(value);
		txtStepSize.setEnabled(value);
		txtGrassEnergy.setEnabled(value);
	}
	
	/**
	 * Sets up the simulation with the current configuration options
	 */
	private void configureSimulation() {
		justReset = false;
		
		populationSize.setValue(0.0);
		
		//infectionData.setPopulation((Integer)txtPopulation.getValue());
		//infectionData.setInfected(0);
		
		// Newly configured prototype for infected turtle
		/*
		InfectedTurtle proto = new InfectedTurtle(
				world,
				infectionData,
				world.getCollisionSystem(),
				(Double)txtRandTurn.getValue(),
				(Double)txtStepSize.getValue(),
				(Double)txtInfected.getValue()/100.0,
				(Double)txtContagious.getValue()/100.0,
				(Double)txtHealTime.getValue(),
				(Double)txtHealRand.getValue()
				);
		*/
		
		// double startEnergy, double grassEnergy, double moveEnergy, double spawnEnergy
		SheepTurtle proto = new SheepTurtle(
				world,
				world.getCollisionSystem(),
				world.getGrass(),
				populationSize,
				(Double)txtRandTurn.getValue(),
				(Double)txtStepSize.getValue(),
				(Double)txtStartEnergy.getValue(), //50.0,
				(Double)txtGrassEnergy.getValue(), // 50.0,
				(Double)txtMoveEnergy.getValue(), //50.0,
				(Double)txtSpawnEnergy.getValue() // 100.0
				);
		
		world.getProtoCopier().regiseter("Turtle", proto);
		
		PlanterEntity planter = new PlanterEntity(world.getPopulation(), world.getGrass(), world.getGridSize() * world.getPatchSize());
		
		world.getProtoCopier().regiseter("Planter", planter);
		
		for (int i=0; i<25; i++) {
			world.getPopulation().add((PlanterEntity)world.getProtoCopier().makeCopy("Planter"));
		}
		
		// Populate the world
		for (int i =0; i < (Integer)txtPopulation.getValue(); i++) {
			Turtle t = (Turtle) world.getProtoCopier().makeCopy("Turtle");
			t.setx(Rand.getRandom().nextDouble() * world.getWidth());
			t.sety(Rand.getRandom().nextDouble() * world.getHeight());
			t.setHeading(Rand.getRandom().nextDouble() * 360);
			
			world.getPopulation().add(t);
		}
	}
	
	/**
	 * Start the simulation
	 */
	private void startSimulation() {
		world.start();
		graph.start();
	}
	
	/**
	 * Stop the simulation
	 */
	private void stopSimulation() {
		world.stop();
		graph.stop();
	}
	
	/**
	 * Get it started.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SwingSheepController controller = new SwingSheepController();
				controller.setVisible(true);
			}
		});
	}
}
