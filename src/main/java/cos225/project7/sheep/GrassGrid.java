package cos225.project7.sheep;

import java.awt.Color;

import cos225.project7.data.MappedDataGrid;
import cos225.project7.math.Rand;

public class GrassGrid {
	private MappedDataGrid<Color> colorGrid;
	private Color dirt;
	private Color grass;
	
	public GrassGrid(int gridSize, int patchSize) {
		dirt = new Color(0.55f, 0.4f, 0.2f);
		grass = new Color(0.7f, 0.85f, 0.1f);
		
		colorGrid = new MappedDataGrid<Color>(dirt, gridSize, patchSize);
		reset();
	}
	
	public void reset() {
		for (int y=0; y<colorGrid.getGridSize(); y++) {
			for (int x=0; x<colorGrid.getGridSize(); x++) {
				if (Rand.getRandom().nextBoolean())
					colorGrid.setDataAt(x, y, dirt);
				else
					colorGrid.setDataAt(x, y, grass);
			}
		}
	}
	
	public boolean eatGrass(double x, double y) {
		if (colorGrid.getMappedData(x, y) == grass) {
			colorGrid.setMappedData(x, y, dirt);
			return true;
		}
		return false;
	}
	
	public Color getAt(int x, int y) {
		return colorGrid.getDataAt(x, y);
	}

	public void plantGrass(double x, double y) {
		// TODO Auto-generated method stub
		colorGrid.setMappedData(x, y, grass);
	}
}
