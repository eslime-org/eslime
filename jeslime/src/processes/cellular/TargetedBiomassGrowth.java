package processes.cellular;

import java.util.ArrayList;
import java.util.HashSet;

import org.dom4j.Element;

import geometries.Geometry;
import io.project.ProcessLoader;
import processes.StepState;
import processes.gillespie.GillespieState;
import structural.GeneralParameters;
import structural.Lattice;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

/**
 * Adds a fixed amount of biomass to every cell with a cell
 * state matching the target.
 * 
 * @author dbborens
 *
 */
public class TargetedBiomassGrowth extends CellProcess {

	// How much biomass to accumulate per time step
	private double delta;
	
	// If false, apply() will be called on cells after their
	// biomass is updated. If true, you must call apply() on
	// a cell before the new biomass accumulates.
	private boolean defer;
	
	// Only feed cells if they are of the target type.
	private int targetCellType;
	
	public TargetedBiomassGrowth(ProcessLoader loader, Lattice lattice, int id,
			Geometry geom, GeneralParameters p) {
		super(loader, lattice, id, geom, p);
		
		delta = Double.valueOf(get("delta"));
		
		defer = Boolean.valueOf(get("defer"));
		
		targetCellType = Integer.valueOf(get("target"));
		
	}

	public TargetedBiomassGrowth(Lattice lattice, Geometry geom, 
			double delta, boolean defer, int target) {
		super(null, lattice, 0, geom, null);
		
		this.delta = delta;
		this.defer = defer;
		this.targetCellType = target;
	}
	
	public void fire(StepState state) throws HaltCondition {
		
		// If the target state doesn't exist, don't waste any time
		// checking cells.
		int targetCount = lattice.getStateMapViewer().getCount(targetCellType);
		if (targetCount == 0) {
			return;
		}
		
		ArrayList<Coordinate> targetSites = new ArrayList<Coordinate>(targetCount);
		// Feed the cells.
		for (Coordinate site : activeSites) {
			if (lattice.isOccupied(site) && lattice.getCell(site).getState() == targetCellType) {
				lattice.getCell(site).feed(delta);
				targetSites.add(site);
			}
		}
		
		// If we're not defering updates, tell the cells to use the
		// new data right away. Note that we want to do this after
		// every cell has been "fed," in case there are non-local
		// interactions.
		if (!defer) {
			for (Coordinate site : targetSites) {
				lattice.apply(site);
			}
		}
		
		// It would be annoying to highlight cells just for being fed, so we
		// don't.
	}

	@Override
	public void target(GillespieState gs) throws HaltCondition {
		// There's only one event that can happen--we update.
		if (gs != null) {
			gs.add(this.getID(), 1, 0.0D);
		}
	}

}
