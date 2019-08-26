package cos225.project7.sheep;

import cos225.project7.math.Rand;
import cos225.project7.simulation.Entity;
import cos225.project7.simulation.EntityPopulation;

public class PlanterEntity extends Entity {
	private GrassGrid grass;
	private double jumpTime = 0.0;
	private double jumpSize;
	
	public PlanterEntity(EntityPopulation population, GrassGrid grass, double jumpSize) {
		super(population);
		this.grass = grass;
		this.jumpSize = jumpSize;
	}
	
	public PlanterEntity copy() {
		return new PlanterEntity(population, grass, jumpSize);
	}
	
	@Override
	public void update(double timeDelta) {
		jumpTime -= timeDelta;
		if (jumpTime < 0.0)
		{
			jumpTime = 0.1;
			position.set(Rand.getRandom().nextDouble() * jumpSize, Rand.getRandom().nextDouble() * jumpSize);
			grass.plantGrass(position.getX(), position.getY());
		}
	}
}
