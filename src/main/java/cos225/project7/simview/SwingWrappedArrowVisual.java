package cos225.project7.simview;

import cos225.project7.math.ValueMapper;
import cos225.project7.math.ValueWrapper;

/**
 * Arrow Swing Visual that wraps around
 * 
 * @author Nathan
 *
 */
public class SwingWrappedArrowVisual extends SwingArrowVisual {
	private ValueWrapper wrap;
	private ValueMapper map;
	private double zoom = 10.0;
	
	/**
	 * Constructor.  It uses the gridSize and patchSizes to ensure the
	 * visuals align with the collision information correctly.
	 * 
	 * @param gridSize  Size of the grid used
	 * @param patchSize  Size of the patches used
	 */
	public SwingWrappedArrowVisual(int gridSize, int patchSize) {
		zoom = (double) patchSize;
		map = new ValueMapper(1.0 / patchSize, gridSize / 2.0);
		wrap = new ValueWrapper(0, gridSize);
		
		//map = new ValueMapper(1.0, (gridSize*patchSize) / 2.0);
		//wrap = new ValueWrapper(0, gridSize*patchSize);
	}
	
	/**
	 * Set the x coordinate with wrapping
	 */
	public void setX(double x) {
		this.x = (int) (wrap.wrap(map.map(x))*zoom);
		//this.x = (int) (wrap.wrap(map.map(x)));
	}

	/**
	 * Set the y coordinate with wrapping
	 */
	public void setY(double y) {
		this.y = (int) (wrap.wrap(map.map(y))*zoom);
		//this.y = (int) (wrap.wrap(map.map(y)));
	}
}
