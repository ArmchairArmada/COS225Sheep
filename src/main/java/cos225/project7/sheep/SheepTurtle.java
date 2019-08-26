package cos225.project7.sheep;

import java.awt.Color;

import cos225.project7.data.MutableValue;
import cos225.project7.math.Rand;
import cos225.project7.simulation.ColliderEntityComponent;
import cos225.project7.simulation.CollisionSystem;
import cos225.project7.simview.SwingArrowVisual;
import cos225.project7.simview.SwingWorld;
import cos225.project7.simview.SwingWrappedArrowVisual;
import cos225.project7.turtle.RandomWalk;
import cos225.project7.turtle.Turtle;


/**
 * Turtle that can bump and infect other turtles
 * 
 * @author Nathan
 *
 */
public class SheepTurtle extends Turtle {
	private RandomWalk randomWalk = null;
	private SwingArrowVisual visual;
	private SwingWorld world;
	private double speed;
	private double turn;
	private CollisionSystem collisionSystem;
	private ColliderEntityComponent collider;
	private double energy;
	private double startEnergy;
	private double grassEnergy;
	private double moveEnergy;
	private double spawnEnergy;
	private GrassGrid grass;
	private MutableValue<Double> populationSize;
	
	/**
	 * Infected Turtle
	 * 
	 * @param world  World the turtle lives in
	 * @param infectionData  Object that keeps track of how many turtles are infected
	 * @param collisionSystem  System for managing collisions between turtles
	 * @param speed  Movement speed
	 * @param turn  Turn randomness
	 * @param startInfectionChance  Probability of starting infected
	 * @param infectionChance  Probability of getting infected if it touches an infected Turtle
	 * @param healTime  Time before healing
	 * @param healRand  Additional random amount of time for healing
	 */
	public SheepTurtle(SwingWorld world, CollisionSystem collisionSystem, GrassGrid grass, MutableValue<Double> populationSize, double speed, double turn, double startEnergy, double grassEnergy, double moveEnergy, double spawnEnergy) {
		super(world.getPopulation());
		this.world = world;
		this.collisionSystem = collisionSystem;
		this.speed = speed;
		this.turn = turn;
		this.startEnergy = startEnergy;
		this.grassEnergy = grassEnergy;
		this.moveEnergy = moveEnergy;
		this.spawnEnergy = spawnEnergy;
		energy = startEnergy;
		this.grass = grass;
		this.populationSize = populationSize;
	}
	
	/**
	 * Create copy of this InfectedTurtle
	 */
	public SheepTurtle copy() {
		return new SheepTurtle(world, collisionSystem, grass, populationSize, speed, turn, startEnergy, grassEnergy, moveEnergy, spawnEnergy);
	}
	
	/**
	 * Initialize the entity
	 */
	@Override
	public void init() {
		collider = collisionSystem.createDefaultCollider(this);
		randomWalk = new RandomWalk(this, speed, turn);
		visual = new SwingWrappedArrowVisual(world.getGridSize(), world.getPatchSize());
		world.getVisuals().add(visual);
		visual.setColor(Color.WHITE);
		populationSize.setValue(populationSize.getValue() + 1.0);
	}
	
	/**
	 * Clean up the entity when it is destroyed.
	 */
	@Override
	public void cleanup() {
		populationSize.setValue(populationSize.getValue() - 1.0);
		world.getVisuals().remove(visual);
		collider.cleanup();
		super.cleanup();
	}
	
	/**
	 * Receive simple message
	 */
	public void message(String msg) {
	}
	
	/**
	 * Update the Infected Turtle by having it randomly walk, bump, and infect stuff
	 * 
	 * @param timeDelta  Amount of time that has passed since last update
	 */
	@Override
	public void update(double timeDelta) {
		randomWalk.update(timeDelta);
		collider.update();
		visual.setX(xcor());
		visual.setY(ycor());
		visual.setAngle(angle);
		
		energy -= moveEnergy * timeDelta;
		if (grass.eatGrass(position.getX(), position.getY())) {
			energy += grassEnergy;
		}
		
		if (energy <= 0.0)
			kill();
		
		if (energy >= spawnEnergy) {
			energy = startEnergy;
			SheepTurtle sheep = copy();
			//sheep.setHeading(heading);
			sheep.setHeading(Rand.getRandom().nextDouble() * 360);
			sheep.setx(position.getX());
			sheep.sety(position.getY());
			population.add(sheep);
		}
	}
}
